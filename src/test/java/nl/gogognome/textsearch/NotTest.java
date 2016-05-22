package nl.gogognome.textsearch;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class NotTest {

    private final Expression otherExpression = mock(Expression.class);
    private final Not not = new Not(otherExpression);

    @Test
    public void testMatches() {
        assertNotMatches(false, true);
        assertNotMatches(true, false);
    }

    private void assertNotMatches(boolean otherExpressionMatches, boolean expectedOrMatches) {
        when(otherExpression.matches(anyString())).thenReturn(otherExpressionMatches);
        assertEquals(expectedOrMatches, not.matches("bla"));
    }
}