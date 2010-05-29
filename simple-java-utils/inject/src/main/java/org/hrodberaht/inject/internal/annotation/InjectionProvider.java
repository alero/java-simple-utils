package org.hrodberaht.inject.internal.annotation;

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

    public InjectionProvider(Class serviceClass, String qualifierName) {
        this.serviceClass = serviceClass;
        this.qualifierName = qualifierName;
    }

    @Override
    public Object get() {
        return SimpleInjection.get(serviceClass, qualifierName);
    }
}
