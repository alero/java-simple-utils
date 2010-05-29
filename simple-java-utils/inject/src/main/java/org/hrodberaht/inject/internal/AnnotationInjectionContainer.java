package org.hrodberaht.inject.internal;

import org.hrodberaht.inject.SPIRuntimeException;
import org.hrodberaht.inject.SimpleInjection;
import org.hrodberaht.inject.internal.annotation.AnnotationInjection;

import java.util.HashMap;

/**
 * Simple Java Utils
 *
 * @author Robert Alexandersson
 *         2010-maj-29 12:38:22
 * @version 1.0
 * @since 1.0
 */
public class AnnotationInjectionContainer extends InjectionContainerBase implements InjectionContainer {


    private HashMap<Object, ServiceRegister> registeredServices = new HashMap<Object, ServiceRegister>();
    private HashMap<String, ServiceRegister> registeredNamedServices = new HashMap<String, ServiceRegister>();

    private AnnotationInjection annotationInjection = new AnnotationInjection();

    public <T> T getService(Class<T> service, SimpleInjection.Scope forcedScope, String qualifier) {

        if (!registeredNamedServices.containsKey(service) && service.getClass().isInterface()) {
            throw new SPIRuntimeException("Service {0} not registered in SimpleInjection and is an interface", service);
        }
        ServiceRegister serviceRegister = registeredNamedServices.get(service);
        return instantiateService(service, forcedScope, serviceRegister);
    }

    @SuppressWarnings(value = "unchecked")
    public <T> T getService(Class<T> service, SimpleInjection.Scope forcedScope) {
        ServiceRegister serviceRegister = registeredServices.get(service);
        return instantiateService(service, forcedScope, serviceRegister);
    }


    public Object createInstance(ServiceRegister serviceRegister) {


            return annotationInjection.createInstance(serviceRegister.getService());


    }

    @Override
    public void register(Class anInterface, Class<Object> service, SimpleInjection.Scope scope, SimpleInjection.RegisterType type) {
        if (registeredServices.containsKey(anInterface)) {
            throw new SPIRuntimeException("Can not overwrite an existing service");
        }
        registeredServices.put(anInterface,
                new ServiceRegister(service, createInstance(new ServiceRegister(service)), scope, normalizeType(type))
        );
    }

    @Override
    public void register(String namedInstance, Class<Object> service, SimpleInjection.Scope scope, SimpleInjection.RegisterType type) {
        if (registeredNamedServices.containsKey(namedInstance)) {
            throw new SPIRuntimeException("Can not overwrite an existing service");
        }
        registeredNamedServices.put(namedInstance,
                new ServiceRegister(service, createInstance(new ServiceRegister(service)), scope, normalizeType(type))
        );
    }


}