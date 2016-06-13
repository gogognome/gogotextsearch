package nl.gogognome.textsearch.string;

import nl.gogognome.textsearch.criteria.*;

/**
 * <p>This class checks whether a {@link Criterion} matches a specified String.</p>
 *
 * <p>Typical usage:</p>
 *
 * <pre>
 *   Criterion searchCriterion = new Parser().parse("foo AND bar");
 *   CriterionMatcher matcher = new StringSearchFactory().caseInsensitiveCriterionMatcher();
 *   boolean matches = matcher.matches("Barefoot is a movie directed by Andrew Flemming.", criterion);
 *   // matches == true
 * </pre>
 */
public class CriterionMatcher {

    private final StringSearch stringSearch;

    CriterionMatcher(StringSearch stringSearch) {
        this.stringSearch = stringSearch;
    }

    public boolean matches(String text, Criterion criterion) {
        Class<?> criterionClass = criterion.getClass();
        if (criterionClass.equals(StringLiteral.class)) {
            return stringSearch.indexOf(text, ((StringLiteral)criterion).getLiteral()) != -1;
        } else if (criterionClass.equals(Not.class)) {
            return !matches(text, ((Not) criterion).getCriterion());
        } else if (criterionClass.equals(And.class)) {
            return matches(text, ((And) criterion).getLeft()) && matches(text, ((And) criterion).getRight());
        } else if (criterionClass.equals(Or.class)) {
            return matches(text, ((Or) criterion).getLeft()) || matches(text, ((Or) criterion).getRight());
        } else {
            throw new IllegalArgumentException("Unknown Criterion implementation found: " + criterionClass);
        }
    }
}
