package nl.gogognome.textsearch;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrTest {

    private final Expression left = mock(Expression.class);
    private final Expression right = mock(Expression.class);
    private final Or or = new Or(left, right);

    @Test
    public void testMatches() {
        assertOrMatches(false, false, false);
        assertOrMatches(true, false, true);
        assertOrMatches(false, true, true);
        assertOrMatches(true, true, true);
    }

    private void assertOrMatches(boolean leftMatches, boolean rightMatches, boolean expectedOrMatches) {
        when(left.matches(anyString())).thenReturn(leftMatches);
        when(right.matches(anyString())).thenReturn(rightMatches);
        assertEquals(expectedOrMatches, or.matches("bla"));
    }
}