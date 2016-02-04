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
import org.hrodberaht.inject.register.RegistrationModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServiceRegister {

    private static final Logger LOG = LoggerFactory.getLogger(ServiceRegister.class);

    private Class service;
    private Object singleton;
    private SimpleInjection.Scope scope;
    private SimpleInjection.RegisterType registerType = SimpleInjection.RegisterType.WEAK;
    private ServiceRegister overriddenService = null;

    private RegistrationModule module;

    public ServiceRegister(
            Class aService, Object singleton, SimpleInjection.Scope scope, SimpleInjection.RegisterType registerType) {
        this.service = aService;
        this.singleton = singleton;
        this.scope = scope;
        this.registerType = registerType;
        if (LOG.isDebugEnabled()) {
            LOG.debug("ServiceRegister for" +
                    " service=" + service.getName() +
                    " singleton=" + singleton +
                    " scope=" + scope +
                    " registerType=" + registerType +
                    ""
            );
        }
    }

    public ServiceRegister(Class aService) {
        this.service = aService;
    }

    public Class getService() {
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

    public ServiceRegister getOverriddenService() {
        return overriddenService;
    }

    public void setOverriddenService(ServiceRegister overriddenService) {
        this.overriddenService = overriddenService;
    }

    public RegistrationModule getModule() {
        return module;
    }

    public void setModule(RegistrationModule module) {
        this.module = module;
    }

    @Override
    public ServiceRegister clone() {
        return new ServiceRegister(this.service, null, this.scope, this.registerType);
    }
}