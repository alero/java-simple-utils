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

package org.hrodberaht.i18n.formatter;

import org.hrodberaht.directus.exception.MessageRuntimeException;


/**
 * Simple Java Utils
 *
 * @author Robert Alexandersson
 * @version 1.0
 * @since 1.0
 */
public class IntegerFormatter extends Formatter
{
     /**
     * Returns an object representation of its argument.
     */
    public Object convertToObject(String target)
    {
        try {
            return new Integer(target);
        }
        catch (NumberFormatException e) {
            throw new MessageRuntimeException(e);
        }
    }

    /**
     * Returns a formatted version of its argument.
     */
    public String convertToString(Object obj) {
        return (obj == null ? null : obj.toString());
    }
}
