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

package org.hrodberaht.inject;

import org.hrodberaht.inject.internal.InjectionContainer;
import org.hrodberaht.inject.internal.SimpleInjectionContainer;
import org.hrodberaht.inject.internal.annotation.AnnotationInjectionContainer;
import org.hrodberaht.inject.internal.annotation.InjectionKey;
import org.hrodberaht.inject.internal.guice.GuiceInjectionContainer;
import org.hrodberaht.inject.internal.spring.SpringInjectionContainer;
import org.hrodberaht.inject.register.RegistrationModule;

import java.lang.annotation.Annotation;

/**
 * Simple Java Utilts - Container
 *
 * @author Robert Alexandersson
 * 2010-mar-27 14:05:34
 * @version 1.0
 * @since 1.0
 */
public class SimpleInjection {



    // Perhaps a SimpleInjection basic singleton value instead of static fot these variables ...
    private static InjectionContainer injectionContainer = new SimpleInjectionContainer();


    public enum Scope {
        SINGLETON, NEW
    }


    public enum RegisterType {
        WEAK, NORMAL, OVERRIDE_NORMAL, FINAL
    }


    /**
     * Will retrieve a service as it has been registered, scope's supported today are {@link SimpleInjection.Scope#SINGLETON} and {@link SimpleInjection.Scope#NEW}
     *
     * @param service the interface service intended for creation
     * @param <T> the typed service intended for creation
     * @return an instance of the interface requested will be created/fetched and returned from the internal register
     */
    public static <T> T get(Class<T> service) {
        return injectionContainer.getService(service, null);
    }

    /**
     * Will retrieve a service as it has been registered, scope's supported today are {@link SimpleInjection.Scope#SINGLETON} and {@link SimpleInjection.Scope#NEW}
     *
     * @param service the interface service intended for creation
     * @param qualifier the named service intended for creation
     * @return an instance of the interface requested will be created/fetched and returned from the internal register
     */
    public static <T> T get(Class<T> service, String qualifier) {
        if(qualifier == null || "".equals(qualifier)){
            return injectionContainer.getService(service, null);
        }
        return injectionContainer.getService(service, null, qualifier);
    }

    /**
     * Will retrieve a service as it has been registered, scope's supported today are {@link SimpleInjection.Scope#SINGLETON} and {@link SimpleInjection.Scope#NEW}
     *
     * @param service the interface service intended for creation
     * @param qualifier the named service intended for creation
     * @return an instance of the interface requested will be created/fetched and returned from the internal register
     */
    public static <T> T get(Class<T> service, Class<? extends Annotation> qualifier) {
        if(qualifier == null){
            return injectionContainer.getService(service, null);
        }
        return injectionContainer.getService(service, null, qualifier);
    }

    /**
     * Will retrieve a service and force the scope to {@link SimpleInjection.Scope#NEW}
     *
     * @param service the interface service intended for creation
     * @param <T> the typed service intended for creation
     * @return an instance of the interface requested will be created/fetched and returned from the internal register
     */
    public static <T> T getNew(Class<T> service) {
        return injectionContainer.getService(service, Scope.NEW);
    }
    /**
     * Will retrieve a service and force the scope to {@link SimpleInjection.Scope#SINGLETON}
     *
     * @param service the interface service intended for creation
     * @param <T> the typed service intended for creation
     * @return an instance of the interface requested will be created/fetched and returned from the internal register
     */
    public static <T> T getSingleton(Class<T> service) {
        return injectionContainer.getService(service, Scope.SINGLETON);
    }



    protected synchronized static void register(Class anInterface, Class service, Scope scope, RegisterType type) {
        injectionContainer.register(anInterface, service, scope,  type);
    }

    protected synchronized static void register(InjectionKey key, Class service, Scope scope, RegisterType type) {
        injectionContainer.register(key, service, scope,  type);
    }

    public static void register(RegistrationModule module) {
        injectionContainer.register(module);
    }
    
    protected synchronized static void setContainerInjectAnnotationCompliantMode(){
        SimpleInjection.injectionContainer = new AnnotationInjectionContainer();
    }

    protected synchronized static void setContainerSimpleInjection(){
        SimpleInjection.injectionContainer = new SimpleInjectionContainer();
    }

    protected synchronized static void setContainerGuice() {
        SimpleInjection.injectionContainer = new GuiceInjectionContainer() ;
    }

    protected synchronized static void setContainerSpring() {
        SimpleInjection.injectionContainer = new SpringInjectionContainer();            
    }

    protected synchronized static InjectionContainer getContainer(){
        // TODO: make this support clone or remove
        return injectionContainer;
    }

}