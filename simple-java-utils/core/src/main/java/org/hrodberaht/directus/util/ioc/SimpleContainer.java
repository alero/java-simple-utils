/*
 * Copyright (c) 2010.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package org.hrodberaht.directus.util.ioc;

import org.hrodberaht.directus.exception.MessageRuntimeException;

import java.util.HashMap;

/**
 * Simple Java Utilts - Container
 *
 * @author Robert Alexandersson
 * 2010-mar-27 14:05:34
 * @version 1.0
 * @since 1.0
 */
public class SimpleContainer {

    public enum Scope {
        SINGLETON, NEW
    }


    public enum RegisterType {
        WEAK, NORMAL, OVERRIDE_NORMAL, FINAL
    }

    private static HashMap<Object, ServiceRegister> registeredServices = new HashMap<Object, ServiceRegister>();

    private static SimpleContainerInstanceCreator simpleContainerInstanceCreator = null;

    /**
     * Will retrieve a service as it has been registered, scope's supported today are {@link Scope#SINGLETON} and {@link Scope#NEW}
     *
     * @param service the interface service intended for creation 
     * @param <T> the typed service intended for creation
     * @return an instance of the interface requested will be created/fetched and returned from the internal register
     */
    public static <T> T get(Class<T> service) {
        return getService(service, null);
    }

    /**
     * Will retrieve a service and force the scope to {@link Scope#NEW}
     *
     * @param service the interface service intended for creation
     * @param <T> the typed service intended for creation
     * @return an instance of the interface requested will be created/fetched and returned from the internal register
     */
    public static <T> T getNew(Class<T> service) {
        return getService(service, Scope.NEW);
    }
    /**
     * Will retrieve a service and force the scope to {@link Scope#SINGLETON}
     *
     * @param service the interface service intended for creation
     * @param <T> the typed service intended for creation
     * @return an instance of the interface requested will be created/fetched and returned from the internal register
     */
    public static <T> T getSingleton(Class<T> service) {
        return getService(service, Scope.SINGLETON);
    }


    protected synchronized static void cleanRegister(){
        registeredServices.clear();        
    }

    protected synchronized static void register(Class anInterface, Class<Object> service, Scope scope, RegisterType type) {
        if (registeredServices.containsKey(anInterface)) {            
            reRegisterSupport(anInterface, type);
        }

        registeredServices.put(anInterface, new ServiceRegister(service, createInstance(service), scope, normalizeType(type)));

    }

    protected synchronized static void registerInstanceCreator(SimpleContainerInstanceCreator simpleContainerInstanceCreator) {
        SimpleContainer.simpleContainerInstanceCreator = simpleContainerInstanceCreator;
    }

    private static RegisterType normalizeType(RegisterType type) {
        if(type == RegisterType.OVERRIDE_NORMAL){
            return RegisterType.NORMAL;
        }
        return type;
    }

    private static void reRegisterSupport(Class anInterface, RegisterType type) {

        ServiceRegister serviceRegister = registeredServices.get(anInterface);
        if(serviceRegister.registerType == RegisterType.WEAK){
            registeredServices.remove(anInterface);
            return;
        }

        if(serviceRegister.registerType == RegisterType.NORMAL){
            if(type == RegisterType.OVERRIDE_NORMAL){
                registeredServices.remove(anInterface);
                return;
            }
            throw new MessageRuntimeException(
                    "Service {0} is already registered, to override register please use the override method"
                    , anInterface);
        }
        if(serviceRegister.registerType == RegisterType.FINAL){
            throw new MessageRuntimeException(
                    "A FINAL Service for {0} is already registered, can not reRegister", anInterface);
        }

    }

    @SuppressWarnings(value = "unchecked")
    private static <T> T getService(Class<T> service, Scope forcedScope) {

        if(simpleContainerInstanceCreator != null){
            if(forcedScope != null && !simpleContainerInstanceCreator.supportForcedInstanceScope()){
                throw new MessageRuntimeException("Can not use forced scope for service {0}", service);
            }
            if(simpleContainerInstanceCreator.supportServiceCreation(service)){
                return simpleContainerInstanceCreator.getService(service);
            }
        }
        if (!registeredServices.containsKey(service)) {
            throw new MessageRuntimeException("Service {0} not registered in SimpleContainer", service);
        }
        ServiceRegister serviceRegister = registeredServices.get(service);
        if (forcedScope == null && serviceRegister.scope == Scope.NEW) {
            return (T) createInstance(serviceRegister.service);
        } else if (Scope.NEW == forcedScope) {
            return (T) createInstance(serviceRegister.service);
        }
        
        return (T) serviceRegister.singleton;
    }


    private static Object createInstance(Class<Object> service) {
        try {
            return service.newInstance();
        } catch (InstantiationException e) {
            throw new MessageRuntimeException("Could not create an instance of {0}", e, service);
        } catch (IllegalAccessException e) {
            throw new MessageRuntimeException("Could not create an instance of {0}", e, service);
        }
    }

    private static class ServiceRegister {
        private Class<Object> service;
        private Object singleton;
        private Scope scope;
        private RegisterType registerType = RegisterType.WEAK;

        private ServiceRegister(Class<Object> aService, Object singleton, Scope scope, RegisterType registerType) {
            this.service = aService;
            this.singleton = singleton;
            this.scope = scope;
            this.registerType = registerType;
        }
    }
}
