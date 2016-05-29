package nl.gogognome.textsearch.textfile;

import org.junit.Test;

import static org.junit.Assert.*;

public class RangeTest {

    private final Range range3_3 = new Range(3, 3);
    private final Range range0_5 = new Range(0, 5);
    private final Range range3_7 = new Range(3, 7);
    private final Range range4_8 = new Range(4, 8);
    private final Range range5_9 = new Range(5, 9);

    @Test(expected = IllegalArgumentException.class)
    public void whenStartAfterEndThenConstructorShouldThrowException() {
        new Range(5, 4);
    }

    @Test
    public void testToString() {
        assertEquals("[0,5)", range0_5.toString());
        assertEquals("[3,3)", range3_3.toString());
    }

    @Test
    public void testEquals() {
        assertEquals(range0_5, range0_5);
        assertEquals(range0_5, new Range(0, 5));
        assertEquals(range3_3, new Range(3, 3));
        assertFalse(range0_5.equals(range3_3));
        assertFalse(range0_5.equals(new Range(0, 6)));
        assertFalse(range0_5.equals(new Range(0, 4)));
        assertFalse(range0_5.equals(new Range(1, 5)));
        assertFalse(range0_5.equals(new Range(-1, 5)));
    }

    @Test
    public void testHashcode() {
        assertHashCodeEqualsIfRangesAreEqual(range0_5, new Range(0, 5), true);
        assertHashCodeEqualsIfRangesAreEqual(range3_3, new Range(3, 3), true);
        assertHashCodeEqualsIfRangesAreEqual(range0_5, range3_3, false);
        assertHashCodeEqualsIfRangesAreEqual(range0_5, new Range(0, 6), false);
        assertHashCodeEqualsIfRangesAreEqual(range0_5, new Range(0, 4), false);
        assertHashCodeEqualsIfRangesAreEqual(range0_5, new Range(1, 5), false);
        assertHashCodeEqualsIfRangesAreEqual(range0_5, new Range(-1, 5), false);
    }

    private void assertHashCodeEqualsIfRangesAreEqual(Range range1, Range range2, boolean expectedEquals) {
        assertEquals(expectedEquals, range1.hashCode() == range2.hashCode());
    }

    @Test
    public void testCompareTo() {
        assertTrue(range0_5.compareTo(range3_3) < 0);
        assertTrue(range0_5.compareTo(new Range(1, 5)) < 0);
        assertTrue(range3_3.compareTo(range0_5) > 0);
        assertTrue(range3_3.compareTo(new Range(2, 3)) > 0);
        assertTrue(range3_3.compareTo(range3_3) == 0);
        assertTrue(range3_3.compareTo(new Range(3, 6)) == 0);
    }

    @Test
    public void testIsEmpty() {
        assertTrue(range3_3.isEmpty());
        assertFalse(range0_5.isEmpty());
    }

    @Test
    public void testIntersects() {
        assertTrue(range0_5.intersects(range0_5));
        assertTrue(range0_5.intersects(range3_7));
        assertTrue(range0_5.intersects(range4_8));
        assertFalse(range0_5.intersects(range5_9));
        assertFalse(range3_3.intersects(range3_3));
        assertFalse(range3_3.intersects(range0_5));
        assertFalse(range0_5.intersects(range3_3));
    }
}