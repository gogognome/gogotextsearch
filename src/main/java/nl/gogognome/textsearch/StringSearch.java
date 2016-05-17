package nl.gogognome.textsearch;

public class StringSearch {

    private final int sizeOfAlphabet = 65536;
    private final String pattern;
    private final int[] rightMostOccurrence;
    private final int[] rightMostOccurrenceLowercase;

    public StringSearch(String pattern) {
        this.pattern = pattern;
        if (pattern == null || pattern.length() == 0) {
            throw new IllegalArgumentException("pattern must be a string with length at least one.");
        }

        rightMostOccurrence = new int[sizeOfAlphabet];
        rightMostOccurrenceLowercase = new int[sizeOfAlphabet];
        for (int i=0; i<sizeOfAlphabet; i++) {
            rightMostOccurrence[i] = -1;
        }
        for (int i=0; i<pattern.length(); i++) {
            rightMostOccurrence[pattern.charAt(i)] = i;
            rightMostOccurrenceLowercase[Character.toLowerCase(pattern.charAt(i))] = i;
        }
    }

    public int findIndexIn(String text) {
        int patternLength = pattern.length();
        int maxNrIterations = text.length() - patternLength;
        int skip;
        for (int i = 0; i <= maxNrIterations; i += skip) {
            skip = 0;
            for (int j = patternLength-1; j >= 0; j--) {
                char c = text.charAt(i + j);
                if (pattern.charAt(j) != c) {
                    skip = Math.max(1, j - rightMostOccurrence[c]);
                    break;
                }
            }
            if (skip == 0)
            {
                return i;
            }
        }
        return -1;
    }

    public int findIndexInIgnoringCase(String text) {
        int patternLength = pattern.length();
        int maxNrIterations = text.length() - patternLength;
        int skip;
        for (int i = 0; i <= maxNrIterations; i += skip) {
            skip = 0;
            for (int j = patternLength-1; j >= 0; j--) {
                char c = Character.toLowerCase(text.charAt(i + j));
                if (Character.toLowerCase(pattern.charAt(j)) != c) {
                    skip = Math.max(1, j - rightMostOccurrenceLowercase[c]);
                    break;
                }
            }
            if (skip == 0)
            {
                return i;
            }
        }
        return -1;
    }

}
