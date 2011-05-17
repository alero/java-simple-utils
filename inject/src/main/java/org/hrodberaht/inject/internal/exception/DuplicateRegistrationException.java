/*
 * ~ Copyright (c) 2010.
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
 */

package org.hrodberaht.inject.internal.exception;

import java.text.MessageFormat;

/**
 * Simple Java Utils - Container
 *
 * @author Robert Alexandersson
 *         2010-maj-28 19:00:29
 * @version 1.0
 * @since 1.0
 */
public class DuplicateRegistrationException extends RuntimeException{
    private Object[] args = null;


    public DuplicateRegistrationException(String message) {
        super(message);
    }

    public DuplicateRegistrationException(String message, Throwable e) {
        super(message, e);
    }

    public DuplicateRegistrationException(Throwable e) {
        super(e);
    }

    public DuplicateRegistrationException(String message, Object... args) {
        super(message);
        this.args = args;
    }

    public DuplicateRegistrationException(String message, Throwable e, Object... args) {
        super(message, e);
        this.args = args;
    }

    public DuplicateRegistrationException(Throwable e, Object... args) {
        super(e);
        this.args = args;
    }

    @Override
    public String toString() {
        if(args != null){
            return DuplicateRegistrationException.class.getName()
                    +": "+ MessageFormat.format(super.getMessage(), args);
        }
        return super.toString();
    }

    @Override
    public String getMessage() {
        if(args != null){
            return MessageFormat.format(super.getMessage(), args);
        }
        return super.getMessage();
    }
}
