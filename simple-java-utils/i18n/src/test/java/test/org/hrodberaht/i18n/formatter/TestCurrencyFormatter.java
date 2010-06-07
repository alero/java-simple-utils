package test.org.hrodberaht.i18n.formatter;

import org.hrodberaht.directus.exception.MessageRuntimeException;
import org.hrodberaht.i18n.formatter.Formatter;
import org.hrodberaht.i18n.formatter.types.CurrencyData;
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
public class TestCurrencyFormatter {

    @Before
    public void init() {
        System.setProperty("localeprovide.locale", "sv_SE");
    }

    @After
    public void destroy(){
        System.clearProperty("localeprovide.locale");
    }

    @Test
    public void simpleDecimalParseFormat() {
        String testDouble = "12 314 kr";
        assertEquals("12 314,00 kr", testFormatAndParse(testDouble));
    }

    @Test
    public void simpleDecimalParseFormatNoCharacter() {
        String testDouble = "12 314";
        Formatter<CurrencyData> formatter = Formatter.getFormatter(CurrencyData.class);
        CurrencyData aDouble = formatter.convertToObject(testDouble);
        String aStringDouble = formatter.convertToString(aDouble);
        assertEquals(testDouble + ",00 kr", aStringDouble);
    }

    @Test(expected = MessageRuntimeException.class)
    public void simpleDecimalParseFormatEmptyCharacters() {
        String testDouble = "12 314,43 kr ";
        testFormatAndParse(testDouble);
    }


    @Test(expected = MessageRuntimeException.class)
    public void simpleDecimalParseFormatDuplicateDecimalCharacters() {
        String testDouble = "12 314,43, kr";
        testFormatAndParse(testDouble);
    }

    @Test(expected = MessageRuntimeException.class)
    public void simpleDecimalParseFormatBadPlacedGroupingSeparator() {
        String testDouble = "1 12 314 kr";
        assertEquals("112 314,00 kr", testFormatAndParse(testDouble));
    }

    @Test
    public void simpleDecimalParseFormatSeveralGroupingSeparator() {
        String testDouble = "11 123 314 kr";
        assertEquals("11 123 314,00 kr", testFormatAndParse(testDouble));
    }

    @Test
    public void simpleDecimalOtherLocale() {
        System.setProperty("localeprovide.locale", "en_US");
        String testDouble = "$11,123,314";
        assertEquals("$11,123,314.00", testFormatAndParse(testDouble));
    }

    @Test
    public void simpleDecimalOtherNegativeLocale() {
        System.setProperty("localeprovide.locale", "en_US");
        // Is this for real, i had no clue the US did negative currency like this...
        String testDouble = "($11,123,314)";
        assertEquals("($11,123,314.00)", testFormatAndParse(testDouble));
    }

    private String testFormatAndParse(String testDouble) {
        Formatter<CurrencyData> formatter = Formatter.getFormatter(CurrencyData.class);
        CurrencyData aDouble = formatter.convertToObject(testDouble);
        return formatter.convertToString(aDouble);
    }

    /*@Test
    public void testAll() {
        Locale[] locales = NumberFormat.getAvailableLocales();
        double myNumber = -1234.56;
        NumberFormat form;

        System.out.println("FORMAT");
        for (int i = 0; i < locales.length; ++i) {
            System.out.print(locales[i].getDisplayName());
            form = NumberFormat.getCurrencyInstance(locales[i]);
            if (form instanceof DecimalFormat) {
                System.out.print(": " + ((DecimalFormat) form).toPattern());
            }
            System.out.print(" -> " + form.format(myNumber));
            try {
                System.out.println(" -> " + form.parse(form.format(myNumber)));
            } catch (ParseException e) {
            }
        }

    } */

}