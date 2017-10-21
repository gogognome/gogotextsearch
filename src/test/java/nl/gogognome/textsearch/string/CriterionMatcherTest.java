package nl.gogognome.textsearch.string;

import nl.gogognome.textsearch.criteria.*;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;

public class CriterionMatcherTest {

    private final StringSearchFactory stringSearchFactory = new StringSearchFactory();

    @Test(expected = IllegalArgumentException.class)
    public void whenUnknownCriterionImplementationIsPassedThenMatchesShouldThrowException() {
        CriterionMatcher criterionMatcher = stringSearchFactory.caseSensitiveCriterionMatcher(Mockito.mock(Criterion.class));
        criterionMatcher.matches("bladiebla");
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
        CriterionMatcher criterionMatcher = stringSearchFactory.caseSensitiveCriterionMatcher(criterion);
        assertTrue(criterionMatcher.matches(text));
    }

    private void assertNotMatchesCaseSensitive(String text, Criterion criterion) {
        CriterionMatcher criterionMatcher = stringSearchFactory.caseSensitiveCriterionMatcher(criterion);
        assertFalse(criterionMatcher.matches(text));
    }

    @Test
    public void testMatches_caseInsensitive() {
        assertMatchesCaseInsensitive(new StringLiteral("DIEB"), "bladiebla");
        assertMatchesCaseInsensitive(new And(new StringLiteral("bla"), new StringLiteral("DIEB")), "bladiebla");
        assertMatchesCaseInsensitive(new Or(new StringLiteral("BLA"), new StringLiteral("blup")), "bladiebla");
        assertMatchesCaseInsensitive(new Or(new StringLiteral("blup"), new StringLiteral("BLA")), "bladiebla");
        assertMatchesCaseInsensitive(new Not(new StringLiteral("BlUp")), "bladiebla");

        assertNotMatchesCaseInsensitive(new StringLiteral("bLuP"), "bladiebla");
        assertNotMatchesCaseInsensitive(new And(new StringLiteral("blup"), new StringLiteral("DIEB")), "bladiebla");
        assertNotMatchesCaseInsensitive(new And(new StringLiteral("DIEB"), new StringLiteral("blup")), "bladiebla");
        assertNotMatchesCaseInsensitive(new Or(new StringLiteral("blop"), new StringLiteral("blup")), "bladiebla");
        assertNotMatchesCaseInsensitive(new Not(new StringLiteral("BLA")), "bladiebla");
    }

    @Test
    public void testMatchesWithZeroStringArguments() {
        assertNotMatchesCaseInsensitive(new StringLiteral("bla"));
        assertMatchesCaseInsensitive(new Not(new StringLiteral("bla")));
    }

    @Test
    public void testMatchesWithTwoStringArguments() {
        assertMatchesCaseInsensitive(new StringLiteral("foo"), "foo", "bar");
        assertMatchesCaseInsensitive(new StringLiteral("bar"), "foo", "bar");
        assertMatchesCaseInsensitive(new StringLiteral("bar"), "bar", "bar");
        assertNotMatchesCaseInsensitive(new StringLiteral("foo"), "one", "two");
        assertNotMatchesCaseInsensitive(new Not(new StringLiteral("foo")), "foo", "bar");
        assertNotMatchesCaseInsensitive(new Not(new StringLiteral("bar")), "foo", "bar");
    }

    private void assertMatchesCaseInsensitive(Criterion criterion, String... text) {
        CriterionMatcher criterionMatcher = stringSearchFactory.caseInsensitiveCriterionMatcher(criterion);
        assertTrue(criterionMatcher.matches(text));
    }

    private void assertNotMatchesCaseInsensitive(Criterion criterion, String... text) {
        CriterionMatcher criterionMatcher = stringSearchFactory.caseInsensitiveCriterionMatcher(criterion);
        assertFalse(criterionMatcher.matches(text));
    }
}