package nl.gogognome.textsearch;

import org.junit.Test;

import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Supplier;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

public class StringSearchTest {

    @Test(expected = IllegalArgumentException.class)
    public void constructWithNullShouldFail() {
        new StringSearch(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructWithEmptyStringShouldFail() {
        new StringSearch("");
    }

    @Test
    public void testFindIndexIn() {
        assertFindIndexIn("", "a", -1);
        assertFindIndexIn("abc", "a", 0);
        assertFindIndexIn("abc", "b", 1);
        assertFindIndexIn("abc", "c", 2);
        assertFindIndexIn("abacabcab", "abc", 4);
        assertFindIndexIn("XabcXabcX", "abc", 1);
        assertFindIndexIn("abc", "abc", 0);
        assertFindIndexIn("XXabc", "abc", 2);
        assertFindIndexIn("aaaabbaaabbaaaabbbb", "aaaabbbb", 11);
        assertFindIndexIn("abc", "B", -1);
    }

    @Test
    public void testCaseSensitivity() {
        assertFindIndexInIgnoringCase("abc", "B", 1);
        assertFindIndexInIgnoringCase("AbCdEfG", "CdE", 2);
        assertFindIndexInIgnoringCase("AbCdEfG", "cDe", 2);
    }

    @Test
    public void canSearchForSameTextInMultipleTexts() {
        StringSearch testSearch = new StringSearch("test");
        assertEquals(5, testSearch.findIndexIn("bla 'test'"));
        assertEquals(16, testSearch.findIndexIn("This is another test!"));
        assertEquals(8, testSearch.findIndexInIgnoringCase("Another TEST!"));
    }

    @Test
    public void findIndexInShouldBeFasterThanIndexOf() {
        String someText = "d fkj sdfkj sdfklasdhjkfhasjkdf jhfd sakdhfalksjhfd askjdhf ssf asjkshfshfashfhfa sdfashgds dfaljkshfshfshdfjklshdflkhasgd gf s dfjhjslkfhsljdhf s fjshf fasfhaskljhfsafsafsahfs hwefhw0efwejfw[efj asjfakshdfl hsaidfuhaso  shf shflhsd fhsafhasjd;afk sadfnjsahfjlksahdfsafsadf k ashdgf sgdfgfsgfjioehf sadbfjklsabdflahsbdf lkjsdf f hjsaf hsa dgfjsa gfsa gfsak dfsgd jlkdafsjkh bladieblabladiebla dflskfa das";

        AtomicLong nrIterationsIndexOf = new AtomicLong(0);
        executeForOneSecond(nrIterationsIndexOf, () -> someText.indexOf("bladieblabladiebla"));

        AtomicLong nrIterationsFindIndexOf = new AtomicLong(0);
        StringSearch bladiebla = new StringSearch("bladieblabladiebla");
        executeForOneSecond(nrIterationsFindIndexOf, () -> bladiebla.findIndexIn(someText));

        assertTrue("nr iterations findIndexOf: " + nrIterationsFindIndexOf + ", nr iterations indexOf: " + nrIterationsIndexOf,
                nrIterationsFindIndexOf.get() > nrIterationsIndexOf.get());
    }

    private void executeForOneSecond(AtomicLong nrIterations, Supplier<Integer> action) {
        int stepSize = 100000;
        long startTime = System.currentTimeMillis();
        long endTime = startTime + 1000;
        while (System.currentTimeMillis() < endTime) {
            for (int i = 0; i < stepSize; i++) {
                action.get();
            }
            nrIterations.incrementAndGet();
        }
    }

    private void assertFindIndexIn(String text, String pattern, int expectedIndex) {
        StringSearch stringSearch = new StringSearch(pattern);
        assertEquals("Pattern: " + pattern + ", text: " + text, expectedIndex, stringSearch.findIndexIn(text));
    }

    private void assertFindIndexInIgnoringCase(String text, String pattern, int expectedIndex) {
        StringSearch stringSearch = new StringSearch(pattern);
        assertEquals("Pattern: " + pattern + ", text: " + text, expectedIndex, stringSearch.findIndexInIgnoringCase(text));
    }
}
