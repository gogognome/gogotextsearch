package nl.gogognome.textsearch;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SuffixArrayTest {

    @Test
    public void testIndexOf() {
        assertEquals(-1, new SuffixArray("abc").indexOf("efg"));
        assertEquals(0, new SuffixArray("abc").indexOf("abc"));
        assertEquals(3, new SuffixArray("cdeabc").indexOf("abc"));
        assertEquals(88, new SuffixArray(" fddsklf ssdf dklfj;a lfe09terig fgns fghasjdkfg reih gejfadfk kasdfasjf dlkfuigywer gr bladiebla dasjdkhsd f").indexOf("bladiebla"));
    }

}