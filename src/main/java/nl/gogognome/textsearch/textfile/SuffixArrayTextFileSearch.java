package nl.gogognome.textsearch.textfile;

import nl.gogognome.textsearch.SuffixArray;
import nl.gogognome.textsearch.criteria.And;
import nl.gogognome.textsearch.criteria.Criterion;
import nl.gogognome.textsearch.criteria.StringLiteral;

import java.util.Iterator;

public class SuffixArrayTextFileSearch implements TextFileSearch {

    private final SuffixArray suffixArray;

    SuffixArrayTextFileSearch(String data) {
        suffixArray = new SuffixArray(data, false);
    }

    @Override
    public Iterator<String> matchesIterator(Criterion criterion) {
        getIndexesFor(criterion);
        return null;
    }

    private void getIndexesFor(Criterion criterion) {
        Class<? extends Criterion> criterionClass = criterion.getClass();
        if (criterionClass.equals(StringLiteral.class)) {
            suffixArray.indexesOf(((StringLiteral) criterion).getLiteral());
        } else if (criterionClass.equals(And.class)) {
            And and = (And) criterion;
            getIndexesFor(and.getLeft());
            getIndexesFor(and.getRight());
        }
    }
}
