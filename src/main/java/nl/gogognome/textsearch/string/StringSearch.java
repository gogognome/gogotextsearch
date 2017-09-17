package nl.gogognome.textsearch.string;

/**
 * Interface for find the first occurrence of a search text within a String.
 */
public interface StringSearch {

    /**
     * Gets the index of the first occurrence of <code>pattern</code> in <code>text</code>.
     * @param text the text to be searched.
     * @param pattern the text to be found.
     * @return the index of the first occurrence or -1 if <code>pattern</code> does not occur in <code>text</code>
     *         or if one of the parameters is <code>null</code>
     */
    int indexOf(String text, String pattern);
}
