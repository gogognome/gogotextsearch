# gogo text search

Simple library for searching text in strings using expressions like "foo and bar and not (blub or blob)"
and for searching large texts fast.

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/nl.gogognome/gogotextsearch/badge.svg?style=plastic)](https://maven-badges.herokuapp.com/maven-central/nl.gogognome/gogotextsearch)

## String search

The package `nl.gogognome.textsearch.string` contains classes to search strings. There are two different
searches implemented:

1. Search the first index of a search string within a string
2. Check whether a search criterion matches with a string

Both kind of searches can be case-sensitive or case-insensitive. Use the `StringSearchFactory` to create an instance
of the search or matcher class that you need.

### Search for the first index of a search string

If you are looking for an implementation of `String.indexOf()` that is case-insensitive,
you can do this:

    StringSearch stringSearch = new StringSearchFactory().caseInsensitiveStringSearch();
    int index = stringSearch.indexOf("Barefoot is a movie directed by Andrew Flemming.", "Foot");
    // index == 4

The `StringSearch` interface also offers methods to start searching from a specific index in the text onwards, and
a method to find all indexes where the pattern occurs in the text.

    List<Integer> indexes = stringSearch.indexesOf("Barefoot is a movie directed by Andrew Flemming.", "re");
    // indexes == [2, 22, 35]

For a case-sensitive implementation of `StringSearch` use `new StringSearchFactory().caseSensitiveStringSearch()`
instead. Under the hood the implementation for case-sensitive search uses `String.indexOf()`.
It is still useful to have both case-sensitive and case-insensitive implementations, because both are used by
the criterion matcher for case-sensitive and case-insensitive matching of search criteria.

### Search for indexes of a pattern using Boyer Moore

To search large texts, a faster algorithm to use is the Boyer Moore algorithm. This algorithm
requires a little time to initialize a few arrays based on the pattern. After that, searching is many times faster
than `String.indexOf()`.

    BoyerMoore boyerMoore = new BoyerMoore("Foot", CaseSensitivity.INSENSITIVE);
    int index = boyerMoore.indexIn("Barefoot is a movie directed by Andrew Flemming.");
    // index == 4

The class `BoyerMoore` also offers methods to start searching from a specific index in the text onwards, and a method
to find all indexes where the pattern occurs in the text.

    BoyerMoore boyerMoore = new BoyerMoore("re", CaseSensitivity.INSENSITIVE);
    List<Integer> indexes = boyerMoore.indexesIn("Barefoot is a movie directed by Andrew Flemming.");
    // indexes == [2, 22, 35]

The StringSearch implementation returned by the `StringSearchFactory` wil actually switch from `String.indexOf()`
to Boyer-Moore if the text is larger than a certain threshold.

### Search for indexes of a pattern using Suffix Arrays

If you want to find the index of different string literals in one string again and again the for large strings,
it is more efficient to use a _suffix array_ than the search string solutions of the previous section.
A suffix array is a technique to quickly generate a kind of lookup table for the
string using very little extra memory. You can use it like this:

    BoyerMoore boyerMoore = new BoyerMoore("Foot", CaseSensitivity.INSENSITIVE);
    int index = boyerMoore.indexIn("Barefoot is a movie directed by Andrew Flemming.");
    // index == 4

Once you have a `SuffixArray` instance created you can search as often as you want for any search string.

The class `SuffixArray` also offers a method to find all indexes where the pattern occurs in the text.

    new BoyerMoore("re", CaseSensitivity.INSENSITIVE);
    List<Integer> indexes = boyerMoore.indexesIn("Barefoot is a movie directed by Andrew Flemming.");
    // indexes == [2, 22, 35]

## Searching with search criterion

The sections above describe how to search efficiently for occurrences of a pattern in a text. Sometimes
you want to search using a _search criterion_ like `(foo AND bar) AND NOT (foobar)`.
A search criterion can be a literal text or one or more search criteria combined using AND, OR and NOT operators.

### Examples

`foo` matches any line containing the literal text `foo`.

`foo AND bar` matches any line containing both literals `foo` and `bar`. The order in which `foo` and `bar` are
present in the matching line is unimportant.

The keyword AND is optional: `foo bar` is equivalent too `foo AND bar`.

To match the exact literal `foo bar` put it between single or double quotes: both `'foo bar'` and `"foo bar"` match
any line containing the exact literal `foo bar`.

`foo OR bar` matches any line containing at least one of the literals `foo` and `bar`.

`NOT foo` matches any line that does not contain `foo`.

Use brackets to group parts of the criterion: `(foo OR bar) AND (blob OR blub)`.

The keywords `AND`, `OR` and `NOT` are case-insensitive.

### The Criterion interface and Parser class

The package `nl.gogognome.textsearch.criteria` contains the `Criterion` interface and classes implementing
this interface:

* `StringLiteral`
* `And`
* `Or`
* `Not`

You can use these classes to build search criteria:

    Criterion searchCriterion = new And(new StringLiteral("foo"), new StringLiteral("bar"));

But the easiest way is to use the `Parser` class:

    Criterion criterion = new Parser().parse("foo AND bar"); 

### Check whether a string matches a criterion

Once you have created a `Criterion` instance you can use it to check whether a string matches this criterion:

    Criterion criterion = new Parser().parse("foo AND bar");
    CriterionMatcher matcher = new StringSearchFactory().caseInsensitiveCriterionMatcher(criterion);
    boolean matches = matcher.matches("Barefoot is a movie directed by Andrew Flemming.");
    // matches == true

A special use case is that the `Criterion` must be matched against a number of attributes of an object.
This could be implemented by joining these attributes' values to a single string, separated by a character
that does not occur in the `Criterion`, and then matching
the `Criterion` against this resulting string. This approach however would require a lot of string copying,
which is time-consuming and produces a lot of garbage on the heap. To overcome this problem
the method `matches()` accepts a varargs string argument.
The strings passed to the method are treated as if they were all joined together to a single string, separated
by a character that does not occur in the {@link Criterion}.

    Criterion criterion = new Parser().parse("foo AND bar");
    CriterionMatcher matcher = new StringSearchFactory().caseInsensitiveCriterionMatcher(criterion);
    boolean matches1 = matcher.matches("Bart", "food");
    // matches1 == true
    boolean matches2 = matcher.matches("ba", "rt food");
    // matches2 == false because "bar" is not found

## Text file search

The package `nl.gogognome.textsearch.textfile` contains the interface `TextFileSearch`. This interface specifies
a method to get an `Iterator` that returns lines of a text file that match a search criterion.

Currently, two implementations are available for this interface: `OneOffTextFileSearch` and `SuffixArrayTextFileSearch`.

These two implementations are used by the classes `CachedSearchableTextFile` and `NonCachedSearchableTextFile`,
which make it easier to search a file multiple times and get the matches in a list instead as an `Iterator`.

### OneOffTextFileSearch

This class is intended for searching a file just once. Its constructor expects an input stream to the file contents
and a `StringSearch` instance for matching a line with a criterion. Finally, you ask for the `Iterator` that returns
all lines matching a specific criterion. The `Iterator` returns each matching line of the text file.
`OneOffTextFileSearch` does not close the input stream.

    // InputStream inputStream = ...
    Criterion searchCriterion = new Parser().parse("foo AND bar");
    Charset charset = StandardCharsets.UTF_8;
    CriterionMatcher.Builder criterionMatcherBuilder = new StringSearchFactory().caseInsensitiveCriterionMatcherBuilder();
    Iterator<String> iter = new OneOffTextFileSearch(inputStream, charset, criterionMatcherBuilder).matchesIterator(searchCriterion);
    while (iter.hasNext()) {
        String nextLine = iter.next();
        // use nextLine
    }
    inputStream.close();

You are only allowed to ask for an iterator once, because the input stream is only read once. This library makes
no assumptions about the capabilities of the input stream, whether its position can be reset or not. If you need to search
the file again with a different criterion, either create a new `OneOffTextFileSearch`
or use the `SuffixArrayTextFileSearch` which is optimized for multiple searches on the same file.

### SuffixArrayTextFileSearch

This class is intended for searching a file multiple times. Its constructor expects a String of the file's contents.
Next call `matchesIterator()` with a `Criterion` as often as you want to find matches.

    SuffixArrayTextFileSearch fileSearch = new SuffixArrayTextFileSearch("Some huge file's contents here...");
    Iterator<String> iter1 = fileSearch.matchesIterator(new Parser().parse("foo AND bar"));
    // Iterate until iter1 has no more elements
    Iterator<String> iter2 = fileSearch.matchesIterator(new Parser().parse("'something else'"));
    // Iterate until iter2 has no more elements

### CachedSearchableTextFile and NonCachedSearchableTextFile

These two classes offer the same methods. Their constructors expect a `File` and a `Charset`.
`NonCachedSearchableTextFile` takes care of opening and closing the `InputStream` for
the underlying `OneOffTextFileSearch` and `CachedSearchableTextFile` reads the file and creates the underlying
`SuffixArrayTextFileSearch` with the file's contents.

Typical usage:

    CachedSearchableTextFile searchableTextFile = new CachedSearchableTextFile(file, Charset.forName("UTF-8"));
    List<String> matches1 = searchableTextFile.getAllLinesMatching(new Parser().parse("foo AND bar"));

    // Get matches 100 up to 200 (i.e., skip 100 and next take 200-100 matches)
    List<String> matches2 = searchableTextFile.getLinesMatching(new Parser().parse("'something else'"), 100, 200);

### Releases

| Date              | Release | Description   |
| ----------------- |:-------:| -----------   |
| August 10, 2016   | 1.0.0   | Initial release. |
| October 19, 2016  | 1.0.1   | Fixed precedence of NOT operator. Now it has highest precedence. |
| October 27, 2016  | 1.0.2   | Added a StringSearch implementation that only matches if the search text equals the text to be searched. |
| October 8, 2017   | 2.0.0   | Redesigned the API of the `nl.gogognome.textsearch.string` completely. Added Boyer-Moore algorithm. Made initialization of Suffix Array faster (O(n) instead of O(n*log(n)) |
