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
import org.hrodberaht.i18n.formatter.types.CurrencyData;

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
public class CurrencyFormatter extends NumberFormatter {
    public final static int SCALE = 2;

    public Object convertToObject(String target) {
        if (target == null) {
            return null;
        }

        NumberFormat formatter = NumberFormat.getCurrencyInstance(locale);
        // Insert currency symbol if absent

        target = interpolateSymbol(target, formatter);


        Number parsedNumber = parseNumber(target, formatter);
        BigDecimal value = new BigDecimal(parsedNumber.doubleValue());
        // value = value.setScale(SCALE, BigDecimal.ROUND_HALF_EVEN);
        return new CurrencyData(value.doubleValue());

    }

    private String interpolateSymbol(String target, NumberFormat formatter) {
        DecimalFormat decimalFormat = (DecimalFormat) formatter;
        if(!"".equals(decimalFormat.getPositivePrefix())){
            String symbol = decimalFormat.getPositivePrefix();
            if (!hasSymbol(target, symbol)) {
                return symbol+target;
            }
        }
        if(!"".equals(decimalFormat.getPositiveSuffix())){
            String symbol = decimalFormat.getPositiveSuffix();
            if (!hasSymbol(target, symbol)) {
                return target+symbol;
            }
        }
        return target;
    }

    private boolean hasSymbol(String target, String symbol) {
        if (target.indexOf(symbol) != -1) {
            return true;
        }
        return false;
    }

    /**
     * Returns a string representation of its argument formatted as a
     * currency value.
     */
    public String convertToString(Object obj) {
        if (obj == null) {
            return null;
        }

        NumberFormat formatter = NumberFormat.getCurrencyInstance(locale);
        try {
            BigDecimal number = (BigDecimal) obj;
            number = number.setScale(SCALE, BigDecimal.ROUND_HALF_UP);
            formatter = fixCharacterJVMErrorsForDecimalFormat(formatter);
            return formatter.format(number.doubleValue());
        }
        catch (IllegalArgumentException e) {
            throw new MessageRuntimeException(e);
        }

    }
}
