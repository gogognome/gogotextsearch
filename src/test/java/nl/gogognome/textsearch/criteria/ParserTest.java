package nl.gogognome.textsearch.criteria;

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
        assertParse("a AND NOT b OR c", "((a AND NOT b) OR c)");
        assertParse("a AND NOT b c", "((a AND NOT b) AND c)");

        assertParse("'foo bar'", "foo bar");
        assertParse("\"foo bar\"", "foo bar");
        assertParse("'and'", "and");
        assertParse("'or'", "or");
        assertParse("'not'", "not");

        assertParse("a and (b or c) or (not d and e) ", "((a AND (b OR c)) OR (NOT d AND e))");

        assertParseShouldFail(null, "No criterion found");
        assertParseShouldFail(" ", "No criterion found");
        assertParseShouldFail("a and", "Unexpected end of text");
        assertParseShouldFail("a (", "Expected end of criterion after \"a\"");
        assertParseShouldFail("a )", "Expected end of criterion after \"a\"");
        assertParseShouldFail("a or", "Unexpected end of text");
        assertParseShouldFail("a not", "Expected end of criterion after \"a\"");
        assertParseShouldFail("not", "Expected criterion after \"NOT\"");
        assertParseShouldFail("(", "Expected criterion after \"(\"");
        assertParseShouldFail("(a", "Expected \")\" after \"(\" and criterion");
    }

    private void assertParse(String text, String parsedText) {
        Criterion criterion = new Parser().parse(text);
        assertEquals(parsedText, criterion.toString());
    }

    private void assertParseShouldFail(String text, String expectedExceptionMessage) {
        try {
            Criterion criterion = new Parser().parse(text);
            fail("Expected exception was not thrown when parsing " + text + ", result is " + criterion);
        } catch (IllegalArgumentException e) {
            assertEquals(expectedExceptionMessage, e.getMessage());
        }
    }
}