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

import com.google.inject.Module;
import org.hrodberaht.inject.internal.annotation.InjectionKey;
import org.hrodberaht.inject.internal.guice.GuiceInjectionContainer;
import org.hrodberaht.inject.internal.spring.SpringInjectionContainer;
import org.hrodberaht.inject.register.RegistrationModule;

import java.lang.annotation.Annotation;

/**
 * Simple Java Utils
 *
 * @author Robert Alexandersson
 * @version 1.0
 * @since 1.0
 */
public class InjectionRegisterJava extends InjectionRegisterBase<InjectionRegisterJava> {

    public InjectionRegisterJava(){
        super();
    }

    public InjectionRegisterJava(Container container) {
        this.container = (SimpleInjection) container;
    }

    public InjectionRegisterJava finalRegister(Class anInterface, Class service, SimpleInjection.Scope scope) {
        assert anInterface.isInterface();
        container.register(anInterface, service, scope, SimpleInjection.RegisterType.FINAL);
        return this;
    }

    public InjectionRegisterJava reRegister(Class anInterface, Class service, SimpleInjection.Scope scope) {
        assert anInterface.isInterface();
        container.register(anInterface, service, scope, SimpleInjection.RegisterType.OVERRIDE_NORMAL);
        return this;
    }

    public InjectionRegisterJava register(Class anInterface, Class service, SimpleInjection.Scope scope) {
        assert anInterface.isInterface();
        container.register(anInterface, service, scope, SimpleInjection.RegisterType.NORMAL);
        return this;
    }

    public InjectionRegisterJava register(String namedService, Class anInterface, Class service, SimpleInjection.Scope scope) {
        assert anInterface.isInterface();
        container.register(new InjectionKey(namedService, anInterface), service, scope, SimpleInjection.RegisterType.NORMAL);
        return this;
    }

    private InjectionRegisterJava register(Class<? extends Annotation> qualifier, Class anInterface, Class service, SimpleInjection.Scope scope) {
        assert anInterface.isInterface();
        container.register(new InjectionKey(qualifier, anInterface), service, scope, SimpleInjection.RegisterType.NORMAL);
        return this;
    }

    public InjectionRegisterJava registerDefault(Class anInterface, Class service, SimpleInjection.Scope scope) {
        assert anInterface.isInterface();
        container.register(anInterface, service, scope, SimpleInjection.RegisterType.WEAK);
        return this;
    }

    public InjectionRegisterJava register(Class anInterface, Class service) {
        register(anInterface, service, SimpleInjection.Scope.NEW);
        return this;
    }

    public InjectionRegisterJava register(Class service) {
        register(service, service, SimpleInjection.Scope.NEW);
        return this;
    }

    public InjectionRegisterJava register(String namedService, Class anInterface, Class service) {
        register(namedService, anInterface, service, SimpleInjection.Scope.NEW);
        return this;
    }
    public InjectionRegisterJava register(Class<? extends Annotation> qualifier, Class anInterface, Class service) {
        register(qualifier, anInterface, service, SimpleInjection.Scope.NEW);
        return this;
    }



    public InjectionRegisterJava registerDefault(Class anInterface, Class service) {
        registerDefault(anInterface, service, SimpleInjection.Scope.NEW);
        return this;
    }

    public InjectionRegisterJava reRegister(Class anInterface, Class service) {
        reRegister(anInterface, service, SimpleInjection.Scope.NEW);
        return this;
    }

    public InjectionRegisterJava finalRegister(Class anInterface, Class service) {
        finalRegister(anInterface, service, SimpleInjection.Scope.NEW);
        return this;
    }


    public InjectionRegisterJava registerSpringResource(String... resources) {
        ((SpringInjectionContainer) container.getContainer())
                .registerConfigResource(resources);
        return this;
    }


    public InjectionRegisterJava register(RegistrationModule module) {
        container.register(module);
        return this;
    }

    public InjectionRegisterJava registerGuiceModule(Module... resources) {
        ((GuiceInjectionContainer) container.getContainer())
                .registerModule(resources);
        return this;
    }

    public Container getContainer() {
        return container;
    }

    public ScopeContainer getScopedContainer() {
        return container;
    }


}
