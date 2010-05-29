package org.hrodberaht.inject.internal;

import org.hrodberaht.inject.SimpleInjection;

/**
 * Simple Java Utils
 *
 * @author Robert Alexandersson
 *         2010-maj-29 12:48:43
 * @version 1.0
 * @since 1.0
 */
public interface InjectionContainer {

    <T> T getService(Class<T> service, SimpleInjection.Scope forcedScope, String qualifier);
    <T> T getService(Class<T> service, SimpleInjection.Scope forcedScope);

    void register(Class anInterface, Class<Object> service, SimpleInjection.Scope scope, SimpleInjection.RegisterType type);
    void register(String namedInstance, Class<Object> service, SimpleInjection.Scope scope, SimpleInjection.RegisterType type);

    


}
