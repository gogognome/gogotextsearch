package nl.gogognome.textsearch.textfile;

import nl.gogognome.textsearch.criteria.Criterion;
import nl.gogognome.textsearch.string.CriterionMatcher;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Iterator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class OneOffTextFileSearchTest {

    private CriterionMatcher criterionMatcher = mock(CriterionMatcher.class);
    private CriterionMatcher.Builder criterionMatcherBuilder = mock(CriterionMatcher.Builder.class);
    private Criterion someCriterion = mock(Criterion.class);

    @Before
    public void setUpMock() {
        when(criterionMatcherBuilder.build(any(Criterion.class))).thenReturn(criterionMatcher);
    }

    @Test
    public void whenIteratorIsRetrievedForSecondTimeThenExceptionShouldBeThrown() {
        OneOffTextFileSearch oneOffTextFileSearch = buildOneOffTextFileSearch("bla\ndie\nbla");
        oneOffTextFileSearch.matchesIterator(someCriterion);

        try {
            oneOffTextFileSearch.matchesIterator(someCriterion);
            fail("Expected exception was not thrown");
        } catch (Exception e) {
            assertEquals(IllegalStateException.class, e.getClass());
        }
    }

    @Test
    public void emptyInputStreamShouldHaveNoMatches() {
        when(criterionMatcher.matches(anyString())).thenReturn(true);

        OneOffTextFileSearch oneOffTextFileSearch = buildOneOffTextFileSearch("");

        assertFalse(oneOffTextFileSearch.matchesIterator(someCriterion).hasNext());
        verify(criterionMatcher, never()).matches(anyString());
    }

    @Test
    public void inputStreamWithTwoLinesAndOneOfWhichMatchesShouldReturnOneLine() {
        when(criterionMatcher.matches(anyString())).thenReturn(false, true);

        OneOffTextFileSearch oneOffTextFileSearch = buildOneOffTextFileSearch("one\ntwo");

        Iterator<String> iterator = oneOffTextFileSearch.matchesIterator(someCriterion);
        assertTrue(iterator.hasNext());
        assertEquals("two", iterator.next());
        assertFalse(iterator.hasNext());
        verify(criterionMatcher, times(2)).matches(anyString());
    }

    @Test
    public void testExceptionHandling() throws IOException {
        InputStream inputStream = mock(InputStream.class);
        when(inputStream.read()).thenThrow(new IOException("Test exception"));
        when(inputStream.read(anyObject())).thenThrow(new IOException("Test exception"));
        when(inputStream.read(anyObject(), anyInt(), anyInt())).thenThrow(new IOException("Test exception"));

        OneOffTextFileSearch oneOffTextFileSearch = new OneOffTextFileSearch(inputStream, Charset.forName("UTF-8"), criterionMatcherBuilder);

        try {
            oneOffTextFileSearch.matchesIterator(someCriterion);
            fail("Expected exception was not thrown");
        } catch (RuntimeException e) {
            assertEquals("A problem occurred while reading the next line from the file", e.getMessage());
            assertEquals(IOException.class, e.getCause().getClass());
        }
    }

    private OneOffTextFileSearch buildOneOffTextFileSearch(String text) {
        Charset charset = Charset.forName("UTF-8");
        return new OneOffTextFileSearch(new ByteArrayInputStream(text.getBytes(charset)), charset, criterionMatcherBuilder);
    }
}