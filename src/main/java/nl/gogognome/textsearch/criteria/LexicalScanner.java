package nl.gogognome.textsearch.criteria;

import java.util.ArrayList;
import java.util.Collection;

class LexicalScanner {

    private final String text;
    private int index;
    private final Collection<String> tokens = new ArrayList<>();

    public LexicalScanner(String text) {
        this.text = text;
    }

    public Collection<String> scan() {
        while (text != null && index < text.length()) {
            skipWhiteSpaces();
            if (index < text.length()) {
                if (isOperator()) {
                    tokens.add(text.substring(index, index + 1));
                    index++;
                } else {
                    int startIndex = index;
                    index++;
                    while (index < text.length() && !Character.isWhitespace(text.charAt(index)) && !isOperator()) {
                        index++;
                    }
                    tokens.add(text.substring(startIndex, index));
                }
            }
        }
        return tokens;
    }

    private boolean isOperator() {
        return "()".indexOf(text.charAt(index)) != -1;
    }

    private void skipWhiteSpaces() {
        while (index < text.length() && Character.isWhitespace(text.charAt(index))) {
            index++;
        }
    }

}
