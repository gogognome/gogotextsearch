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
    public <CV extends CriterionVisitor> CV accept(CV visitor) {
        visitor.visit(this);
        return visitor;
    }

    @Override
    public String toString() {
        return literal;
    }
}
