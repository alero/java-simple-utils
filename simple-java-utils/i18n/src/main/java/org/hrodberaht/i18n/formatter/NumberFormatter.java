package org.hrodberaht.i18n.formatter;

import org.hrodberaht.directus.exception.MessageRuntimeException;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParsePosition;

/**
 * Simple Java Utils
 *
 * @author Robert Alexandersson
 *         2010-jun-07 20:30:59
 * @version 1.0
 * @since 1.0
 */
public class NumberFormatter extends Formatter {

    protected Number parseNumber(String target, NumberFormat decimalFormat) {
        decimalFormat = fixCharacterJVMErrorsForDecimalFormat(decimalFormat);
        validateGroupingSeparator(target, decimalFormat);
        ParsePosition position = new ParsePosition(0);
        Number number = decimalFormat.parse(target, position);
        if (position.getIndex() != target.length()) {
            if(position.getErrorIndex() != -1){
                throw new MessageRuntimeException("Parse failed on character \"{0}\" at position: {1} for {2}",
                    target.charAt(position.getErrorIndex()), position.getErrorIndex(), target);
            }
            throw new MessageRuntimeException("Parse failed on character \"{0}\" at position: {1} for {2}",
                    target.charAt(position.getIndex()), position.getIndex(), target);
            
        }
        return number;
    }

    private void validateGroupingSeparator(String target, NumberFormat format) {
        DecimalFormat decimalFormat = (DecimalFormat) format;
        DecimalFormatSymbols decimalFormatSymbols = decimalFormat.getDecimalFormatSymbols();
        char groupingSeparator = decimalFormatSymbols.getGroupingSeparator();
        char decimalSeparator = decimalFormatSymbols.getDecimalSeparator();
        int decimalPosition = target.indexOf(decimalSeparator);
        if(target.startsWith(decimalFormat.getNegativePrefix())){
            target = target.substring(decimalFormat.getNegativePrefix().length(),
                    target.length()-decimalFormat.getNegativeSuffix().length());
        } else {
            target = target.substring(decimalFormat.getPositivePrefix().length(),
                    target.length()-decimalFormat.getPositiveSuffix().length());
        }
        int targetLength = target.length();
        boolean firstSeparator = true;
        if (target.indexOf(groupingSeparator) != -1) {
            int groupingLength = decimalFormat.getGroupingSize();
            for (int i = 0; i < target.length(); i++) {
                if (target.charAt(i) == groupingSeparator
                        && validatePosition(targetLength, decimalPosition, groupingLength, i)) {
                    if (firstSeparator) {
                        firstSeparator = false;
                    } else {
                        throw new MessageRuntimeException("Parse failed on character \"{0}\" at position: {1} for {3}"
                                , groupingSeparator, i, target);
                    }
                }
            }
        }
    }

    private boolean validatePosition(int targetLength, int decimalPosition, int groupingLength, int position) {
        if(decimalPosition != -1){
            return
                    Math.abs((targetLength-decimalPosition)-position) % groupingLength != 0;
        }
        return position % groupingLength != 0;
    }



    protected NumberFormat fixCharacterJVMErrorsForDecimalFormat(NumberFormat format) {
        DecimalFormat decimalFormat = (DecimalFormat) format;
        DecimalFormatSymbols decimalFormatSymbols = decimalFormat.getDecimalFormatSymbols();
        if (decimalFormatSymbols.getGroupingSeparator() == '\u00A0') {
            decimalFormatSymbols.setGroupingSeparator(' ');
            // char 160 (breaking space) is replaced by a normal space character 32 
            decimalFormat.setDecimalFormatSymbols(decimalFormatSymbols);
        }
        return format;
    }

}
