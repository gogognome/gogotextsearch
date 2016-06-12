package nl.gogognome.textsearch.string;

import org.junit.Test;

import static org.junit.Assert.*;

public class CaseSensitiveStringSearchTest {

    @Test
    public void testindexOf() {
        CaseSensitiveStringSearch caseSensitiveStringSearch = new CaseSensitiveStringSearch();
        assertEquals(caseSensitiveStringSearch.indexOf(null, "A"), -1);
        assertEquals(caseSensitiveStringSearch.indexOf("A", null), -1);
        assertEquals(caseSensitiveStringSearch.indexOf(null, null), -1);
        assertEquals(caseSensitiveStringSearch.indexOf("A", "A"), 0);
        assertEquals(caseSensitiveStringSearch.indexOf("a", "A"), -1);
        assertEquals(caseSensitiveStringSearch.indexOf("A", "a"), -1);
        assertEquals(caseSensitiveStringSearch.indexOf("a", "a"), 0);

        assertEquals(caseSensitiveStringSearch.indexOf("a", "ba"), -1);
        assertEquals(caseSensitiveStringSearch.indexOf("ba", "a"), 1);

        assertEquals(caseSensitiveStringSearch.indexOf("Royal Blue", " Royal Blue"), -1);
        assertEquals(caseSensitiveStringSearch.indexOf(" Royal Blue", "Royal Blue"), 1);
        assertEquals(caseSensitiveStringSearch.indexOf("Royal Blue", "royal"), -1);
        assertEquals(caseSensitiveStringSearch.indexOf("Royal Blue", "oyal"), 1);
        assertEquals(caseSensitiveStringSearch.indexOf("Royal Blue", "al"), 3);
        assertEquals(caseSensitiveStringSearch.indexOf("", "royal"), -1);
        assertEquals(caseSensitiveStringSearch.indexOf("Royal Blue", ""), 0);
        assertEquals(caseSensitiveStringSearch.indexOf("Royal Blue", "BLUE"), -1);
        assertEquals(caseSensitiveStringSearch.indexOf("Royal Blue", "bluestring"), -1);
        assertEquals(caseSensitiveStringSearch.indexOf("Royal Blue", "Royal Blue LONGSTRING"), -1);
    }

}