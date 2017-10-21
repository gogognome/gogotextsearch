package nl.gogognome.textsearch.textfile;

import nl.gogognome.textsearch.criteria.*;
import nl.gogognome.textsearch.string.SuffixArray;

import java.util.Iterator;
import java.util.List;

import static nl.gogognome.textsearch.CaseSensitivity.INSENSITIVE;

/**
 * <p>This class is intended for searching a file multiple times. Its constructor expects a String
 * of the file's contents. Next call {@link #matchesIterator(Criterion)}}  with a {@link Criterion} as often as
 * you want to find matches.</p>
 *
 * <p>Example usage:</p>
 * <pre>
 *    SuffixArrayTextFileSearch fileSearch = new SuffixArrayTextFileSearch("Some huge file's contents here...");
 *    Iterator<String> iter1 = fileSearch.matchesIterator(new Parser().parse("foo AND bar"));
 *    // Iterate until iter1 has no more elements
 *    Iterator<String> iter2 = fileSearch.matchesIterator(new Parser().parse("'something else'"));
 *    // Iterate until iter2 has no more elements
 * </pre>
 */
public class SuffixArrayTextFileSearch implements TextFileSearch {

    private final SuffixArray suffixArray;
    private final MultilineString multilineString;
    private final int dataLength;

    public SuffixArrayTextFileSearch(String text) {
        suffixArray = new SuffixArray(text, INSENSITIVE);
        multilineString = new MultilineString(text);
        dataLength = text.length();
    }

    @Override
    public Iterator<String> matchesIterator(Criterion criterion) {
        RangeSet rangeSet = new RangeSetBuilder().getRangeSetsFor(criterion);
        if (rangeSet == null) {
            throw new IllegalArgumentException("The criterion could not be converted to a range set");
        }
        return new MatchesIterator(rangeSet.iterator());
    }

    private class MatchesIterator implements Iterator<String> {

        private final Iterator<Range> rangeSetIterator;
        private Range currentRange;

        MatchesIterator(Iterator<Range> rangeSetIterator) {
            this.rangeSetIterator = rangeSetIterator;
            if (rangeSetIterator.hasNext()) {
                currentRange = rangeSetIterator.next();
            }
        }

        @Override
        public boolean hasNext() {
            return currentRange != null;
        }

        @Override
        public String next() {
            String result = suffixArray.substring(currentRange.getStart(), multilineString.getEndOfLineExcludingNewLine(currentRange.getStart()));
            int nextStart = multilineString.getEndOfLineIncludingNewLine(currentRange.getStart());
            currentRange = new Range(nextStart, currentRange.getEnd());
            if (currentRange.isEmpty()) {
                currentRange = rangeSetIterator.hasNext() ? rangeSetIterator.next() : null;
            }
            return result;
        }
    }

    private class RangeSetBuilder implements CriterionVisitor {

        private RangeSet rangeSet;

        RangeSet getRangeSet() {
            return rangeSet;
        }

        @Override
        public void visit(And and) {
            RangeSet leftRangeSet = getRangeSetsFor(and.getLeft());
            leftRangeSet.retain(getRangeSetsFor(and.getRight()));
            rangeSet = leftRangeSet;
        }

        @Override
        public void visit(Or or) {
            RangeSet leftRangeSet = getRangeSetsFor(or.getLeft());
            leftRangeSet.add(getRangeSetsFor(or.getRight()));
            rangeSet = leftRangeSet;
        }

        @Override
        public void visit(Not not) {
            rangeSet = new RangeSet()
                    .add(new Range(0, dataLength))
                    .remove(getRangeSetsFor(not.getCriterion()));
        }

        @Override
        public void visit(StringLiteral stringLiteral) {
            List<Integer> indexes = suffixArray.indexesOf((stringLiteral).getLiteral());
            rangeSet = new RangeSet();
            for (int index : indexes) {
                rangeSet.add(new Range(multilineString.getStartOfLine(index), multilineString.getEndOfLineIncludingNewLine(index)));
            }
        }

        private RangeSet getRangeSetsFor(Criterion criterion) {
            criterion.accept(this);
            return getRangeSet();
        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " with data: " + suffixArray.toString();
    }
}
