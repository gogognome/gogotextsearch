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

The package `nl.gogognome.textsearch.string` contains classes to search strings. One class will check whether a
string matches a criterion. The other classes implement a case insensitive `indexOf()` method.

### Check whether a string matches a criterion

Once you have created a `Criterion` instance you can use it to check whether a string matches this criterion:

    Criterion searchCriterion = new Parser().parse("foo AND bar"); 
    boolean matches = new StringSearch().matches("Barefoot is a movie directed by Andrew Flemming.", criterion);
    // matches == true
    
The class `StringSearch` matches stirng literala case-insensitively, hence `Barefoot` matches `foo AND bar`.

### Alternatives to String.indexOf()

If you are looking for an implemention of `String.indexOf()` that is case insensitive, 
you can use `CaseInsensitiveStringSearch` like this:

    CaseInsensitiveStringSearch caseInsensitiveStringSearch = new CaseInsensitiveStringSearch()
    int index = caseInsensitiveStringSearch.indexOfIgnoreCase("Barefoot is a movie directed by Andrew Flemming.", "Foot");
    // index == 4

If you have want to find the index of different string literals in one string again and again the for large strings
it is more efficient to use _suffix arrays_. This is a technique to quickly generate a kind of lookup table for the
string using very little extra memory. You can use it like this:

    boolean caseSensitive = true;
    SuffixArray suffixArray = new SuffixArray("bla blop bla", caseSensitive);
    int index = suffixArray.indexOf("blop");
    // index == 4
    List<Integer> indixes = suffixArray.indexesOf("bla");
    // indixes == [0, 9]

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

