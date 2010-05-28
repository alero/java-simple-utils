package org.hrodberaht.inject;

import java.text.MessageFormat;

/**
 * Simple Java Utils
 *
 * @author Robert Alexandersson
 *         2010-maj-28 19:00:29
 * @version 1.0
 * @since 1.0
 */
public class SPIRuntimeException extends RuntimeException{
    private Object[] args = null;


    public SPIRuntimeException(String message) {
        super(message);
    }

    public SPIRuntimeException(String message, Throwable e) {
        super(message, e);
    }

    public SPIRuntimeException(Throwable e) {
        super(e);
    }

    public SPIRuntimeException(String message, Object... args) {
        super(message);
        this.args = args;
    }

    public SPIRuntimeException(String message, Throwable e, Object... args) {
        super(message, e);
        this.args = args;
    }

    public SPIRuntimeException(Throwable e, Object... args) {
        super(e);
        this.args = args;
    }

    @Override
    public String toString() {
        if(args != null){
            return SPIRuntimeException.class.getName()
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
