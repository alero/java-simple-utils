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

import org.hrodberaht.inject.internal.InjectionKey;
import org.hrodberaht.inject.internal.ServiceRegister;
import org.hrodberaht.inject.internal.ServiceRegisterNamed;
import org.hrodberaht.inject.register.InjectionRegister;

import java.lang.annotation.Annotation;
import java.util.Collection;

/**
 * Simple Java Utils - Container
 *
 * @author Robert Alexandersson
 *         2010-maj-28 21:12:27
 * @version 1.0
 * @since 1.0
 */
public abstract class InjectionRegisterBase<T extends InjectionRegister> implements InjectionRegister {

    protected SimpleInjection container = null;

    public SimpleInjection getInnerContainer() {
        return container;
    }

    protected InjectionRegisterBase() {
        this.container = new SimpleInjection();
        activateContainerDefault();
    }

    protected InjectionRegisterBase(InjectionRegister register) {
        this.container = register.getInnerContainer();        
    }

    public T activateContainerJavaXInject() {
        container.setContainerInjectAnnotationCompliantMode();
        return (T) this;
    }

    public T activateContainerSimple() {
        container.setContainerSimpleInjection();
        return (T) this;
    }

    public T activateContainerDefault() {
        container.setContainerInjectAnnotationCompliantMode();
        return (T) this;
    }

    public InjectionRegister register(Class<? extends Annotation> qualifier, Class serviceDefinition, Class service) {
        register(qualifier, serviceDefinition, service, SimpleInjection.Scope.NEW);
        return this;
    }

    public InjectionRegister register(Class serviceDefinition, Class service) {
        register(serviceDefinition, service, SimpleInjection.Scope.NEW);
        return this;
    }

    public InjectionRegister register(Class service) {
        register(service, service, SimpleInjection.Scope.NEW);
        return this;
    }

    public InjectionRegister overrideRegister(Class<? extends Annotation> qualifier, Class serviceDefinition, Class service) {
        overrideRegister(qualifier, serviceDefinition, service, SimpleInjection.Scope.NEW);
        return this;
    }

    public InjectionRegister overrideRegister(Class serviceDefinition, Class service) {
        overrideRegister(serviceDefinition, service, SimpleInjection.Scope.NEW);
        return this;
    }

    public InjectionRegister overrideRegister(Class service) {
        overrideRegister(service, service, SimpleInjection.Scope.NEW);
        return this;
    }

    public InjectionRegister finalRegister(Class<? extends Annotation> qualifier, Class serviceDefinition, Class service) {
        finalRegister(qualifier, serviceDefinition, service, SimpleInjection.Scope.NEW);
        return this;
    }



    public InjectionRegister finalRegister(Class serviceDefinition, Class service) {
        finalRegister(serviceDefinition, service, SimpleInjection.Scope.NEW);
        return this;
    }

    public InjectionRegister finalRegister(Class service) {
        finalRegister(service, service, SimpleInjection.Scope.NEW);
        return this;
    }

    public Container getContainer() {
        return container;
    }

    public ScopeContainer getScopedContainer() {
        return container;
    }

    public InjectContainer getInjectContainer() {
        return container;
    }

    protected InjectionRegister finalRegister(Class serviceDefinition, Class service, SimpleInjection.Scope scope) {
        container.register(serviceDefinition, service, scope, SimpleInjection.RegisterType.FINAL);
        return this;
    }

    protected InjectionRegister overrideRegister(Class serviceDefinition, Class service, SimpleInjection.Scope scope) {
        container.register(serviceDefinition, service, scope, SimpleInjection.RegisterType.OVERRIDE_NORMAL);
        return this;
    }

    protected InjectionRegister register(Class serviceDefinition, Class service, SimpleInjection.Scope scope) {
        container.register(serviceDefinition, service, scope, SimpleInjection.RegisterType.NORMAL);
        return this;
    }

    protected InjectionRegister register(
            Class<? extends Annotation> qualifier,
            Class serviceDefinition,
            Class service, SimpleInjection.Scope scope) {
        container.register(
                new InjectionKey(qualifier, serviceDefinition, false)
                , service, scope, SimpleInjection.RegisterType.NORMAL
        );
        return this;
    }

    protected InjectionRegister overrideRegister(
            Class<? extends Annotation> qualifier,
            Class serviceDefinition,
            Class service, SimpleInjection.Scope scope) {
        container.register(
                new InjectionKey(qualifier, serviceDefinition, false)
                , service, scope, SimpleInjection.RegisterType.OVERRIDE_NORMAL
        );
        return this;
    }

    private InjectionRegister finalRegister(
            Class<? extends Annotation> qualifier,
            Class serviceDefinition,
            Class service,
            ScopeContainer.Scope scope) {
        container.register(
                new InjectionKey(qualifier, serviceDefinition, false)
                , service, scope, SimpleInjection.RegisterType.FINAL
        );
        return this;
    }

    public void printRegistration(){
        System.out.println("\nThe current results in the container is as follows: ");


        Collection<ServiceRegister> registers = container.getServiceRegister();

        for(ServiceRegister serviceRegister:registers){

            printRegistration(serviceRegister);
            if(serviceRegister.getOverriddenService() != null){
                System.out.print("-- Overrides service ");
                if(serviceRegister.getOverriddenService().getModule() != null){
                    System.out.println("from module:"
                        +serviceRegister.getOverriddenService().getModule().getClass().getName());
                }else {
                    System.out.println("");
                }
                printRegistration(serviceRegister.getOverriddenService());
            }
            System.out.println("\n");
        }

        System.out.println("--------- InjectionRegisterModule Information Printer is Done ----------  ");
    }

    private void printRegistration(ServiceRegister serviceRegister) {
        
        if(serviceRegister instanceof ServiceRegisterNamed){
            ServiceRegisterNamed named = (ServiceRegisterNamed)serviceRegister;
            System.out.println("serviceDefinition:     "+named.getKey().getServiceDefinition());
            System.out.println("   with qualifier:     "+named.getKey().getQualifier());
            System.out.println("   is type safe:       "+(named.getKey().getAnnotation() == null ? "false" : "true"));
            System.out.println("serviceImplementation: "+named.getService().getName());
            System.out.println("scope:                 "+named.getScope().name());
            System.out.println("registrationType:      "+named.getRegisterType().name());

        }

        else {
            System.out.println("serviceImplementation: "+serviceRegister.getService().getName());
            System.out.println("scope:                 "+serviceRegister.getScope().name());
            System.out.println("registrationType:      "+serviceRegister.getRegisterType().name());
        }
    }

}
