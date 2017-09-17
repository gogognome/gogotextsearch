package nl.gogognome.textsearch.string;

/**
 * Case sensitive implementation of {@link StringSearch}.
 */
class CaseSensitiveStringSearch implements StringSearch {

    private StringMatcher previousStringMatcher;
    private String previousPattern;
    private Object lock = new Object();

    @Override
    public int indexOf(String text, String pattern) {
        if (text == null || pattern == null) {
            return -1;
        }

        StringMatcher stringMatcher;
        synchronized (lock) {
            if (pattern.equals(previousPattern)) {
                stringMatcher = previousStringMatcher;
            } else {
                stringMatcher = new StringMatcher(pattern);
                previousStringMatcher = stringMatcher;
                previousPattern = pattern;
            }
        }
        return stringMatcher.match(text);
    }

}
