package nl.gogognome.textsearch.string;

import nl.gogognome.textsearch.criteria.Criterion;
import nl.gogognome.textsearch.criteria.Parser;
import org.junit.Test;

import static org.junit.Assert.*;

public class StringSearchFactoryTest {

    private static final Criterion CRITERION_A = new Parser().parse("A");

    private final StringSearchFactory factory = new StringSearchFactory();

    @Test
    public void testCaseInsenstiveStringSearch() {
        StringSearch stringSearch = factory.caseInsensitiveStringSearch();
        assertNotNull(stringSearch);
        assertEquals(0, stringSearch.indexOf("A", "a"));
        assertEquals(0, stringSearch.indexOf("A", "A"));
        assertTrue(stringSearch.equals("A", "a"));
        assertTrue(stringSearch.equals("A", "A"));
        assertFalse(stringSearch.equals("A", "AB"));
    }

    @Test
    public void testCaseSenstiveStringSearch() {
        StringSearch stringSearch = factory.caseSensitiveStringSearch();
        assertNotNull(stringSearch);
        assertEquals(-1, stringSearch.indexOf("A", "a"));
        assertEquals(0, stringSearch.indexOf("A", "A"));
        assertFalse(stringSearch.equals("A", "a"));
        assertTrue(stringSearch.equals("A", "A"));
        assertFalse(stringSearch.equals("A", "AB"));
    }

    @Test
    public void testCaseInsensitiveCriterionMatcher() {
        CriterionMatcher criterionMatcher_A = factory.caseInsensitiveCriterionMatcher(CRITERION_A);
        assertTrue(criterionMatcher_A.matches("A"));
        assertTrue(criterionMatcher_A.matches("a"));
    }

    @Test
    public void testCaseInsensitiveCriterionMatcherBuilder() {
        CriterionMatcher.Builder criterionMatcherBuilder = factory.caseInsensitiveCriterionMatcherBuilder();
        CriterionMatcher criterionMatcher_A = criterionMatcherBuilder.build(CRITERION_A);
        assertTrue(criterionMatcher_A.matches("A"));
        assertTrue(criterionMatcher_A.matches("a"));
    }

    @Test
    public void testCaseSensitiveCriterionMatcher() {
        CriterionMatcher criterionMatcher_A = factory.caseSensitiveCriterionMatcher(CRITERION_A);
        assertTrue(criterionMatcher_A.matches("A"));
        assertFalse(criterionMatcher_A.matches("a"));
    }

    @Test
    public void testCaseSensitiveCriterionMatcherBuilder() {
        CriterionMatcher.Builder criterionMatcherBuilder = factory.caseSensitiveCriterionMatcherBuilder();
        CriterionMatcher criterionMatcher_A = criterionMatcherBuilder.build(CRITERION_A);
        assertTrue(criterionMatcher_A.matches("A"));
        assertFalse(criterionMatcher_A.matches("a"));
    }

    @Test
    public void testCaseInsensitiveStringEqualsCriterionMatcher() {
        CriterionMatcher criterionMatcher_A = factory.caseInsensitiveStringEqualsCriterionMatcher(CRITERION_A);
        assertTrue(criterionMatcher_A.matches("A"));
        assertTrue(criterionMatcher_A.matches("a"));
        assertFalse(criterionMatcher_A.matches("AB"));
    }

    @Test
    public void testCaseInsensitiveStringEqualsCriterionMatcherBuilder() {
        CriterionMatcher.Builder criterionMatcherBuilder = factory.caseInsensitiveStringEqualsCriterionMatcherBuilder();
        CriterionMatcher criterionMatcher_A = criterionMatcherBuilder.build(CRITERION_A);
        assertTrue(criterionMatcher_A.matches("A"));
        assertTrue(criterionMatcher_A.matches("a"));
        assertFalse(criterionMatcher_A.matches("ab"));
    }

    @Test
    public void testCaseSensitiveStringEqualsCriterionMatcher() {
        CriterionMatcher criterionMatcher_A = factory.caseSensitiveStringEqualsCriterionMatcher(CRITERION_A);
        assertTrue(criterionMatcher_A.matches("A"));
        assertFalse(criterionMatcher_A.matches("a"));
        assertFalse(criterionMatcher_A.matches("AB"));
    }

    @Test
    public void testCaseSensitiveStringEqualsCriterionMatcherBuilder() {
        CriterionMatcher.Builder criterionMatcherBuilder = factory.caseSensitiveStringEqualsCriterionMatcherBuilder();
        CriterionMatcher criterionMatcher_A = criterionMatcherBuilder.build(CRITERION_A);
        assertTrue(criterionMatcher_A.matches("A"));
        assertFalse(criterionMatcher_A.matches("a"));
        assertFalse(criterionMatcher_A.matches("AB"));
    }

}