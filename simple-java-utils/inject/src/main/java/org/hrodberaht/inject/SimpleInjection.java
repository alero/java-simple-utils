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

package org.hrodberaht.inject;

import org.hrodberaht.inject.creators.SimpleContainerInstanceCreator;


import java.util.HashMap;

/**
 * Simple Java Utilts - Container
 *
 * @author Robert Alexandersson
 * 2010-mar-27 14:05:34
 * @version 1.0
 * @since 1.0
 */
public class SimpleInjection {

    private static boolean injectAnnotationCompliantMode = false;

    public enum Scope {
        SINGLETON, NEW
    }


    public enum RegisterType {
        WEAK, NORMAL, OVERRIDE_NORMAL, FINAL
    }

    private static HashMap<Object, ServiceRegister> registeredServices = new HashMap<Object, ServiceRegister>();
    private static HashMap<String, ServiceRegister> registeredNamedServices = new HashMap<String, ServiceRegister>();

    private static SimpleContainerInstanceCreator simpleContainerInstanceCreator = null;

    /**
     * Will retrieve a service as it has been registered, scope's supported today are {@link SimpleInjection.Scope#SINGLETON} and {@link SimpleInjection.Scope#NEW}
     *
     * @param service the interface service intended for creation
     * @param <T> the typed service intended for creation
     * @return an instance of the interface requested will be created/fetched and returned from the internal register
     */
    public static <T> T get(Class<T> service) {
        return getService(service, null);
    }

    /**
     * Will retrieve a service and force the scope to {@link SimpleInjection.Scope#NEW}
     *
     * @param service the interface service intended for creation
     * @param <T> the typed service intended for creation
     * @return an instance of the interface requested will be created/fetched and returned from the internal register
     */
    public static <T> T getNew(Class<T> service) {
        return getService(service, Scope.NEW);
    }
    /**
     * Will retrieve a service and force the scope to {@link SimpleInjection.Scope#SINGLETON}
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
    protected synchronized static void register(String namedInstance, Class<Object> service, Scope scope, RegisterType type) {
        if (registeredNamedServices.containsKey(namedInstance)) {
            reRegisterSupport(namedInstance, type);
        }
        registeredNamedServices.put(namedInstance, new ServiceRegister(service, createInstance(service), scope, normalizeType(type)));
    }

    protected synchronized static void registerInstanceCreator(SimpleContainerInstanceCreator simpleContainerInstanceCreator) {
        SimpleInjection.simpleContainerInstanceCreator = simpleContainerInstanceCreator;
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
            throw new SPIRuntimeException(
                    "Service {0} is already registered, to override register please use the override method"
                    , anInterface);
        }
        if(serviceRegister.registerType == RegisterType.FINAL){
            throw new SPIRuntimeException(
                    "A FINAL Service for {0} is already registered, can not reRegister", anInterface);
        }

    }

     private static void reRegisterSupport(String namedInstance, RegisterType type) {

        ServiceRegister serviceRegister = registeredNamedServices.get(namedInstance);
        if(serviceRegister.registerType == RegisterType.WEAK){
            registeredNamedServices.remove(namedInstance);
            return;
        }

        if(serviceRegister.registerType == RegisterType.NORMAL){
            if(type == RegisterType.OVERRIDE_NORMAL){
                registeredNamedServices.remove(namedInstance);
                return;
            }
            throw new SPIRuntimeException(
                    "Service {0} is already registered, to override register please use the override method"
                    , namedInstance);
        }
        if(serviceRegister.registerType == RegisterType.FINAL){
            throw new SPIRuntimeException(
                    "A FINAL Service for {0} is already registered, can not reRegister", namedInstance);
        }

    }

    @SuppressWarnings(value = "unchecked")
    private static <T> T getService(Class<T> service, Scope forcedScope) {

        if(simpleContainerInstanceCreator != null){
            if(forcedScope != null && !simpleContainerInstanceCreator.supportForcedInstanceScope()){
                throw new SPIRuntimeException("Can not use forced scope for service {0}", service);
            }
            if(simpleContainerInstanceCreator.supportServiceCreation(service)){
                return simpleContainerInstanceCreator.getService(service);
            }
        }
        if (!registeredServices.containsKey(service)) {
            throw new SPIRuntimeException("Service {0} not registered in SimpleInjection", service);
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
            if(isProvider(service)){
                return service;                    
            }
            if(injectAnnotationCompliantMode){
                return AnnotationInjection.createInstance(service);
            }

            return service.newInstance(); // uses empty constructor

        } catch (InstantiationException e) {
            throw new SPIRuntimeException("Could not create an instance of {0}", e, service);
        } catch (IllegalAccessException e) {
            throw new SPIRuntimeException("Could not create an instance of {0}", e, service);
        }
    }

    private static boolean isProvider(Class<Object> service) {
        if(injectAnnotationCompliantMode){

            return AnnotationInjection.isProvider(service);
        }
        return false;
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