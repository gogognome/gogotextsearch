package nl.gogognome.textsearch;

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
        if (searchString.length() > dataLength) {
            return -1;
        }
        if (searchString.isEmpty()) {
            return 0;
        }
        if (!caseSensitive) {
            searchString = searchString.toLowerCase();
        }

        int low = 0;
        int high = dataLength;

        while (low + 1< high) {
            int mid = (low + high) / 2;

            int index = suffixArray[mid];
            int signum = compare(index, searchString);
            if (signum == 0) {
                return index;
            } else if (signum < 0) {
                low = mid;
            } else {
                high = mid;
            }
        }
        int index = suffixArray[low];
        if (index + searchString.length() <= dataLength && compare(index, searchString) == 0) {
            return index;
        }
        return -1;
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