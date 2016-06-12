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
        assertTrue(criterionMatcher.matches("A", new Parser().parse("A")));
        assertTrue(criterionMatcher.matches("A", new Parser().parse("a")));
    }

    @Test
    public void testCaseSensitveCriterionMatcher() {
        CriterionMatcher criterionMatcher = factory.caseSensitiveCriterionMatcher();
        assertTrue(criterionMatcher.matches("A", new Parser().parse("A")));
        assertFalse(criterionMatcher.matches("A", new Parser().parse("a")));
    }

}