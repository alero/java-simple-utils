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

import org.hrodberaht.directus.util.formatter.types.CurrencyData;
import org.hrodberaht.directus.util.formatter.types.MeasureData;
import org.hrodberaht.directus.util.formatter.types.PercentData;
import org.hrodberaht.directus.util.locale.LocaleProvider;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.Date;
import java.util.Locale;

/**
 * Simple Java Utils
 *
 * @author Robert Alexandersson
 * @version 1.0
 * @since 1.0
 */
public class Formatter<T> {

    protected Locale locale = LocaleProvider.getProfile().getLocale();

    protected Class valueType;


    public static <T> Formatter<T> getFormatter(Class<T> aType) {
        return getFormatter(aType, null);
    }

    public static <T> Formatter<T> getFormatter(Class<T> aType, DateFormatter.DateConvert dateConvert) {
        if (String.class.isAssignableFrom(aType)) {
            return new Formatter<T>();
        } else if (CurrencyData.class.isAssignableFrom(aType)) {
            return new CurrencyFormatter();
        } else if (MeasureData.class.isAssignableFrom(aType)) {
            return new MeasureFormatter();
        } else if (PercentData.class.isAssignableFrom(aType)) {
            return new PercentageFormatter();
        } else if (Date.class.isAssignableFrom(aType)) {
            return dateConvert != null ? new DateFormatter(dateConvert) : new DateFormatter();
        } else if (Integer.class.isAssignableFrom(aType)) {
            return new IntegerFormatter();
        } else if (int.class.isAssignableFrom(aType)) {
            return new IntegerFormatter();
        } else if (Long.class.isAssignableFrom(aType)) {
            return new LongFormatter();
        } else if (long.class.isAssignableFrom(aType)) {
            return new LongFormatter();
        } else if (Boolean.class.isAssignableFrom(aType)) {
            return new BooleanFormatter();
        } else if (Boolean.TYPE.isAssignableFrom(aType)) {
            return new BooleanFormatter();
        }

        return null;
    }


    /**
     * Returns <code>true</code> if the provided class is an array getType,
     * implements one of the Formatter classes currently supported.
     *
     * @see #getFormatter(Class)
     */
    public static boolean isSupportedType(Class type) {
        return getFormatter(type) != null;
    }

    public String convertToString(T value) {
        return value.toString();
    }

    public T convertToObject(String string) {
        return string == null ? null : (T) string.trim();
    }

    protected Number parseAndErrorhandleNumber(String target, NumberFormat formatter) {
        ParsePosition parsePosition = new ParsePosition(0);
        Number parsedNumber = formatter.parse(target.trim(), parsePosition);
        checkParsePositionForErrors(target, parsePosition);
        return parsedNumber;
    }

    protected Date parseAndErrorhandleDate(String target, DateFormat formatter) {
        ParsePosition parsePosition = new ParsePosition(0);
        Date parsedDate = formatter.parse(target.trim(), parsePosition);
        checkParsePositionForErrors(target, parsePosition);
        return parsedDate;
    }

    private void checkParsePositionForErrors(String target, ParsePosition parsePosition) {
        if (parsePosition.getIndex() != target.length()) {
            throw new FormatException("Parsing stopped for char {0} at position {1}"
                    , target.charAt(parsePosition.getIndex()), parsePosition.getIndex()
            );
        }
        if (parsePosition.getErrorIndex() != -1) {
            throw new FormatException("Parsing failed on char {0} at position {1}"
                    , target.charAt(parsePosition.getErrorIndex()), parsePosition.getErrorIndex()
            );
        }
    }


}
