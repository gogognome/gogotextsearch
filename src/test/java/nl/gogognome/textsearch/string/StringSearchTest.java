package nl.gogognome.textsearch.string;

import nl.gogognome.textsearch.criteria.*;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;

public class StringSearchTest {

    @Test
    public void testMatches() {
        assertMatches("bladiebla", new StringLiteral("dieb"));
        assertMatches("bladiebla", new StringLiteral("DiEb"));
        assertMatches("bladiebla", new And(new StringLiteral("bla"), new StringLiteral("dieb")));
        assertMatches("bladiebla", new Or(new StringLiteral("bla"), new StringLiteral("blup")));
        assertMatches("bladiebla", new Or(new StringLiteral("blup"), new StringLiteral("bla")));
        assertMatches("bladiebla", new Not(new StringLiteral("blup")));

        assertNotMatches("bladiebla", new StringLiteral("blup"));
        assertNotMatches("bladiebla", new And(new StringLiteral("blup"), new StringLiteral("dieb")));
        assertNotMatches("bladiebla", new And(new StringLiteral("dieb"), new StringLiteral("blup")));
        assertNotMatches("bladiebla", new Or(new StringLiteral("blop"), new StringLiteral("blup")));
        assertNotMatches("bladiebla", new Not(new StringLiteral("bla")));
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenUnknownCriterionImplementationIsPassedThenMatchesShouldThrowException() {
        new  StringSearch().matches("bladiebla", Mockito.mock(Criterion.class));
    }

    private void assertMatches(String text, Criterion criterion) {
        assertTrue(new StringSearch().matches(text, criterion));
    }

    private void assertNotMatches(String text, Criterion criterion) {
        assertFalse(new StringSearch().matches(text, criterion));
    }
}