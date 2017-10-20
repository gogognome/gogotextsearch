package nl.gogognome.textsearch.criteria;

class Token {
    String text;
    private boolean betweenQuotes;

    public static Token of(String text) {
        Token token = new Token();
        token.text = text;
        return token;
    }

    static Token betweenQuotes(String text) {
        Token token = Token.of(text);
        token.betweenQuotes = true;
        return token;
    }

    boolean isOperator(String operator) {
        return !betweenQuotes && text.toLowerCase().equals(operator);
    }
}
