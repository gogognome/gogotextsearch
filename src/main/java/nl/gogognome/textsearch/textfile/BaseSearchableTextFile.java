package nl.gogognome.textsearch.textfile;

import nl.gogognome.textsearch.criteria.Criterion;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Base class for a text file that can be searched one or more times.
 */
abstract class BaseSearchableTextFile {

    protected TextFileSearch textFileSearch;

    /**
     * Gets all lines from the text file that match the {@link Criterion}
     * @param criterion a {@link Criterion}
     * @return all matching lines in the order they appear in the text file
     * @throws IOException if a problem occurs while reading the text file
     */
    public List<String> getAllLinesMatching(Criterion criterion) throws IOException {
        return getLinesMatching(criterion, 0, Integer.MAX_VALUE);
    }

    /**
     * Gets the selected lines from the text file that match the {@link Criterion}.
     * When all matching lines are numbered starting with zero then this method only returns
     * the matching lines with numbers <code>n</code> for which holds: <code>start <= n && n < end</code>.
     *
     * @param criterion a {@link Criterion}
     * @return the selected lines in the order they appear in the text file
     * @throws IOException if a problem occurs while reading the text file
     */
    public List<String> getLinesMatching(Criterion criterion, int start, int end) throws IOException {
        try {
            ensureTextFileSearchIsInitizialized();
            List<String> result = new ArrayList<>(Math.min(end - start, 1000));
            Iterator<String> iter = textFileSearch.matchesIterator(criterion);
            int index = 0;
            while (index < end && iter.hasNext()) {
                String matchingLine = iter.next();
                if (start <= index) {
                    result.add(matchingLine);
                }
                index++;
            }
            return result;
        } finally {
            onSearchFinished();
        }
    }

    /**
     * This method is called once at the beginning of the methods that get matching lines.
     * This method should ensure that the text file is initialized for searching.
     * @throws IOException if a problem occurs while reading the text file
     */
    protected abstract void ensureTextFileSearchIsInitizialized() throws IOException;

    /**
     * This method is called once at the end of the methods that get matching lines.
     * This method should ensure that resources claimed by {@link #ensureTextFileSearchIsInitizialized()} are released.
     * @throws IOException if a problem occurs while reading the text file
     */
    protected abstract void onSearchFinished() throws IOException;

}
