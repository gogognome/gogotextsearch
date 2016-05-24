package nl.gogognome.textsearch.criteria;

import nl.gogognome.textsearch.criteria.StringLiteral;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class StringLiteralTest {

    private StringLiteral stringLiteral = new StringLiteral("abc");

    @Test
    public void testGetter() {
        assertEquals("abc", stringLiteral.getLiteral());
    }

    @Test
    public void testToString() {
        assertEquals("abc", stringLiteral.toString());
    }
}