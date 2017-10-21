package nl.gogognome.textsearch.textfile;

import nl.gogognome.textsearch.criteria.StringLiteral;
import nl.gogognome.textsearch.string.SuffixArray;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;

import static java.util.Arrays.asList;
import static nl.gogognome.textsearch.CaseSensitivity.INSENSITIVE;
import static org.junit.Assert.*;

public class CachedSearchableTextFileTest {

    private final static String TEXT = "bla bloep bla";
    private final static Charset CHARSET = Charset.forName("UTF-8");
    private File file;

    @Before
    public void createTemporaryFile() throws IOException {
        file = File.createTempFile("cachedsearchabletextfile", ".txt");
        if (file.exists()) {
            assertTrue(file.delete());
        }
        Files.write(file.toPath(), TEXT.getBytes(CHARSET), StandardOpenOption.CREATE_NEW);
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
        searchableTextFile.ensureTextFileSearchIsInitialized();

        searchableTextFile.onSearchFinished();

        assertNotNull(searchableTextFile.textFileSearch);
    }

    @Test
    public void whenFinishMethodIsCalledTwiceThenTextFileSearchMustOnlyBeSetOnce() throws IOException {
        CachedSearchableTextFile searchableTextFile = new CachedSearchableTextFile(file, CHARSET);
        searchableTextFile.ensureTextFileSearchIsInitialized();
        TextFileSearch expectedTextFileSearch = searchableTextFile.textFileSearch;

        searchableTextFile.ensureTextFileSearchIsInitialized();

        assertSame(expectedTextFileSearch, searchableTextFile.textFileSearch);
    }

    @Test
    public void initMethodShouldInitializeTextFileSearchCorrectly() throws IOException {
        CachedSearchableTextFile searchableTextFile = new CachedSearchableTextFile(file, CHARSET);

        searchableTextFile.ensureTextFileSearchIsInitialized();

        assertNotNull(searchableTextFile.textFileSearch);
        // A trick: use the toString() of the TextFileSearch to assert it was initialized correctly
        assertEquals("SuffixArrayTextFileSearch with data: " + new SuffixArray(TEXT, INSENSITIVE),
                searchableTextFile.textFileSearch.toString());
    }

    @Test
    public void firstLineStartsAfterByteOrderMark() throws IOException {
        byte[][] boms = new byte[][] {
                { (byte) 0xef, (byte) 0xbb, (byte) 0xbf },
                { (byte) 0xfe, (byte) 0xff },
                { (byte) 0xff, (byte) 0xfe }
        };

        for (byte[] bom : boms) {
            byte[] bytes = new byte[bom.length + TEXT.length()];
            System.arraycopy(bom, 0, bytes, 0, bom.length);
            System.arraycopy(TEXT.getBytes(CHARSET), 0, bytes, bom.length, TEXT.length());
            Files.write(file.toPath(), bytes);

            CachedSearchableTextFile searchableTextFile = new CachedSearchableTextFile(file, CHARSET);

            List<String> matchingLines = searchableTextFile.getLinesMatching(new StringLiteral("bla"), 0, 100);
            assertEquals("Byte order marker: " + Arrays.toString(bom), asList(TEXT), matchingLines);
        }
    }

}