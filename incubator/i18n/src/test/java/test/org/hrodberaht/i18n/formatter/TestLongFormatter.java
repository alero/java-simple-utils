package test.org.hrodberaht.i18n.formatter;

import org.hrodberaht.directus.exception.MessageRuntimeException;
import org.hrodberaht.i18n.formatter.Formatter;
import org.junit.After;
import org.junit.Before;
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
public class TestLongFormatter {

    @Before
    public void init() {
        System.setProperty("localeprovide.locale", "sv_SE");
    }

    @After
    public void destroy() {
        System.clearProperty("localeprovide.locale");
    }

    @Test
    public void simpleIntegerParseFormat() {
        String testLong = "14123123123131";
        Formatter<Long> formatter = Formatter.getFormatter(Long.class);
        Long aLong = formatter.convertToObject(testLong);
        String aStringLong = formatter.convertToString(aLong);
        assertEquals(testLong, aStringLong);
    }

    @Test(expected = MessageRuntimeException.class)
    public void simpleIntegerParseFormatFail() {
        String testLong = "14123123123131.12";
        Formatter<Long> formatter = Formatter.getFormatter(Long.class);
        Long aLong = formatter.convertToObject(testLong);
        String aStringLong = formatter.convertToString(aLong);
        assertEquals(testLong, aStringLong);
    }

    @Test
    public void simpleIntegerParseFormatFailVerifyMessage() {
        try {
            String testLong = "14123123123131.12";
            Formatter<Long> formatter = Formatter.getFormatter(Long.class);
            Long aLong = formatter.convertToObject(testLong);
            assertTrue("Not allowed to reach this", false);

        } catch (MessageRuntimeException e) {
            assertEquals("Parse failed on character \".\" at position: 14 for 14123123123131.12", e.getMessage());
        }
    }


}