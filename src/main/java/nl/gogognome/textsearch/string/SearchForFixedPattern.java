package nl.gogognome.textsearch.string;

import java.util.List;

/**
 * Objects implementing this interface can be used to efficiently search different texts for the same pattern.
 */
public interface SearchForFixedPattern {

    /**
     * Gets the index of the first occurrence of the pattern in <code>text</code>.
     *
     * <p>The returned index is the smallest value <i>k</i> for which:
     * <blockquote><pre>
     * <i>k</i> &gt;= 0 {@code &&} text.startsWith(pattern, <i>k</i>)
     * </pre></blockquote>
     *
     * If no such value of <i>k</i> exists, then {@code -1} is returned.
     *
     * @param text the text to search.
     * @return the index of the first occurrence or -1 if <code>pattern</code> does not occur in the text.
     * or if one of the parameters is <code>null</code>
     */
    int indexIn(String text);

    /**
     * Gets the index of the first occurrence of the pattern in <code>text</code> with index equal to or larger
     * than <code>startIndex</code>.
     *
     * <p>The returned index is the smallest value <i>k</i> for which:
     * <blockquote><pre>
     * <i>k</i> &gt;= startIndex {@code &&} text.startsWith(pattern, <i>k</i>)
     * </pre></blockquote>
     *
     * If no such value of <i>k</i> exists, then {@code -1} is returned.
     @param text the text to search.
     * @param startIndex the start index for the search.
     * @return the index of the first occurrence or -1 if <code>pattern</code> does not occur in the text.
     * or if one of the parameters is <code>null</code>
     */
    int indexIn(String text, int startIndex);

    /**
     * Gets indexes of all occurrences of the pattern in <code>text</code>.
     *
     * <p>The returned indexes are all values <i>k</i> for which:
     * <blockquote><pre>
     * <i>k</i> &gt;= 0 {@code &&} text.startsWith(pattern, <i>k</i>)
     * </pre></blockquote>
     *
     * @param text the text to search.
     * @return a list of indexes of all occurrence of the pattern. The indexes are sorted increasingly.
     * Never returns <code>null</code>. If no occurrences are present, an empty list is returned.
     */
    List<Integer> indexesIn(String text);
}
