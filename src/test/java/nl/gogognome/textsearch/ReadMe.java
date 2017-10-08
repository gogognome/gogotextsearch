package nl.gogognome.textsearch;

import nl.gogognome.textsearch.criteria.And;
import nl.gogognome.textsearch.criteria.Criterion;
import nl.gogognome.textsearch.criteria.Parser;
import nl.gogognome.textsearch.criteria.StringLiteral;
import nl.gogognome.textsearch.string.*;
import nl.gogognome.textsearch.textfile.CachedSearchableTextFile;
import nl.gogognome.textsearch.textfile.OneOffTextFileSearch;
import nl.gogognome.textsearch.textfile.SuffixArrayTextFileSearch;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.Assert.*;

/**
 * This class contains sample code for the read me file.
 */
public class ReadMe {

    @Test
    public void stringSearchSample() {
        StringSearch stringSearch = new StringSearchFactory().caseInsensitiveStringSearch();
        int index = stringSearch.indexOf("Barefoot is a movie directed by Andrew Flemming.", "Foot");
        // index == 4

        List<Integer> indexes = stringSearch.indexesOf("Barefoot is a movie directed by Andrew Flemming.", "re");
        // indexes == [2, 22, 35]

        assertEquals(4, index);
        assertEquals(asList(2, 22, 35), indexes);
    }

    @Test
    public void boyerMooreSample() {
        BoyerMoore boyerMoore = new BoyerMoore("Foot", CaseSensitivity.INSENSITIVE);
        int index = boyerMoore.indexIn("Barefoot is a movie directed by Andrew Flemming.");
        // index == 4

        BoyerMoore boyerMoore2 = new BoyerMoore("re", CaseSensitivity.INSENSITIVE);
        List<Integer> indexes = boyerMoore2.indexesIn("Barefoot is a movie directed by Andrew Flemming.");
        // indexes == [2, 22, 35]

        assertEquals(4, index);
        assertEquals(asList(2, 22, 35), indexes);
    }

    @Test
    public void suffixArraySample() {
        SuffixArray suffixArray = new SuffixArray("Barefoot is a movie directed by Andrew Flemming.", CaseSensitivity.INSENSITIVE);
        int index = suffixArray.indexOf("Foot");
        // index == 4
        List<Integer> indexes = suffixArray.indexesOf("re");
        // indexes == [2, 22, 35]

        assertEquals(4, index);
        assertEquals(asList(2, 22, 35), indexes);
    }

    @Test
    public void buildCriterionYourselfSample() {
        Criterion searchCriterion = new And(new StringLiteral("foo"), new StringLiteral("bar"));

        assertEquals("(foo AND bar)", searchCriterion.toString());
    }

    @Test
    public void criterionParserSample() {
        Criterion searchCriterion = new Parser().parse("foo AND bar");

        assertEquals("(foo AND bar)", searchCriterion.toString());
    }

    @Test
    public void criterionMatcherSample() {
        Criterion searchCriterion = new Parser().parse("foo AND bar");
        CriterionMatcher matcher = new StringSearchFactory().caseInsensitiveCriterionMatcher();
        boolean matches = matcher.matches(searchCriterion, "Barefoot is a movie directed by Andrew Flemming.");
        // matches == true

        assertTrue(matches);
    }

    @Test
    public void criterionMatcherVarargsSample() {
        Criterion searchCriterion = new Parser().parse("foo AND bar");
        CriterionMatcher matcher = new StringSearchFactory().caseInsensitiveCriterionMatcher();
        boolean matches1 = matcher.matches(searchCriterion, "Bart", "food");
        // matches1 == true
        boolean matches2 = matcher.matches(searchCriterion, "ba", "rt food");
        // matches2 == false because "bar" is not found

        assertTrue(matches1);
        assertFalse(matches2);
    }

    @Test
    public void oneOffTextFileSearchSample() throws Exception {
        List<String> actualMatches = new ArrayList<>();
        InputStream inputStream = new ByteArrayInputStream("foo\nfoo bar\nbar\nbarefoot".getBytes(StandardCharsets.UTF_8.name()));

        // InputStream inputStream = ...
        Criterion searchCriterion = new Parser().parse("foo AND bar");
        Charset charset = StandardCharsets.UTF_8;
        CriterionMatcher criterionMatcher = new StringSearchFactory().caseInsensitiveCriterionMatcher();
        Iterator<String> iter = new OneOffTextFileSearch(inputStream, charset, criterionMatcher).matchesIterator(searchCriterion);
        while (iter.hasNext()) {
            String nextLine = iter.next();
            // use nextLine
            actualMatches.add(nextLine); // remove from README.md
        }
        inputStream.close();

        assertEquals(asList("foo bar", "barefoot"), actualMatches);
    }

    @Test
    public void suffixArrayTextFileSearchSample() {
        SuffixArrayTextFileSearch fileSearch = new SuffixArrayTextFileSearch("Some huge file's contents here...");
        Iterator<String> iter1 = fileSearch.matchesIterator(new Parser().parse("foo AND bar"));
        // Iterate until iter1 has no more elements
        Iterator<String> iter2 = fileSearch.matchesIterator(new Parser().parse("'something else'"));
        // Iterate until iter2 has no more elements

        assertFalse(iter1.hasNext());
        assertFalse(iter2.hasNext());
    }

    @Test
    public void cachedSearchableTextFileSample() throws Exception {
        File file = File.createTempFile("cachedSearchableTextFile", "txt");
        try {
            CachedSearchableTextFile searchableTextFile = new CachedSearchableTextFile(file, Charset.forName("UTF-8"));
            List<String> matches1 = searchableTextFile.getAllLinesMatching(new Parser().parse("foo AND bar"));

            // Get matches 100 up to 200 (i.e., skip 100 and next take 200-100 matches)
            List<String> matches2 = searchableTextFile.getLinesMatching(new Parser().parse("'something else'"), 100, 200);

            assertTrue(matches1.isEmpty());
            assertTrue(matches2.isEmpty());
        }finally {
            assertTrue(file.delete());
        }
    }

}
