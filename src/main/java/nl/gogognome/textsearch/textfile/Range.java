package nl.gogognome.textsearch.textfile;

class Range implements Comparable<Range> {

    private final static Range EMPTY = new Range(0, 0);

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
        return this.start < that.end && that.start < this.end;
    }

    public Range intersection(Range that) {
        if (intersects(that)) {
            if (this.equals(that)) {
                return this;
            }
            return new Range(Math.max(this.start, that.start), Math.min(this.end, that.end));
        }
        return EMPTY;
    }

    public boolean contains(Range that) {
        return this.getStart() <= that.getStart() && this.getEnd() >= that.getEnd();
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
        int signum = this.start - that.start;
        return signum != 0 ? signum : this.end - that.end;
    }

    @Override
    public String toString() {
        return "[" + start + ',' + end + ')';
    }
}
