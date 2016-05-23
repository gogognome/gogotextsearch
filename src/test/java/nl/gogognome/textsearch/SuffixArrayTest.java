package nl.gogognome.textsearch;

import org.junit.Test;

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

        assertEquals(-1, caseSensitiveStringSearch("Royal Blue", " Royal Blue"));
        assertEquals(1, caseSensitiveStringSearch(" Royal Blue", "Royal Blue"));
        assertEquals(-1, caseSensitiveStringSearch("Royal Blue", "royal"));
        assertEquals(1, caseSensitiveStringSearch("Royal Blue", "oyal"));
        assertEquals(3, caseSensitiveStringSearch("Royal Blue", "al"));
        assertEquals(-1, caseSensitiveStringSearch("", "royal"));
        assertEquals(0, caseSensitiveStringSearch("Royal Blue", ""));
        assertEquals(-1, caseSensitiveStringSearch("Royal Blue", "BLUE"));
        assertEquals(-1, caseSensitiveStringSearch("Royal Blue", "BIGLONGSTRING"));
        assertEquals(-1, caseSensitiveStringSearch("Royal Blue", "Royal Blue LONGSTRING"));
    }

    @Test
    public void testIndexOfCaseInsensitive() {
        assertEquals(0, caseInsensitiveStringSearch("A", "A"));
        assertEquals(0, caseInsensitiveStringSearch("a", "A"));
        assertEquals(0, caseInsensitiveStringSearch("A", "a"));
        assertEquals(0, caseInsensitiveStringSearch("a", "a"));

        assertEquals(-1, caseInsensitiveStringSearch("a", "ba"));
        assertEquals(1, caseInsensitiveStringSearch("ba", "a"));

        assertEquals(-1, caseInsensitiveStringSearch("Royal Blue", " Royal Blue"));
        assertEquals(1, caseInsensitiveStringSearch(" Royal Blue", "Royal Blue"));
        assertEquals(0, caseInsensitiveStringSearch("Royal Blue", "royal"));
        assertEquals(1, caseInsensitiveStringSearch("Royal Blue", "oyal"));
        assertEquals(3, caseInsensitiveStringSearch("Royal Blue", "al"));
        assertEquals(-1, caseInsensitiveStringSearch("", "royal"));
        assertEquals(0, caseInsensitiveStringSearch("Royal Blue", ""));
        assertEquals(6, caseInsensitiveStringSearch("Royal Blue", "BLUE"));
        assertEquals(-1, caseInsensitiveStringSearch("Royal Blue", "BIGLONGSTRING"));
        assertEquals(-1, caseInsensitiveStringSearch("Royal Blue", "Royal Blue LONGSTRING"));
    }

    private int caseSensitiveStringSearch(String data, String textToFind) {
        return new SuffixArray(data, true).indexOf(textToFind);
    }

    private int caseInsensitiveStringSearch(String data, String textToFind) {
        return new SuffixArray(data, false).indexOf(textToFind);
    }

}