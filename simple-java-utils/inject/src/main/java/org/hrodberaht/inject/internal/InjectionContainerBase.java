package org.hrodberaht.inject.internal;

import org.hrodberaht.inject.SimpleInjection;

import java.util.HashMap;

/**
 * Simple Java Utils
 *
 * @author Robert Alexandersson
 *         2010-maj-29 13:07:33
 * @version 1.0
 * @since 1.0
 */
public abstract class InjectionContainerBase {


    protected HashMap<Object, ServiceRegister> registeredServices = new HashMap<Object, ServiceRegister>();
    protected HashMap<String, ServiceRegister> registeredNamedServices = new HashMap<String, ServiceRegister>();


    @SuppressWarnings(value = "unchecked")
    protected <T> T instantiateService(Class<T> service, SimpleInjection.Scope forcedScope, ServiceRegister serviceRegister) {
        if(serviceRegister == null){
            return (T) createInstance(new ServiceRegister((Class<Object>) service));
        }
        if (forcedScope == null && serviceRegister.getScope() == SimpleInjection.Scope.NEW) {
            return (T) createInstance(serviceRegister);
        } else if (SimpleInjection.Scope.NEW == forcedScope) {
            return (T) createInstance(serviceRegister);
        }

        return (T) serviceRegister.getSingleton();
    }

    protected static SimpleInjection.RegisterType normalizeType(SimpleInjection.RegisterType type) {
        if(type == SimpleInjection.RegisterType.OVERRIDE_NORMAL){
            return SimpleInjection.RegisterType.NORMAL;
        }
        return type;
    }

    abstract Object createInstance(ServiceRegister serviceRegister);

}
