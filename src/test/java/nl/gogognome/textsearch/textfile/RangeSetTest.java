package nl.gogognome.textsearch.textfile;

import org.junit.Test;

import static org.junit.Assert.*;

public class RangeSetTest {

    @Test
    public void testAddRange() {
        assertEquals("{  }", new RangeSet().toString());
        assertEquals("{ [2,5) }", new RangeSet().addRange(new Range(2, 5)).toString());
        assertEquals("{ [0,1), [2,5) }", new RangeSet().addRange(new Range(2, 5)).addRange(new Range(0, 1)).toString());
        assertEquals("{ [0,1), [2,5) }", new RangeSet().addRange(new Range(0, 1)).addRange(new Range(2, 5)).toString());
        assertEquals("{ [0,5) }", new RangeSet().addRange(new Range(0, 3)).addRange(new Range(2, 5)).toString());
        assertEquals("{ [0,5) }", new RangeSet().addRange(new Range(2, 5)).addRange(new Range(0, 3)).toString());
        assertEquals("{ [0,5) }", new RangeSet().addRange(new Range(0, 3)).addRange(new Range(3, 5)).toString());
        assertEquals("{ [0,5) }", new RangeSet().addRange(new Range(3, 5)).addRange(new Range(0, 3)).toString());
        assertEquals("{ [3,5) }", new RangeSet().addRange(new Range(3, 5)).addRange(new Range(4, 4)).toString());
        assertEquals("{ [0,9) }", new RangeSet().addRange(new Range(1, 3)).addRange(new Range(4, 6)).addRange(new Range(0, 9)).toString());
    }
}