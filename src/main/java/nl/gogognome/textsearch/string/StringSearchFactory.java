package nl.gogognome.textsearch.string;

/**
 * A factory class for creating {@link StringSearch} and {@link CriterionMatcher} instances.
 */
public class StringSearchFactory {

    /**
     * @return creates a @{@link StringSearch} that is case sensitive
     */
    public StringSearch caseSensitiveStringSearch() {
        return new CaseSensitiveStringSearch();
    }

    /**
     * @return creates a @{@link StringSearch} that is case insensitive
     */
    public StringSearch caseInsensitiveStringSearch() {
        return new CaseInsensitiveStringSearch();
    }

    /**
     * @return creates a @{@link CriterionMatcher} that is case sensitive
     */
    public CriterionMatcher caseSensitiveCriterionMatcher() {
        return new CriterionMatcher(caseSensitiveStringSearch());
    }

    /**
     * @return creates a @{@link CriterionMatcher} that is case insensitive
     */
    public CriterionMatcher caseInsensitiveCriterionMatcher() {
        return new CriterionMatcher(caseInsensitiveStringSearch());
    }

}
