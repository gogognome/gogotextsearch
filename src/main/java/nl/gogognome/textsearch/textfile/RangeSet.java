package nl.gogognome.textsearch.textfile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static java.util.stream.Collectors.joining;

class RangeSet implements Iterable<Range> {

    // invariant: ranges are sorted increasingly on start and are disjoint
    private final List<Range> ranges = new ArrayList<>();

    public Iterator<Range> iterator() {
        return ranges.iterator();
    }

    public RangeSet addRange(Range range) {
        int index = Collections.binarySearch(ranges, range);
        if (index < 0) {
            index = -(index + 1);
        }
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

    @Override
    public String toString() {
        return "{ " + ranges.stream().map(Range::toString).collect(joining(", ")) + " }";
    }
}
