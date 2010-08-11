package org.hrodberaht.inject.register.internal;

import java.lang.annotation.Annotation;

/**
 * Simple Java Utils
 *
 * @author Robert Alexandersson
 *         2010-jun-17 20:22:48
 * @version 1.0
 * @since 1.0
 */
public interface Registration {
    Registration annotated(Class<? extends Annotation> annotation);
    Registration named(String named);
    void with(Class theService);
    void withInstance(Object aTire);
}
