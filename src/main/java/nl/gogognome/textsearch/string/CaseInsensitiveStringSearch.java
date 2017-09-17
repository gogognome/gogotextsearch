package nl.gogognome.textsearch.string;

/**
 * Case insensitive implementation of {@link StringSearch}.
 * Based on code from http://stackoverflow.com/questions/1126227/indexof-case-sensitive
 */
class CaseInsensitiveStringSearch implements StringSearch {

    public int indexOf(final String data, final String searchText) {
        int i = 0;
        if (data != null && searchText != null) {
            if (searchText.isEmpty()) {
                return 0;
            }
            int searchStringLength = searchText.length();
            char[] searchCharLc = new char[searchStringLength];
            char[] searchCharUc = new char[searchStringLength];
            searchText.toUpperCase().getChars(0, searchStringLength, searchCharUc, 0);
            searchText.toLowerCase().getChars(0, searchStringLength, searchCharLc, 0);
            int j = 0;
            for (int checkStrLength = data.length(); i < checkStrLength; i++) {
                char charAt = data.charAt(i);
                if (charAt == searchCharLc[j] || charAt == searchCharUc[j]) {
                    if (++j == searchStringLength) {
                        return i - j + 1;
                    }
                } else { // faster than: else if (j != 0) {
                    i = i - j;
                    j = 0;
                }
            }
        }
        return -1;
    }
}
