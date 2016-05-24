package nl.gogognome.textsearch.criteria;

class Not implements Expression {

    private final Expression expression;

    public Not(Expression expression) {
        this.expression = expression;
    }

    @Override
    public boolean matches(String text) {
        return !expression.matches(text);
    }

    @Override
    public String toString() {
        return "NOT " + expression.toString();
    }

}
