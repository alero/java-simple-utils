/*
 * Copyright 2004 Jonathan M. Lehr
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.alex.formatter;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;

/**
 * Simple Java Utils
 *
 * @author Robert Alexandersson
 * @version 1.0
 * @since 1.0
 */
public class CurrencyFormatter extends Formatter
{
    /** The default scale for currency values */
    public final static int SCALE = 2;
    /** The key used to look up the currency error string */


    protected Object convertToObject(String target)
    {
        // Insert currency symbol if absent
        if (!hasSymbol(target)) {
            target = interpolateSymbol(target);
        }

        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        BigDecimal value;
        try {
            Number parsedNumber = formatter.parse(target.trim());
            value = new BigDecimal(parsedNumber.toString());
            value.setScale(SCALE, BigDecimal.ROUND_HALF_EVEN);
        }
        catch (NumberFormatException e) {
            throw new FormatException(target, e);
        }
        catch (ParseException e) {
            throw new FormatException(target, e);
        }

        return value;
    }

    private String interpolateSymbol(String target) {
        return target;
    }

    private boolean hasSymbol(String target) {
        return false;
    }

    /**
     * Returns a string representation of its argument formatted as a
     * currency value.
     */
    public String format(Object obj)
    {
        if (obj == null)  return null;
        
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        String string = null;
        
        try {
            BigDecimal number = (BigDecimal) obj;
            number = number.setScale(SCALE, BigDecimal.ROUND_HALF_UP);
            return formatter.format(number.doubleValue());
        }
        catch (IllegalArgumentException e) {
            throw new FormatException(e);
        }
        catch (ClassCastException e) {
            throw new FormatException(e);
        }

    }
}
