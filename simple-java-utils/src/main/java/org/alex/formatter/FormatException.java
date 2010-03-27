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

import java.text.MessageFormat;

/**
 * Simple Java Utils
 *
 * @author Robert Alexandersson
 * @version 1.0
 * @since 1.0
 */
public class FormatException extends RuntimeException
{

    private Object[] args = null;

    public FormatException(String message) {
        super(message);
    }

    public FormatException(String message, Object... args) {
        super(message);
        this.args = args;
    }

    public FormatException(String message, Throwable cause, Object... args) {
        super(message, cause);
        this.args = args;
    }

    public FormatException(Throwable cause) {
        super(cause);
    }


    @Override
    public String toString() {
        if(args != null){
            return MessageFormat.format(getMessage(), args);
        }
        return super.toString();
    }
}
