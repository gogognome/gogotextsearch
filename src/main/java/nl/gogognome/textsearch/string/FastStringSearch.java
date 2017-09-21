package nl.gogognome.textsearch.string;

import nl.gogognome.textsearch.CaseSensitivity;

import java.util.ArrayList;
import java.util.List;

import static nl.gogognome.textsearch.CaseSensitivity.SENSITIVE;

public class FastStringSearch implements StringSearch {

    private final CaseSensitivity caseSensitivity;
    private BoyerMoore previousStringMatcher;
    private String previousPattern;
    private long smallTextLimit = 100_000L;

    public FastStringSearch(CaseSensitivity caseSensitivity) {
        this.caseSensitivity = caseSensitivity;
    }

    public void setSmallTextLimit(long smallTextLimit) {
        this.smallTextLimit = smallTextLimit;
    }

    @Override
    public boolean equals(String s, String t) {
        if (s == null || t == null || s.length() != t.length()) {
            return false;
        }
        if (s == t) {
            return true;
        }

        if (caseSensitivity == SENSITIVE) {
            return s.equals(t);
        }

        // This convoluted loop is copied from String.equals()
        int n = s.length();
        int i = 0;
        while (n-- != 0) {
            char c = s.charAt(i);
            char d = t.charAt(i);
            if (c == d || Character.toLowerCase(c) == Character.toLowerCase(d)) {
                i++;
            } else {
                return false;
            }
        }

        return true;
    }

    @Override
    public int indexOf(String text, String pattern) {
        return indexOf(text, pattern, 0);
    }

    @Override
    public List<Integer> indexesOf(String text, String pattern) {
        List<Integer> indexes = new ArrayList<>();
        int startIndex = 0;
        while (true) {
            int index = indexOf(text, pattern, startIndex);
            if (index == -1) {
                break;
            }
            indexes.add(index);
            startIndex = index + 1;
        }
        return indexes;
    }

    @Override
    public int indexOf(String text, String pattern, int startIndex) {
        if (text == null || pattern == null) {
            return -1;
        }

        if (pattern != previousPattern && (long) text.length() * (long) (pattern.length() - startIndex) < smallTextLimit) {
            if (caseSensitivity == SENSITIVE) {
                return text.indexOf(pattern, startIndex);
            } else {
                return indexOfCaseInsensitve(text, pattern, startIndex);
            }
        }
        BoyerMoore stringMatcher;
        if (pattern == previousPattern) {
            stringMatcher = previousStringMatcher;
        } else {
            stringMatcher = new BoyerMoore(pattern, caseSensitivity);
            previousStringMatcher = stringMatcher;
            previousPattern = pattern;
        }
        return stringMatcher.indexOf(text, startIndex);
    }

    private int indexOfCaseInsensitve(String text, String pattern, int startIndex) {
        if (startIndex >= text.length()) {
            return pattern.length() == 0 ? startIndex : -1;
        }
        if (pattern.length() == 0) {
            return startIndex;
        }

        int max =  text.length() - pattern.length();
        char first = pattern.charAt(0);
        char firstLower = Character.toLowerCase(first);

        for (int i = startIndex; i <= max; i++) {
            /* Look for first character. */
            if (text.charAt(i) != first && Character.toLowerCase(text.charAt(i)) != firstLower ) {
                while (++i <= max && text.charAt(i) != first && Character.toLowerCase(text.charAt(i)) != firstLower);
            }

            /* Found first character, now look at the rest of pattern */
            if (i <= max) {
                int j = i + 1;
                int end = j + pattern.length() - 1;
                for (int k = 1; j < end && (text.charAt(j) == pattern.charAt(k) || Character.toLowerCase(text.charAt(j)) == Character.toLowerCase(pattern.charAt(k))); j++, k++);

                if (j == end) {
                    /* Found whole string. */
                    return i;
                }
            }
        }

        return -1;
    }

}
