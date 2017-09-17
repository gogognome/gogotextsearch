package nl.gogognome.textsearch.string;

/**
 * Case sensitive implementation of {@link StringSearch} that checks whether the search text is equal to the data.
 * The returned index will be 0 when the strings are equal, otherwise -1 is returned.
 */
class CaseSensitiveStringEquals implements StringSearch {

    @Override
    public int indexOf(String text, String pattern) {
        return text != null && pattern != null && text.equals(pattern) ? 0 : -1;
    }
}
