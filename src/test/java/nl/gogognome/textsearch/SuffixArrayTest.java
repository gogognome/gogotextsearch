package nl.gogognome.textsearch;

import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;

public class SuffixArrayTest {

    @Test
    public void testIndexOf() {
        SuffixArray suffixArray = new SuffixArray(" fddsklf ssdf dklfj;a lfe09terig fgns fghasjdkfg reih gejfadfk kasdfasjf dlkfuigywer gr bladiebla dasjdkhsd f");
        assertEquals(88, suffixArray.indexOf("bladiebla"));
    }

    @Test
    public void performance() {
        Random r = new Random(1230);
        int size = 1000000;
        StringBuilder sb = new StringBuilder(size);
        for (int i=0; i<size; i++) {
            sb.append((char) r.nextInt());
        }
        String text = sb.toString();
//        String text = " fddsklf ssdf dklfj;a lfe09terig fgns fghasjdkfg reih gejfadfk kasdfasjf dlkfuigywer gr bladiebla dasjdkhsd f";
        String searchText = "bladiebla";
        SuffixArray suffixArray = new SuffixArray(text);
        System.out.println("suffix array created");
        int n = 1000;
        long start = System.currentTimeMillis();
        for (int i=0; i<n; i++) {
//            suffixArray.indexOf(searchText);
            text.indexOf(searchText);
        }
        long end = System.currentTimeMillis();
        float duration = (end - start) / 1000f ;
        float nrIterationsPerSecond = n / duration;
        System.out.println("n: " + n + " iterations in " + duration + " ms" + " correponds to " + nrIterationsPerSecond + " nr iterations/second");
    }
}