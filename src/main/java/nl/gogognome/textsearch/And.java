package nl.gogognome.textsearch;

public class And implements Expression {

    private final Expression left;
    private final Expression right;


    public And(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public String toString() {
        return '(' + left.toString() + " AND " + right.toString() + ')';
    }
}
