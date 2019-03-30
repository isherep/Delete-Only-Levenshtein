

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * The test class TestDistance.
 *
 * @author  (your name)
 * @version (a version number or a date)
 */
public class TestDistance{
    /**
     * Default constructor for test class TestDistance
     */
    public TestDistance(){
    }

    /**
     * Sets up the test fixture.
     *
     * Called before every test case method.
     */
    @Before
    public void setUp(){
    }

    /**
     * Tears down the test fixture.
     *
     * Called after every test case method.
     */
    @After
    public void tearDown(){
    }
    
    
    @Test
    public void testDeleteOnlyLevenshtein(){
        
        assertEquals(1, ModifiedLevenshtein.deleteOnlyLevenshtein("appple", "apple")); 
        assertEquals(5, ModifiedLevenshtein.deleteOnlyLevenshtein("diffffffference", "difference")); 
        assertEquals(4, ModifiedLevenshtein.deleteOnlyLevenshtein("apppleeee", "apple"));
        assertEquals(5, ModifiedLevenshtein.deleteOnlyLevenshtein("jjeeaannss", "jeans"));
        assertEquals(14, ModifiedLevenshtein.deleteOnlyLevenshtein("nnnineviiiitiiiisssshhhh", "ninevitish")); 
        assertEquals(22, ModifiedLevenshtein.deleteOnlyLevenshtein("pppppppuuuuuuuuuggggggggg", "pug"));

    }
    
    
    @Test
    public void testDeleteAndReplaceWrongCasing(){
        assertEquals(1, ModifiedLevenshtein.deleteAndReplaceWrongCasing("Ninevitish", "ninevitish")); 
        assertEquals(6, ModifiedLevenshtein.deleteAndReplaceWrongCasing("Inermes", "IIIIIIInermes"));
        assertEquals(1, ModifiedLevenshtein.deleteAndReplaceWrongCasing("appple", "apple"));
    }

}
