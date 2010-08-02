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

public class ServiceRegisterRegular extends ServiceRegister{

    private Class serviceDefinition;

    public ServiceRegisterRegular(ServiceRegister aService) {
        super(aService.getService(), aService.getSingleton()
                , aService.getScope(), aService.getRegisterType());
        super.setModule(aService.getModule());
        super.setOverriddenService(aService.getOverriddenService());
    }

    public ServiceRegisterRegular(Class aService) {
        super(aService);
    }

    public Class getServiceDefinition() {
        return serviceDefinition;
    }

    public void setServiceDefinition(Class serviceDefinition) {
        this.serviceDefinition = serviceDefinition;
    }
}