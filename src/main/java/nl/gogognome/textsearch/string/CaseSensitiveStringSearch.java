package nl.gogognome.textsearch.string;

/**
 * Case sensitive implementation of {@link StringSearch}.
 */
class CaseSensitiveStringSearch implements StringSearch {

    @Override
    public int indexOf(String data, String searchText) {
        if (data == null || searchText == null) {
            return -1;
        }

        return data.indexOf(searchText);
    }
}
