package nl.gogognome.textsearch.textfile;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;

/**
 * <p>This class uses the {@link SuffixArrayTextFileSearch} to cache a text file and make searches fast.</p>
 *
 * <p>Example usage:</p>
 * <pre>
 *   CachedSearchableTextFile searchableTextFile = new CachedSearchableTextFile(file, Charset.forName("UTF-8"));
 *   List<String> matches1 = searchableTextFile.getAllLinesMatching(new Parser().parse("foo AND bar"));
 *   // Get matches 100 up to 200 (i.e., skip 100 and next take 200-100 matches)
 *   List<String> matches2 = searchableTextFile.getLinesMatching(new Parser().parse("'something else'"), 100, 200);
 * </pre>
 */
public class CachedSearchableTextFile extends BaseSearchableTextFile {

    private final File file;
    private final Charset charset;

    public CachedSearchableTextFile(File file, Charset charset) {
        this.file = file;
        this.charset = charset;
    }

    @Override
    protected void ensureTextFileSearchIsInitialized() throws IOException {
        if (textFileSearch != null) {
            return;
        }

        byte[] bytes = Files.readAllBytes(file.toPath());
        int offset = 0;
        int length = bytes.length;
        if (length > 3 && bytes[0] == (byte) 0xef && bytes[1] == (byte) 0xbb && bytes[2] == (byte) 0xbf) {
            offset = 3;
            length -= 3;
        } else if (length > 2 && bytes[0] == (byte) 0xfe && bytes[1] == (byte) 0xff) {
            offset = 2;
            length -= 2;
        } else if (length > 2 && bytes[0] == (byte) 0xff && bytes[1] == (byte) 0xfe) {
            offset = 2;
            length -= 2;
        }

        textFileSearch = new SuffixArrayTextFileSearch(new String(bytes, offset, length, charset));
    }

    @Override
    protected void onSearchFinished() throws IOException {
        // no action required
    }
}
