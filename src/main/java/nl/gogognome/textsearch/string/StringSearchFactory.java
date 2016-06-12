package nl.gogognome.textsearch.string;

public class StringSearchFactory {

    public StringSearch caseSensitiveStringSearch() {
        return new CaseSensitiveStringSearch();
    }

    public StringSearch caseInsensitiveStringSearch() {
        return new CaseInsensitiveStringSearch();
    }

    public CriterionMatcher caseSensitiveCriterionMatcher() {
        return new CriterionMatcher(caseSensitiveStringSearch());
    }

    public CriterionMatcher caseInsensitiveCriterionMatcher() {
        return new CriterionMatcher(caseInsensitiveStringSearch());
    }

}
