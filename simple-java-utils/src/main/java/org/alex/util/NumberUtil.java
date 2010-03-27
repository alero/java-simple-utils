package org.alex.util;

/**
 * Simple Java Utils
 *
 * @author Robert Alexandersson
 * @version 1.0
 * @since 1.0
 */
public class NumberUtil {


    public static Long parseLong(String value){
        if(value == null){
            return null;            
        }
        return Long.parseLong(value);
    }

    public static Integer parseInt(String value) {
        if(value == null){
            return null;
        }
        return Integer.parseInt(value);
    }
}
