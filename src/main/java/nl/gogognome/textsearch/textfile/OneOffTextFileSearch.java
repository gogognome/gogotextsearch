package nl.gogognome.textsearch.textfile;

import nl.gogognome.textsearch.criteria.Criterion;
import nl.gogognome.textsearch.string.CriterionMatcher;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Iterator;

/**
 * <p>This class is intended for searching a file just once. Its constructor expects an input stream to the file
 * contents and a {@link nl.gogognome.textsearch.string.StringSearch} instance for matching a line with a criterion.
 * Finally, you ask for the iterator that returns  all lines matching a specific criterion.
 * The iterator returns each matching line of the text file. This class does not close the input stream.</p>
 *
 * <p>Example usage:</p>
 * <pre>
 *   InputStream inputStream = ...;
 *   Criterion criterion = new Parser().parse("foo AND bar");
 *   Charset charset = StandardCharsets.UTF_8;
 *   CriterionMatcher criterionMatcher = new StringSearchFactory().caseInsensitiveCriterionMatcher(criterion);
 *   Iterator<String> iter = new OneOffTextFileSearch(inputStream, charset, criterionMatcher).matchesIterator(searchCriterion);
 *   while (iter.hasNext()) {
 *       String nextLine = iter.next();
 *       // use nextLine
 *       actualMatches.add(nextLine); // remove from README.md
 *   }
 *   inputStream.close();
 * </pre>
 *
 * <p>You are only allowed to ask for an iterator once, because the input stream is only read once. This library makes
 * no assumptions on the capabilities of the input stream, whether its position can be reset or not.
 * If you need to search  the file again with a different criterion, either create a new {@link OneOffTextFileSearch}
 * or use the {@link SuffixArrayTextFileSearch} which is optimized for multiple searches on the same file.</p>
 */
public class OneOffTextFileSearch implements TextFileSearch {

    private final CriterionMatcher.Builder criterionMatcherBuilder;
    private BufferedReader bufferedReader;

    public OneOffTextFileSearch(InputStream inputStream, Charset charset, CriterionMatcher.Builder criterionMatcherBuilder) {
        this.criterionMatcherBuilder = criterionMatcherBuilder;
        bufferedReader = new BufferedReader(new InputStreamReader(inputStream, charset));
    }

    @Override
    public Iterator<String> matchesIterator(Criterion criterion) {
        if (bufferedReader == null) {
            throw new IllegalStateException("The iterator can be used only once! Create a new instance of this class if you need to iterate again.");
        }
        CriterionMatcher criterionMatcher = criterionMatcherBuilder.build(criterion);
        MatchIterator iterator = new MatchIterator(bufferedReader, criterionMatcher);
        bufferedReader = null;
        return iterator;
    }

    private static class MatchIterator implements Iterator<String> {

        private final CriterionMatcher criterionMatcher;
        private final BufferedReader bufferedReader;
        private String nextValue;

        private MatchIterator(BufferedReader bufferedReader, CriterionMatcher criterionMatcher) {
            this.bufferedReader = bufferedReader;
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
                    if (criterionMatcher.matches(line)) {
                        nextValue = line;
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException("A problem occurred while reading the next line from the file", e);
            }
        }
    }
}
