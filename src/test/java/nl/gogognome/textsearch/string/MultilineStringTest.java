package nl.gogognome.textsearch.string;

import org.junit.Test;

import static org.junit.Assert.*;

public class MultilineStringTest {

    @Test
    public void testStartOfLine() {
        MultilineString multilineString = new MultilineString("bla\n\rdie\nbla");
        assertEquals(0, multilineString.getStartOfLine(0));
        assertEquals(0, multilineString.getStartOfLine(1));
        assertEquals(0, multilineString.getStartOfLine(2));
        assertEquals(5, multilineString.getStartOfLine(5));
        assertEquals(5, multilineString.getStartOfLine(6));
        assertEquals(5, multilineString.getStartOfLine(7));
        assertEquals(9, multilineString.getStartOfLine(9));
        assertEquals(9, multilineString.getStartOfLine(10));
        assertEquals(9, multilineString.getStartOfLine(11));
    }

    @Test
    public void testEndOfLineIncludingNewLineWithNonConsecutiveNewLines() {
        MultilineString multilineString = new MultilineString("bla\n\rdie\n\rbla");
        assertEquals(5, multilineString.getEndOfLineIncludingNewLine(0));
        assertEquals(5, multilineString.getEndOfLineIncludingNewLine(1));
        assertEquals(5, multilineString.getEndOfLineIncludingNewLine(2));
        assertEquals(10, multilineString.getEndOfLineIncludingNewLine(5));
        assertEquals(10, multilineString.getEndOfLineIncludingNewLine(6));
        assertEquals(10, multilineString.getEndOfLineIncludingNewLine(7));
        assertEquals(13, multilineString.getEndOfLineIncludingNewLine(10));
        assertEquals(13, multilineString.getEndOfLineIncludingNewLine(11));
        assertEquals(13, multilineString.getEndOfLineIncludingNewLine(12));
    }

    @Test
    public void testEndOfLineIncludingNewLinesWithTwoConsecutiveNewLines() {
        MultilineString multilineString = new MultilineString("A\n\nB\r\n\r\nC");
        assertEquals(2, multilineString.getEndOfLineIncludingNewLine(0));
        assertEquals(2, multilineString.getEndOfLineIncludingNewLine(1));
        assertEquals(3, multilineString.getEndOfLineIncludingNewLine(2));
        assertEquals(6, multilineString.getEndOfLineIncludingNewLine(3));
        assertEquals(6, multilineString.getEndOfLineIncludingNewLine(4));
        assertEquals(8, multilineString.getEndOfLineIncludingNewLine(6));
        assertEquals(8, multilineString.getEndOfLineIncludingNewLine(7));
    }

    @Test
    public void testEndOfLineIncludingNewLinesWithNewLineAtEndOfString() {
        MultilineString multilineString = new MultilineString("A\n");
        assertEquals(2, multilineString.getEndOfLineIncludingNewLine(0));
        assertEquals(2, multilineString.getEndOfLineIncludingNewLine(1));
    }

    @Test
    public void testEndOfLineIncludingNewLinesWithoutNewLineAtEndOfString() {
        MultilineString multilineString = new MultilineString("A");
        assertEquals(1, multilineString.getEndOfLineIncludingNewLine(0));
    }

    @Test
    public void testEndOfLineExcludingNewLineWithNonConsecutiveNewLines() {
        MultilineString multilineString = new MultilineString("bla\n\rdie\n\rbla");
        assertEquals(3, multilineString.getEndOfLineExcludingNewLine(0));
        assertEquals(3, multilineString.getEndOfLineExcludingNewLine(1));
        assertEquals(3, multilineString.getEndOfLineExcludingNewLine(2));
        assertEquals(8, multilineString.getEndOfLineExcludingNewLine(5));
        assertEquals(8, multilineString.getEndOfLineExcludingNewLine(6));
        assertEquals(8, multilineString.getEndOfLineExcludingNewLine(7));
        assertEquals(13, multilineString.getEndOfLineExcludingNewLine(10));
        assertEquals(13, multilineString.getEndOfLineExcludingNewLine(11));
        assertEquals(13, multilineString.getEndOfLineExcludingNewLine(12));
    }

    @Test
    public void testEndOfLineExcludingNewLinesWithTwoConsecutiveNewLines() {
        MultilineString multilineString = new MultilineString("A\n\nB\r\n\r\nC");
        assertEquals(1, multilineString.getEndOfLineExcludingNewLine(0));
        assertEquals(1, multilineString.getEndOfLineExcludingNewLine(1));
        assertEquals(2, multilineString.getEndOfLineExcludingNewLine(2));
        assertEquals(4, multilineString.getEndOfLineExcludingNewLine(3));
        assertEquals(4, multilineString.getEndOfLineExcludingNewLine(4));
        assertEquals(6, multilineString.getEndOfLineExcludingNewLine(6));
        assertEquals(7, multilineString.getEndOfLineExcludingNewLine(7));
    }

    @Test
    public void testEndOfLineExcludingNewLinesWithNewLineAtEndOfString() {
        MultilineString multilineString = new MultilineString("A\n");
        assertEquals(1, multilineString.getEndOfLineExcludingNewLine(0));
        assertEquals(1, multilineString.getEndOfLineExcludingNewLine(1));
    }

    @Test
    public void testEndOfLineExcludingNewLinesWithoutNewLineAtEndOfString() {
        MultilineString multilineString = new MultilineString("A");
        assertEquals(1, multilineString.getEndOfLineExcludingNewLine(0));
    }

}