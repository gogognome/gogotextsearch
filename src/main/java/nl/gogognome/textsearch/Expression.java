package nl.gogognome.textsearch;

public interface Expression {

    /**
     * Checks whether the text matches with this expression.
     * @param text the text
     * @return true if the text matches with this expression; false otherwise
     */
    boolean matches(String text);
}
