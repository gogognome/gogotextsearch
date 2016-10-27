package nl.gogognome.textsearch.string;

/**
 * Case sensitive implementation of {@link StringSearch} that checks whether the search text is equal to the data.
 * The returned index will be 0 when the strings are equal, otherwise -1 is returned.
 */
class CaseSensitiveStringEquals implements StringSearch {

    @Override
    public int indexOf(String data, String searchText) {
        return data != null && searchText != null && data.equals(searchText) ? 0 : -1;
    }
}
