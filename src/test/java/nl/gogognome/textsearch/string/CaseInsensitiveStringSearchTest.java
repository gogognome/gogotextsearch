package nl.gogognome.textsearch.string;

import nl.gogognome.textsearch.string.CaseInsensitiveStringSearch;
import org.junit.Test;

import static org.junit.Assert.*;

public class CaseInsensitiveStringSearchTest {
    @Test
    public void testIndexOfIgnoreCase() {
        CaseInsensitiveStringSearch caseInsensitiveStringSearch = new CaseInsensitiveStringSearch();
        assertEquals(caseInsensitiveStringSearch.indexOfIgnoreCase("A", "A"), 0);
        assertEquals(caseInsensitiveStringSearch.indexOfIgnoreCase("a", "A"), 0);
        assertEquals(caseInsensitiveStringSearch.indexOfIgnoreCase("A", "a"), 0);
        assertEquals(caseInsensitiveStringSearch.indexOfIgnoreCase("a", "a"), 0);

        assertEquals(caseInsensitiveStringSearch.indexOfIgnoreCase("a", "ba"), -1);
        assertEquals(caseInsensitiveStringSearch.indexOfIgnoreCase("ba", "a"), 1);

        assertEquals(caseInsensitiveStringSearch.indexOfIgnoreCase("Royal Blue", " Royal Blue"), -1);
        assertEquals(caseInsensitiveStringSearch.indexOfIgnoreCase(" Royal Blue", "Royal Blue"), 1);
        assertEquals(caseInsensitiveStringSearch.indexOfIgnoreCase("Royal Blue", "royal"), 0);
        assertEquals(caseInsensitiveStringSearch.indexOfIgnoreCase("Royal Blue", "oyal"), 1);
        assertEquals(caseInsensitiveStringSearch.indexOfIgnoreCase("Royal Blue", "al"), 3);
        assertEquals(caseInsensitiveStringSearch.indexOfIgnoreCase("", "royal"), -1);
        assertEquals(caseInsensitiveStringSearch.indexOfIgnoreCase("Royal Blue", ""), 0);
        assertEquals(caseInsensitiveStringSearch.indexOfIgnoreCase("Royal Blue", "BLUE"), 6);
        assertEquals(caseInsensitiveStringSearch.indexOfIgnoreCase("Royal Blue", "BIGLONGSTRING"), -1);
        assertEquals(caseInsensitiveStringSearch.indexOfIgnoreCase("Royal Blue", "Royal Blue LONGSTRING"), -1);
    }
}