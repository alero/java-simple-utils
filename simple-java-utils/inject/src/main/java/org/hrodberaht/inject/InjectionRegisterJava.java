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

import org.hrodberaht.inject.register.annotation.AnnotationRegistrationModule;
import org.hrodberaht.inject.internal.annotation.InjectionKey;
import org.hrodberaht.inject.internal.spring.SpringInjectionContainer;

/**
 * Simple Java Utils
 *
 * @author Robert Alexandersson
 * @version 1.0
 * @since 1.0
 */
public class InjectionRegisterJava extends InjectionRegisterBase {

    public static void finalRegister(Class anInterface, Class service, SimpleInjection.Scope scope){
        SimpleInjection.register(anInterface, service, scope, SimpleInjection.RegisterType.FINAL);
    }

    public static void reRegister(Class anInterface, Class service, SimpleInjection.Scope scope){
        SimpleInjection.register(anInterface, service, scope, SimpleInjection.RegisterType.OVERRIDE_NORMAL);
    }

    public static void register(Class anInterface, Class service, SimpleInjection.Scope scope){
        SimpleInjection.register(anInterface, service, scope, SimpleInjection.RegisterType.NORMAL);
    }

    public static void register(String namedService, Class anInterface, Class service, SimpleInjection.Scope scope){
        SimpleInjection.register(new InjectionKey(namedService, anInterface), service, scope, SimpleInjection.RegisterType.NORMAL);
    }

    public static void registerDefault(Class anInterface, Class service, SimpleInjection.Scope scope){
        SimpleInjection.register(anInterface, service, scope, SimpleInjection.RegisterType.WEAK);
    }

    public static void register(Class anInterface, Class service){
        register(anInterface, service, SimpleInjection.Scope.NEW);        
    }

    public static void register(Class service) {
        register(service, service, SimpleInjection.Scope.NEW);
    }

    public static void register(String namedService, Class anInterface, Class service) {
        register(namedService, anInterface, service, SimpleInjection.Scope.NEW);
    }

    public static void registerDefault(Class anInterface, Class service){
        registerDefault(anInterface, service, SimpleInjection.Scope.NEW);
    }

    public static void reRegister(Class anInterface, Class service){
        reRegister(anInterface, service, SimpleInjection.Scope.NEW);
    }

    public static void finalRegister(Class anInterface, Class service){
        finalRegister(anInterface, service, SimpleInjection.Scope.NEW);
    }


    public static void registerSpringResource(String... resources){
        ((SpringInjectionContainer)SimpleInjection.getContainer())
                .registerConfigResource(resources);
    }


    public static void register(AnnotationRegistrationModule module) {
        SimpleInjection.register(module);   
    }
}
