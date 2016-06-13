package nl.gogognome.textsearch.textfile;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static java.util.Collections.binarySearch;
import static java.util.stream.Collectors.joining;

/**
 * <p>This class implements a set of {@link Range}s. Instances of this class are mutable.</p>
 * <p>This range set keeps a sorted list of non-overlapping {@link Range}s to represent the range set.</p>
 */
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

    private void joinWithNext(int index) {
        Range range1 = ranges.get(index);
        Range range2 = ranges.get(index + 1);
        Range union = new Range(Math.min(range1.getStart(), range2.getStart()), Math.max(range1.getEnd(), range2.getEnd()));
        ranges.set(index, union);
        ranges.remove(index+1);
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

    public RangeSet remove(RangeSet rangeSet) {
        for (Range range : rangeSet) {
            remove(range);
        }
        return this;
    }

    private boolean canBeJoinedWithNext(int index) {
        Range range = ranges.get(index);
        Range nextRange = ranges.get(index + 1);
        return range.intersects(nextRange) || range.getEnd() == nextRange.getStart();
    }

    private int getInsertionIndex(Range range) {
        int index = binarySearch(ranges, range);
        if (index < 0) {
            index = -(index + 1);
        }
        return index;
    }

    /**
     * Determines the intersection of this range set and the parameter and then changes this change set to be
     * equal to the intersection.
     * @param rangeSet a range set
     * @return this (for method chaining
     */
    public RangeSet retain(RangeSet rangeSet) {
        List<Range> ranges1 = new ArrayList<>(this.ranges);
        List<Range> ranges2 = rangeSet.ranges;
        int i1 = 0;
        int i2 = 0;
        ranges.clear();

        // invariant: ranges contains the intersection of ranges1[0..i1) and ranges2[0..i2)
        // bound: ranges1.size() + ranges2.size() - i1 - i2 >= 0
        while (i1 < ranges1.size() && i2 < ranges2.size()) {
            Range range1 = ranges1.get(i1);
            Range range2 = ranges2.get(i2);
            Range intersection = range1.intersection(range2);
            if (!intersection.isEmpty()) {
                ranges.add(intersection);
                if (range1.getEnd() < range2.getEnd()) {
                    i1++;
                } else {
                    i2++;
                }
            } else if (range1.getEnd() <= range2.getStart()) {
                i1++;
            } else  {
                i2++;
            }
        }

        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof RangeSet) {
            RangeSet that = (RangeSet) obj;
            return this.ranges.equals(that.ranges);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return ranges.hashCode();
    }

    @Override
    public String toString() {
        return "{ " + ranges.stream().map(Range::toString).collect(joining(", ")) + " }";
    }
}
