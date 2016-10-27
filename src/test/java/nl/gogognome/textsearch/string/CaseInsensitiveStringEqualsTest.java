package nl.gogognome.textsearch.string;

import org.junit.Test;

import static org.junit.Assert.*;

public class CaseInsensitiveStringEqualsTest {

    @Test
    public void testindexOf() {
        CaseInsensitiveStringEquals caseInsensitiveStringEquals = new CaseInsensitiveStringEquals();
        assertEquals(caseInsensitiveStringEquals.indexOf(null, "A"), -1);
        assertEquals(caseInsensitiveStringEquals.indexOf("A", null), -1);
        assertEquals(caseInsensitiveStringEquals.indexOf(null, null), -1);
        assertEquals(caseInsensitiveStringEquals.indexOf("A", "A"), 0);
        assertEquals(caseInsensitiveStringEquals.indexOf("a", "A"), 0);
        assertEquals(caseInsensitiveStringEquals.indexOf("A", "a"), 0);
        assertEquals(caseInsensitiveStringEquals.indexOf("a", "a"), 0);

        assertEquals(caseInsensitiveStringEquals.indexOf("a", "ba"), -1);
        assertEquals(caseInsensitiveStringEquals.indexOf("ba", "a"), -1);

        assertEquals(caseInsensitiveStringEquals.indexOf("Royal Blue", "Royal Blue"), 0);
        assertEquals(caseInsensitiveStringEquals.indexOf(" Royal Blue", "Royal Blue"), -1);
        assertEquals(caseInsensitiveStringEquals.indexOf(" Royal Blue", "Royal Blue"), -1);
        assertEquals(caseInsensitiveStringEquals.indexOf("Royal Blue", "royal"), -1);
        assertEquals(caseInsensitiveStringEquals.indexOf("Royal Blue", "BLUE"), -1);
    }
}