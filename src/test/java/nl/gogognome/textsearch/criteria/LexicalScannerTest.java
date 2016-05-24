package nl.gogognome.textsearch.criteria;

import nl.gogognome.textsearch.criteria.LexicalScanner;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.*;

public class LexicalScannerTest {

    @Test
    public void testScanner() {
        assertScanReturnsTokens("");
        assertScanReturnsTokens("   ");
        assertScanReturnsTokens("  abc  ", "abc");
        assertScanReturnsTokens("(bla)", "(", "bla", ")");
        assertScanReturnsTokens(" ( bla ) ", "(", "bla", ")");
        assertScanReturnsTokens("1234.5 and 679", "1234.5", "and", "679");
    }

    private void assertScanReturnsTokens(String text, String... expectedTokens) {
        Collection<String> tokens = new LexicalScanner(text).scan();
        assertEquals(Arrays.asList(expectedTokens), new ArrayList<>(tokens));
    }
}