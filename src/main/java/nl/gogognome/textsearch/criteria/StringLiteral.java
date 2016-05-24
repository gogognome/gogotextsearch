package nl.gogognome.textsearch.criteria;

import nl.gogognome.textsearch.CaseInsensitiveStringSearch;

class StringLiteral implements Expression {

    private final static CaseInsensitiveStringSearch CASE_INSENSITIVE_STRING_SEARCH = new CaseInsensitiveStringSearch();
    private final String literal;

    public StringLiteral(String literal) {
        this.literal = literal;
    }

    @Override
    public boolean matches(String text) {
        return CASE_INSENSITIVE_STRING_SEARCH.indexOfIgnoreCase(text, literal) != -1;
    }

    @Override
    public String toString() {
        return literal;
    }
}
