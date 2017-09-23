package nl.gogognome.textsearch.string;

import org.junit.Test;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static nl.gogognome.textsearch.CaseSensitivity.INSENSITIVE;
import static nl.gogognome.textsearch.CaseSensitivity.SENSITIVE;
import static org.junit.Assert.assertEquals;

public class FastStringSearchTest {

    @Test
    public void indexOf_caseInsensitive_underSmallTextLimit() {
        FastStringSearch fastStringSearch = new FastStringSearch(INSENSITIVE);
        fastStringSearch.setSmallTextLimit(Long.MAX_VALUE);
        testIndexOfCaseInsensitive(fastStringSearch);
    }

    @Test
    public void indexOf_caseInsensitive_aboveSmallTextLimit() {
        FastStringSearch fastStringSearch = new FastStringSearch(INSENSITIVE);
        fastStringSearch.setSmallTextLimit(0);
        testIndexOfCaseInsensitive(fastStringSearch);
    }

    private void testIndexOfCaseInsensitive(FastStringSearch fastStringSearch) {
        assertEquals(-1, fastStringSearch.indexOf(null, "A"));
        assertEquals(-1, fastStringSearch.indexOf("A", null));
        assertEquals(-1, fastStringSearch.indexOf(null, null));
        assertEquals(0, fastStringSearch.indexOf("A", "A"));
        assertEquals(0, fastStringSearch.indexOf("a", "A"));
        assertEquals(0, fastStringSearch.indexOf("A", "a"));
        assertEquals(0, fastStringSearch.indexOf("a", "a"));

        assertEquals(-1, fastStringSearch.indexOf("a", "ba"));
        assertEquals(1, fastStringSearch.indexOf("ba", "a"));

        assertEquals(-1, fastStringSearch.indexOf("Royal Blue", " Royal Blue"));
        assertEquals(1, fastStringSearch.indexOf(" Royal Blue", "Royal Blue"));
        assertEquals(0, fastStringSearch.indexOf("Royal Blue", "royal"));
        assertEquals(1, fastStringSearch.indexOf("Royal Blue", "oyal"));
        assertEquals(3, fastStringSearch.indexOf("Royal Blue", "al"));
        assertEquals(-1, fastStringSearch.indexOf("", "royal"));
        assertEquals(0, fastStringSearch.indexOf("Royal Blue", ""));
        assertEquals(6, fastStringSearch.indexOf("Royal Blue", "BLUE"));
        assertEquals(-1, fastStringSearch.indexOf("Royal Blue", "BIGLONGSTRING"));
        assertEquals(-1, fastStringSearch.indexOf("Royal Blue", "Royal Blue LONGSTRING"));

        assertEquals(emptyList(), fastStringSearch.indexesOf("bla", "bananas"));
        assertEquals(asList(0), fastStringSearch.indexesOf("bananas", "bananas"));
        assertEquals(asList(0, 8), fastStringSearch.indexesOf("bananas bAnAnAs", "bananas"));
        assertEquals(asList(0, 8, 16), fastStringSearch.indexesOf("bananas BANANAS bananas", "bananas"));

        assertEquals(asList(0, 8, 16), fastStringSearch.indexesOf("bananas BANANAS bananas", "bananas"));

        assertEquals(asList(0, 1, 2, 3, 4, 5), fastStringSearch.indexesOf("aAaAaAa", "aa"));
        assertEquals(asList(0, 1, 2, 3, 4, 5, 6, 7), fastStringSearch.indexesOf("aaaaaaa", ""));

        assertEquals(0, fastStringSearch.indexOf("bla BLA", "bla", 0));
        assertEquals(4, fastStringSearch.indexOf("bla BLA", "bla", 1));
        assertEquals(-1, fastStringSearch.indexOf("bla BLA", "bla", 5));
        assertEquals(-1, fastStringSearch.indexOf("bla BLA", "bla", 7));
        assertEquals(0, fastStringSearch.indexOf("bla bla", "", 0));
        assertEquals(6, fastStringSearch.indexOf("bla bla", "", 6));
        assertEquals(7, fastStringSearch.indexOf("bla bla", "", 7));
        assertEquals(-1, fastStringSearch.indexOf("bla bla", "", 8));
    }

