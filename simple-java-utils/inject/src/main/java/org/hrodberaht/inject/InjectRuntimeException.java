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
public class InjectRuntimeException extends RuntimeException{
    private Object[] args = null;


    public InjectRuntimeException(String message) {
        super(message);
    }

    public InjectRuntimeException(String message, Throwable e) {
        super(message, e);
    }

    public InjectRuntimeException(Throwable e) {
        super(e);
    }

    public InjectRuntimeException(String message, Object... args) {
        super(message);
        this.args = args;
    }

    public InjectRuntimeException(String message, Throwable e, Object... args) {
        super(message, e);
        this.args = args;
    }

    public InjectRuntimeException(Throwable e, Object... args) {
        super(e);
        this.args = args;
    }

    @Override
    public String toString() {
        if(args != null){
            return InjectRuntimeException.class.getName()
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
