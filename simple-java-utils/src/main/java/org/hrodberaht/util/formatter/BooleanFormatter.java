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

package org.hrodberaht.util.formatter;

import java.util.Arrays;
import java.util.List;

/**
 * Simple Java Utils
 *
 * @author Robert Alexandersson
 * @version 1.0
 * @since 1.0
 */
public class BooleanFormatter extends Formatter
{

    static final String CONVERT_MSG = "Unable to create Boolean object from ";
    static final List TRUE_VALUES = Arrays.asList(new String[]  {
            "yes", "true", "on", "1", "enabled" });
    static final List FALSE_VALUES = Arrays.asList(new String[] {
            "no", "false", "off", "0", "disabled" });
    
    public Object convertToObject(String target)
    {
        if (Formatter.isEmptyValue(target))
            return null;
        
        String stringValue = target.trim().toLowerCase();
        
        if (TRUE_VALUES.contains(stringValue))  return Boolean.TRUE;
        if (FALSE_VALUES.contains(stringValue)) return Boolean.FALSE;

        throw new FormatException(CONVERT_MSG + stringValue);
    }
    
    public String convertToString(Object target)
    {
        if (target == null)
            return null;
        
        boolean isTrue = ((Boolean) target).booleanValue();
        
        return isTrue ? "Yes" : "No";
    }
}
