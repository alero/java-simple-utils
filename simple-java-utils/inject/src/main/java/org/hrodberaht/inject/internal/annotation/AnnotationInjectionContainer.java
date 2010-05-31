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
import org.hrodberaht.inject.internal.ServiceRegister;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

    private List<InjectionMetaData> injectionMetaDataCache = new ArrayList<InjectionMetaData>();


    public <T> T getService(Class<T> service, SimpleInjection.Scope forcedScope, String qualifier) {
        assert (qualifier != null);
        if (!registeredNamedServices.containsKey(service) && service.getClass().isInterface()) {
            throw new InjectRuntimeException("Service {0} not registered in SimpleInjection and is an interface", service);
        }
        ServiceRegister serviceRegister = registeredNamedServices.get(qualifier);
        if (serviceRegister == null && !service.isInterface()) {
            serviceRegister = register(qualifier, (Class<Object>) service);
        }
        return instantiateService(service, forcedScope, serviceRegister);
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

    private ServiceRegister registerForInterface(Class<Object> service) {
        AnnotationInjection annotationInjection = new AnnotationInjection(injectionMetaDataCache);
        InjectionMetaData injectionMetaData = annotationInjection.findInjectionData(service, null, false);
        register(service, injectionMetaData.getServiceClass(), null, null);        
        return registeredServices.get(service);
    }

    ServiceRegister register(Class<Object> service) {
        register(service, service, SimpleInjection.Scope.NEW, null);
        return registeredServices.get(service);
    }

    ServiceRegister register(String namedInstance, Class<Object> service) {
        register(namedInstance, service, SimpleInjection.Scope.NEW, null);
        return registeredServices.get(service);
    }

    public Object createInstance(ServiceRegister serviceRegister) {
        AnnotationInjection annotationInjection = new AnnotationInjection(injectionMetaDataCache);
        return annotationInjection.createInstance(serviceRegister.getService());
    }

    @Override
    public synchronized void register(Class anInterface, Class<Object> service, SimpleInjection.Scope scope, SimpleInjection.RegisterType type) {
        if (registeredServices.containsKey(anInterface)) {
            throw new InjectRuntimeException("Can not overwrite an existing service");
        }
        InjectionMetaData injectionMetaData = createInjectionMetaData(service, null);
        registeredServices.put(anInterface,
                new ServiceRegister(service, null, getAnnotationScope(injectionMetaData), normalizeType(type))
        );

    }

    @Override
    public synchronized void register(String namedInstance, Class<Object> service, SimpleInjection.Scope scope, SimpleInjection.RegisterType type) {
        if (registeredNamedServices.containsKey(namedInstance)) {
            throw new InjectRuntimeException("Can not overwrite an existing service");
        }
        InjectionMetaData injectionMetaData = createInjectionMetaData(service, namedInstance);
        registeredNamedServices.put(namedInstance,
                new ServiceRegister(service, null, getAnnotationScope(injectionMetaData), normalizeType(type))
        );

    }

    private SimpleInjection.Scope getAnnotationScope(InjectionMetaData injectionMetaData) {
        return injectionMetaData.isSingleton() ? SimpleInjection.Scope.SINGLETON : SimpleInjection.Scope.NEW;
    }

    private InjectionMetaData createInjectionMetaData(Class<Object> service, String qualifier) {
        AnnotationInjection annotationInjection = new AnnotationInjection(injectionMetaDataCache);
        InjectionMetaData injectionMetaData =
                annotationInjection.createInjectionMetaData(service, qualifier, InjectionUtils.isProvider(service));
        injectionMetaDataCache.add(injectionMetaData);
        return injectionMetaData;
    }


}