package nl.gogognome.textsearch.criteria;

import java.util.Iterator;

/**
 * <p>Use this class to parse a string representations of a {@link Criterion}.</p>
 *
 * <p>Typical usage:</p>
 * <pre>
 *     Criterion searchCriterion = new Parser().parse("foo AND bar");
 * </pre>
 */
public class Parser {

    private Token currentToken;
    private Iterator<Token> tokenIterator;

    /**
     * Parses a string representations of a Criterion.
     * @param text a string representations
     * @return a Criterion
     * @throws IllegalArgumentException if the string contains a syntax error
     */
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
        while (currentToken != null && currentToken.isOperator("or")) {
            nextToken(); // skip "OR"
            Criterion right = parseAndCriterion();
            left = new Or(left, right);
        }
        return left;
    }

    private Criterion parseAndCriterion() {
        Criterion left = parseSimpleCriterion();
        while (currentToken != null && (currentToken.isOperator("and") ||
                (!currentToken.isOperator("or") && !currentToken.isOperator("not") && !currentToken.isOperator("(") && !currentToken.isOperator(")")))) {
            if (currentToken.isOperator("and")) {
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
        if (currentToken.isOperator("not")) {
            nextToken(); // skip "NOT"
            if (currentToken == null) {
                throw new IllegalArgumentException("Expected criterion after \"NOT\"");
            }
            return new Not(parseSimpleCriterion());
        } else if (currentToken.isOperator("(")) {
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
        Criterion literal = new StringLiteral(currentToken.text);
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
