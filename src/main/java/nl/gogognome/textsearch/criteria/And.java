package nl.gogognome.textsearch.criteria;

public class And implements Criterion {

    private final Criterion left;
    private final Criterion right;

    public And(Criterion left, Criterion right) {
        this.left = left;
        this.right = right;
    }

    public Criterion getLeft() {
        return left;
    }

    public Criterion getRight() {
        return right;
    }

    @Override
    public String toString() {
        return '(' + left.toString() + " AND " + right.toString() + ')';
    }

}
