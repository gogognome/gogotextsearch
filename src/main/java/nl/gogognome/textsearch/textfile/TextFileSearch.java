package nl.gogognome.textsearch.textfile;

import nl.gogognome.textsearch.criteria.Criterion;

import java.util.Iterator;
import java.util.stream.Stream;

public interface TextFileSearch {

    Iterator<String> matchesIterator(Criterion criterion);

}
