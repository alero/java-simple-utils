/*
 * <!--
 *   ~ Copyright (c) 2010.
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
 *   -->
 */

package org.hrodberaht.i18n.locale;

import org.hrodberaht.inject.Container;
import org.hrodberaht.inject.InjectionRegisterJava;

import java.util.Locale;

/**
 * Simple Java Utils
 *
 * @author Robert Alexandersson
 * @version 1.0
 * @since 1.0
 */
public class LocaleProvider {

    private static Container injectionContainer;

    static{
        InjectionRegisterJava registerJava = new InjectionRegisterJava();
        registerJava.registerDefault(ProviderInterface.class, SimpleLocaleProvider.class);
        injectionContainer = registerJava.getContainer();
    }

    /**
     * Use the injection container to change the implementation fo the LocaleProvider.
     * Default version setup is SimpleLocaleProvider and anything implementing ProviderInterface will suffice.
     *
     * To Change registered service simply do this
     * SimpleInjection container = LocaleProvider.getInjectionContainer();
     * container.register(ProviderInterface.class, ThreadLocaleProvider.class);
     *
     * @return
     */
    public static Container getInjectionContainer() {
        return injectionContainer;
    }

    public static LocaleProfile getProfile(){
        return injectionContainer.get(ProviderInterface.class).getProfile();
    }
    
    public static Locale getSystemLocale(){
        return injectionContainer.get(ProviderInterface.class).getSystemLocale();
    }
}
