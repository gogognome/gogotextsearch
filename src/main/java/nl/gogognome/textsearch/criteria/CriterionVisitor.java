package nl.gogognome.textsearch.criteria;

public interface CriterionVisitor {

   void visit(And and);
   void visit(Or or);
   void visit(Not not);
   void visit(StringLiteral stringLiteral);
}
