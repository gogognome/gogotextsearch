package nl.gogognome.textsearch.string;

import nl.gogognome.textsearch.criteria.*;

import java.util.function.BiPredicate;

/**
 * <p>This class checks whether a {@link Criterion} matches a specified String.</p>
 *
 * <p>Typical usage:</p>
 *
 * <pre>
 *   Criterion searchCriterion = new Parser().parse("foo AND bar");
 *   CriterionMatcher matcher = new StringSearchFactory().caseInsensitiveCriterionMatcher();
 *   boolean matches = matcher.matches(criterion, "Barefoot is a movie directed by Andrew Flemming.");
 *   // matches == true
 * </pre>
 *
 * <p>A special use case is that the {@link Criterion} must be matched against a number of attributes of an object.
 * This could be implemented by joining these attributes' values to a single string, separated by a character
 * that does not occur in the `Criterion`, and then matching
 * the {@link Criterion} against this resulting string. This approach however would require a lot of string copying
 * which is time consuming and produces a lot of gargabe on the heap. To overcome this problem
 * the method {@link CriterionMatcher#matches(Criterion, String...)} accepts a varargs string argument.
 * The strings passed to the method are treated as if they were all joined together to a single string, separated
 * by a character that does not occur in the {@link Criterion}.
 * </p>
 *
 * <pre>
 *   Criterion searchCriterion = new Parser().parse("foo AND bar");
 *   CriterionMatcher matcher = new StringSearchFactory().caseInsensitiveCriterionMatcher();
 *   boolean matches1 = matcher.matches(criterion, "Bart", "food");
 *   // matches1 == true
 *   boolean matches2 = matcher.matches(criterion, "ba", "rt food");
 *   // matches2 == false because "bar" is not found
 * </pre>
 *
 */
public class CriterionMatcher {

    private final BiPredicate<String, String> stringMatchesLiteral;

    public CriterionMatcher(BiPredicate<String, String> stringMatchesLiteral) {
        this.stringMatchesLiteral = stringMatchesLiteral;
    }

    public boolean matches(Criterion criterion, String... textElements) {
        Class<?> criterionClass = criterion.getClass();
        if (criterionClass.equals(StringLiteral.class)) {
            return matchesAny((StringLiteral) criterion, textElements);
        } else if (criterionClass.equals(Not.class)) {
            return !matches(((Not) criterion).getCriterion(), textElements);
        } else if (criterionClass.equals(And.class)) {
            return matches(((And) criterion).getLeft(), textElements) && matches(((And) criterion).getRight(), textElements);
        } else if (criterionClass.equals(Or.class)) {
            return matches(((Or) criterion).getLeft(), textElements) || matches(((Or) criterion).getRight(), textElements);
        } else {
            throw new IllegalArgumentException("Unknown Criterion implementation found: " + criterionClass);
        }
    }

    private boolean matchesAny(StringLiteral criterion, String... textElements) {
        for (String textElement : textElements) {
            if (stringMatchesLiteral.test(textElement, criterion.getLiteral())) {
                return true;
            }
        }
        return false;
    }
}
