package nl.gogognome.textsearch;

public class SuffixArray {
    private final String dataString;
    private final int[] suffixArray;
    private final int length;

    public SuffixArray(String data) {
        dataString = data;
        length = dataString.length();
        suffixArray = new int[length];

        for (int i = 0; i < length; i++) {
            suffixArray[i] = i;
        }

        quicksort(0, length);
    }

    private void quicksort(int lowerBound, int upperBound) {
        int r = lowerBound;
        int w = lowerBound;
        int b = upperBound;
        if (upperBound - lowerBound > 1) {
            int pivotIndex = (lowerBound + upperBound) / 2;

            while (w != b) {
                int signum = compare(suffixArray[w], suffixArray[pivotIndex]);
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
        while (index1 < length && index2 < length) {
            char c1 = dataString.charAt(index1);
            char c2 = dataString.charAt(index2);
            int signum = c1 - c2;
            if (signum != 0) {
                return signum;
            }
            index1++;
            index2++;
        }
        return 0;
    }

    private void swap(int index1, int index2) {
        int temp = suffixArray[index1];
        suffixArray[index1] = suffixArray[index2];
        suffixArray[index2] = temp;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer(1000);
        for (int i = 0; i < dataString.length(); i++) {
            sb.append(dataString.substring(suffixArray[i])).append('\n');
        }
        return sb.toString();
    }

    public int indexOf(String searchString) {
        int low = 0;
        int high = dataString.length() - 1;
        int mid;
        int idx;

        while (low < high) {
            mid = low + (high - low) / 2;

            idx = suffixArray[mid];

            if (dataString.substring(idx).startsWith(searchString)) {
                return idx;
            } else if (dataString.substring(idx).compareTo(searchString) <= 0) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return -1;
    }

}