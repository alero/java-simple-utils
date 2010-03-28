/*
 * Copyright (c) 2010.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package org.hrodberaht.directus.util.formatter;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;

/**
 * Simple Java Utils
 *
 * @author Robert Alexandersson
 * @version 1.0
 * @since 1.0
 */
public class PercentageFormatter extends Formatter {
    /**
     * The default scale for percentage values
     */
    public final static int PERCENTAGE_SCALE = 2;

    /**
     * Unformats its argument and returns a BigDecimal instance
     * initialized with the resulting string value
     *
     * @return a BigDecimal initialized with the provided string
     */
    public Object convertToObject(String target)
    {
        try {
            NumberFormat formatter = DecimalFormat.getPercentInstance(locale);
            Number parsedNumber = formatter.parse(target.trim());
            return new BigDecimal(parsedNumber.doubleValue());
        }
        catch (NumberFormatException e) {
            throw new FormatException(e);
        }
        catch (ParseException e) {
            throw new FormatException(e);
        }
    }

    /**
     * Returns a string representation of its argument, formatted as a
     * percentage value.
     *
     * @return a formatted String
     */
    public String convertToString(Object value) {
        if (value == null)
            return "";


        try {
            BigDecimal bigDecValue;
            if(value instanceof Double){
                bigDecValue = new BigDecimal((Double)value);
            }else {
                bigDecValue = (BigDecimal)value;
            }

            bigDecValue = bigDecValue.setScale(PERCENTAGE_SCALE,
                                               BigDecimal.ROUND_HALF_UP);
            return NumberFormat.getPercentInstance(locale).
                format(bigDecValue.doubleValue());
        }
        catch (IllegalArgumentException iae) {
            throw new FormatException("Unable to format {0} as a percentage value", iae, value);
        }
        

    }
}
