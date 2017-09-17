package nl.gogognome.textsearch.string;

/**
 * Case insensitive implementation of {@link StringSearch}.
 * Based on code from http://stackoverflow.com/questions/1126227/indexof-case-sensitive
 */
class CaseInsensitiveStringSearch implements StringSearch {

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
                stringMatcher = new StringMatcher(pattern, true);
                previousStringMatcher = stringMatcher;
                previousPattern = pattern;
            }
        }
        return stringMatcher.match(text);
    }
}
