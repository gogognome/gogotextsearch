package nl.gogognome.textsearch.string;

import nl.gogognome.textsearch.string.SuffixArray;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.*;

public class SuffixArrayTest {

    @Test
    public void testIndexOfCaseSensitive() {
        assertEquals(0, caseSensitiveStringSearch("A", "A"));
        assertEquals(-1, caseSensitiveStringSearch("a", "A"));
        assertEquals(-1, caseSensitiveStringSearch("A", "a"));
        assertEquals(0, caseSensitiveStringSearch("a", "a"));

        assertEquals(-1, caseSensitiveStringSearch("a", "ba"));
        assertEquals(1, caseSensitiveStringSearch("ba", "a"));
        assertEquals(1, caseSensitiveStringSearch("baba", "a"));

        assertEquals(-1, caseSensitiveStringSearch("Royal Blue", " Royal Blue"));
        assertEquals(1, caseSensitiveStringSearch(" Royal Blue", "Royal Blue"));
        assertEquals(-1, caseSensitiveStringSearch("Royal Blue", "royal"));
        assertEquals(1, caseSensitiveStringSearch("Royal Blue", "oyal"));
        assertEquals(3, caseSensitiveStringSearch("Royal Blue", "al"));
        assertEquals(-1, caseSensitiveStringSearch("", "royal"));
        assertEquals(-1, caseSensitiveStringSearch("Royal Blue", "BLUE"));
        assertEquals(-1, caseSensitiveStringSearch("Royal Blue", "BIGLONGSTRING"));
        assertEquals(-1, caseSensitiveStringSearch("Royal Blue", "Royal Blue LONGSTRING"));
    }

    @Test
    public void testIndexesOfCaseSensitive() {
        assertSearchCaseSensitiveIndexes("A", "A", 0);
        assertSearchCaseSensitiveIndexes("a", "A");
        assertSearchCaseSensitiveIndexes("A", "a");
        assertSearchCaseSensitiveIndexes("a", "a", 0);
        assertSearchCaseSensitiveIndexes("aa", "a", 0, 1);
        assertSearchCaseSensitiveIndexes("abba", "a", 0, 3);
        assertSearchCaseSensitiveIndexes("aaaaa", "a", 0, 1, 2, 3, 4);
    }

