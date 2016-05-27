package nl.gogognome.textsearch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SuffixArray {
    private final String data;
    private final int dataLength;
    private final int[] suffixArray;
    private final boolean caseSensitive;

    public SuffixArray(String data, boolean caseSensitive) {
        this.data = caseSensitive ? data : data.toLowerCase();
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

    public String toString() {
        StringBuilder sb = new StringBuilder(1000);
        for (int i = 0; i < data.length(); i++) {
            sb.append(data.substring(suffixArray[i])).append('\n');
        }
        return sb.toString();
    }

    public int indexOf(String searchString) {
        List<Integer> indexes = indexesOf(searchString);
        return indexes.isEmpty() ? -1 : indexes.get(0);
    }

    public List<Integer> indexesOf(String searchString) {
        validateSearchString(searchString);

        if (searchString.length() > dataLength) {
            return Collections.emptyList();
        }

        if (!caseSensitive) {
            searchString = searchString.toLowerCase();
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
            int signum = Character.compare(data.charAt(index+offset), searchString.charAt(offset));
            if (signum != 0) {
                return signum;
            }
        }
        return 0;
    }
}