package nl.gogognome.textsearch.criteria;

import nl.gogognome.textsearch.criteria.StringLiteral;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class StringLiteralTest {

    @Test
    public void testMatches() {
        assertFalse(new StringLiteral("bla").matches(null));
        assertFalse(new StringLiteral("bla").matches(""));
        assertFalse(new StringLiteral("bla").matches("b dsfj asd b l   adf asf"));
        assertTrue(new StringLiteral("bla").matches("bla"));
        assertTrue(new StringLiteral("bla").matches("BlA"));
        assertTrue(new StringLiteral("bla").matches("BLA"));
        assertTrue(new StringLiteral("bla").matches("dsfdf BlA"));
        assertTrue(new StringLiteral("bla").matches("dsfdf blA dsafa"));
    }

}