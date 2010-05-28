package org.hrodberaht.inject;

import javax.inject.Inject;
import java.lang.reflect.Constructor;

/**
 * Simple Java Utils
 *
 * @author Robert Alexandersson
 *         2010-maj-28 20:26:43
 * @version 1.0
 * @since 1.0
 */
public class AnnotationInjection {

    private static final Class<Inject> INJECT_CLASS = Inject.class;

    public static Object createInstance(Class<Object> service) {

        return null;  //To change body of created methods use File | Settings | File Templates.
    }


    private boolean constructorNeedsInjection(final Constructor<?> constructor) {
        return constructor.isAnnotationPresent(INJECT_CLASS);
    }


    public static boolean isProvider(Class<Object> service) {
        // TODO: look at this later
        return false;
    }
}
