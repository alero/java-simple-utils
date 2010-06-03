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

import java.lang.annotation.Annotation;
import java.util.HashMap;

/**
 * Simple Java Utils
 *
 * @author Robert Alexandersson
 *         2010-maj-29 13:07:33
 * @version 1.0
 * @since 1.0
 */
public abstract class InjectionContainerBase {


    protected HashMap<Class, ServiceRegister> registeredServices = new HashMap<Class, ServiceRegister>();
    protected HashMap<InjectionKey, ServiceRegister> registeredNamedServices = new HashMap<InjectionKey, ServiceRegister>();

    protected InjectionKey getNamedKey(String qualifier, Class service) {
        return new InjectionKey(qualifier, service);
    }

    protected InjectionKey getAnnotatedKey(Class<? extends Annotation> qualifier, Class service) {
        return new InjectionKey(qualifier, service);
    }

    @SuppressWarnings(value = "unchecked")
    protected <T> T instantiateService(Class<T> service, SimpleInjection.Scope forcedScope, ServiceRegister serviceRegister) {
        if (forcedScope == null && serviceRegister.getScope() == SimpleInjection.Scope.NEW) {
            return (T) createInstance(serviceRegister);
        } else if (SimpleInjection.Scope.NEW == forcedScope) {
            return (T) createInstance(serviceRegister);
        }
        if (serviceRegister.getSingleton() == null) {
            serviceRegister.setSingleton(createInstance(serviceRegister));
        }
        return (T) serviceRegister.getSingleton();
    }

    protected static SimpleInjection.RegisterType normalizeType(SimpleInjection.RegisterType type) {
        if (type == SimpleInjection.RegisterType.OVERRIDE_NORMAL) {
            return SimpleInjection.RegisterType.NORMAL;
        }
        return type;
    }

    protected abstract Object createInstance(ServiceRegister serviceRegister);

}
