package nl.gogognome.textsearch.textfile;

import nl.gogognome.textsearch.criteria.Criterion;

import java.util.Iterator;

public interface TextFileSearch {

    Iterator<String> matchesIterator(Criterion criterion);

}
