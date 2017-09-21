package nl.gogognome.textsearch.string;

import nl.gogognome.textsearch.criteria.Parser;
import org.junit.Test;

import static org.junit.Assert.*;

public class StringSearchFactoryTest {

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
    public void testCaseInsensitveCriterionMatcher() {
        CriterionMatcher criterionMatcher = factory.caseInsensitiveCriterionMatcher();
        assertTrue(criterionMatcher.matches(new Parser().parse("A"), "A"));
        assertTrue(criterionMatcher.matches(new Parser().parse("a"), "A"));
    }

    @Test
    public void testCaseSensitveCriterionMatcher() {
        CriterionMatcher criterionMatcher = factory.caseSensitiveCriterionMatcher();
        assertTrue(criterionMatcher.matches(new Parser().parse("A"), "A"));
        assertFalse(criterionMatcher.matches(new Parser().parse("a"), "A"));
    }

    @Test
    public void testCaseInsensitveStringEqualsCriterionMatcher() {
        CriterionMatcher criterionMatcher = factory.caseInsensitiveStringEqualsCriterionMatcher();
        assertTrue(criterionMatcher.matches(new Parser().parse("A"), "A"));
        assertTrue(criterionMatcher.matches(new Parser().parse("a"), "A"));
        assertFalse(criterionMatcher.matches(new Parser().parse("a"), "ab"));
    }

    @Test
    public void testCaseSensitveStringEqualsCriterionMatcher() {
        CriterionMatcher criterionMatcher = factory.caseSensitiveStringEqualsCriterionMatcher();
        assertTrue(criterionMatcher.matches(new Parser().parse("A"), "A"));
        assertFalse(criterionMatcher.matches(new Parser().parse("a"), "A"));
        assertFalse(criterionMatcher.matches(new Parser().parse("a"), "ab"));
    }

}