    @Test
    public void indexOf_caseSensitive_underSmallTextLimit() {
        FastStringSearch fastStringSearch = new FastStringSearch(SENSITIVE);
        fastStringSearch.setSmallTextLimit(Long.MAX_VALUE);
        testIndexOfCaseSensitive(fastStringSearch);
    }

    @Test
    public void indexOf_caseSensitive_aboveSmallTextLimit() {
        FastStringSearch fastStringSearch = new FastStringSearch(SENSITIVE);
        fastStringSearch.setSmallTextLimit(0);
        testIndexOfCaseSensitive(fastStringSearch);
    }

    private void testIndexOfCaseSensitive(FastStringSearch fastStringSearch) {
        assertEquals(-1, fastStringSearch.indexOf(null, "A"));
        assertEquals(-1, fastStringSearch.indexOf("A", null));
        assertEquals(-1, fastStringSearch.indexOf(null, null));
        assertEquals(0, fastStringSearch.indexOf("A", "A"));
        assertEquals(-1, fastStringSearch.indexOf("a", "A"));
        assertEquals(-1, fastStringSearch.indexOf("A", "a"));
        assertEquals(0, fastStringSearch.indexOf("a", "a"));

        assertEquals(-1, fastStringSearch.indexOf("a", "ba"));
        assertEquals(1, fastStringSearch.indexOf("ba", "a"));

        assertEquals(-1, fastStringSearch.indexOf("Royal Blue", " Royal Blue"));
        assertEquals(1, fastStringSearch.indexOf(" Royal Blue", "Royal Blue"));
        assertEquals(-1, fastStringSearch.indexOf("Royal Blue", "royal"));
        assertEquals(1, fastStringSearch.indexOf("Royal Blue", "oyal"));
        assertEquals(3, fastStringSearch.indexOf("Royal Blue", "al"));
        assertEquals(-1, fastStringSearch.indexOf("", "royal"));
        assertEquals(0, fastStringSearch.indexOf("Royal Blue", ""));
        assertEquals(-1, fastStringSearch.indexOf("Royal Blue", "BLUE"));
        assertEquals(-1, fastStringSearch.indexOf("Royal Blue", "BIGLONGSTRING"));
        assertEquals(-1, fastStringSearch.indexOf("Royal Blue", "Royal Blue LONGSTRING"));

        assertEquals(emptyList(), fastStringSearch.indexesOf("bananas", "bla"));
        assertEquals(asList(0), fastStringSearch.indexesOf("bananas", "bananas"));
        assertEquals(asList(0, 8), fastStringSearch.indexesOf("bananas bananas", "bananas"));
        assertEquals(asList(0, 8, 16), fastStringSearch.indexesOf("bananas bananas bananas", "bananas"));
        assertEquals(asList(0, 16), fastStringSearch.indexesOf("bananas BANANAS bananas", "bananas"));

        assertEquals(asList(0, 16), fastStringSearch.indexesOf("bananas BANANAS bananas", "bananas"));

        assertEquals(asList(0, 1, 2, 3, 4, 5), fastStringSearch.indexesOf("aaaaaaa", "aa"));
        assertEquals(asList(0, 1, 2, 3, 4, 5, 6, 7), fastStringSearch.indexesOf("aaaaaaa", ""));

        assertEquals(0, fastStringSearch.indexOf("bla bla", "bla", 0));
        assertEquals(4, fastStringSearch.indexOf("bla bla", "bla", 1));
        assertEquals(-1, fastStringSearch.indexOf("bla bla", "bla", 5));
        assertEquals(-1, fastStringSearch.indexOf("bla bla", "bla", 7));
        assertEquals(0, fastStringSearch.indexOf("bla bla", "", 0));
        assertEquals(6, fastStringSearch.indexOf("bla bla", "", 6));
        assertEquals(7, fastStringSearch.indexOf("bla bla", "", 7));
        assertEquals(-1, fastStringSearch.indexOf("bla bla", "", 8));
    }

}