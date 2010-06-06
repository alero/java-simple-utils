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

/**
 * Simple Java Utils
 *
 * @author Robert Alexandersson
 *         2010-maj-28 21:12:27
 * @version 1.0
 * @since 1.0
 */
public abstract class InjectionRegisterBase<T> {

    protected SimpleInjection container = null;

    protected InjectionRegisterBase() {
        this.container = new SimpleInjection();
        activateContainerDefault();
    }

    public T activateContainerJavaXInject() {
        container.setContainerInjectAnnotationCompliantMode();
        return (T) this;
    }

    public T activateContainerDefault() {
        container.setContainerSimpleInjection();
        return (T) this;
    }

    public T activateContainerSpring() {
        container.setContainerSpring();
        return (T) this;
    }

    public T activateContainerGuice() {
        container.setContainerGuice();
        return (T) this;
    }

}
