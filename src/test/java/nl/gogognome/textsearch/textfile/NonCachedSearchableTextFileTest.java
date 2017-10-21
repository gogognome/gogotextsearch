package nl.gogognome.textsearch.textfile;

import nl.gogognome.textsearch.criteria.StringLiteral;
import nl.gogognome.textsearch.string.StringSearchFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Iterator;

import static org.junit.Assert.*;

public class NonCachedSearchableTextFileTest {

    private final static String DATA = "bla bloep bla";
    private final static Charset CHARSET = Charset.forName("UTF-8");
    private final StringSearchFactory stringSearchFactory = new StringSearchFactory();
    private File file;

    @Before
    public void createTemporaryFile() throws IOException {
        file = File.createTempFile("noncachedsearchabletextfile", ".txt");
        if (file.exists()) {
            assertTrue(file.delete());
        }
        Files.write(file.toPath(), DATA.getBytes(CHARSET), StandardOpenOption.CREATE_NEW);
    }

    @After
    public void deleteTemporaryFile() {
        if (file.exists()) {
            assertTrue(file.delete());
        }
    }

    @Test
    public void whenInitMethodIsNotCalledThenTextFileSearchMustBeNull() {
        NonCachedSearchableTextFile searchableTextFile = new NonCachedSearchableTextFile(file, CHARSET, stringSearchFactory.caseInsensitiveCriterionMatcherBuilder());

        assertNull(searchableTextFile.textFileSearch);
    }

    @Test
    public void whenFinishMethodIsCalledThenTextFileSearchMustBeSetToNull() throws IOException {
        NonCachedSearchableTextFile searchableTextFile = new NonCachedSearchableTextFile(file, CHARSET, stringSearchFactory.caseInsensitiveCriterionMatcherBuilder());
        searchableTextFile.ensureTextFileSearchIsInitialized();

        searchableTextFile.onSearchFinished();

        assertNull(searchableTextFile.textFileSearch);
    }

    @Test
    public void whenFinishMethodIsCalledTwiceThenTextFileSearchMustBeSetTwice() throws IOException {
        NonCachedSearchableTextFile searchableTextFile = new NonCachedSearchableTextFile(file, CHARSET, stringSearchFactory.caseInsensitiveCriterionMatcherBuilder());
        searchableTextFile.ensureTextFileSearchIsInitialized();
        TextFileSearch firstTextFileSearch = searchableTextFile.textFileSearch;
        searchableTextFile.onSearchFinished();

        try {
            searchableTextFile.ensureTextFileSearchIsInitialized();

            assertNotNull(searchableTextFile.textFileSearch);
            assertNotSame(firstTextFileSearch, searchableTextFile.textFileSearch);
        } finally {
            searchableTextFile.onSearchFinished();
        }
    }

    @Test
    public void initMethodShouldInitializeTextFileSearchCorrectly() throws IOException {
        NonCachedSearchableTextFile searchableTextFile = new NonCachedSearchableTextFile(file, CHARSET, stringSearchFactory.caseInsensitiveCriterionMatcherBuilder());
        try {
            searchableTextFile.ensureTextFileSearchIsInitialized();

            assertNotNull(searchableTextFile.textFileSearch);

            // Trick: check that correct file was read by matching with the complete file itself
            Iterator<String> iterator = searchableTextFile.textFileSearch.matchesIterator(new StringLiteral(DATA));
            assertTrue(iterator.hasNext());
            iterator.next();
            assertFalse(iterator.hasNext());
        } finally {
            searchableTextFile.onSearchFinished();
        }
    }
}