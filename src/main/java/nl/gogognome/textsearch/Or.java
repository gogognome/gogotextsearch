package nl.gogognome.textsearch;

public class Or implements Expression {

    private final Expression left;
    private final Expression right;


    public Or(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public String toString() {
        return '(' + left.toString() + " OR " + right.toString() + ')';
    }
}