    @Test(expected = IllegalArgumentException.class)
    public void caseSensitiveStringSearchForNullShouldFail() {
        caseSensitiveStringSearch("bla", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void caseSensitiveStringSearchForEmptyStringShouldFail() {
        caseSensitiveStringSearch("bla", "");
    }

    @Test
    public void testIndexOfCaseInsensitive() {
        assertEquals(0, caseInsensitiveStringSearch("A", "A"));
        assertEquals(0, caseInsensitiveStringSearch("a", "A"));
        assertEquals(0, caseInsensitiveStringSearch("A", "a"));
        assertEquals(0, caseInsensitiveStringSearch("a", "a"));

        assertEquals(-1, caseInsensitiveStringSearch("a", "ba"));
        assertEquals(1, caseInsensitiveStringSearch("ba", "a"));
        assertEquals(1, caseInsensitiveStringSearch("baba", "a"));

        assertEquals(-1, caseInsensitiveStringSearch("Royal Blue", " Royal Blue"));
        assertEquals(1, caseInsensitiveStringSearch(" Royal Blue", "Royal Blue"));
        assertEquals(0, caseInsensitiveStringSearch("Royal Blue", "royal"));
        assertEquals(1, caseInsensitiveStringSearch("Royal Blue", "oyal"));
        assertEquals(3, caseInsensitiveStringSearch("Royal Blue", "al"));
        assertEquals(-1, caseInsensitiveStringSearch("", "royal"));
        assertEquals(6, caseInsensitiveStringSearch("Royal Blue", "BLUE"));
        assertEquals(-1, caseInsensitiveStringSearch("Royal Blue", "BIGLONGSTRING"));
        assertEquals(-1, caseInsensitiveStringSearch("Royal Blue", "Royal Blue LONGSTRING"));
    }

    @Test
    public void testIndexesOfCaseInsensitive() {
        assertSearchCaseInsensitiveIndexes("A", "A", 0);
        assertSearchCaseInsensitiveIndexes("a", "A", 0);
        assertSearchCaseInsensitiveIndexes("A", "a", 0);
        assertSearchCaseInsensitiveIndexes("a", "a", 0);
        assertSearchCaseInsensitiveIndexes("aA", "A", 0, 1);
        assertSearchCaseInsensitiveIndexes("abBA", "a", 0, 3);
        assertSearchCaseInsensitiveIndexes("aAaAa", "A", 0, 1, 2, 3, 4);
    }

    @Test(expected = IllegalArgumentException.class)
    public void caseInsensitiveStringSearchForNullShouldFail() {
        caseInsensitiveStringSearch("bla", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void caseInsnsitiveStringSearchForEmptyStringShouldFail() {
        caseInsensitiveStringSearch("bla", "");
    }

    private int caseSensitiveStringSearch(String data, String textToFind) {
        return new SuffixArray(data, true).indexOf(textToFind);
    }

    private void assertSearchCaseSensitiveIndexes(String data, String textToFind, int... expectedIndexes) {
        Collection<Integer> actualIndexes = new SuffixArray(data, true).indexesOf(textToFind);
        assertEquals(Arrays.toString(expectedIndexes), actualIndexes.toString());
    }

    private void assertSearchCaseInsensitiveIndexes(String data, String textToFind, int... expectedIndexes) {
        Collection<Integer> actualIndexes = new SuffixArray(data, false).indexesOf(textToFind);
        assertEquals(Arrays.toString(expectedIndexes), actualIndexes.toString());
    }

    private int caseInsensitiveStringSearch(String data, String textToFind) {
        return new SuffixArray(data, false).indexOf(textToFind);
    }

    @Test
    public void testStartOfLine() {
        SuffixArray suffixArray = new SuffixArray("bla\n\rdie\n\rbla", false);
        assertEquals(0, suffixArray.getStartOfLine(0));
        assertEquals(0, suffixArray.getStartOfLine(1));
        assertEquals(0, suffixArray.getStartOfLine(2));
        assertEquals(5, suffixArray.getStartOfLine(5));
        assertEquals(5, suffixArray.getStartOfLine(6));
        assertEquals(5, suffixArray.getStartOfLine(7));
        assertEquals(10, suffixArray.getStartOfLine(10));
        assertEquals(10, suffixArray.getStartOfLine(11));
        assertEquals(10, suffixArray.getStartOfLine(12));
    }

    @Test
    public void testEndOfLineIncludingNewLine() {
        SuffixArray suffixArray = new SuffixArray("bla\n\rdie\n\rbla", false);
        assertEquals(5, suffixArray.getEndOfLineIncludingNewLine(0));
        assertEquals(5, suffixArray.getEndOfLineIncludingNewLine(1));
        assertEquals(5, suffixArray.getEndOfLineIncludingNewLine(2));
        assertEquals(10, suffixArray.getEndOfLineIncludingNewLine(5));
        assertEquals(10, suffixArray.getEndOfLineIncludingNewLine(6));
        assertEquals(10, suffixArray.getEndOfLineIncludingNewLine(7));
        assertEquals(13, suffixArray.getEndOfLineIncludingNewLine(10));
        assertEquals(13, suffixArray.getEndOfLineIncludingNewLine(11));
        assertEquals(13, suffixArray.getEndOfLineIncludingNewLine(12));

        suffixArray = new SuffixArray("bla\ndie\n\nbla", false);
        assertEquals(4, suffixArray.getEndOfLineIncludingNewLine(0));
        assertEquals(8, suffixArray.getEndOfLineIncludingNewLine(4));
        assertEquals(8, suffixArray.getEndOfLineIncludingNewLine(7));
        assertEquals(9, suffixArray.getEndOfLineIncludingNewLine(8));
    }

    @Test
    public void testEndOfLineExcludingNewLine() {
        SuffixArray suffixArray = new SuffixArray("bla\n\rdie\n\rbla", false);
        assertEquals(3, suffixArray.getEndOfLineExcludingNewLine(0));
        assertEquals(3, suffixArray.getEndOfLineExcludingNewLine(1));
        assertEquals(3, suffixArray.getEndOfLineExcludingNewLine(2));
        assertEquals(3, suffixArray.getEndOfLineExcludingNewLine(3));
        assertEquals(4, suffixArray.getEndOfLineExcludingNewLine(4));
        assertEquals(8, suffixArray.getEndOfLineExcludingNewLine(5));
        assertEquals(8, suffixArray.getEndOfLineExcludingNewLine(6));
        assertEquals(8, suffixArray.getEndOfLineExcludingNewLine(7));
        assertEquals(8, suffixArray.getEndOfLineExcludingNewLine(8));
        assertEquals(9, suffixArray.getEndOfLineExcludingNewLine(9));
        assertEquals(13, suffixArray.getEndOfLineExcludingNewLine(10));
        assertEquals(13, suffixArray.getEndOfLineExcludingNewLine(11));
        assertEquals(13, suffixArray.getEndOfLineExcludingNewLine(12));
    }

    @Test
    public void testToString() {
        assertEquals("", new SuffixArray("", false).toString());

        assertEquals("a\n", new SuffixArray("a", false).toString());

        assertEquals(
                "ab\n" +
                "b\n",
                new SuffixArray("ab", false).toString());

        assertEquals(
                "anananas\n" +
                "ananas\n" +
                "anas\n" +
                "as\n" +
                "banananas\n" +
                "nananas\n" +
                "nanas\n" +
                "nas\n" +
                "s\n",
                new SuffixArray("banananas", false).toString());
    }

    @Test
    public void testSubstring() {
        assertEquals("cd", new SuffixArray("abcde", false).substring(2, 4));
        assertEquals("abcde", new SuffixArray("abcde", true).substring(0, 5));
        assertEquals("ABCDE", new SuffixArray("ABCDE", true).substring(0, 5));

        assertSubstringShouldFail("abcde", 3, 2);
        assertSubstringShouldFail("abcde", -1, 2);
        assertSubstringShouldFail("abcde", 0, 6);
    }

    private void assertSubstringShouldFail(String data, int startIndex, int endIndex) {
        SuffixArray suffixArray = new SuffixArray(data, false);
        try {
            suffixArray.substring(startIndex, endIndex);
            fail("Expected exception was not thrown!");
        } catch (Exception e) {
            assertTrue("Expected IndexOutOfBOundsException (or subclass) but found " + e.getClass(),
                    e instanceof IndexOutOfBoundsException);
        }
    }
}