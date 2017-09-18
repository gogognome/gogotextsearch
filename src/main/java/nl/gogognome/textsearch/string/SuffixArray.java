package nl.gogognome.textsearch.string;

import nl.gogognome.textsearch.CaseSensitivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static nl.gogognome.textsearch.CaseSensitivity.INSENSITIVE;

/**
 * <p>If you want to find the index of different string literals in one string again and again the for large strings,
 * it is more efficient to use a <i>suffix array</i> than <code>String.indexOf()</code>.
 * A suffix array is a technique to quickly generate a kind of lookup table for the string using very little
 * extra memory.</p>
 *
 * <p>You can use it like this:</p>
 *
 * <pre><code>
 *   boolean caseSensitive = true;
 *   SuffixArray suffixArray = new SuffixArray("bla blop bla", caseSensitive);
 *   int index = suffixArray.indexOf("blop");
 *   // index == 4
 *   List<Integer> indices = suffixArray.indexesOf("bla");
 *   // indices == [0, 9]
 * </code></pre>
 */
public class SuffixArray {

    private final String text;
    private final int textLength;
    private final int[] suffixArray;
    private final CaseSensitivity caseSensitivity;

    /**
     * Constructs a suffix array for <code>text</code>. String searches can be case sensitive or case insensitive,
     * depending on the paramter <code>caseSensitive</code>
     * @param text the text to be searched later on
     * @param caseSensitivity case sensitivity
     */
    public SuffixArray(String text, CaseSensitivity caseSensitivity) {
        this.text = text;
        this.caseSensitivity = caseSensitivity;
        textLength = this.text.length();
        suffixArray = new int[textLength];

        new sais().suffixSort(text, suffixArray, textLength, caseSensitivity);
    }

    /**
     * @return a string representation  of the suffix array
     */
    public String toString() {
        // length of constructed string is 2 + 3 + 4 + ... + (textLength + 1)
        // 1 + 2 + ... + n = (n+1) * n / 2 is a well-known equation
        // Substitute n by textLength + 1 and you get the length plus 1 (because the sum starts with 2 instead of 1)
        int stringLength = (textLength + 2) * (textLength + 1) / 2 - 1;
        StringBuilder sb = new StringBuilder(stringLength);
        for (int i = 0; i < text.length(); i++) {
            sb.append(text.substring(suffixArray[i])).append('\n');
        }
        return sb.toString();
    }

    /**
     * Gets the index of the first occurrence of <code>searchString</code> in the text.
     * @param searchString the text to be found
     * @return the index of the first occurrence or -1 if the <code>searchString</code> does not occur
     *                     at all in the text
     */
    public int indexOf(String searchString) {
        List<Integer> indexes = indexesOf(searchString);
        return indexes.isEmpty() ? -1 : indexes.get(0);
    }

    /**
     * Gets indexes of all occurrence of <code>searchString</code> in the text.
     * @param searchString the text to be found
     * @return a list of indexes of all occurrence of <code>searchString</code>. The indexes are sorted increasingly.
     *         Never returns <code>null</code>. If no occurrences are present, an empty list is returned.
     */
    public List<Integer> indexesOf(String searchString) {
        validateSearchString(searchString);

        if (searchString.length() > textLength) {
            return Collections.emptyList();
        }

        List<Integer> indexes = new ArrayList<>();
        int suffixArrayIndex = findFirstMatch(searchString);
        while (suffixArrayIndex >= 0 && matchesAtSuffixArrayIndex(searchString, suffixArray[suffixArrayIndex])) {
            indexes.add(suffixArray[suffixArrayIndex]);
            suffixArrayIndex--;
        }
        Collections.sort(indexes);
        return indexes;
    }

    private void validateSearchString(String searchString) {
        if (searchString == null || searchString.isEmpty()) {
            throw new IllegalArgumentException("The search string must not a non eempty string");
        }
    }

    private boolean matchesAtSuffixArrayIndex(String searchString, int textIndex) {
        return textIndex + searchString.length() <= textLength && compare(textIndex, searchString) == 0;
    }

    private int findFirstMatch(String searchString) {
        int low = 0;
        int high = textLength;

        while (low + 1< high) {
            int mid = (low + high) / 2;

            int index = suffixArray[mid];
            int signum = compare(index, searchString);
            if (signum <= 0) {
                low = mid;
            } else {
                high = mid;
            }
        }
        return low;
    }

    private int compare(int index, String searchString) {
        for (int offset = 0; offset<searchString.length() && index + offset < textLength; offset++) {
            char c1 = text.charAt(index + offset);
            char c2 = searchString.charAt(offset);
            if (caseSensitivity == INSENSITIVE) {
                c1 = Character.toLowerCase(c1);
                c2 = Character.toLowerCase(c2);
            }
            int signum = Character.compare(c1, c2);
            if (signum != 0) {
                return signum;
            }
        }
        return 0;
    }

    /**
     * Gets a substring from the text.
     * @param start start index. Must be smaller than the length of the text
     * @param end end index. <code>start + end</code> must be smaller than or equal to the length of the text
     * @return the substring
     * @exception  IndexOutOfBoundsException  if start or end are incorrect
     */
    public String substring(int start, int end) {
        return text.substring(start, end);
    }
}