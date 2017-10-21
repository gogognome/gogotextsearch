package nl.gogognome.textsearch.criteria;

public class Or implements Criterion {

    private final Criterion left;
    private final Criterion right;

    public Or(Criterion left, Criterion right) {
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
    public <CV extends CriterionVisitor> CV accept(CV visitor) {
        visitor.visit(this);
        return visitor;
    }

    @Override
    public String toString() {
        return '(' + left.toString() + " OR " + right.toString() + ')';
    }
}
