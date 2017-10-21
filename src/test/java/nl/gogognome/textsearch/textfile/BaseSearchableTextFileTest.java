package nl.gogognome.textsearch.textfile;

import nl.gogognome.textsearch.criteria.Criterion;
import nl.gogognome.textsearch.criteria.StringLiteral;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class BaseSearchableTextFileTest {

    private final TextFileSearch textFileSearchMock = mock(TextFileSearch.class);
    private final StringLiteral criterion = new StringLiteral("test");

    @Test
    public void getAllLinesMatchingShouldReturnAllMatchingLines() throws IOException {
        List<String> expectedLines = asList("one", "two", "three");
        setupAllMatchingLines(expectedLines);

        List<String> actualLines = new SearchableTextFileImpl().getAllLinesMatching(criterion);

        assertEquals(expectedLines, actualLines);
    }

    @Test
    public void getLinesMatchingShouldReturnMatchingLines() throws IOException {
        List<String> expectedLines = asList("one", "two", "three");
        setupAllMatchingLines(expectedLines);

        assertEquals(emptyList(), new SearchableTextFileImpl().getLinesMatching(criterion, 0, 0));
        assertEquals(emptyList(), new SearchableTextFileImpl().getLinesMatching(criterion, 1, 1));
        assertEquals(emptyList(), new SearchableTextFileImpl().getLinesMatching(criterion, 5, 5));
        assertEquals(asList("one"), new SearchableTextFileImpl().getLinesMatching(criterion, 0, 1));
        assertEquals(asList("one", "two"), new SearchableTextFileImpl().getLinesMatching(criterion, 0, 2));
        assertEquals(asList("two", "three"), new SearchableTextFileImpl().getLinesMatching(criterion, 1, 3));
        assertEquals(asList("three"), new SearchableTextFileImpl().getLinesMatching(criterion, 2, 5));
    }

    @Test
    public void getAllLinesMatchingShouldCallInitAndFinishMethodOnce() throws IOException {
        setupAllMatchingLines(asList("one", "two", "three"));
        SearchableTextFileImpl searchableTextFile = new SearchableTextFileImpl();

        for (int nrIterationsCompleted=0; nrIterationsCompleted<10; nrIterationsCompleted++) {
            assertEquals(nrIterationsCompleted, searchableTextFile.nrInitCalls);
            assertEquals(nrIterationsCompleted, searchableTextFile.nrFinishCalls);

            searchableTextFile.getAllLinesMatching(criterion);
        }
    }

    private void setupAllMatchingLines(List<String> expectedLines) {
        when(textFileSearchMock.matchesIterator(any(Criterion.class))).then(invocation -> expectedLines.iterator());
    }

    class SearchableTextFileImpl extends BaseSearchableTextFile {

        private int nrInitCalls;
        private int nrFinishCalls;

        @Override
        protected void ensureTextFileSearchIsInitialized() throws IOException {
            nrInitCalls++;
            textFileSearch = textFileSearchMock;
        }

        @Override
        protected void onSearchFinished() throws IOException {
            nrFinishCalls++;
            textFileSearch = null;
        }
    }
}