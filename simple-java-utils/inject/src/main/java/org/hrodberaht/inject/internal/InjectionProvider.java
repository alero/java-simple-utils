package org.hrodberaht.inject.internal;

import org.hrodberaht.inject.SimpleInjection;

import javax.inject.Provider;

/**
 * Simple Java Utils
 *
 * @author Robert Alexandersson
 *         2010-maj-29 00:46:09
 * @version 1.0
 * @since 1.0
 */
public class InjectionProvider implements Provider {

    private Class serviceClass;
    private String qualifierName;
    private AnnotationInjection annotationInjection;

    public InjectionProvider(Class serviceClass, String qualifierName, AnnotationInjection annotationInjection) {
        this.serviceClass = serviceClass;
        this.qualifierName = qualifierName;
        this.annotationInjection = annotationInjection;
    }

    @Override
    public Object get() {
        return SimpleInjection.get(serviceClass, qualifierName);
    }
}
