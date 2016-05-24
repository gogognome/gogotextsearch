package nl.gogognome.textsearch.textfile;

import nl.gogognome.textsearch.criteria.Expression;

import java.util.Iterator;

public interface TextFileSearch {

    Iterator<String> matchesIterator(Expression expression);
}
