# gogo text search
Simple library for searching text in strings using expressions like "foo and bar and not (blub or blob)"

## Search criterion

What is searched for is defined by a _search criterion_. A search criterion can be a literal text or one or more search
criteria combined using AND, OR and NOT operators.

### Examples

`foo` matches any line containing the literal text `foo`.

`foo AND bar` matches any line containing both literals `foo` and `bar`. The order in which `foo` and `bar` are
 present in the matching line is unimportant.

The keyword AND is optional: `foo bar` is equivalent too `foo AND bar`.

To match the exact literal `foo bar` put it between single or double quotes: both `'foo bar'` and `"foo bar"` match
any line containing the exact literal `foo bar`.

`foo OR bar` matches any line containing at leat one of the literals `foo` and `bar`. 

`not foo` matches any line that does not contain `foo`.

Use brackets to group parts of the criterion: `(foo OR bar) AND (blob OR blub)`.

The keywords `AND`, `OR` and `NOT` are case insensitive.

### The Criterion interface and Parser class

THe package nl.gogognome.textsearch.criteria contains the `Criterion` interface and classes implementing this interface:

* StringLiteral
* And
* Or
* Not

You can use these classes to build search criteria, but the easiest way is to use the `Parser` class:

    Criterion searchCriterion = new Parser().parse("foo AND bar"); 

## String search

The package `nl.gogognome.textsearch.string` contains classes to search strings. There are two different 
searches implemented:

1. Search the first index of a search string within a string
2. Check whether a search criterion matches with a string

Both kind of searches can be case sensitive or case insensitive. Use the `StringSearchFactory` to create an instance
of the search or matcher class that you need.

### Search for the first index of a search string

If you are looking for an implementation of `String.indexOf()` that is case insensitive, 
you can do this:

    StringSearch stringSearch = new StringSearchFactory().caseInsensitiveStringSearch();
    int index = stringSearch.indexOfIgnoreCase("Barefoot is a movie directed by Andrew Flemming.", "Foot");
    // index == 4

For a case senstive implementation use `new StringSearchFactory().caseSensitiveStringSearch()` instead. Under the
hood the implementation for case sensitive search uses `String.indexOf()`. It is still useful to have both
case senstive and case insensitive implementations, because both are used by the criterion matcher for case sensitive
and case insensitive matching of search criteria.

### Search for indexes of a search string using Suffix Arrays

If you want to find the index of different string literals in one string again and again the for large strings,
it is more efficient to use a _suffix array_ than the search string solutions of the previous section. 
A suffix array is a technique to quickly generate a kind of lookup table for the
string using very little extra memory. You can use it like this:

    boolean caseSensitive = true;
    SuffixArray suffixArray = new SuffixArray("bla blop bla", caseSensitive);
    int index = suffixArray.indexOf("blop");
    // index == 4
    List<Integer> indixes = suffixArray.indexesOf("bla");
    // indixes == [0, 9]

Once you have a SuffixArray instance created you can search as often as you want for any search string.

### Check whether a string matches a criterion

Once you have created a `Criterion` instance you can use it to check whether a string matches this criterion:

    Criterion searchCriterion = new Parser().parse("foo AND bar");
    CriterionMatcher matcher = new StringSearchFactory().caseInsensitiveCriterionMatcher();
    boolean matches = matcher.matches("Barefoot is a movie directed by Andrew Flemming.", criterion);
    // matches == true

## Text file search

The package `nl.gogognome.textsearch.textfile` contains the interface `TextFileSearch`. This interface specifies
a method to get an iterator that returns lines of a text file that match a search criterion.

Currently two implementations are available for this interface: `OneOffTextFileSearch` and `SuffixArrayTextFileSearch`.

### OneOffTextFileSearch

This class is intended for searching a file just once. Its constructor expects an input stream to the file contents
and a `StringSearch` instance for matching a line with a criterion. Finally, you ask for the iterator that returns
all lines matching a specific criterion. The iterator returns each matching line of the text file. 
`OneOffTextFileSearch` does not close the input stream.

    InputStream inputStream = ...;
    Criterion searchCriterion = new Parser().parse("foo AND bar"); 
    Iterator<String> iter = new OneOffTextFileSearch(inputStream, new StringSearch()).matchesIterator(searchCriterion);
    while (iter.hasNext()) {
        String nextLine = iter.next();
    }
    inputStream.close();
     
You are only allowed to ask for an iterator once, because the input stream is only read once. This library makes
no assumptions on the capabilities of the input stream, whether its position can be reset or not. If you need to search
the file again with a different criterion, either create a new `OneOffTextFileSearch` 
or use the `SuffixArrayTextFileSearch` which is optimized for multiple searches on the same file.

### SuffixArrayTextFileSearch

This section will be written soon.