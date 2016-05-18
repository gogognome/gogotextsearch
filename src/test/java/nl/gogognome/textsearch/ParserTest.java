package nl.gogognome.textsearch;

import org.junit.Test;

import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testPasrer() {
        assertParse("bla", "bla");
        assertParse("(bla)", "bla");
        assertParse("a and b", "(a AND b)");
        assertParse("a b", "(a AND b)");
        assertParse("a b c", "((a AND b) AND c)"); // and is optional
        assertParse("a and b AnD c AND d ", "(((a AND b) AND c) AND d)");

        assertParse("a or b", "(a OR b)");
        assertParse("a or b Or c OR d ", "(((a OR b) OR c) OR d)");

        assertParse("not a", "NOT a");
        assertParse("nOt b", "NOT b");
        assertParse("not not a", "NOT NOT a");
        assertParse("NOT (c and d)", "NOT (c AND d)");

        assertParse("a and (b or c) or (not d and e) ", "((a AND (b OR c)) OR NOT (d AND e))");

        assertParseShouldFail(null, "No expression found");
        assertParseShouldFail(" ", "No expression found");
        assertParseShouldFail("a and", "Unexpected end of text");
        assertParseShouldFail("a (", "Expected end of expression after \"a\"");
        assertParseShouldFail("a )", "Expected end of expression after \"a\"");
        assertParseShouldFail("a or", "Unexpected end of text");
        assertParseShouldFail("a not", "Expected end of expression after \"a\"");
        assertParseShouldFail("not", "Expected expression after \"NOT\"");
        assertParseShouldFail("(", "Expected expression after \"(\"");
        assertParseShouldFail("(a", "Expected \")\" after \"(\" and expression");
    }

    private void assertParse(String text, String parsedText) {
        Expression expression = new Parser().parse(text);
        assertEquals(parsedText, expression.toString());
    }

    private void assertParseShouldFail(String text, String expectedExceptionMessage) {
        try {
            Expression expression = new Parser().parse(text);
            fail("Expected exception was not thrown when parsing " + text + ", result is " + expression);
        } catch (IllegalArgumentException e) {
            assertEquals(expectedExceptionMessage, e.getMessage());
        }
    }
}