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
import org.hrodberaht.inject.creators.annotation.RegistrationInstance;
import org.hrodberaht.inject.creators.annotation.RegistrationModule;
import org.hrodberaht.inject.internal.InjectionContainer;
import org.hrodberaht.inject.internal.InjectionContainerBase;
import org.hrodberaht.inject.internal.ServiceRegister;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
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

    private List<InjectionMetaData> injectionMetaDataCache = new ArrayList<InjectionMetaData>();

    public <T> T getService(Class<T> service, SimpleInjection.Scope forcedScope, String qualifier) {
        assert (qualifier != null);
        InjectionKey key = getNamedKey(qualifier, service);
        return getQualifiedService(service, forcedScope, key);
    }

 

    @Override
    public <T> T getService(Class<T> service, SimpleInjection.Scope forcedScope, Class<? extends Annotation> qualifier) {
        assert (qualifier != null);
        InjectionKey key = getAnnotatedKey(qualifier, service);
        return getQualifiedService(service, forcedScope, key);
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
        AnnotationInjection annotationInjection = new AnnotationInjection(injectionMetaDataCache);
        return annotationInjection.createInstance(serviceRegister.getService());
    }

    @Override
    public synchronized void register(Class anInterface, Class service, SimpleInjection.Scope scope, SimpleInjection.RegisterType type) {
        if (registeredServices.containsKey(anInterface)) {
            throw new InjectRuntimeException("Can not overwrite an existing service");
        }
        InjectionMetaData injectionMetaData = createInjectionMetaData(service, null);
        registeredServices.put(anInterface,
                new ServiceRegister(service, null, getAnnotationScope(injectionMetaData), normalizeType(type))
        );

    }

    @Override
    public synchronized void register(InjectionKey key, Class service, SimpleInjection.Scope scope, SimpleInjection.RegisterType type) {
        register(key, service);       
    }

    @Override
    public void register(RegistrationModule... modules) {
        for(RegistrationModule module:modules){
            for(RegistrationInstance instance: module.getRegistrations()){
                InjectionKey key = instance.getInjectionKey();
                createAnStoreRegistration(instance, key);
            }
        }
    }

    private ServiceRegister registerForInterface(Class<Object> service) {
        AnnotationInjection annotationInjection = new AnnotationInjection(injectionMetaDataCache);
        InjectionMetaData injectionMetaData = annotationInjection.findInjectionData(service, null, false);
        register(service, injectionMetaData.getServiceClass(), null, null);        
        return registeredServices.get(service);
    }

    private ServiceRegister register(InjectionKey key, Class service) {
        RegistrationInstance instance = new RegistrationInstance(service);
        if(key.getAnnotation() != null){
            instance.annotated(key.getAnnotation());
        }else if(key.getName() != null){
            instance.namned(key.getName());
        }
        return createAnStoreRegistration(instance, key);
    }

    private ServiceRegister register(Class<Object> service) {
        register(service, service, SimpleInjection.Scope.NEW, null);
        return registeredServices.get(service);
    }



    private ServiceRegister createAnStoreRegistration(RegistrationInstance instance, InjectionKey key) {
        InjectionMetaData injectionMetaData = createInjectionMetaData(key);
        ServiceRegister register = createServiceRegister(instance, injectionMetaData);
        registeredNamedServices.put(key, register);
        return register;
    }

    private ServiceRegister createServiceRegister(RegistrationInstance instance, InjectionMetaData injectionMetaData) {
        return new ServiceRegister(instance.getService(), null, getAnnotationScope(injectionMetaData), normalizeType(SimpleInjection.RegisterType.NORMAL));
    }

    private InjectionMetaData createInjectionMetaData(InjectionKey key) {
        return createInjectionMetaData(key.getServiceDefinition(), getQualifier(key));
    }

    private String getQualifier(InjectionKey key) {
        if(key.getName() != null){
            return key.getName();
        }else if(key.getAnnotation() != null){
            return key.getAnnotation().getName();            
        }
        return null;  //To change body of created methods use File | Settings | File Templates.
    }

    private SimpleInjection.Scope getAnnotationScope(InjectionMetaData injectionMetaData) {
        return injectionMetaData.isSingleton() ? SimpleInjection.Scope.SINGLETON : SimpleInjection.Scope.NEW;
    }

    private InjectionMetaData createInjectionMetaData(Class service, String qualifier) {
        AnnotationInjection annotationInjection = new AnnotationInjection(injectionMetaDataCache);
        InjectionMetaData injectionMetaData =
                annotationInjection.createInjectionMetaData(service, qualifier, InjectionUtils.isProvider(service));
        injectionMetaDataCache.add(injectionMetaData);
        return injectionMetaData;
    }


}