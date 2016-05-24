package nl.gogognome.textsearch.criteria;

import nl.gogognome.textsearch.criteria.And;
import nl.gogognome.textsearch.criteria.Expression;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AndTest {

    private final Expression left = mock(Expression.class);
    private final Expression right = mock(Expression.class);
    private final And and = new And(left, right);

    @Test
    public void testMatches() {
        assertAndMatches(false, false, false);
        assertAndMatches(true, false, false);
        assertAndMatches(false, true, false);
        assertAndMatches(true, true, true);
    }

    private void assertAndMatches(boolean leftMatches, boolean rightMatches, boolean expectedOrMatches) {
        when(left.matches(anyString())).thenReturn(leftMatches);
        when(right.matches(anyString())).thenReturn(rightMatches);
        assertEquals(expectedOrMatches, and.matches("bla"));
    }
}