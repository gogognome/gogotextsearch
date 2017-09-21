package nl.gogognome.textsearch.string;

import static nl.gogognome.textsearch.CaseSensitivity.INSENSITIVE;
import static nl.gogognome.textsearch.CaseSensitivity.SENSITIVE;

/**
 * A factory class for creating {@link StringSearch} and {@link CriterionMatcher} instances.
 */
public class StringSearchFactory {

    /**
     * @return creates a @{@link StringSearch} that is case sensitive
     */
    public StringSearch caseSensitiveStringSearch() {
        return new FastStringSearch(SENSITIVE);
    }

    /**
     * @return creates a @{@link StringSearch} that is case insensitive
     */
    public StringSearch caseInsensitiveStringSearch() {
        return new FastStringSearch(INSENSITIVE);
    }

    /**
     * @return creates a @{@link CriterionMatcher} that is case sensitive
     */
    public CriterionMatcher caseSensitiveCriterionMatcher() {
        StringSearch stringSearch = caseSensitiveStringSearch();
        return new CriterionMatcher((text, literal) -> stringSearch.indexOf(text, literal) != -1);
    }

    /**
     * @return creates a @{@link CriterionMatcher} that is case insensitive
     */
    public CriterionMatcher caseInsensitiveCriterionMatcher() {
        StringSearch stringSearch = caseInsensitiveStringSearch();
        return new CriterionMatcher((text, literal) -> stringSearch.indexOf(text, literal) != -1);
    }


    /**
     * @return creates a @{@link CriterionMatcher} that is case sensitive
     */
    public CriterionMatcher caseSensitiveStringEqualsCriterionMatcher() {
        StringSearch stringSearch = caseSensitiveStringSearch();
        return new CriterionMatcher((text, literal) -> stringSearch.equals(text, literal));
    }

    /**
     * @return creates a @{@link CriterionMatcher} that is case insensitive
     */
    public CriterionMatcher caseInsensitiveStringEqualsCriterionMatcher() {
        StringSearch stringSearch = caseInsensitiveStringSearch();
        return new CriterionMatcher((text, literal) -> stringSearch.equals(text, literal));
    }

}
