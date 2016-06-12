package nl.gogognome.textsearch.string;

import nl.gogognome.textsearch.criteria.*;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;

public class CriterionMatcherTest {

    private final StringSearchFactory stringSearchFactory = new StringSearchFactory();

    @Test(expected = IllegalArgumentException.class)
    public void whenUnknownCriterionImplementationIsPassedThenMatchesShouldThrowException() {
        new CriterionMatcher(stringSearchFactory.caseSensitiveStringSearch()).matches("bladiebla", Mockito.mock(Criterion.class));
    }

    @Test
    public void testMatches_caseSensitive() {
        assertMatchesCaseSensitive("bladiebla", new StringLiteral("dieb"));
        assertMatchesCaseSensitive("bladiebla", new And(new StringLiteral("bla"), new StringLiteral("dieb")));
        assertMatchesCaseSensitive("bladiebla", new Or(new StringLiteral("bla"), new StringLiteral("blup")));
        assertMatchesCaseSensitive("bladiebla", new Or(new StringLiteral("blup"), new StringLiteral("bla")));
        assertMatchesCaseSensitive("bladiebla", new Not(new StringLiteral("blup")));

        assertNotMatchesCaseSensitive("bladiebla", new StringLiteral("blup"));
        assertNotMatchesCaseSensitive("bladiebla", new And(new StringLiteral("blup"), new StringLiteral("dieb")));
        assertNotMatchesCaseSensitive("bladiebla", new And(new StringLiteral("dieb"), new StringLiteral("blup")));
        assertNotMatchesCaseSensitive("bladiebla", new Or(new StringLiteral("blop"), new StringLiteral("blup")));
        assertNotMatchesCaseSensitive("bladiebla", new Not(new StringLiteral("bla")));
    }

    private void assertMatchesCaseSensitive(String text, Criterion criterion) {
        assertTrue(new CriterionMatcher(stringSearchFactory.caseSensitiveStringSearch()).matches(text, criterion));
    }

    private void assertNotMatchesCaseSensitive(String text, Criterion criterion) {
        assertFalse(new CriterionMatcher(stringSearchFactory.caseSensitiveStringSearch()).matches(text, criterion));
    }

    @Test
    public void testMatches_caseInsensitive() {
        assertMatchesCaseInsensitive("bladiebla", new StringLiteral("DIEB"));
        assertMatchesCaseInsensitive("bladiebla", new And(new StringLiteral("bla"), new StringLiteral("DIEB")));
        assertMatchesCaseInsensitive("bladiebla", new Or(new StringLiteral("BLA"), new StringLiteral("blup")));
        assertMatchesCaseInsensitive("bladiebla", new Or(new StringLiteral("blup"), new StringLiteral("BLA")));
        assertMatchesCaseInsensitive("bladiebla", new Not(new StringLiteral("BlUp")));

        assertNotMatchesCaseInsensitive("bladiebla", new StringLiteral("bLuP"));
        assertNotMatchesCaseInsensitive("bladiebla", new And(new StringLiteral("blup"), new StringLiteral("DIEB")));
        assertNotMatchesCaseInsensitive("bladiebla", new And(new StringLiteral("DIEB"), new StringLiteral("blup")));
        assertNotMatchesCaseInsensitive("bladiebla", new Or(new StringLiteral("blop"), new StringLiteral("blup")));
        assertNotMatchesCaseInsensitive("bladiebla", new Not(new StringLiteral("BLA")));
    }

    private void assertMatchesCaseInsensitive(String text, Criterion criterion) {
        assertTrue(new CriterionMatcher(stringSearchFactory.caseInsensitiveStringSearch()).matches(text, criterion));
    }

    private void assertNotMatchesCaseInsensitive(String text, Criterion criterion) {
        assertFalse(new CriterionMatcher(stringSearchFactory.caseInsensitiveStringSearch()).matches(text, criterion));
    }
}