package nl.gogognome.textsearch.criteria;

public interface Criterion {

    void accept(CriterionVisitor visitor);

}
