package nl.gogognome.textsearch.textfile;

import nl.gogognome.textsearch.string.StringSearchFactory;

import java.io.*;
import java.nio.charset.Charset;

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
