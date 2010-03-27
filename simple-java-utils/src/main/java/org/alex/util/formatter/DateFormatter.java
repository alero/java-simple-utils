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

package org.alex.util.formatter;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.util.Date;

/**
 * Simple Java Utils
 *
 * @author Robert Alexandersson
 * @version 1.0
 * @since 1.0
 */
public class DateFormatter extends Formatter {

    /**
     * Unformats its argument and return a java.util.Date instance
     * initialized with the resulting string.
     *
     * @return a java.util.Date intialized with the provided string
     */
    public Object convertToObject(String target)
    {
        DateFormat formatter = DateFormat.getDateInstance(DateFormat.SHORT, locale);
        // formatter.setLenient(false);
        ParsePosition parsePosition = new ParsePosition(0);
        Date date = formatter.parse(target, parsePosition);
        if (date == null)
            throw new FormatException(target);

        return date;
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
        DateFormat formatter = DateFormat.getDateInstance(DateFormat.SHORT, locale);
        formatter.setLenient(false);
        formatter.format(value, buf, new FieldPosition(0));

        return buf.toString();
    }
}
