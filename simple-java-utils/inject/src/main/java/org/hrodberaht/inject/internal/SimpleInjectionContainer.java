/*
 * ~ Copyright (c) 2010.
 *   ~ Licensed under the Apache License, Version 2.0 (the "License");
 *   ~ you may not use this file except in compliance with the License.
 *   ~ You may obtain a copy of the License at
 *   ~
 *   ~        http://www.apache.org/licenses/LICENSE-2.0
 *   ~
 *   ~ Unless required by applicable law or agreed to in writing, software
 *   ~ distributed under the License is distributed on an "AS IS" BASIS,
 *   ~ WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   ~ See the License for the specific language governing permissions and limitations under the License.
 */

package org.hrodberaht.inject.internal;

import org.hrodberaht.inject.InjectRuntimeException;
import org.hrodberaht.inject.SimpleInjection;
import org.hrodberaht.inject.internal.annotation.InjectionKey;
import org.hrodberaht.inject.register.RegistrationModule;

import java.lang.annotation.Annotation;

/**
 * Simple Java Utils - Container
 *
 * @author Robert Alexandersson
 *         2010-maj-29 12:38:22
 * @version 1.0
 * @since 1.0
 */
public class SimpleInjectionContainer extends InjectionContainerBase implements InjectionContainer, RegistrationInjectionContainer {


    public <T> T getService(Class<T> service, SimpleInjection.Scope forcedScope, String qualifier) {
        InjectionKey key = getNamedKey(qualifier, service);
        return getQualifiedService(service, forcedScope, key);
    }

    public <T> T getService(Class<T> service, SimpleInjection.Scope forcedScope, Class<? extends Annotation> qualifier) {
        InjectionKey key = getAnnotatedKey(qualifier, service);
        return getQualifiedService(service, forcedScope, key);
    }
    
    public <T> T getService(Class<T> service, SimpleInjection.Scope forcedScope) {
        ServiceRegister serviceRegister = findServiceImplementation(service);
        return instantiateService(service, forcedScope, serviceRegister);
    }


    public void register(Class anInterface, Class service, SimpleInjection.Scope scope, SimpleInjection.RegisterType type) {
        if (registeredServices.containsKey(anInterface)) {
            reRegisterSupport(anInterface, type);
        }
        registeredServices.put(anInterface,
                new ServiceRegister(service, createInstance(new ServiceRegister(service)), scope, normalizeType(type))
        );
    }


    public void register(InjectionKey key, Class service, SimpleInjection.Scope scope, SimpleInjection.RegisterType type) {

        if (registeredNamedServices.containsKey(key)) {
            reRegisterSupport(key, type);
        }
        registeredNamedServices.put(key,
                new ServiceRegister(service, createInstance(new ServiceRegister(service)), scope, normalizeType(type))
        );
    }

    
    public void register(RegistrationModule... modules) {

    }

    @SuppressWarnings(value = "unchecked")
    private <T> T getQualifiedService(Class<T> service, SimpleInjection.Scope forcedScope, InjectionKey key) {
        if (!registeredNamedServices.containsKey(key)) {
            throw new InjectRuntimeException("Service {0} not registered in SimpleInjection", service);
        }
        ServiceRegister serviceRegister = registeredNamedServices.get(key);
        if (serviceRegister == null && !service.getClass().isInterface()) {
            serviceRegister = register(key, (Class<Object>) service);
        }
        return instantiateService(service, forcedScope, serviceRegister);
    }

    private void reRegisterSupport(Class anInterface, SimpleInjection.RegisterType type) {
        ServiceRegister serviceRegister = registeredServices.get(anInterface);
        if (serviceRegister.getRegisterType() == SimpleInjection.RegisterType.WEAK) {
            registeredServices.remove(anInterface);
            return;
        }

        if (serviceRegister.getRegisterType() == SimpleInjection.RegisterType.NORMAL) {
            if (type == SimpleInjection.RegisterType.OVERRIDE_NORMAL) {
                registeredServices.remove(anInterface);
                return;
            }
            throw new InjectRuntimeException(
                    "Service {0} is already registered, to override register please use the override method"
                    , anInterface);
        }
        if (serviceRegister.getRegisterType() == SimpleInjection.RegisterType.FINAL) {
            throw new InjectRuntimeException(
                    "A FINAL Service for {0} is already registered, can not reRegister", anInterface);
        }

    }

    private void reRegisterSupport(InjectionKey key, SimpleInjection.RegisterType type) {
        ServiceRegister serviceRegister = registeredNamedServices.get(key);
        if (serviceRegister.getRegisterType() == SimpleInjection.RegisterType.WEAK) {
            registeredNamedServices.remove(key);
            return;
        }

        if (serviceRegister.getRegisterType() == SimpleInjection.RegisterType.NORMAL) {
            if (type == SimpleInjection.RegisterType.OVERRIDE_NORMAL) {
                registeredNamedServices.remove(key);
                return;
            }
            throw new InjectRuntimeException(
                    "Service {0} is already registered, to override register please use the override method"
                    , key.getServiceDefinition());
        }
        if (serviceRegister.getRegisterType() == SimpleInjection.RegisterType.FINAL) {
            throw new InjectRuntimeException(
                    "A FINAL Service for {0} is already registered, can not reRegister", key.getQualifier());
        }

    }


    private <T> ServiceRegister findServiceImplementation(Class<T> service) {
        if (!registeredServices.containsKey(service)) {
            if (service.isInterface()) { // TODO support this for classes as well? = inheritance support
                for (ServiceRegister serviceRegister : registeredServices.values()) {
                    if (service.isAssignableFrom(serviceRegister.getService())) {
                        // TODO first make sure there is only one usable service
                        return serviceRegister;
                    }
                }
            }
            throw new InjectRuntimeException("Service {0} not registered in SimpleInjection", service);
        }
        ServiceRegister serviceRegister = registeredServices.get(service);
        return serviceRegister;
    }



    ServiceRegister register(InjectionKey key, Class<Object> service) {
        register(key.getServiceDefinition(), service, SimpleInjection.Scope.NEW, SimpleInjection.RegisterType.NORMAL);
        return registeredServices.get(service);
    }

    public Object createInstance(ServiceRegister serviceRegister) {
        try {
            return serviceRegister.getService().newInstance(); // uses empty constructor
        } catch (InstantiationException e) {
            throw new InjectRuntimeException("Could not create an instance of {0}", e, serviceRegister.getService());
        } catch (IllegalAccessException e) {
            throw new InjectRuntimeException("Could not create an instance of {0}", e, serviceRegister.getService());
        }
    }


}
