package nl.gogognome.textsearch.criteria;

public interface Criterion {

    <CV extends CriterionVisitor> CV accept(CV visitor);

}
