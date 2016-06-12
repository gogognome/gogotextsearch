package nl.gogognome.textsearch.criteria;

import nl.gogognome.textsearch.string.CaseInsensitiveStringSearch;

public class StringLiteral implements Criterion {

    private final static CaseInsensitiveStringSearch CASE_INSENSITIVE_STRING_SEARCH = new CaseInsensitiveStringSearch();
    private final String literal;

    public StringLiteral(String literal) {
        this.literal = literal;
    }

    public String getLiteral() {
        return literal;
    }

    @Override
    public String toString() {
        return literal;
    }
}
