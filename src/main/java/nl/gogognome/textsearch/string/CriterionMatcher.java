package nl.gogognome.textsearch.string;

import java.util.function.*;
import nl.gogognome.textsearch.criteria.*;

/**
 * <p>This class checks whether a {@link Criterion} matches a specified String.</p>
 *
 * <p>Typical usage:</p>
 *
 * <pre>
 *   Criterion criterion = new Parser().parse("foo AND bar");
 *   CriterionMatcher matcher = new StringSearchFactory().caseInsensitiveCriterionMatcher(searchCriterion);
 *   boolean matches = matcher.matches("Barefoot is a movie directed by Andrew Flemming.");
 *   // matches == true
 * </pre>
 *
 * <p>A special use case is that the {@link Criterion} must be matched against a number of attributes of an object.
 * For example, you want to match the {@link Criterion} against the first name, last name, street and city of
 * relations.
 * This could be implemented by joining these attributes' values to a single string, separated by a character
 * that does not occur in the `Criterion`, and then matching
 * the {@link Criterion} against this resulting string. This approach however would require a lot of string copying
 * which is time consuming and produces a lot of garbage on the heap. To overcome this problem
 * the method {@link CriterionMatcher#matches(String...)} has a varargs string parameter.
 * The strings passed to the method are treated as if they were all joined together to a single string, separated
 * by a character that does not occur in the {@link Criterion}.
 * </p>
 *
 * <pre>
 *   Criterion criterion = new Parser().parse("foo AND bar");
 *   CriterionMatcher matcher = new StringSearchFactory().caseInsensitiveCriterionMatcher(criterion);
 *   boolean matches1 = matcher.matches("Bart", "food");
 *   // matches1 == true
 *   boolean matches2 = matcher.matches("ba", "rt food");
 *   // matches2 == false because "bar" is not found
 * </pre>
 *
 */
public class CriterionMatcher {

    private final MatchingCriterion matchingCriterion;

    private CriterionMatcher(Criterion criterion, Function<String, Predicate<String>> buildStringMatcher) {
        matchingCriterion = new MatchingCriterionBuilder(buildStringMatcher).getMatchingCriterionFor(criterion);
        if (matchingCriterion == null) {
            throw new IllegalArgumentException("The criterion could not be converted to a CriterionMatcher");
        }
    }

    public boolean matches(String text) {
        return matchingCriterion.matches(text);
    }

    public boolean matches(String... texts) {
        return matchingCriterion.matchesAny(texts);
    }

    private static class MatchingCriterionBuilder implements CriterionVisitor {

        private final Function<String, Predicate<String>> buildStringMatcher;
        private MatchingCriterion matchingCriterion;

        private MatchingCriterionBuilder(Function<String, Predicate<String>> buildStringMatcher) {
            this.buildStringMatcher = buildStringMatcher;
        }

        private MatchingCriterion getMatchingCriterion() {
            return matchingCriterion;
        }

        @Override
        public void visit(And and) {
            this.matchingCriterion = new MatchingAnd(
                    getMatchingCriterionFor(and.getLeft()),
                    getMatchingCriterionFor(and.getRight()));
        }

        @Override
        public void visit(Or or) {
            matchingCriterion = new MatchingOr(
                    getMatchingCriterionFor(or.getLeft()),
                    getMatchingCriterionFor(or.getRight()));
        }

        @Override
        public void visit(Not not) {
            matchingCriterion = new MatchingNot(
                    getMatchingCriterionFor(not.getCriterion()));
        }

        @Override
        public void visit(StringLiteral stringLiteral) {
            matchingCriterion = new MatchingStringLiteral(buildStringMatcher.apply(stringLiteral.getLiteral()));
        }

        private MatchingCriterion getMatchingCriterionFor(Criterion criterion) {
            criterion.accept(this);
            return getMatchingCriterion();
        }
    }

    private interface MatchingCriterion {
        boolean matches(String text);
        boolean matchesAny(String... texts);
    }

    private static class MatchingStringLiteral implements MatchingCriterion {

        private final Predicate<String> stringMatcher;

        private MatchingStringLiteral(Predicate<String> stringMatcher) {
            this.stringMatcher = stringMatcher;
        }

        @Override
        public boolean matches(String text) {
            return stringMatcher.test(text);
        }

        @Override
        public boolean matchesAny(String... texts) {
            for (String text : texts) {
                if (matches(text)) {
                    return true;
                }
            }
            return false;
        }
    }

    private static class MatchingAnd implements MatchingCriterion {

        private final MatchingCriterion left;
        private final MatchingCriterion right;

        private MatchingAnd(MatchingCriterion left, MatchingCriterion right) {
            this.left = left;
            this.right = right;
        }

        @Override
        public boolean matches(String text) {
            return left.matches(text) && right.matches(text);
        }

        @Override
        public boolean matchesAny(String... texts) {
            return left.matchesAny(texts) && right.matchesAny(texts);
        }
    }

    private static class MatchingOr implements MatchingCriterion {

        private final MatchingCriterion left;
        private final MatchingCriterion right;

        private MatchingOr(MatchingCriterion left, MatchingCriterion right) {
            this.left = left;
            this.right = right;
        }

        @Override
        public boolean matches(String text) {
            return left.matches(text) || right.matches(text);
        }

        @Override
        public boolean matchesAny(String... texts) {
            return left.matchesAny(texts) || right.matchesAny(texts);
        }
    }

    private static class MatchingNot implements MatchingCriterion {

        private final MatchingCriterion matchingCriterion;

        private MatchingNot(MatchingCriterion matchingCriterion) {
            this.matchingCriterion = matchingCriterion;
        }

        @Override
        public boolean matches(String text) {
            return !matchingCriterion.matches(text);
        }

        @Override
        public boolean matchesAny(String... texts) {
            return !matchingCriterion.matchesAny(texts);
        }
    }

    public static class Builder {

        private Function<String, Predicate<String>> buildStringMatcher;

        Builder(Function<String, Predicate<String>> buildStringMatcher) {
            this.buildStringMatcher = buildStringMatcher;
        }

        public CriterionMatcher build(Criterion criterion) {
            return new CriterionMatcher(criterion, buildStringMatcher);
        }
    }

}
