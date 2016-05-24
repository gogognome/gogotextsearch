package nl.gogognome.textsearch.criteria;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class NotTest {

    private final Criterion otherCriterion = mock(Criterion.class);
    private final Not not = new Not(otherCriterion);

    @Test
    public void testGetters() {
        assertSame(otherCriterion, not.getCriterion());
    }

    @Test
    public void testToString() {
        when(otherCriterion.toString()).thenReturn("other");
        assertEquals("NOT other", not.toString());
    }
}