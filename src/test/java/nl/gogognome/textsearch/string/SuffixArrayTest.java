package nl.gogognome.textsearch.string;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;

import static nl.gogognome.textsearch.CaseSensitivity.INSENSITIVE;
import static nl.gogognome.textsearch.CaseSensitivity.SENSITIVE;
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
        assertEquals(0, caseSensitiveStringSearch("\nbla\n", "\nbla"));
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
        assertSearchCaseSensitiveIndexes("\nbla\n", "\nbla", 0);
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
        assertEquals(0, caseSensitiveStringSearch("\nbla\n", "\nbla"));
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

    private int caseSensitiveStringSearch(String text, String pattern) {
        return new SuffixArray(text, SENSITIVE).indexOf(pattern);
    }

    private void assertSearchCaseSensitiveIndexes(String text, String pattern, int... expectedIndexes) {
        Collection<Integer> actualIndexes = new SuffixArray(text, SENSITIVE).indexesOf(pattern);
        assertEquals(Arrays.toString(expectedIndexes), actualIndexes.toString());
    }

    private void assertSearchCaseInsensitiveIndexes(String text, String pattern, int... expectedIndexes) {
        Collection<Integer> actualIndexes = new SuffixArray(text, INSENSITIVE).indexesOf(pattern);
        assertEquals(Arrays.toString(expectedIndexes), actualIndexes.toString());
    }

    private int caseInsensitiveStringSearch(String text, String pattern) {
        return new SuffixArray(text, INSENSITIVE).indexOf(pattern);
    }

    @Test
    public void testToString() {
        assertEquals("", new SuffixArray("", INSENSITIVE).toString());

        assertEquals("a\n", new SuffixArray("a", INSENSITIVE).toString());

        assertEquals(
                "ab\n" +
                "b\n",
                new SuffixArray("ab", INSENSITIVE).toString());

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
                new SuffixArray("banananas", INSENSITIVE).toString());
    }

    @Test
    public void testSubstring() {
        assertEquals("cd", new SuffixArray("abcde", INSENSITIVE).substring(2, 4));
        assertEquals("abcde", new SuffixArray("abcde", SENSITIVE).substring(0, 5));
        assertEquals("ABCDE", new SuffixArray("ABCDE", SENSITIVE).substring(0, 5));

        assertSubstringShouldFail("abcde", 3, 2);
        assertSubstringShouldFail("abcde", -1, 2);
        assertSubstringShouldFail("abcde", 0, 6);
    }

    private void assertSubstringShouldFail(String text, int startIndex, int endIndex) {
        SuffixArray suffixArray = new SuffixArray(text, INSENSITIVE);
        try {
            suffixArray.substring(startIndex, endIndex);
            fail("Expected exception was not thrown!");
        } catch (Exception e) {
            assertTrue("Expected IndexOutOfBOundsException (or subclass) but found " + e.getClass(),
                    e instanceof IndexOutOfBoundsException);
        }
    }
}