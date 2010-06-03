package org.hrodberaht.inject.internal.annotation;

import org.hrodberaht.inject.SimpleInjection;

import java.lang.annotation.Annotation;

/**
 * Simple Java Utils
 *
 * @author Robert Alexandersson
 *         2010-jun-03 17:48:02
 * @version 1.0
 * @since 1.0
 */
public interface InjectionContainerAnnotations {
    void register(Class<? extends Annotation> anAnnotation,
                  Class anInterface, Class service,
                  SimpleInjection.Scope scope, SimpleInjection.RegisterType type);
}
