package org.hrodberaht.i18n.formatter;

import org.hrodberaht.directus.exception.MessageRuntimeException;

/**
 * Simple Java Utils
 *
 * @author Robert Alexandersson
 *         2010-apr-01 17:35:51
 * @version 1.0
 * @since 1.0
 */
public class LongFormatter extends Formatter{
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
