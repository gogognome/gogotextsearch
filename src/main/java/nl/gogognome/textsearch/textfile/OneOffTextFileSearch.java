package nl.gogognome.textsearch.textfile;

import nl.gogognome.textsearch.criteria.Expression;
import nl.gogognome.textsearch.textfile.TextFileSearch;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;

public class OneOffTextFileSearch implements TextFileSearch {

    private BufferedReader bufferedReader;
    private MatchIterator iterator;

    OneOffTextFileSearch(InputStream inputStream) {
        bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
    }

    @Override
    public Iterator<String> matchesIterator(Expression expression) {
        if (bufferedReader == null) {
            throw new IllegalStateException("The iterator can be used only once! Create a new instance of this class if you need to iterate again.");
        }
        return new MatchIterator(expression);
    }

    private class MatchIterator implements Iterator<String> {

        private final Expression expression;
        private String nextValue;

        private MatchIterator(Expression expression) {
            this.expression = expression;
        }

        @Override
        public boolean hasNext() {
            return nextValue != null;
        }

        @Override
        public String next() {
            String result = nextValue;
            determineNextValue();
            return result;
        }

        private void determineNextValue() {
            try {
                nextValue = null;
                for (String line = bufferedReader.readLine(); line != null && nextValue == null; line = bufferedReader.readLine()) {
                    if (expression.matches(line)) {
                        nextValue = line;
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException("A problem ocurred while reading the next line from the file", e);
            }
        }
    }
}
