package nl.gogognome.textsearch.criteria;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrTest {

    private final Criterion left = mock(Criterion.class);
    private final Criterion right = mock(Criterion.class);
    private final Or or = new Or(left, right);

    @Test
    public void testGetters() {
        assertSame(left, or.getLeft());
        assertSame(right, or.getRight());
    }

    @Test
    public void testToString() {
        when(left.toString()).thenReturn("left");
        when(right.toString()).thenReturn("right");
        assertEquals("(left OR right)", or.toString());
    }
}