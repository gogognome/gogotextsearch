package nl.gogognome.textsearch.string;

import org.junit.Test;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static nl.gogognome.textsearch.CaseSensitivity.INSENSITIVE;
import static nl.gogognome.textsearch.CaseSensitivity.SENSITIVE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class BoyerMooreTest {

    private BoyerMoore caseSensitveBoyerMoore = new BoyerMoore("bananas", SENSITIVE);
    private BoyerMoore caseInsensitveBoyerMoore = new BoyerMoore("bananas", INSENSITIVE);

    @Test
    public void defaultConstructorIsCaseSensitive() {
        BoyerMoore boyerMoore = new BoyerMoore("a");
        assertFalse(boyerMoore.equals("A"));
    }

    @Test
    public void testIndexIn() {
        assertEquals(0, caseSensitveBoyerMoore.indexIn("bananas"));
        assertEquals(-1, caseSensitveBoyerMoore.indexIn("BANANAS"));
        assertEquals(13, caseSensitveBoyerMoore.indexIn("Replic de la bananas"));
        assertEquals(-1, caseSensitveBoyerMoore.indexIn("banananananas"));
        assertEquals(0, caseSensitveBoyerMoore.indexIn("bananas bananas"));

        assertEquals(0, caseInsensitveBoyerMoore.indexIn("bAnAnAs"));
        assertEquals(13, caseInsensitveBoyerMoore.indexIn("Replic de la Bananas"));
    }

    @Test
    public void testIndexInWithStartIndex() {
        assertEquals(0, caseSensitveBoyerMoore.indexIn("bananas", 0));
        assertEquals(-1, caseSensitveBoyerMoore.indexIn("BANANAS", 0));
        assertEquals(-1, caseSensitveBoyerMoore.indexIn("bananas", 1));
        assertEquals(8, caseSensitveBoyerMoore.indexIn("bananas bananas", 1));

        assertEquals(8, caseInsensitveBoyerMoore.indexIn("bananas BaNaNaS", 1));
    }

    @Test
    public void testIndexesIn() {
        assertEquals(emptyList(), caseSensitveBoyerMoore.indexesIn("bla"));
        assertEquals(asList(0), caseSensitveBoyerMoore.indexesIn("bananas"));
        assertEquals(asList(0, 8), caseSensitveBoyerMoore.indexesIn("bananas bananas"));
        assertEquals(asList(0, 8, 16), caseSensitveBoyerMoore.indexesIn("bananas bananas bananas"));
        assertEquals(asList(0, 16), caseSensitveBoyerMoore.indexesIn("bananas BANANAS bananas"));

        assertEquals(asList(0, 8, 16), caseInsensitveBoyerMoore.indexesIn("bananas BANANAS bananas"));

        assertEquals(asList(0, 1, 2, 3, 4, 5), new BoyerMoore("aa").indexesIn("aaaaaaa"));
        assertEquals(asList(0, 1, 2, 3, 4, 5, 6, 7), new BoyerMoore("").indexesIn("aaaaaaa"));
    }

    @Test
    public void specialCases() {
        assertEquals(12, new BoyerMoore("NNAAMAN").indexIn("ANPANMANNAM_NNAAMAN")); // bad character rule
        assertEquals(13, new BoyerMoore("ANAMPNAM").indexIn("MANPANAMANAP_ANAMPNAM")); // good suffix rule
        assertEquals(5, new BoyerMoore("AAAAAA").indexIn("AAAABAAAAAA")); // good suffix rule
    }
}