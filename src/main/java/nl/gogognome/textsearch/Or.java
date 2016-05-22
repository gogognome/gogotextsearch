package nl.gogognome.textsearch;

class Or implements Expression {

    private final Expression left;
    private final Expression right;


    public Or(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public boolean matches(String text) {
        return left.matches(text) || right.matches(text);
    }

    @Override
    public String toString() {
        return '(' + left.toString() + " OR " + right.toString() + ')';
    }
}
