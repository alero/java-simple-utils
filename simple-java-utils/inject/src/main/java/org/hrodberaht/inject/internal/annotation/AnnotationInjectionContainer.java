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

package org.hrodberaht.inject.internal.annotation;

import org.hrodberaht.inject.InjectRuntimeException;
import org.hrodberaht.inject.SimpleInjection;
import org.hrodberaht.inject.internal.InjectionContainer;
import org.hrodberaht.inject.internal.InjectionContainerBase;
import org.hrodberaht.inject.internal.RegistrationInjectionContainer;
import org.hrodberaht.inject.internal.ServiceRegister;
import org.hrodberaht.inject.register.internal.RegistrationInstanceSimple;
import org.hrodberaht.inject.register.RegistrationModule;
import org.hrodberaht.inject.register.RegistrationModuleAnnotation;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

/**
 * Simple Java Utils - Container
 *
 * @author Robert Alexandersson
 *         2010-maj-29 12:38:22
 * @version 1.0
 * @since 1.0
 */
public class AnnotationInjectionContainer extends InjectionContainerBase implements InjectionContainer, RegistrationInjectionContainer {

    private List<InjectionMetaData> injectionMetaDataCache = new ArrayList<InjectionMetaData>();
    private SimpleInjection container;

    public AnnotationInjectionContainer(SimpleInjection container) {
        this.container = container;
    }

    public <T> T getService(Class<T> service, SimpleInjection.Scope forcedScope, String qualifier) {
        assert (qualifier != null);
        InjectionKey key = getNamedKey(qualifier, service);
        return getQualifiedService(service, forcedScope, key);
    }


    public <T> T getService(Class<T> service, SimpleInjection.Scope forcedScope, Class<? extends Annotation> qualifier) {
        assert (qualifier != null);
        InjectionKey key = getAnnotatedKey(qualifier, service);
        return getQualifiedService(service, forcedScope, key);
    }


    @SuppressWarnings(value = "unchecked")
    public <T> T getService(Class<T> service, SimpleInjection.Scope forcedScope) {
        ServiceRegister serviceRegister = registeredServices.get(service);
        if (serviceRegister == null && !service.isInterface()) {
            serviceRegister = register((Class<Object>) service);
        } else if (serviceRegister == null && service.isInterface()) {
            serviceRegister = registerForInterface((Class<Object>) service);
        }
        return instantiateService(service, forcedScope, serviceRegister);
    }

    public Object createInstance(ServiceRegister serviceRegister) {
        AnnotationInjection annotationInjection = new AnnotationInjection(injectionMetaDataCache, container, this);
        return annotationInjection.createInstance(serviceRegister.getService());
    }

    public synchronized void register(Class serviceDefinition, Class service, SimpleInjection.Scope scope, SimpleInjection.RegisterType type) {
        if (registeredServices.containsKey(serviceDefinition)) {
            throw new InjectRuntimeException("Can not overwrite an existing service");
        }
        InjectionMetaData injectionMetaData = createInjectionMetaData(service, null);
        registeredServices.put(serviceDefinition,
                new ServiceRegister(service, null, getAnnotationScope(injectionMetaData), normalizeType(type))
        );
    }

    public synchronized void register(InjectionKey key, Class service, SimpleInjection.Scope scope, SimpleInjection.RegisterType type) {
        register(key, service);
    }

    public void register(RegistrationModule... modules) {
        for (RegistrationModule module : modules) {
            RegistrationModuleAnnotation aModule = (RegistrationModuleAnnotation) module;
            for (RegistrationInstanceSimple instance : aModule.getRegistrations()) {
                InjectionKey key = instance.getInjectionKey();
                createAnStoreRegistration(instance, key);
            }
        }
    }


    public Class findService(InjectionKey key) {
        if (key.getQualifier() == null) {
            super.registeredServices.get(key.getServiceDefinition()).getService();
        }
        return super.registeredNamedServices.get(key).getService();
    }

    private <T> T getQualifiedService(Class<T> service, SimpleInjection.Scope forcedScope, InjectionKey key) {
        if (!registeredNamedServices.containsKey(key) && service.getClass().isInterface()) {
            throw new InjectRuntimeException("Service {0} not registered in SimpleInjection and is an interface", service);
        }

        ServiceRegister serviceRegister = registeredNamedServices.get(key);
        if (serviceRegister == null && !service.isInterface()) {
            serviceRegister = register(key, service);
        }
        return instantiateService(service, forcedScope, serviceRegister);
    }

    private ServiceRegister registerForInterface(Class<Object> service) {
        AnnotationInjection annotationInjection = new AnnotationInjection(injectionMetaDataCache, container, this);
        InjectionMetaData injectionMetaData = annotationInjection.findInjectionData(service, null, false);
        register(service, injectionMetaData.getServiceClass(), null, null);
        return registeredServices.get(service);
    }

    private ServiceRegister register(InjectionKey key, Class service) {
        RegistrationInstanceSimple instance = new RegistrationInstanceSimple(service);
        if (key.getAnnotation() != null) {
            instance.annotated(key.getAnnotation());
        } else if (key.getName() != null) {
            instance.namned(key.getName());
        }
        return createAnStoreRegistration(instance, key);
    }

    private ServiceRegister register(Class<Object> service) {
        register(service, service, SimpleInjection.Scope.NEW, null);
        return registeredServices.get(service);
    }


    private ServiceRegister createAnStoreRegistration(RegistrationInstanceSimple instance, InjectionKey key) {
        InjectionMetaData injectionMetaData = createInjectionMetaData(instance, key);
        ServiceRegister register = createServiceRegister(instance, injectionMetaData);
        if (key.getQualifier() == null) {
            registeredServices.put(key.getServiceDefinition(), register);
        } else {
            registeredNamedServices.put(key, register);

        }
        return register;
    }

    private ServiceRegister createServiceRegister(RegistrationInstanceSimple instance, InjectionMetaData injectionMetaData) {
        return new ServiceRegister(instance.getService(), null, getAnnotationScope(injectionMetaData), normalizeType(SimpleInjection.RegisterType.NORMAL));
    }

    private InjectionMetaData createInjectionMetaData(RegistrationInstanceSimple instance, InjectionKey key) {
        return createInjectionMetaData(instance.getService(), key);
    }

    private InjectionMetaData createInjectionMetaData(Class service, InjectionKey key) {
        AnnotationInjection annotationInjection = new AnnotationInjection(injectionMetaDataCache, container, this);
        return annotationInjection.createInjectionMetaData(service, key, InjectionUtils.isProvider(service));
    }


    private SimpleInjection.Scope getAnnotationScope(InjectionMetaData injectionMetaData) {

        return injectionMetaData.getScope();
    }


}