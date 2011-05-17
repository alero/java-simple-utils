package test.org.hrodberaht.i18n.formatter;

import org.hrodberaht.directus.exception.MessageRuntimeException;
import org.hrodberaht.i18n.formatter.Formatter;
import org.hrodberaht.i18n.formatter.types.PercentData;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Simple Java Utils
 *
 * @author Robert Alexandersson
 *         2010-jun-07 19:48:36
 * @version 1.0
 * @since 1.0
 */
public class TestPercentageFormatter {

    @Before
    public void init() {
        System.setProperty("localeprovide.locale", "sv_SE");
    }

    @After
    public void destroy() {
        System.clearProperty("localeprovide.locale");
    }

    @Test
    public void simpleDecimalParseFormat() {
        String testDouble = "12 314%";
        testFormatAndParse(testDouble);
    }

    @Test
    public void simpleDecimalParseFormatNoCharacter() {
        String testDouble = "12 314";
        Formatter<PercentData> formatter = Formatter.getFormatter(PercentData.class);
        PercentData aDouble = formatter.convertToObject(testDouble);
        String aStringDouble = formatter.convertToString(aDouble);
        assertEquals(testDouble + "%", aStringDouble);
    }

    @Test(expected = MessageRuntimeException.class)
    public void simpleDecimalParseFormatEmptyCharacters() {
        String testDouble = "12 314,43% ";
        testFormatAndParse(testDouble);
    }


    @Test(expected = MessageRuntimeException.class)
    public void simpleDecimalParseFormatDuplicateDecimalCharacters() {
        String testDouble = "12 314,43,%";
        testFormatAndParse(testDouble);
    }

    @Test(expected = MessageRuntimeException.class)
    public void simpleDecimalParseFormatBadPlacedGroupingSeparator() {
        String testDouble = "1 12 314%";
        testFormatAndParse(testDouble);
    }

    @Test
    public void simpleDecimalParseFormatSeveralGroupingSeparator() {
        String testDouble = "11 123 314%";
        testFormatAndParse(testDouble);
    }


    private void testFormatAndParse(String testDouble) {
        Formatter<PercentData> formatter = Formatter.getFormatter(PercentData.class);
        PercentData aDouble = formatter.convertToObject(testDouble);
        String aStringDouble = formatter.convertToString(aDouble);
        assertEquals(testDouble, aStringDouble);
    }


}