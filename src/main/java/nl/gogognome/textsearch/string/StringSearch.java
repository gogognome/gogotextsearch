package nl.gogognome.textsearch.string;

import java.util.List;

/**
 * Interface for find patterns in texts.
 */
public interface StringSearch {

    /**
     * Checks whether the string <code>s</code> and <code>t</code> are equal.
     * @param s a string
     * @param t a string
     * @return <code>true</code> if <code>s</code> is not <code>null></code> and <code>t</code> is not <code>null</code>
     *         and if the strings are equal; <code>false</code> otherwise
     */
    boolean equals(String s, String t);

    /**
     * Gets the index of the first occurrence of <code>pattern</code> in <code>text</code>.
     * @param text the text to be searched.
     * @param pattern the text to be found.
     * @return the index of the first occurrence or -1 if <code>pattern</code> does not occur in <code>text</code>
     *         or if one of the parameters is <code>null</code>
     */
    int indexOf(String text, String pattern);

    /**
     * Gets the index of the first occurrence of <code>pattern</code> in <code>text</code> with index equal to or larger
     * than <code>startIndex</code>.
     * @param text the text to be searched.
     * @param pattern the text to be found.
     * @param startIndex the start index for the search.
     * @return the index of the first occurrence or -1 if <code>pattern</code> does not occur in <code>text</code>
     *         or if one of the parameters is <code>null</code>
     */
    int indexOf(String text, String pattern, int startIndex);

    /**
     * Gets indexes of all occurrence of <code>pattern</code> in <code>text</code>.
     *
     * @param text the text to be searched.
     * @param pattern the pattern to search for.
     * @return a list of indexes of all occurrence of <code>pattern</code>. The indexes are sorted increasingly.
     * Never returns <code>null</code>. If no occurrences are present, an empty list is returned.
     */
    List<Integer> indexesOf(String text, String pattern);
}
