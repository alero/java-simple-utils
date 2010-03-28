/*
 * Copyright (c) 2010.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package org.hrodberaht.directus.util.locale;

import org.hrodberaht.directus.util.ioc.JavaContainerRegister;
import org.hrodberaht.directus.util.ioc.SimpleContainer;

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
        JavaContainerRegister.registerDefault(ProviderInterface.class, SimpleLocaleProvider.class);
    }

    public static LocaleProfile getProfile(){
        return SimpleContainer.get(ProviderInterface.class).getProfile();
    }
    
    public static Locale getSystemLocale(){
        return SimpleContainer.get(ProviderInterface.class).getSystemLocale();
    }
}