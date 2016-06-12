package nl.gogognome.textsearch.textfile;

import nl.gogognome.textsearch.criteria.Criterion;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

abstract class BaseSearchableTextFile {

    protected TextFileSearch textFileSearch;

    public List<String> getAllLinesMatching(Criterion criterion) throws IOException {
        return getLinesMatching(criterion, 0, Integer.MAX_VALUE);
    }

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


    protected abstract void ensureTextFileSearchIsInitizialized() throws IOException;

    protected abstract void onSearchFinished() throws IOException;

}
