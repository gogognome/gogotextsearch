package nl.gogognome.textsearch.criteria;

import java.util.ArrayList;
import java.util.Collection;

class LexicalScanner {

    private final String text;
    private int index;
    private final Collection<Token> tokens = new ArrayList<>();

    LexicalScanner(String text) {
        this.text = text;
    }

    Collection<Token> scan() {
        while (text != null && index < text.length()) {
            skipWhiteSpaces();
            if (index < text.length()) {
                if (isBracket()) {
                    tokens.add(Token.of(text.substring(index, index + 1)));
                    index++;
                } else if (isQuote()) {
                    char quoteChar = text.charAt(index);
                    index++;
                    int startIndex = index;
                    while (index < text.length() && text.charAt(index) != quoteChar) {
                        index++;
                    }
                    if (index == text.length()) {
                        throw new IllegalArgumentException("String literal starting at index " + (startIndex-1) + " was not terminated with a " + quoteChar + " character");
                    }
                    tokens.add(Token.betweenQuotes(text.substring(startIndex, index)));
                    index++; // skip closing quote
                } else {
                    int startIndex = index;
                    index++;
                    while (index < text.length() && !Character.isWhitespace(text.charAt(index)) && !isBracket()) {
                        index++;
                    }
                    tokens.add(Token.of(text.substring(startIndex, index)));
                }
            }
        }
        return tokens;
    }

    private boolean isBracket() {
        return "()".indexOf(text.charAt(index)) != -1;
    }

    private boolean isQuote() {
        return "'\"".indexOf(text.charAt(index)) != -1;
    }

    private void skipWhiteSpaces() {
        while (index < text.length() && Character.isWhitespace(text.charAt(index))) {
            index++;
        }
    }

}
