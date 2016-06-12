package nl.gogognome.textsearch.string;

import nl.gogognome.textsearch.criteria.*;

public class StringSearch {

    private CaseInsensitiveStringSearch caseInsensitiveStringSearch = new CaseInsensitiveStringSearch();

    public boolean matches(String text, Criterion criterion) {
        Class<?> criterionClass = criterion.getClass();
        if (criterionClass.equals(StringLiteral.class)) {
            return caseInsensitiveStringSearch.indexOfIgnoreCase(text, ((StringLiteral)criterion).getLiteral()) != -1;
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
