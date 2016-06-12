package nl.gogognome.textsearch.criteria;

public class StringLiteral implements Criterion {

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
