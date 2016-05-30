package nl.gogognome.textsearch.textfile;

import org.junit.Test;

import static org.junit.Assert.*;

public class RangeSetTest {

    @Test
    public void testAddingRanges() {
        assertEquals("{  }", new RangeSet().toString());
        assertEquals("{ [2,5) }", new RangeSet().add(new Range(2, 5)).toString());
        assertEquals("{ [0,1), [2,5) }", new RangeSet().add(new Range(2, 5)).add(new Range(0, 1)).toString());
        assertEquals("{ [0,1), [2,5) }", new RangeSet().add(new Range(0, 1)).add(new Range(2, 5)).toString());
        assertEquals("{ [0,5) }", new RangeSet().add(new Range(0, 3)).add(new Range(2, 5)).toString());
        assertEquals("{ [0,5) }", new RangeSet().add(new Range(2, 5)).add(new Range(0, 3)).toString());
        assertEquals("{ [0,5) }", new RangeSet().add(new Range(0, 3)).add(new Range(3, 5)).toString());
        assertEquals("{ [0,5) }", new RangeSet().add(new Range(3, 5)).add(new Range(0, 3)).toString());
        assertEquals("{ [3,5) }", new RangeSet().add(new Range(3, 5)).add(new Range(4, 4)).toString());
        assertEquals("{ [0,9) }", new RangeSet().add(new Range(1, 3)).add(new Range(4, 6)).add(new Range(0, 9)).toString());
    }

    @Test
    public void testAddingRangeSets() {
        assertEquals("{  }", new RangeSet().add(new RangeSet()).toString());
        assertEquals("{ [3,7) }", new RangeSet().add(new RangeSet().add(new Range(3, 7))).toString());
        assertEquals("{ [1,5), [7,9) }", new RangeSet().add(new Range(1, 5)).add(new RangeSet().add(new Range(7, 9))).toString());
        assertEquals("{ [1,5), [7,9) }", new RangeSet().add(new Range(7, 9)).add(new RangeSet().add(new Range(1, 5))).toString());
        assertEquals("{ [3,9) }", new RangeSet().add(new Range(3, 8)).add(new RangeSet().add(new Range(4, 9))).toString());
    }

    @Test
    public void testRemovingRanges() {
        assertEquals("{  }", new RangeSet().remove(new Range(2, 5)).toString());
        assertEquals("{  }", new RangeSet().add(new Range(2, 5)).remove(new Range(2, 5)).toString());
        assertEquals("{ [0,2) }", new RangeSet().add(new Range(0, 3)).remove(new Range(2, 5)).toString());
        assertEquals("{ [5,8) }", new RangeSet().add(new Range(3, 8)).remove(new Range(2, 5)).toString());
        assertEquals("{ [0,2), [5,9) }", new RangeSet().add(new Range(0, 9)).remove(new Range(2, 5)).toString());
        assertEquals("{ [2,3), [10,11) }", new RangeSet().add(new Range(2, 4)).add(new Range(5, 7)).add(new Range(9,11)).remove(new Range(3,10)).toString());
    }
}