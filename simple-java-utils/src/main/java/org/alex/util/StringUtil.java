package org.alex.util;

/**
 * Simple Java Utils
 *
 * @author Robert Alexandersson
 * @version 1.0
 * @since 1.0
 */
public class StringUtil {

    public static boolean isBlank(String value) {
        return value == null || "".equals(value);  //To change body of created methods use File | Settings | File Templates.
    }

}