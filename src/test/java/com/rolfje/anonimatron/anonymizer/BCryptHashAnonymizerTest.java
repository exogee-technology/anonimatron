package com.rolfje.anonimatron.anonymizer;

import com.rolfje.anonimatron.synonyms.Synonym;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.regex.Pattern;

public class BCryptHashAnonymizerTest {

    private static final String input = "$2a$14$CM5dhKuXaqck.JmO/qdAKe4T7vtslNSVDX8rVxjzLrm28zkxhZ/rW";
    private BCryptHashAnonymizer bcryptAnonymizer;

    @Before
    public void setUp() {
        bcryptAnonymizer = new BCryptHashAnonymizer();
    }

    @Test
    public void testHappyFlow() {
        Synonym synonym = bcryptAnonymizer.anonymize(input, Integer.MAX_VALUE, false);

        assertNotEquals(synonym.getFrom(), synonym.getTo());
        assertEquals(bcryptAnonymizer.getType(), synonym.getType());
        assertEquals(synonym.getFrom().toString().length(), BCryptHashAnonymizerTest.input.length());
        assertFalse(synonym.isShortLived());

        System.out.println("-------------------------------");
        System.out.println(synonym.getTo().toString());
        System.out.println("-------------------------------");

        assertTrue(Pattern.matches("^\\$2b\\$14\\$[A-Za-z0-9/]{53}$", synonym.getTo().toString()));
    }

    @Test
    public void testPHPVariant() {
        Synonym synonym = bcryptAnonymizer.anonymize(BCryptHashAnonymizerTest.input, Integer.MAX_VALUE, false, new HashMap<String, String>() {{
                put("variant", "2y");
        }});

        assertTrue(Pattern.matches("^\\$2y\\$14\\$[A-Za-z0-9/]{53}$", synonym.getTo().toString()));
    }

    @Test
    public void testOriginalVariant() {
        Synonym synonym = bcryptAnonymizer.anonymize(BCryptHashAnonymizerTest.input, Integer.MAX_VALUE, false, new HashMap<String, String>() {{
                put("variant", "2");
        }});

        assertTrue(Pattern.matches("^\\$2\\$14\\$[A-Za-z0-9/]{54}$", synonym.getTo().toString()));
    }

    @Test
    public void testDifferentDifficulty() {
        Synonym synonym = bcryptAnonymizer.anonymize(BCryptHashAnonymizerTest.input, Integer.MAX_VALUE, false, new HashMap<String, String>() {{
                put("difficulty", "10");
        }});

        assertTrue(Pattern.matches("^\\$2b\\$10\\$[A-Za-z0-9/]{53}$", synonym.getTo().toString()));
    }

    @Test
    public void testNullInput() {
        assertNull(bcryptAnonymizer.anonymize(null, Integer.MAX_VALUE, true));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIncorrectInputType() {
        bcryptAnonymizer.anonymize(new Long(0), Integer.MAX_VALUE, true);
    }
}