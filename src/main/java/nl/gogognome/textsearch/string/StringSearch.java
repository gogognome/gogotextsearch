package nl.gogognome.textsearch.string;

/**
 * Interface for find the first occurrence of a search text within a String.
 */
public interface StringSearch {

    /**
     * Gets the index of the first occurrence of <code>searchText</code> in <code>data</code>.
     * @param data the data to be searched.
     * @param searchText the text to be found.
     * @return the index of the first occurrence or -1 if <code>searchText</code> does not occur in <code>data</code>
     *         or if one of the parameters is <code>null</code>
     */
    int indexOf(String data, String searchText);
}
