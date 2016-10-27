package nl.gogognome.textsearch.string;

/**
 * Case insensitive implementation of {@link StringSearch} that checks whether the search text is equal to the data.
 * The returned index will be 0 when the strings are equal, otherwise -1 is returned.
 */
public class CaseInsensitiveStringEquals implements StringSearch {

    private final CaseInsensitiveStringSearch caseInsensitiveStringSearch = new CaseInsensitiveStringSearch();

    @Override
    public int indexOf(String data, String searchText) {
        if (data == null || searchText == null || data.length() != searchText.length()) {
            return -1;
        }
        return caseInsensitiveStringSearch.indexOf(data, searchText);
    }

}
