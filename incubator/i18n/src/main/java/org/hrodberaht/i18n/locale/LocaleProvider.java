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

    private LocaleProvider() {
    }

    static{
        InjectionRegisterJava registerJava = new InjectionRegisterJava();
        registerJava.registerDefault(ProviderInterface.class, SimpleLocaleProvider.class);
        injectionContainer = registerJava.getContainer();
    }

    public static void setThreadLocaleProvider(){
        InjectionRegisterJava registerJava = new InjectionRegisterJava(
               LocaleProvider.getInjectionContainer());
        registerJava.reRegister(ProviderInterface.class, ThreadLocaleProvider.class);
    }

    public static void seDefaultLocaleProvider() {
        InjectionRegisterJava registerJava = new InjectionRegisterJava(
               LocaleProvider.getInjectionContainer());
        registerJava.reRegister(ProviderInterface.class, SimpleLocaleProvider.class);
    }

    /**
     * Use the injection container to change the implementation fo the LocaleProvider.
     * Default version setup is SimpleLocaleProvider and anything implementing ProviderInterface will suffice.
     *
     * To Change registered service simply do this
     * InjectionRegisterJava registerJava = new InjectionRegisterJava(
     *          (SimpleInjection)LocaleProvider.getInjectionContainer());
     * registerJava.register(ProviderInterface.class, ThreadLocaleProvider.class);
     *
     * @return the Container used for LocaleProvider
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

    public static void setProfile(LocaleProfile profile){
        injectionContainer.get(ProviderInterface.class).setStatefulProfile(profile);
    }



}
