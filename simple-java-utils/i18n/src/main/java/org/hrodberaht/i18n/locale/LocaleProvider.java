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

import org.hrodberaht.inject.InjectionRegisterJava;
import org.hrodberaht.inject.SimpleInjection;

import java.util.Locale;

/**
 * Simple Java Utils
 *
 * @author Robert Alexandersson
 * @version 1.0
 * @since 1.0
 */
public class LocaleProvider {

    static{
        InjectionRegisterJava.registerDefault(ProviderInterface.class, SimpleLocaleProvider.class);
    }

    public static LocaleProfile getProfile(){
        return SimpleInjection.get(ProviderInterface.class).getProfile();
    }
    
    public static Locale getSystemLocale(){
        return SimpleInjection.get(ProviderInterface.class).getSystemLocale();
    }
}