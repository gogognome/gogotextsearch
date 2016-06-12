package nl.gogognome.textsearch.textfile;

import nl.gogognome.textsearch.criteria.Criterion;
import nl.gogognome.textsearch.string.CriterionMatcher;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Iterator;

public class OneOffTextFileSearch implements TextFileSearch {

    private final CriterionMatcher criterionMatcher;
    private BufferedReader bufferedReader;

    OneOffTextFileSearch(InputStream inputStream, Charset charset, CriterionMatcher criterionMatcher) {
        this.criterionMatcher = criterionMatcher;
        bufferedReader = new BufferedReader(new InputStreamReader(inputStream, charset));
    }

    @Override
    public Iterator<String> matchesIterator(Criterion criterion) {
        if (bufferedReader == null) {
            throw new IllegalStateException("The iterator can be used only once! Create a new instance of this class if you need to iterate again.");
        }
        MatchIterator iterator = new MatchIterator(bufferedReader, criterion, criterionMatcher);
        bufferedReader = null;
        return iterator;
    }

    private static class MatchIterator implements Iterator<String> {

        private final CriterionMatcher criterionMatcher;
        private final BufferedReader bufferedReader;
        private final Criterion criterion;
        private String nextValue;

        private MatchIterator(BufferedReader bufferedReader, Criterion criterion, CriterionMatcher criterionMatcher) {
            this.bufferedReader = bufferedReader;
            this.criterion = criterion;
            this.criterionMatcher = criterionMatcher;

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
                    if (criterionMatcher.matches(line, criterion)) {
                        nextValue = line;
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException("A problem ocurred while reading the next line from the file", e);
            }
        }
    }
}
