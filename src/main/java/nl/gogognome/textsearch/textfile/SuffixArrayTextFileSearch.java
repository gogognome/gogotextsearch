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

    SuffixArrayTextFileSearch(String text) {
        suffixArray = new SuffixArray(text, INSENSITIVE);
        multilineString = new MultilineString(text);
        dataLength = text.length();
    }

    @Override
    public Iterator<String> matchesIterator(Criterion criterion) {
        return new MatchesIterator(getRangeSetsFor(criterion).iterator());
    }

    private class MatchesIterator implements Iterator<String> {

        private final Iterator<Range> rangeSetIterator;
        private Range currentRange;

        public MatchesIterator(Iterator<Range> rangeSetIterator) {
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

    private RangeSet getRangeSetsFor(Criterion criterion) {
        Class<? extends Criterion> criterionClass = criterion.getClass();
        if (criterionClass.equals(StringLiteral.class)) {
            List<Integer> indexes = suffixArray.indexesOf(((StringLiteral) criterion).getLiteral());
            RangeSet rangeSet = new RangeSet();
            for (int index : indexes) {
                rangeSet.add(new Range(multilineString.getStartOfLine(index), multilineString.getEndOfLineIncludingNewLine(index)));
            }
            return rangeSet;
        } else if (criterionClass.equals(And.class)) {
            And and = (And) criterion;
            RangeSet rangeSet = getRangeSetsFor(and.getLeft());
            rangeSet.retain(getRangeSetsFor(and.getRight()));
            return rangeSet;
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

    @Override
    public String toString() {
        return getClass().getSimpleName() + " with data: " + suffixArray.toString();
    }
}
