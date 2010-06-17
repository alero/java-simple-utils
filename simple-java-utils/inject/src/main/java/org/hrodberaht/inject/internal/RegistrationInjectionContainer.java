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

import org.hrodberaht.inject.SimpleInjection;
import org.hrodberaht.inject.internal.annotation.InjectionKey;
import org.hrodberaht.inject.register.RegistrationModule;

import java.lang.annotation.Annotation;

/**
 * Simple Java Utils - Container
 *
 * @author Robert Alexandersson
 *         2010-maj-29 12:48:43
 * @version 1.0
 * @since 1.0
 */
public interface RegistrationInjectionContainer {


    void register(Class serviceDefinition, Class service,
                  SimpleInjection.Scope scope, SimpleInjection.RegisterType type);
    void register(InjectionKey key, Class service,
                  SimpleInjection.Scope scope, SimpleInjection.RegisterType type);
    void register(RegistrationModule... modules);


}