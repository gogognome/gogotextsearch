package nl.gogognome.textsearch.string;

import nl.gogognome.textsearch.criteria.Criterion;

import java.util.function.Predicate;

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
     * @param criterion the criterion that is matched by the returned @{@link CriterionMatcher}
     * @return creates a @{@link CriterionMatcher} that is case sensitive
     */
    public CriterionMatcher caseSensitiveCriterionMatcher(Criterion criterion) {
        return caseSensitiveCriterionMatcherBuilder().build(criterion);
    }

    public CriterionMatcher.Builder caseSensitiveCriterionMatcherBuilder() {
        return new CriterionMatcher.Builder(pattern -> new BoyerMoorePredicate(new BoyerMoore(pattern, SENSITIVE)));
    }

    /**
     * @param criterion the criterion that is matched by the returned @{@link CriterionMatcher}
     * @return creates a @{@link CriterionMatcher} that is case insensitive
     */
    public CriterionMatcher caseInsensitiveCriterionMatcher(Criterion criterion) {
        return caseInsensitiveCriterionMatcherBuilder().build(criterion);
    }

    /**
     * @return creates a @{@link CriterionMatcher} that is case insensitive
     */
    public CriterionMatcher.Builder caseInsensitiveCriterionMatcherBuilder() {
        return new CriterionMatcher.Builder(pattern -> new BoyerMoorePredicate(new BoyerMoore(pattern, INSENSITIVE)));
    }

    private static class BoyerMoorePredicate implements Predicate<String> {
        private BoyerMoore boyerMoore;

        private BoyerMoorePredicate(BoyerMoore boyerMoore) {
            this.boyerMoore = boyerMoore;
        }

        @Override
        public boolean test(String text) {
            return boyerMoore.indexIn(text) != -1;
        }
    }

    /**
     * @param criterion the criterion that is matched by the returned @{@link CriterionMatcher}
     * @return creates a @{@link CriterionMatcher} that is case sensitive
     */
    public CriterionMatcher caseSensitiveStringEqualsCriterionMatcher(Criterion criterion) {
        return caseSensitiveStringEqualsCriterionMatcherBuilder().build(criterion);
    }

    public CriterionMatcher.Builder caseSensitiveStringEqualsCriterionMatcherBuilder() {
        StringSearch stringSearch = caseSensitiveStringSearch();
        return new CriterionMatcher.Builder(pattern -> text -> stringSearch.equals(text, pattern));
    }

    /**
     * @param criterion the criterion that is matched by the returned @{@link CriterionMatcher}
     * @return creates a @{@link CriterionMatcher} that is case insensitive
     */
    public CriterionMatcher caseInsensitiveStringEqualsCriterionMatcher(Criterion criterion) {
        return caseInsensitiveStringEqualsCriterionMatcherBuilder().build(criterion);
    }

    public CriterionMatcher.Builder caseInsensitiveStringEqualsCriterionMatcherBuilder() {
        StringSearch stringSearch = caseInsensitiveStringSearch();
        return new CriterionMatcher.Builder(pattern -> text -> stringSearch.equals(text, pattern));
    }

}
