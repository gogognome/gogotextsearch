# gogo text search
Simple library for searching text in strings using expressions like "foo and bar and not (blub or blob)"

This library consists of two parts:

1. Searching for a pattern within a text using the Knuth-Morris-Pratt algorithm.
2. Parsing expressions with `and`, `or` and `not` keywords and then evaluate these expressions using the search algorithm mentioned at 1.

## Knuth-Morris-Pratt algorithm
Knuth-Morris-Pratt (KMP) is an efficient algorithm to search texts for a pattern. It can be many factors faster than the regular `String.indexOf()` method.

Searching for the pattern `"test"` in multiple strings can be accomplished by creating an instace of `nl.gogognome.textsearch.KnuthMorrisPrattStringSearch`
and next call `indexOf()` or `caseInsensitiveIndexOf()` to search in texts:

    KnuthMorrisPrattStringSearch testSearch = new KnuthMorrisPrattStringSearch("test");
    assertEquals(5, testSearch.indexOf("bla 'test'"));
    assertEquals(16, testSearch.indexOf("This is another test!"));
    assertEquals(8, testSearch.caseInsensitiveIndexOf("Another TEST!"));

    
