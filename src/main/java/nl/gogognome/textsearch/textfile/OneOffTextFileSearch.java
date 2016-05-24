package nl.gogognome.textsearch.textfile;

import nl.gogognome.textsearch.criteria.Criterion;
import nl.gogognome.textsearch.string.StringSearch;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;

public class OneOffTextFileSearch implements TextFileSearch {

    private final StringSearch stringSearch;
    private BufferedReader bufferedReader;

    OneOffTextFileSearch(InputStream inputStream, StringSearch stringSearch) {
        this.stringSearch = stringSearch;
        bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
    }

    @Override
    public Iterator<String> matchesIterator(Criterion criterion) {
        if (bufferedReader == null) {
            throw new IllegalStateException("The iterator can be used only once! Create a new instance of this class if you need to iterate again.");
        }
        MatchIterator iterator = new MatchIterator(bufferedReader, criterion, stringSearch);
        bufferedReader = null;
        return iterator;
    }

    private static class MatchIterator implements Iterator<String> {

        private final StringSearch stringSearch;
        private final BufferedReader bufferedReader;
        private final Criterion criterion;
        private String nextValue;

        private MatchIterator(BufferedReader bufferedReader, Criterion criterion, StringSearch stringSearch) {
            this.bufferedReader = bufferedReader;
            this.criterion = criterion;
            this.stringSearch = stringSearch;

            determineNextValue();
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
                    if (stringSearch.matches(line, criterion)) {
                        nextValue = line;
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException("A problem ocurred while reading the next line from the file", e);
            }
        }
    }
}
