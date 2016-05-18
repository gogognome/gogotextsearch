package nl.gogognome.textsearch;

import java.util.Iterator;
import java.util.StringTokenizer;

public class Parser {

    private String currentToken;
    private Iterator<String> tokenIterator;

    public Expression parse(String text) {
        tokenIterator = new LexicalScanner(text).scan().iterator();
        nextToken();

        if (currentToken == null) {
            throw new IllegalArgumentException("No expression found");
        }
        return parseOrExpression();
    }

    private Expression parseOrExpression() {
        Expression left = parseAndExpression();
        while (currentToken != null && currentToken.toLowerCase().equals("or")) {
            nextToken(); // skip "OR"
            Expression right = parseAndExpression();
            left = new Or(left, right);
        }
        return left;
    }

    private Expression parseAndExpression() {
        Expression left = parseSimpleExpression();
        while (currentToken != null && currentToken.toLowerCase().equals("and")) {
            nextToken(); // skip "AND"
            Expression right = parseSimpleExpression();
            left = new And(left, right);
        }
        return left;
    }

    private Expression parseSimpleExpression() {
        if (currentToken == null) {
            throw new IllegalArgumentException("Unexpected end of text");
        }
        if (currentToken.toLowerCase().equals("not")) {
            nextToken(); // skip "NOT"
            if (currentToken == null) {
                throw new IllegalArgumentException("Expected expression after \"NOT\"");
            }
            return new Not(parseOrExpression());
        } else if (currentToken.equals("(")) {
            nextToken(); // skip "("
            if (currentToken == null) {
                throw new IllegalArgumentException("Expected expression after \"(\"");
            }
            Expression expression = parseOrExpression();
            if (currentToken == null) {
                throw new IllegalArgumentException("Expected \")\" after \"(\" and expression");
            }
            nextToken(); // skip ")"
            return expression;
        }
        Expression literal = new StringLiteral(currentToken);
        nextToken();
        return literal;
    }

    private void nextToken() {
        if (tokenIterator.hasNext()) {
            currentToken = tokenIterator.next();
        } else {
            currentToken = null;
        }
    }

}
