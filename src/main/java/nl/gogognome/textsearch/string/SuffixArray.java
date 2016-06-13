package nl.gogognome.textsearch.string;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <p>If you want to find the index of different string literals in one string again and again the for large strings,
 * it is more efficient to use a <i>suffix array</i> than <code>String.indexOf()</code>.
 * A suffix array is a technique to quickly generate a kind of lookup table for the string using very little
 * extra memory.</p>
 *
 * <p>You can use it like this:</p>
 *
 * <pre>
 *   boolean caseSensitive = true;
 *   SuffixArray suffixArray = new SuffixArray("bla blop bla", caseSensitive);
 *   int index = suffixArray.indexOf("blop");
 *   // index == 4
 *   List<Integer> indixes = suffixArray.indexesOf("bla");
 *   // indixes == [0, 9]
 * </pre>
 */
public class SuffixArray {

    private final String data;
    private final int dataLength;
    private final int[] suffixArray;
    private final boolean caseSensitive;

    /**
     * Constructs a suffix array for <code>data</code>. String searches can be case sensitive or case insensitive,
     * depending on the paramter <code>caseSensitive</code>
     * @param data the data to be searched later on
     * @param caseSensitive <code>true</code> if searches must be case sensitve; <code>false</code> if searches
     *                      must be case insensitive
     */
    public SuffixArray(String data, boolean caseSensitive) {
        this.data = data;
        this.caseSensitive = caseSensitive;
        dataLength = this.data.length();
        suffixArray = new int[dataLength];

        for (int i = 0; i < dataLength; i++) {
            suffixArray[i] = i;
        }
        quicksort(0, dataLength);
    }

    private void quicksort(int lowerBound, int upperBound) {
        int r = lowerBound;
        int w = lowerBound;
        int b = upperBound;
        if (upperBound - lowerBound > 1) {
            int pivotIndex = suffixArray[(lowerBound + upperBound) / 2];

            while (w != b) {
                int signum = compare(suffixArray[w], pivotIndex);
                if (signum < 0) {
                    swap(r, w);
                    r++;
                    w++;
                } else if (signum == 0) {
                    w++;
                } else {
                    b--;
                    swap(w, b);
                }
            }

            quicksort(lowerBound, r);
            quicksort(w, upperBound);
        }
    }

    private int compare(int index1, int index2) {
        if (index1 == index2) {
            return 0;
        }
        while (index1 < dataLength && index2 < dataLength) {
            char c1 = data.charAt(index1);
            char c2 = data.charAt(index2);
            if (!caseSensitive) {
                c1 = Character.toLowerCase(c1);
                c2 = Character.toLowerCase(c2);
            }
            int signum = c1 - c2;
            if (signum != 0) {
                return signum;
            }
            index1++;
            index2++;
        }
        return index1 - index2;
    }

    private void swap(int index1, int index2) {
        int temp = suffixArray[index1];
        suffixArray[index1] = suffixArray[index2];
        suffixArray[index2] = temp;
    }

    /**
     * @return a string representation  of the suffix array
     */
    public String toString() {
        // length of constructed string is 2 + 3 + 4 + ... + (dataLength + 1)
        // 1 + 2 + ... + n = (n+1) * n / 2 is a well-known equation
        // Substitute n by dataLength + 1 and you get the length plus 1 (because the sum starts with 2 instead of 1)
        int stringLength = (dataLength + 2) * (dataLength + 1) / 2 - 1;
        StringBuilder sb = new StringBuilder(stringLength);
        for (int i = 0; i < data.length(); i++) {
            sb.append(data.substring(suffixArray[i])).append('\n');
        }
        return sb.toString();
    }

    /**
     * Gets the index of the first occurrence of <code>searchString</code> in the data.
     * @param searchString the text to be found
     * @return the index of the first occurrence or -1 if the <code>searchString</code> does not occur
     *                     at all in the data
     */
    public int indexOf(String searchString) {
        List<Integer> indexes = indexesOf(searchString);
        return indexes.isEmpty() ? -1 : indexes.get(0);
    }

    /**
     * Gets indexes of all occurrence of <code>searchString</code> in the data.
     * @param searchString the text to be found
     * @return a list of indexes of all occurrence of <code>searchString</code>. The indexes are sorted increasingly.
     *         Never returns <code>null</code>. If no occurrences are present, an empty list is returned.
     */
    public List<Integer> indexesOf(String searchString) {
        validateSearchString(searchString);

        if (searchString.length() > dataLength) {
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
        return textIndex + searchString.length() <= dataLength && compare(textIndex, searchString) == 0;
    }

    private int findFirstMatch(String searchString) {
        int low = 0;
        int high = dataLength;

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
        for (int offset = 0; offset<searchString.length() && index + offset < dataLength; offset++) {
            char c1 = data.charAt(index + offset);
            char c2 = searchString.charAt(offset);
            if (!caseSensitive) {
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
     * Gets a substring from the data.
     * @param start start index. Must be smaller than the length of the data
     * @param end end index. <code>start + end</code> must be smaller than or equal to the length of the data
     * @return the substring
     * @exception  IndexOutOfBoundsException  if start or end are incorrect
     */
    public String substring(int start, int end) {
        return data.substring(start, end);
    }
}