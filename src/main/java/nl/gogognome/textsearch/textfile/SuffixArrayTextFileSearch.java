package nl.gogognome.textsearch.textfile;

import nl.gogognome.textsearch.SuffixArray;
import nl.gogognome.textsearch.criteria.*;

import java.util.Iterator;
import java.util.List;

public class SuffixArrayTextFileSearch implements TextFileSearch {

    private final SuffixArray suffixArray;
    private final int dataLength;

    SuffixArrayTextFileSearch(String data) {
        suffixArray = new SuffixArray(data, false);
        dataLength = data.length();
    }

    @Override
    public Iterator<String> matchesIterator(Criterion criterion) {
        Iterator<Range> rangeSetIterator = getRangeSetsFor(criterion).iterator();
        return new Iterator<String>() {

            @Override
            public boolean hasNext() {
                return rangeSetIterator.hasNext();
            }

            @Override
            public String next() {
                Range range = rangeSetIterator.next();
                return suffixArray.substring(range.getStart(), range.getEnd());
            }
        };
    }

    private RangeSet getRangeSetsFor(Criterion criterion) {
        Class<? extends Criterion> criterionClass = criterion.getClass();
        if (criterionClass.equals(StringLiteral.class)) {
            List<Integer> indexes = suffixArray.indexesOf(((StringLiteral) criterion).getLiteral());
            RangeSet rangeSet = new RangeSet();
            for (int index : indexes) {
                rangeSet.add(new Range(suffixArray.getStartOfLine(index), suffixArray.getEndOfLine(index)));
            }
            return rangeSet;
        } else if (criterionClass.equals(And.class)) {
            And and = (And) criterion;
            RangeSet rangeSet = getRangeSetsFor(and.getLeft());
            rangeSet.retain(getRangeSetsFor(and.getRight()));
        } else if (criterionClass.equals(Or.class)) {
            Or or = (Or) criterion;
            RangeSet rangeSet = getRangeSetsFor(or.getLeft());
            rangeSet.add(getRangeSetsFor(or.getRight()));
            return rangeSet;
        } else if (criterionClass.equals(Not.class)) {
            Not not = (Not) criterion;
            return new RangeSet().add(new Range(0, dataLength)).remove(getRangeSetsFor(not.getCriterion()));
        }
        throw new IllegalArgumentException("Unsupported criterion class found: " + criterion.getClass());
    }
}
