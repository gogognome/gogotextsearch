package nl.gogognome.textsearch.textfile;

import nl.gogognome.textsearch.string.SuffixArray;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

import static org.junit.Assert.*;

public class CachedSearchableTextFileTest {

    private final static String DATA = "bla bloep bla";
    private final static Charset CHARSET = Charset.forName("UTF-8");
    private File file;

    @Before
    public void createTemporaryFile() throws IOException {
        file = File.createTempFile("cachedsearchabletextfile", ".txt");
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
        CachedSearchableTextFile searchableTextFile = new CachedSearchableTextFile(file, CHARSET);

        assertNull(searchableTextFile.textFileSearch);
    }

    @Test
    public void whenFinishMethodIsCalledThenTextFileSearchMustNotBeSetToNull() throws IOException {
        CachedSearchableTextFile searchableTextFile = new CachedSearchableTextFile(file, CHARSET);
        searchableTextFile.ensureTextFileSearchIsInitizialized();

        searchableTextFile.onSearchFinished();

        assertNotNull(searchableTextFile.textFileSearch);
    }

    @Test
    public void whenFinishMethodIsCalledTwiceThenTextFileSearchMustOnlyBeSetOnce() throws IOException {
        CachedSearchableTextFile searchableTextFile = new CachedSearchableTextFile(file, CHARSET);
        searchableTextFile.ensureTextFileSearchIsInitizialized();
        TextFileSearch expectedTextFileSearch = searchableTextFile.textFileSearch;

        searchableTextFile.ensureTextFileSearchIsInitizialized();

        assertSame(expectedTextFileSearch, searchableTextFile.textFileSearch);
    }

    @Test
    public void initMethodShouldInitializeTextFileSearchCorrectly() throws IOException {
        CachedSearchableTextFile searchableTextFile = new CachedSearchableTextFile(file, CHARSET);

        searchableTextFile.ensureTextFileSearchIsInitizialized();

        assertNotNull(searchableTextFile.textFileSearch);
        // A trick: use the toString() of the TextFileSearch to assert it was initialized correctly
        assertEquals("SuffixArrayTextFileSearch with data: " + new SuffixArray(DATA, false),
                searchableTextFile.textFileSearch.toString());
    }
}