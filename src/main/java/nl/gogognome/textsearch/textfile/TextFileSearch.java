package nl.gogognome.textsearch.textfile;

import nl.gogognome.textsearch.criteria.Criterion;

import java.util.Iterator;

/**
 * Interface for text file searches.
 */
public interface TextFileSearch {

    /**
     * Gets an {@link Iterator} that iterates over the lines that match the criterion.
     * @param criterion a {@link Criterion}
     * @return the iterator
     */
    Iterator<String> matchesIterator(Criterion criterion);

}
