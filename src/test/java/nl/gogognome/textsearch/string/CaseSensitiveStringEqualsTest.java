package nl.gogognome.textsearch.string;

import org.junit.Test;

import static org.junit.Assert.*;

public class CaseSensitiveStringEqualsTest {

    @Test
    public void testindexOf() {
        CaseSensitiveStringEquals caseSensitiveStringEquals = new CaseSensitiveStringEquals();
        assertEquals(caseSensitiveStringEquals.indexOf(null, "A"), -1);
        assertEquals(caseSensitiveStringEquals.indexOf("A", null), -1);
        assertEquals(caseSensitiveStringEquals.indexOf(null, null), -1);
        assertEquals(caseSensitiveStringEquals.indexOf("A", "A"), 0);
        assertEquals(caseSensitiveStringEquals.indexOf("a", "A"), -1);
        assertEquals(caseSensitiveStringEquals.indexOf("A", "a"), -1);
        assertEquals(caseSensitiveStringEquals.indexOf("a", "a"), 0);

        assertEquals(caseSensitiveStringEquals.indexOf("a", "ba"), -1);
        assertEquals(caseSensitiveStringEquals.indexOf("ba", "a"), -1);

        assertEquals(caseSensitiveStringEquals.indexOf("Royal Blue", "Royal Blue"), 0);
        assertEquals(caseSensitiveStringEquals.indexOf(" Royal Blue", "Royal Blue"), -1);
        assertEquals(caseSensitiveStringEquals.indexOf(" Royal Blue", "Royal Blue"), -1);
        assertEquals(caseSensitiveStringEquals.indexOf("Royal Blue", "royal"), -1);
        assertEquals(caseSensitiveStringEquals.indexOf("Royal Blue", "Blue"), -1);
    }
}