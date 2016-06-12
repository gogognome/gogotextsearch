package nl.gogognome.textsearch.textfile;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;

public class CachedSearchableTextFile extends BaseSearchableTextFile {

    private final File file;
    private final Charset charset;

    public CachedSearchableTextFile(File file, Charset charset) {
        this.file = file;
        this.charset = charset;
    }

    @Override
    protected void ensureTextFileSearchIsInitizialized() throws IOException {
        if (textFileSearch != null) {
            return;
        }

        byte[] bytes = Files.readAllBytes(file.toPath());
        textFileSearch = new SuffixArrayTextFileSearch(new String(bytes, charset));
    }

    @Override
    protected void onSearchFinished() throws IOException {
        // no action required
    }
}
