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
    }

    @Test
    public void testCaseSenstiveStringSearch() {
        StringSearch stringSearch = factory.caseSensitiveStringSearch();
        assertNotNull(stringSearch);
        assertEquals(-1, stringSearch.indexOf("A", "a"));
        assertEquals(0, stringSearch.indexOf("A", "A"));
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

}