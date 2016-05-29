package nl.gogognome.textsearch.textfile;

import java.util.Comparator;

public class Range implements Comparable<Range> {

    private final int start;
    private final int end;

    public Range(int start, int end) {
        if (start > end) {
            throw new IllegalArgumentException("Start index must be smaller than or equal to end index");
        }
        this.start = start;
        this.end = end;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    public boolean isEmpty() {
        return start == end;
    }

    public boolean intersects(Range that) {
        return !this.isEmpty() && !that.isEmpty() && this.start < that.end && that.start < this.end;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Range) {
            Range that = (Range) obj;
            return this.start == that.start && this.end == that.end;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return 23* start + 81 * end;
    }

    @Override
    public int compareTo(Range that) {
        return this.start - that.start;
    }

    @Override
    public String toString() {
        return "[" + start + ',' + end + ')';
    }
}
