package nl.gogognome.textsearch.string;

import java.util.List;

/**
 * Objects implementing this interface can be used to efficiently search a fixed text for different patterns.
 */
public interface SearchInFixedText {

    /**
     * Gets the index of the first occurrence of <code>pattern</code> in the text.
     *
     * @param pattern the pattern to search for.
     * @return the index of the first occurrence or -1 if <code>pattern</code> does not occur in the text.
     * or if one of the parameters is <code>null</code>
     */
    int indexOf(String pattern);

    /**
     * Gets the index of the first occurrence of <code>pattern</code> in the text with index equal to or larger
     * than <code>startIndex</code>.
     *
     * @param pattern the pattern to search for.
     * @param startIndex the start index for the search.
     * @return the index of the first occurrence or -1 if <code>pattern</code> does not occur in the text.
     * or if one of the parameters is <code>null</code>
     */
    int indexOf(String pattern, int startIndex);

    /**
     * Gets indexes of all occurrence of <code>pattern</code> in the text.
     *
     * @param pattern the pattern to search for.
     * @return a list of indexes of all occurrence of <code>pattern</code>. The indexes are sorted increasingly.
     * Never returns <code>null</code>. If no occurrences are present, an empty list is returned.
     */
    List<Integer> indexesOf(String pattern);
}
