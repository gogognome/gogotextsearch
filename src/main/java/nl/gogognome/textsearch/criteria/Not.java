package nl.gogognome.textsearch.criteria;

public class Not implements Criterion {

    private final Criterion criterion;

    public Not(Criterion criterion) {
        this.criterion = criterion;
    }

    public Criterion getCriterion() {
        return criterion;
    }

    @Override
    public <CV extends CriterionVisitor> CV accept(CV visitor) {
        visitor.visit(this);
        return visitor;
    }

    @Override
    public String toString() {
        return "NOT " + criterion.toString();
    }

}
