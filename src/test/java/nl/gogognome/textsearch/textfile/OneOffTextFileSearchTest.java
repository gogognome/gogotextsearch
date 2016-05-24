package nl.gogognome.textsearch.textfile;

import nl.gogognome.textsearch.criteria.Criterion;
import nl.gogognome.textsearch.string.StringSearch;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.nio.charset.Charset;
import java.util.Iterator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class OneOffTextFileSearchTest {

    private StringSearch stringSearch = mock(StringSearch.class);
    private Criterion someCriterion = mock(Criterion.class);

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
        when(stringSearch.matches(anyString(), any(Criterion.class))).thenReturn(true);

        OneOffTextFileSearch oneOffTextFileSearch = buildOneOffTextFileSearch("");

        assertFalse(oneOffTextFileSearch.matchesIterator(someCriterion).hasNext());
        verify(stringSearch, never()).matches(anyString(), any(Criterion.class));
    }

    @Test
    public void inputStreamWithTwoLinesAndOneOfWhichMatchesShouldReturnOneLine() {
        when(stringSearch.matches(anyString(), any(Criterion.class))).thenReturn(false, true);

        OneOffTextFileSearch oneOffTextFileSearch = buildOneOffTextFileSearch("one\ntwo");

        Iterator<String> iterator = oneOffTextFileSearch.matchesIterator(someCriterion);
        assertTrue(iterator.hasNext());
        assertEquals("two", iterator.next());
        assertFalse(iterator.hasNext());
        verify(stringSearch, times(2)).matches(anyString(), any(Criterion.class));
    }

    private OneOffTextFileSearch buildOneOffTextFileSearch(String text) {
        return new OneOffTextFileSearch(new ByteArrayInputStream(text.getBytes(Charset.forName("UTF-8"))), stringSearch);
    }
}