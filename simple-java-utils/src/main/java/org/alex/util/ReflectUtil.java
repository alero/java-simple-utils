package org.alex.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Simple Java Utils
 *
 * @author Robert Alexandersson
 * @version 1.0
 * @since 1.0
 */
public class ReflectUtil {
    public static ReflectUtil getInstance() {
        return new ReflectUtil();
    }


    public Object invokeMethod(Object obj, String method) {
        try {
            Method methodToCall = obj.getClass().getMethod(method);
            return methodToCall.invoke(obj);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }        
    }
}
