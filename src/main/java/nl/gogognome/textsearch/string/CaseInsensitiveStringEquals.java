package nl.gogognome.textsearch.string;

/**
 * Case insensitive implementation of {@link StringSearch} that checks whether the search text is equal to the data.
 * The returned index will be 0 when the strings are equal, otherwise -1 is returned.
 */
public class CaseInsensitiveStringEquals implements StringSearch {

    private final CaseInsensitiveStringSearch caseInsensitiveStringSearch = new CaseInsensitiveStringSearch();

    @Override
    public int indexOf(String text, String pattern) {
        if (text == null || pattern == null || text.length() != pattern.length()) {
            return -1;
        }
        return caseInsensitiveStringSearch.indexOf(text, pattern);
    }

}
