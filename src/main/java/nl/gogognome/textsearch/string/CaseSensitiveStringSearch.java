package nl.gogognome.textsearch.string;

class CaseSensitiveStringSearch implements StringSearch {

    @Override
    public int indexOf(String data, String searchText) {
        if (data == null || searchText == null) {
            return -1;
        }

        return data.indexOf(searchText);
    }
}
