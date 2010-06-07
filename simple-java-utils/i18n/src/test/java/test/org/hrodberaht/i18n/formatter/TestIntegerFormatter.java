package test.org.hrodberaht.i18n.formatter;

import org.hrodberaht.directus.exception.MessageRuntimeException;
import org.hrodberaht.i18n.formatter.Formatter;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Simple Java Utils
 *
 * @author Robert Alexandersson
 *         2010-jun-07 19:48:36
 * @version 1.0
 * @since 1.0
 */
public class TestIntegerFormatter {



    @Test
    public void simpleIntegerParseFormat(){
        String testInteger = "14";
        Formatter<Integer> formatter = Formatter.getFormatter(Integer.class);
        Integer aInteger = formatter.convertToObject(testInteger);
        String aStringInteger = formatter.convertToString(aInteger);
        assertEquals(testInteger, aStringInteger);
    }

    @Test(expected = MessageRuntimeException.class)
    public void simpleIntegerParseFormatFail(){
        String testInteger = "14.2";
        Formatter<Integer> formatter = Formatter.getFormatter(Integer.class);
        Integer aInteger = formatter.convertToObject(testInteger);
        String aStringInteger = formatter.convertToString(aInteger);
        assertEquals(testInteger, aStringInteger);
    }

    @Test
    public void simpleIntegerParseFormatFailVerifyMessage(){
        String testInteger = "14.2";
        try {
            Formatter<Integer> formatter = Formatter.getFormatter(Integer.class);
            Integer aInteger = formatter.convertToObject(testInteger);
            assertTrue("Not allowed to reach this", false);

        } catch (MessageRuntimeException e) {
            assertEquals("Parse failed on character \".\" at position: 2 for 14.2", e.getMessage());
        }
    }

 
}