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

    public void setSingleton(Object singleton) {
        this.singleton = singleton;
    }
}