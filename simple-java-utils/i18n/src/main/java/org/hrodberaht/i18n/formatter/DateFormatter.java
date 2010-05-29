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

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;

/**
 * Simple Java Utils
 *
 * @author Robert Alexandersson
 * @version 1.0
 * @since 1.0
 */
public class DateFormatter extends Formatter {

    private static HashMap<Locale, String> longPatternCache = new HashMap<Locale, String>();
    public static enum DateConvert{ Date, DateTime }


    private DateConvert convertDateStyle = DateConvert.Date;
    private int dateStyle = DateFormat.SHORT;
    private int timeStyle = DateFormat.SHORT;

    public DateFormatter(DateConvert dateConvert) {
        convertDateStyle = dateConvert;    
    }

    public DateFormatter() {

    }

    public Object convertToObject(String target)
    {
        DateFormat formatter = getFormat(target);
        return parseAndErrorhandleDate(target, formatter);
    }

    private DateFormat getFormat(String target) {
        if(convertDateStyle == DateConvert.DateTime){
            return DateFormat.getDateTimeInstance(dateStyle, timeStyle, locale);
        }
        String longPattern = getCachedLongPattern();
        if(target != null && longPattern.length() <= target.length() ){
            return DateFormat.getDateTimeInstance(dateStyle, timeStyle, locale);
        }
        return DateFormat.getDateInstance(dateStyle, locale);
    }

    private String getCachedLongPattern() {
        if(longPatternCache.containsKey(locale)){
            return longPatternCache.get(locale);
        }
        SimpleDateFormat dateFormat = (SimpleDateFormat)
                DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, locale);
        String pattern = dateFormat.toLocalizedPattern();
        longPatternCache.put(locale, pattern);
        return pattern;
    }


    /**
     * Returns a string representation of its argument, formatted as a date
     * with the "MM/dd/yyyy" format.
     *
     * @return a formatted String
     */
    public String convertToString(Object value) {
        if (value == null)
            return null;

        StringBuffer buf = new StringBuffer();
        DateFormat formatter = getFormat(null);
        buf = formatter.format(value, buf, new FieldPosition(0));
        return buf.toString();
    }
}
