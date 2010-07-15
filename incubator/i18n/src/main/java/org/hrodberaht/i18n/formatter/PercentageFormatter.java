/*
 * <!--
 *   ~ Copyright (c) 2010.
 *   ~ Licensed under the Apache License, Version 2.0 (the "License");
 *   ~ you may not use this file except in compliance with the License.
 *   ~ You may obtain a copy of the License at
 *   ~
 *   ~        http://www.apache.org/licenses/LICENSE-2.0
 *   ~
 *   ~ Unless required by applicable law or agreed to in writing, software
 *   ~ distributed under the License is distributed on an "AS IS" BASIS,
 *   ~ WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   ~ See the License for the specific language governing permissions and limitations under the License.
 *   -->
 */

package org.hrodberaht.i18n.formatter;

import org.hrodberaht.directus.exception.MessageRuntimeException;
import org.hrodberaht.i18n.formatter.types.PercentData;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Simple Java Utils
 *
 * @author Robert Alexandersson
 * @version 1.0
 * @since 1.0
 */
public class PercentageFormatter extends NumberFormatter {
    /**
     * The default scale for percentage values
     */
    public static final int PERCENTAGE_SCALE = 2;

    /**
     * Unformats its argument and returns a BigDecimal instance
     * initialized with the resulting string value
     *
     * @return a BigDecimal initialized with the provided string
     */
    public Object convertToObject(String target) {
        try {
            NumberFormat formatter = DecimalFormat.getPercentInstance(locale);
            if(target.indexOf("%") == -1){
                target += "%";                
            }
            Number parsedNumber = parseNumber(target, formatter);
            return new PercentData(parsedNumber.doubleValue());
        } catch (NumberFormatException e) {
            throw new MessageRuntimeException(e);
        }
    }



    /**
     * Returns a string representation of its argument, formatted as a
     * percentage value.
     *
     * @return a formatted String
     */
    public String convertToString(Object value) {
        if (value == null) {
            return "";
        }

        try {
            BigDecimal bigDecValue = getBigDecimal(value);
            NumberFormat format = NumberFormat.getPercentInstance(locale);
            format = fixCharacterJVMErrorsForDecimalFormat(format);
            return format.format(bigDecValue.doubleValue());
        } catch (IllegalArgumentException iae) {
            throw new FormatException("Unable to format {0} as a percentage value", iae, value);
        }

    }


    private BigDecimal getBigDecimal(Object value) {
        BigDecimal bigDecValue;
        if (value instanceof Double) {
            bigDecValue = new BigDecimal((Double) value);
        } else {
            bigDecValue = (BigDecimal) value;
        }
        bigDecValue = bigDecValue.setScale(PERCENTAGE_SCALE,
                BigDecimal.ROUND_HALF_UP);
        return bigDecValue;
    }
}
