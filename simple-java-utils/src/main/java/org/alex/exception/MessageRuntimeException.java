package org.alex.exception;

import java.text.MessageFormat;

/**
 * Created by IntelliJ IDEA.
 * User: Robert
 * Date: 2010-mar-27
 * Time: 01:38:52
 * To change this template use File | Settings | File Templates.
 */
public class MessageRuntimeException extends RuntimeException {

    private Object[] args = null;

    public MessageRuntimeException(String message) {
        super(message);
    }

    public MessageRuntimeException(String message, Object ... args) {
        super(message);
        this.args = args;
    }

    @Override
    public String toString() {
        if(args != null){
            return MessageFormat.format(getMessage(), args);            
        }
        return super.toString();
    }
}
