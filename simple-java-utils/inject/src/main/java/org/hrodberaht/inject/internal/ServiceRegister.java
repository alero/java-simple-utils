package org.hrodberaht.inject.internal;

import org.hrodberaht.inject.SimpleInjection;

public class ServiceRegister {

    private Class<Object> service;
    private Object singleton;
    private SimpleInjection.Scope scope;
    private SimpleInjection.RegisterType registerType = SimpleInjection.RegisterType.WEAK;

    public ServiceRegister(Class<Object> aService, Object singleton, SimpleInjection.Scope scope, SimpleInjection.RegisterType registerType) {
        this.service = aService;
        this.singleton = singleton;
        this.scope = scope;
        this.registerType = registerType;
    }

    public ServiceRegister(Class<Object> aService) {
        this.service = aService;
    }

    public Class<Object> getService() {
        return service;
    }

    public Object getSingleton() {
        return singleton;
    }

    public SimpleInjection.Scope getScope() {
        return scope;
    }

    public SimpleInjection.RegisterType getRegisterType() {
        return registerType;
    }
}