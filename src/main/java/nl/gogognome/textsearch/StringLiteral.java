package nl.gogognome.textsearch;

public class StringLiteral implements Expression {

    private final String literal;

    public StringLiteral(String literal) {
        this.literal = literal;
    }

    @Override
    public String toString() {
        return literal;
    }
}
