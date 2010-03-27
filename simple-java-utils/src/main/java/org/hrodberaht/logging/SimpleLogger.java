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

package org.hrodberaht.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;

/**
 * Simple Java Utils
 *
 * @author Robert Alexandersson
 * @version 1.0
 * @since 1.0
 */
public class SimpleLogger {

    private Logger logger = null;

    public SimpleLogger(Class clazz) {
        logger = LoggerFactory.getLogger(clazz);
    }

    public static SimpleLogger getInstance(Class clazz) {
        return new SimpleLogger(clazz);
    }

    public void debug(String message) {
        if (logger.isDebugEnabled()) {
            logger.debug(message);
        }
    }

    public void debug(String message, Object... args) {
        if (logger.isDebugEnabled()) {
            logger.debug(MessageFormat.format(message,args));
        }
    }

    public boolean isDebugEnabled() {
        return logger.isDebugEnabled();
    }

    public void info(String message) {
        if (logger.isInfoEnabled()) {
            logger.info(message);
        }
    }

    public void info(String message, Object... args) {
        if (logger.isInfoEnabled()) {
            logger.info(MessageFormat.format(message,args));
        }
    }

    public void error(Throwable e) {
        if (logger.isErrorEnabled()) {
            logger.error(null, e);
        }
    }

    public void error(String message, Throwable e) {
        if (logger.isErrorEnabled()) {
            logger.error(message, e);
        }
    }

    public void error(String message, Throwable e, Object... args) {
        if (logger.isErrorEnabled()) {
            logger.error(MessageFormat.format(message,args), e);
        }
    }

}
