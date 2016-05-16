package nl.textsearch;

import java.util.function.Function;

public class KnuthMorrisPrattStringSearch {

    private final String pattern;
    private final int[] lspTable;

    public KnuthMorrisPrattStringSearch(String pattern) {
        this.pattern = pattern;
        if (pattern == null || pattern.length() == 0) {
            throw new IllegalArgumentException("pattern must be a string with length at least one.");
        }
        lspTable = computeLspTable(pattern);
    }

    private int[] computeLspTable(String pattern) {
        int[] lsp = new int[pattern.length()];
        lsp[0] = 0;  // Base case
        for (int i = 1; i < pattern.length(); i++) {
            // Start by assuming we're extending the previous LSP
            int j = lsp[i - 1];
            while (j > 0 && pattern.charAt(i) != pattern.charAt(j))
                j = lsp[j - 1];
            if (pattern.charAt(i) == pattern.charAt(j))
                j++;
            lsp[i] = j;
        }
        return lsp;
    }

    public int indexOf(String text) {
        return indexOf(text, (t, i, j) -> text.charAt(i) == pattern.charAt(j));
    }

    public int caseInsensitiveIndexOf(String text) {
        return indexOf(text, (t, i, j) -> text.charAt(i) == pattern.charAt(j)
                || Character.toLowerCase(text.charAt(i)) == Character.toLowerCase(pattern.charAt(j)));
    }

    private int indexOf(String text, CharEqualFunction charEqual) {
        int j = 0;  // Number of chars matched in pattern
        for (int i = 0; i < text.length(); i++) {
            while (j > 0 && !charEqual.apply(text, i, j)) {
                // Fall back in the pattern
                j = lspTable[j - 1];  // Strictly decreasing
            }
            if (charEqual.apply(text, i, j)) {
                // Next char matched, increment position
                j++;
                if (j == pattern.length())
                    return i - (j - 1);
            }
        }

        return -1;  // Not found
    }

    private interface CharEqualFunction {
        boolean apply (String text, int i, int j);
    }
}
