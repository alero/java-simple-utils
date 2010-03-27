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


/**
 * Simple Java Utils
 *
 * @author Robert Alexandersson
 * @version 1.0
 * @since 1.0
 */
public class IntegerFormatter extends Formatter
{
    public final static String INTEGER_ERROR_KEY = "error.integer";
    static final String PARSE_MSG = "Unable to parse an integer value from ";
//    static final String FORMAT_MSG = "Unable to format an integer value from ";

    public String getErrorKey() { return INTEGER_ERROR_KEY; }
    
    /**
     * Returns an object representation of its argument.
     */
    protected Object convertToObject(String target)
    {
        try {
            return new Integer(target);
        }
        catch (NumberFormatException e) {
            throw new FormatException(PARSE_MSG + target, e);
        }
    }

    /**
     * Returns a formatted version of its argument.
     */
    public String format(Object obj) {
        return (obj == null ? null : obj.toString());
    }
}
