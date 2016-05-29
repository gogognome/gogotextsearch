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
        assertScanReturnsTokens("  'asda'  ", "asda");
        assertScanReturnsTokens("  \"asda\"  ", "asda");
        assertScanReturnsTokens("  \"'\"  ", "'");
        assertScanReturnsTokens("  '\"'  ", "\"");
        assertScanReturnsTokens("'a and (b or c)'", "a and (b or c)");
        assertScanThrowsException("\"bla", "String literal starting at index 0 was not terminated with a \" character");
        assertScanThrowsException("'bla", "String literal starting at index 0 was not terminated with a ' character");
    }

    private void assertScanReturnsTokens(String text, String... expectedTokens) {
        Collection<String> tokens = new LexicalScanner(text).scan();
        assertEquals(Arrays.asList(expectedTokens), new ArrayList<>(tokens));
    }
    private void assertScanThrowsException(String text, String expectedExceptionMessage) {
        try {
            new LexicalScanner(text).scan();
            fail("Expected exception was not thrown");
        } catch (Exception e) {
            assertEquals(IllegalArgumentException.class, e.getClass());
            assertEquals(expectedExceptionMessage, e.getMessage());
        }
    }
}