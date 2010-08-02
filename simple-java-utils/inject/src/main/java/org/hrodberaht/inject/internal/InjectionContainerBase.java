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

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * Simple Java Utils - Container
 *
 * @author Robert Alexandersson
 *         2010-maj-29 13:07:33
 * @version 1.0
 * @since 1.0
 */
public abstract class InjectionContainerBase {

    protected HashMap<InjectionKey, ServiceRegister>
            registeredNamedServices = new HashMap<InjectionKey, ServiceRegister>();

    protected InjectionKey getNamedKey(String qualifier, Class serviceDefinition) {
        return new InjectionKey(qualifier, serviceDefinition);
    }

    protected InjectionKey getAnnotatedKey(Class<? extends Annotation> qualifier, Class serviceDefinition) {
        return new InjectionKey(qualifier, serviceDefinition);
    }

    protected InjectionKey getKey(Class serviceDefinition) {
        return new InjectionKey(serviceDefinition);
    }



    @SuppressWarnings(value = "unchecked")
    protected <T> T instantiateService(
            Class<T> service, SimpleInjection.Scope forcedScope, ServiceRegister serviceRegister) {
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

    public Collection<ServiceRegister> getServiceRegister() {
        Collection<ServiceRegister> registry = new ArrayList<ServiceRegister>(50);        
        registry.addAll(getNamedRegisteredServices());
        return registry;
    }


    private Collection<ServiceRegisterNamed> getNamedRegisteredServices(){
        Collection<ServiceRegisterNamed> regulars = new ArrayList<ServiceRegisterNamed>();
        Collection<InjectionKey> keys = registeredNamedServices.keySet();
        for(InjectionKey registerKey:keys){
            ServiceRegister register = registeredNamedServices.get(registerKey);
            ServiceRegisterNamed registerRegular = new ServiceRegisterNamed(register);
            registerRegular.setKey(registerKey);
            regulars.add(registerRegular);
        }
        return regulars;
    }

    protected abstract Object createInstance(ServiceRegister serviceRegister);

    protected <T> ServiceRegister findServiceImplementation(Class<T> service) {
        InjectionKey key = getKey(service);
        if (!registeredNamedServices.containsKey(key)) {
            if (service.isInterface()) { // TODO support this for classes as well? = inheritance support
                for (ServiceRegister serviceRegister : registeredNamedServices.values()) {
                    if (service.isAssignableFrom(serviceRegister.getService())) {
                        // TODO first make sure there is only one usable service
                        return serviceRegister;
                    }
                }
            }
            throw new InjectRuntimeException("Service {0} not registered in SimpleInjection", service);
        }
        return registeredNamedServices.get(key);
    }

}
