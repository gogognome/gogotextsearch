package nl.gogognome.textsearch;

public class Not implements Expression {

    private final Expression expression;

    public Not(Expression expression) {
        this.expression = expression;
    }

    @Override
    public String toString() {
        return "NOT " + expression.toString();
    }
}
