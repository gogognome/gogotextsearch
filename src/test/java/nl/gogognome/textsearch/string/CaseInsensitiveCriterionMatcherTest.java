package nl.gogognome.textsearch.string;

import nl.gogognome.textsearch.string.CaseInsensitiveStringSearch;
import org.junit.Test;

import static org.junit.Assert.*;

public class CaseInsensitiveCriterionMatcherTest {

    @Test
    public void testindexOf() {
        CaseInsensitiveStringSearch caseInsensitiveStringSearch = new CaseInsensitiveStringSearch();
        assertEquals(caseInsensitiveStringSearch.indexOf(null, "A"), -1);
        assertEquals(caseInsensitiveStringSearch.indexOf("A", null), -1);
        assertEquals(caseInsensitiveStringSearch.indexOf(null, null), -1);
        assertEquals(caseInsensitiveStringSearch.indexOf("A", "A"), 0);
        assertEquals(caseInsensitiveStringSearch.indexOf("a", "A"), 0);
        assertEquals(caseInsensitiveStringSearch.indexOf("A", "a"), 0);
        assertEquals(caseInsensitiveStringSearch.indexOf("a", "a"), 0);

        assertEquals(caseInsensitiveStringSearch.indexOf("a", "ba"), -1);
        assertEquals(caseInsensitiveStringSearch.indexOf("ba", "a"), 1);

        assertEquals(caseInsensitiveStringSearch.indexOf("Royal Blue", " Royal Blue"), -1);
        assertEquals(caseInsensitiveStringSearch.indexOf(" Royal Blue", "Royal Blue"), 1);
        assertEquals(caseInsensitiveStringSearch.indexOf("Royal Blue", "royal"), 0);
        assertEquals(caseInsensitiveStringSearch.indexOf("Royal Blue", "oyal"), 1);
        assertEquals(caseInsensitiveStringSearch.indexOf("Royal Blue", "al"), 3);
        assertEquals(caseInsensitiveStringSearch.indexOf("", "royal"), -1);
        assertEquals(caseInsensitiveStringSearch.indexOf("Royal Blue", ""), 0);
        assertEquals(caseInsensitiveStringSearch.indexOf("Royal Blue", "BLUE"), 6);
        assertEquals(caseInsensitiveStringSearch.indexOf("Royal Blue", "BIGLONGSTRING"), -1);
        assertEquals(caseInsensitiveStringSearch.indexOf("Royal Blue", "Royal Blue LONGSTRING"), -1);
    }
}