package nl.gogognome.textsearch.criteria;

import java.util.Iterator;

class Parser {

    private String currentToken;
    private Iterator<String> tokenIterator;

    public Criterion parse(String text) {
        tokenIterator = new LexicalScanner(text).scan().iterator();
        nextToken();

        if (currentToken == null) {
            throw new IllegalArgumentException("No criterion found");
        }
        Criterion result = parseOrCriterion();

        if (currentToken != null) {
            throw new IllegalArgumentException("Expected end of criterion after \"" + result + '"');
        }
        return result;
    }

    private Criterion parseOrCriterion() {
        Criterion left = parseAndCriterion();
        while (currentToken != null && currentToken.toLowerCase().equals("or")) {
            nextToken(); // skip "OR"
            Criterion right = parseAndCriterion();
            left = new Or(left, right);
        }
        return left;
    }

    private Criterion parseAndCriterion() {
        Criterion left = parseSimpleCriterion();
        while (currentToken != null && (currentToken.toLowerCase().equals("and") ||
                (!currentToken.toLowerCase().equals("or") && !currentToken.toLowerCase().equals("not") && !currentToken.equals("(") && !currentToken.equals(")")))) {
            if (currentToken.toLowerCase().equals("and")) {
                nextToken(); // skip "AND"
            }
            Criterion right = parseSimpleCriterion();
            left = new And(left, right);
        }
        return left;
    }

    private Criterion parseSimpleCriterion() {
        if (currentToken == null) {
            throw new IllegalArgumentException("Unexpected end of text");
        }
        if (currentToken.toLowerCase().equals("not")) {
            nextToken(); // skip "NOT"
            if (currentToken == null) {
                throw new IllegalArgumentException("Expected criterion after \"NOT\"");
            }
            return new Not(parseOrCriterion());
        } else if (currentToken.equals("(")) {
            nextToken(); // skip "("
            if (currentToken == null) {
                throw new IllegalArgumentException("Expected criterion after \"(\"");
            }
            Criterion criterion = parseOrCriterion();
            if (currentToken == null) {
                throw new IllegalArgumentException("Expected \")\" after \"(\" and criterion");
            }
            nextToken(); // skip ")"
            return criterion;
        }
        Criterion literal = new StringLiteral(currentToken);
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
