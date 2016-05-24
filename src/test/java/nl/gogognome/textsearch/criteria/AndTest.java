package nl.gogognome.textsearch.criteria;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AndTest {

    private final Criterion left = mock(Criterion.class);
    private final Criterion right = mock(Criterion.class);
    private final And and = new And(left, right);

    @Test
    public void testGetters() {
        assertSame(left, and.getLeft());
        assertSame(right, and.getRight());
    }

    @Test
    public void testToString() {
        when(left.toString()).thenReturn("left");
        when(right.toString()).thenReturn("right");
        assertEquals("(left AND right)", and.toString());
    }
}