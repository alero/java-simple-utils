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

/**
 * Simple Java Utils
 *
 * @author Robert Alexandersson
 * @version 1.0
 * @since 1.0
 */
public class JavaContainerRegister {



    public static void finalRegister(Class anInterface, Class service, SimpleContainer.Scope scope){
        SimpleContainer.register(anInterface, service, scope, SimpleContainer.RegisterType.FINAL);    
    }

    public static void reRegister(Class anInterface, Class service, SimpleContainer.Scope scope){
        SimpleContainer.register(anInterface, service, scope, SimpleContainer.RegisterType.OVERRIDE_NORMAL);
    }

    public static void register(Class anInterface, Class service, SimpleContainer.Scope scope){
        SimpleContainer.register(anInterface, service, scope, SimpleContainer.RegisterType.NORMAL);
    }

    public static void registerDefault(Class anInterface, Class service, SimpleContainer.Scope scope){
        SimpleContainer.register(anInterface, service, scope, SimpleContainer.RegisterType.WEAK);
    }

    public static void register(Class anInterface, Class service){
        register(anInterface, service, SimpleContainer.Scope.SINGLETON);
    }

    public static void registerDefault(Class anInterface, Class service){
        registerDefault(anInterface, service, SimpleContainer.Scope.SINGLETON);
    }

    public static void reRegister(Class anInterface, Class service){
        reRegister(anInterface, service, SimpleContainer.Scope.SINGLETON);
    }

    public static void finalRegister(Class anInterface, Class service){
        finalRegister(anInterface, service, SimpleContainer.Scope.SINGLETON);
    }


    public static void cleanRegister() {
        SimpleContainer.cleanRegister();
    }
}