package nl.gogognome.textsearch.textfile;

import nl.gogognome.textsearch.criteria.Parser;
import org.junit.Test;

import java.util.Iterator;

import static org.junit.Assert.*;

public class SuffixArrayTextFileSearchTest {

    @Test
    public void searchForBlaInEmptyData() {
        Iterator<String> iterator = getIteratorForDataAndCriterion("", "bla");
        assertFalse(iterator.hasNext());
    }

    @Test
    public void searchForBlaInBla() {
        Iterator<String> iterator = getIteratorForDataAndCriterion("bla", "bla");
        assertTrue(iterator.hasNext());
        assertEquals("bla", iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void searchForBlaInOneLineWithTwoMatches() {
        Iterator<String> iterator = getIteratorForDataAndCriterion("bla bla", "bla");
        assertTrue(iterator.hasNext());
        assertEquals("bla bla", iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void searchForBlaInTwoMatchingLines() {
        Iterator<String> iterator = getIteratorForDataAndCriterion("bla1\nbla2", "bla");
        assertTrue(iterator.hasNext());
        assertEquals("bla1", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("bla2", iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void searchFor_A_and_B_withOneMatchingLine() {
        Iterator<String> iterator = getIteratorForDataAndCriterion("A\nB\nBA", "A AND B");
        assertTrue(iterator.hasNext());
        assertEquals("BA", iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void searchFor_A_or_B_withThreeMatchingLines() {
        Iterator<String> iterator = getIteratorForDataAndCriterion("A\nB\nBA", "A OR B");
        assertTrue(iterator.hasNext());
        assertEquals("A", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("B", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("BA", iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void searchFor_not_B_withOneMatchingLine() {
        Iterator<String> iterator = getIteratorForDataAndCriterion("A\nB\nBA", "NOT B");
        assertTrue(iterator.hasNext());
        assertEquals("A", iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void searchFor_not_B_withTwoSubsequentMatchingLines() {
        Iterator<String> iterator = getIteratorForDataAndCriterion("A\nA\nB", "NOT B");
        assertTrue(iterator.hasNext());
        assertEquals("A", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("A", iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void searchForNonExistentOperatorShouldThrowAnException() {
        try {
            new SuffixArrayTextFileSearch("ABCD").matchesIterator(visitor -> {});
            fail("Expected exception was not thrown");
        } catch (Exception e) {
            assertEquals(IllegalArgumentException.class, e.getClass());
            assertTrue("actual message is: " + e.getMessage(),
                    e.getMessage().contains("Unsupported criterion class found: class nl.gogognome.textsearch.textfile.SuffixArrayTextFileSearchTest"));
        }
    }

    private Iterator<String> getIteratorForDataAndCriterion(String data, String criterion) {
        return new SuffixArrayTextFileSearch(data).matchesIterator(new Parser().parse(criterion));
    }
}