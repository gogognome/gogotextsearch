package nl.textsearch;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class KnuthMorrisPrattStringSearchTest {

    @Test(expected = IllegalArgumentException.class)
    public void constructWithNullShouldFail() {
        new KnuthMorrisPrattStringSearch(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructWithEmptyStringShouldFail() {
        new KnuthMorrisPrattStringSearch("");
    }

    @Test
    public void testIndexOf() {
        assertIndexOf("", "a", -1);
        assertIndexOf("abc", "a", 0);
        assertIndexOf("abc", "b", 1);
        assertIndexOf("abc", "c", 2);
        assertIndexOf("abacabcab", "abc", 4);
        assertIndexOf("XabcXabcX", "abc", 1);
        assertIndexOf("abc", "abc", 0);
        assertIndexOf("XXabc", "abc", 2);
        assertIndexOf("aaaabbaaabbaaaabbbb", "aaaabbbb", 11);
    }

    @Test
    public void testCaseSensitivity() {
        assertIndexOf("abc", "B", -1);
        assertCaseInsensitiveIndexOf("abc", "B", 1);
        assertCaseInsensitiveIndexOf("AbCdEfG", "CdE", 2);
        assertCaseInsensitiveIndexOf("AbCdEfG", "cDe", 2);
    }

    @Test
    public void canSearchForSameTextInMultipleTexts() {
        KnuthMorrisPrattStringSearch testSearch = new KnuthMorrisPrattStringSearch("test");
        assertEquals(5, testSearch.indexOf("bla 'test'"));
        assertEquals(16, testSearch.indexOf("This is another test!"));
        assertEquals(8, testSearch.caseInsensitiveIndexOf("Another TEST!"));
    }

    private void assertIndexOf(String text, String pattern, int expectedIndex) {
        KnuthMorrisPrattStringSearch knuthMorrisPrattStringSearch = new KnuthMorrisPrattStringSearch(pattern);
        assertEquals("Pattern: " + pattern + ", text: " + text, expectedIndex, knuthMorrisPrattStringSearch.indexOf(text));
    }

    private void assertCaseInsensitiveIndexOf(String text, String pattern, int expectedIndex) {
        KnuthMorrisPrattStringSearch knuthMorrisPrattStringSearch = new KnuthMorrisPrattStringSearch(pattern);
        assertEquals("Pattern: " + pattern + ", text: " + text, expectedIndex, knuthMorrisPrattStringSearch.caseInsensitiveIndexOf(text));
    }
}
