package test.org.hrodberaht.i18n.formatter;

import org.hrodberaht.i18n.formatter.BooleanFormatter;
import org.hrodberaht.i18n.formatter.Formatter;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Simple Java Utils
 *
 * @author Robert Alexandersson
 *         2010-jun-07 19:48:36
 * @version 1.0
 * @since 1.0
 */
public class TestBooleanFormatter {

  

    @Test
    public void simpleBooleanParseFormatTrue(){
        String testBoolean = "true";
        Formatter<Boolean> formatter = Formatter.getFormatter(Boolean.class);
        Boolean aBoolean = formatter.convertToObject(testBoolean);
        String aStringBoolean = formatter.convertToString(aBoolean);
        assertEquals(BooleanFormatter.BOOLEAN_TRUE, aStringBoolean);
    }

    @Test
    public void simpleBooleanParseFormatFalse(){
        String testBoolean = "false";
        Formatter<Boolean> formatter = Formatter.getFormatter(Boolean.class);
        Boolean aBoolean = formatter.convertToObject(testBoolean);
        String aStringBoolean = formatter.convertToString(aBoolean);
        assertEquals(BooleanFormatter.BOOLEAN_FALSE, aStringBoolean);
    }

    @Test
    public void testAllTrueFormats(){
        List<String> TRUE_VALUES = Arrays.asList(
            "yes", "true", "on", "1", "enabled");
        Formatter<Boolean> formatter = Formatter.getFormatter(Boolean.class);
        for(String testBoolean:TRUE_VALUES){
            Boolean aBoolean = formatter.convertToObject(testBoolean);
            assertEquals(true, aBoolean);
        }
    }

    @Test
    public void testAllFalseFormats(){
        List<String> FALSE_VALUES = Arrays.asList(
            "no", "false", "off", "0", "disabled" );
        Formatter<Boolean> formatter = Formatter.getFormatter(Boolean.class);
        for(String testBoolean:FALSE_VALUES){
            Boolean aBoolean = formatter.convertToObject(testBoolean);
            assertEquals(false, aBoolean);
        }
    }
}
