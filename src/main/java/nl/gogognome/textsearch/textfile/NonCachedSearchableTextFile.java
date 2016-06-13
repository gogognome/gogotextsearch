package nl.gogognome.textsearch.textfile;

import nl.gogognome.textsearch.string.StringSearchFactory;

import java.io.*;
import java.nio.charset.Charset;

/**
 * <p>This class uses the {@link OneOffTextFileSearch} to search a file. The file is read once per search.</p>
 *
 * <p>Example usage:</p>
 * <pre>
 *   NonCachedSearchableTextFile searchableTextFile = new NonCachedSearchableTextFile(file, Charset.forName("UTF-8"));
 *   List<String> matches1 = searchableTextFile.getAllLinesMatching(new Parser().parse("foo AND bar"));
 *   // Get matches 100 up to 200 (i.e., skip 100 and next take 200-100 matches)
 *   List<String> matches2 = searchableTextFile.getLinesMatching(new Parser().parse("'something else'"), 100, 200);
 * </pre>
 */
public class NonCachedSearchableTextFile extends BaseSearchableTextFile {

    private final File file;
    private final Charset charset;
    private final StringSearchFactory stringSearchFactory = new StringSearchFactory();
    private InputStream inputStream;

    public NonCachedSearchableTextFile(File file, Charset charset) {
        this.file = file;
        this.charset = charset;
    }

    @Override
    protected void ensureTextFileSearchIsInitizialized() throws IOException {
        inputStream = new FileInputStream(file);
        textFileSearch = new OneOffTextFileSearch(inputStream, charset, stringSearchFactory.caseInsensitiveCriterionMatcher());
    }

    @Override
    protected void onSearchFinished() throws IOException {
        try {
            if (inputStream != null) {
                inputStream.close();
            }
        } finally {
            inputStream = null;
            textFileSearch = null;
        }
    }

}
