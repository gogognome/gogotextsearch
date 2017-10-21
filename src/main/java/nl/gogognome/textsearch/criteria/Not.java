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
    public void accept(CriterionVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return "NOT " + criterion.toString();
    }

}
