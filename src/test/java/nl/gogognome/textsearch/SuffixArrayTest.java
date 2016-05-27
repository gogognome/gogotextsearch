package nl.gogognome.textsearch;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

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

}