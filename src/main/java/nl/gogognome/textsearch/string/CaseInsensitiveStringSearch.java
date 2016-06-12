package nl.gogognome.textsearch.string;

// Code from http://stackoverflow.com/questions/1126227/indexof-case-sensitive
public class CaseInsensitiveStringSearch {

    public int indexOfIgnoreCase(final String chkstr, final String searchStr) {
        int i = 0;
        if (chkstr != null && searchStr != null) {
            if (searchStr.isEmpty()) {
                return 0;
            }
            int serchStrLength = searchStr.length();
            char[] searchCharLc = new char[serchStrLength];
            char[] searchCharUc = new char[serchStrLength];
            searchStr.toUpperCase().getChars(0, serchStrLength, searchCharUc, 0);
            searchStr.toLowerCase().getChars(0, serchStrLength, searchCharLc, 0);
            int j = 0;
            for (int checkStrLength = chkstr.length(); i < checkStrLength; i++) {
                char charAt = chkstr.charAt(i);
                if (charAt == searchCharLc[j] || charAt == searchCharUc[j]) {
                    if (++j == serchStrLength) {
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
