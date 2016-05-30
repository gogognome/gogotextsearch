package nl.gogognome.textsearch.textfile;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static java.util.Collections.binarySearch;
import static java.util.stream.Collectors.joining;

class RangeSet implements Iterable<Range> {

    // invariant: ranges are sorted increasingly on start and are disjoint
    private final List<Range> ranges = new ArrayList<>();

    public Iterator<Range> iterator() {
        return ranges.iterator();
    }

    public RangeSet add(Range range) {
        int index = getInsertionIndex(range);
        ranges.add(index, range);
        while (index + 1 < ranges.size() && canBeJoinedWithNext(index)) {
            joinWithNext(index);
        }
        while (index > 0 && canBeJoinedWithNext(index-1)) {
            index--;
            joinWithNext(index);
        }
        return this;
    }

    public RangeSet add(RangeSet rangeSet) {
        for (Range range : rangeSet) {
           add(range);
        }
        return this;
    }

    public RangeSet remove(Range range) {
        int index = getInsertionIndex(range);
        if (index > 0 && ranges.get(index-1).intersects(range)) {
            index--;
        }
        while (index < ranges.size() && ranges.get(index).intersects(range)) {
            Range existingRange = ranges.get(index);
            if (range.contains(existingRange)) {
                ranges.remove(index);
            } else if (existingRange.getStart() <= range.getStart() && existingRange.getEnd() <= range.getEnd()) {
                ranges.set(index, new Range(existingRange.getStart(), range.getStart()));
                index++;
            } else if (existingRange.getStart() >= range.getStart() && existingRange.getEnd() >= range.getEnd()) {
                ranges.set(index, new Range(range.getEnd(), existingRange.getEnd()));
                index++;
            } else {
                ranges.set(index, new Range(existingRange.getStart(), range.getStart()));
                index++;
                ranges.add(index, new Range(range.getEnd(), existingRange.getEnd()));
                index++;
            }
        }
        return this;
    }

    private boolean canBeJoinedWithNext(int index) {
        Range range = ranges.get(index);
        Range nextRange = ranges.get(index + 1);
        return range.intersects(nextRange) || range.getEnd() == nextRange.getStart();
    }

    private void joinWithNext(int index) {
        Range range1 = ranges.get(index);
        Range range2 = ranges.get(index + 1);
        Range union = new Range(Math.min(range1.getStart(), range2.getStart()), Math.max(range1.getEnd(), range2.getEnd()));
        ranges.set(index, union);
        ranges.remove(index+1);
    }

    private int getInsertionIndex(Range range) {
        int index = binarySearch(ranges, range);
        if (index < 0) {
            index = -(index + 1);
        }
        return index;
    }

    @Override
    public String toString() {
        return "{ " + ranges.stream().map(Range::toString).collect(joining(", ")) + " }";
    }
}
