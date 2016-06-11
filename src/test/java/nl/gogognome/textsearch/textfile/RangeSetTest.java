package nl.gogognome.textsearch.textfile;

import org.junit.Test;

import static org.junit.Assert.*;

public class RangeSetTest {

    @Test
    public void testAddingRanges() {
        assertEquals(rangeSetOf(), new RangeSet());
        assertEquals(rangeSetOf(2,5), new RangeSet().add(new Range(2, 5)));
        assertEquals(rangeSetOf(0,1, 2,5), new RangeSet().add(new Range(2, 5)).add(new Range(0, 1)));
        assertEquals(rangeSetOf(0,1, 2,5), new RangeSet().add(new Range(0, 1)).add(new Range(2, 5)));
        assertEquals(rangeSetOf(0,5), new RangeSet().add(new Range(0, 3)).add(new Range(2, 5)));
        assertEquals(rangeSetOf(0,5), new RangeSet().add(new Range(2, 5)).add(new Range(0, 3)));
        assertEquals(rangeSetOf(0,5), new RangeSet().add(new Range(0, 3)).add(new Range(3, 5)));
        assertEquals(rangeSetOf(0,5), new RangeSet().add(new Range(3, 5)).add(new Range(0, 3)));
        assertEquals(rangeSetOf(3,5), new RangeSet().add(new Range(3, 5)).add(new Range(4, 4)));
        assertEquals(rangeSetOf(0,9), new RangeSet().add(new Range(1, 3)).add(new Range(4, 6)).add(new Range(0, 9)));
    }

    @Test
    public void testAddingRangeSets() {
        assertEquals(rangeSetOf(), new RangeSet().add(new RangeSet()));
        assertEquals(rangeSetOf(3,7), new RangeSet().add(new RangeSet().add(new Range(3, 7))));
        assertEquals(rangeSetOf(1,5, 7,9), new RangeSet().add(new Range(1, 5)).add(new RangeSet().add(new Range(7, 9))));
        assertEquals(rangeSetOf(1,5, 7,9), new RangeSet().add(new Range(7, 9)).add(new RangeSet().add(new Range(1, 5))));
        assertEquals(rangeSetOf(3,9), new RangeSet().add(new Range(3, 8)).add(new RangeSet().add(new Range(4, 9))));
    }

    @Test
    public void testRemovingRanges() {
        assertEquals(rangeSetOf(), rangeSetOf().remove(new Range(2, 5)));
        assertEquals(rangeSetOf(), rangeSetOf(2, 5).remove(new Range(2, 5)));
        assertEquals(rangeSetOf(0,2), rangeSetOf(0,3).remove(new Range(2, 5)));
        assertEquals(rangeSetOf(5,8), rangeSetOf(3,8).remove(new Range(2, 5)));
        assertEquals(rangeSetOf(0,2, 5,9), rangeSetOf(0,9).remove(new Range(2, 5)));
        assertEquals(rangeSetOf(2,3, 10,11), rangeSetOf(2,4, 5,7,  9,11).remove(new Range(3, 10)));
    }

    @Test
    public void testRemovingRangeSets() {
        assertEquals(rangeSetOf(2,5), rangeSetOf(2,5).remove(rangeSetOf()));
        assertEquals(rangeSetOf(), new RangeSet().remove(rangeSetOf()));
        assertEquals(rangeSetOf(), new RangeSet().remove(rangeSetOf(2,5)));
        assertEquals(rangeSetOf(), rangeSetOf(2,5).remove(rangeSetOf(2,5)));
        assertEquals(rangeSetOf(0,2, 5,7, 10,11), rangeSetOf(0,3, 4,8, 9,11).remove(rangeSetOf(2,5, 7,10)));
    }

    @Test
    public void testRetain() {
        assertEquals(rangeSetOf(), rangeSetOf().retain(rangeSetOf()));
        assertEquals(rangeSetOf(2,5), rangeSetOf(2,5).retain(rangeSetOf(2,5)));
        assertEquals(rangeSetOf(2,3), rangeSetOf(2,5).retain(rangeSetOf(0,3)));
        assertEquals(rangeSetOf(2,3), rangeSetOf(0,3).retain(rangeSetOf(2,5)));
        assertEquals(rangeSetOf(4,5, 9,10), rangeSetOf(0,2, 4,7, 9,11).retain(rangeSetOf(2,5, 7,10)));
        assertEquals(rangeSetOf(4,5, 9,10), rangeSetOf(2,5, 7,10).retain(rangeSetOf(0,2, 4,7, 9,11)));
    }

    @Test
    public void testEquals() {
        assertEquals(rangeSetOf(), rangeSetOf());
        assertEquals(rangeSetOf(2,5), rangeSetOf(2,5));

        RangeSet r2_5__7_10 = rangeSetOf(2,5, 7,10);
        assertEquals(r2_5__7_10, r2_5__7_10);
        assertEquals(r2_5__7_10, rangeSetOf(2,5, 7,10));
        assertFalse(r2_5__7_10.equals(rangeSetOf(2,5)));
        assertFalse(r2_5__7_10.equals(rangeSetOf(7,10)));
        assertFalse(r2_5__7_10.equals(rangeSetOf(2,5, 7,10, 12,13)));
        assertFalse(r2_5__7_10.equals(rangeSetOf(2,10)));

        //noinspection ObjectEqualsNull
        assertFalse(r2_5__7_10.equals(null));
        assertFalse(r2_5__7_10.equals(new Object()));
    }

    @Test
    public void testHashCode() {
        RangeSet empty = rangeSetOf();
        RangeSet r2_5 = rangeSetOf(2,5);
        RangeSet r2_5__7_10 = rangeSetOf(2,5, 7,10);

        assertEquals(rangeSetOf().hashCode(), empty.hashCode());
        assertEquals(rangeSetOf(2,5).hashCode(), r2_5.hashCode());
        assertTrue(empty.hashCode() != r2_5.hashCode());
        assertTrue(empty.hashCode() != r2_5__7_10.hashCode());
        assertTrue(r2_5.hashCode() != r2_5__7_10.hashCode());
    }

    private RangeSet rangeSetOf(int... startEndIndices) {
        RangeSet rangeSet = new RangeSet();
        for (int i=0; i<startEndIndices.length; i+=2) {
            rangeSet.add(new Range(startEndIndices[i], startEndIndices[i+1]));
        }
        return rangeSet;
    }
}