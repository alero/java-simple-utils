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

package org.hrodberaht.util.locale;

import org.hrodberaht.exception.MessageRuntimeException;

import java.util.Locale;

/**
 * Simple Java Utils
 *
 * @author Robert Alexandersson
 * @version 1.0
 * @since 1.0
 */
public class SimpleLocaleProvider implements ProviderInterface{
    public LocaleProfile getProfile() {
        return new LocaleProfile(getSystemLocale());
    }

    public Locale getSystemLocale() {
        String locale = System.getProperty("localeprovide.locale");
        if(locale != null){
            return createLocale(locale);
        }
        return Locale.getDefault();
    }

    private Locale createLocale(String locale) {
        String[] locales = locale.split("_");
        if(locales.length == 1){
            return new Locale(locales[0]);
        } else {
            return new Locale(locales[0], locales[0]);
        }
    }

    @Override
    public StatefulProfile statefulProfileSupport() {
        return StatefulProfile.NONE;
    }

    @Override
    public void setStatefulProfile(LocaleProfile localeProfile) {
        throw MessageRuntimeException.createError("SimpleLocaleProvider does not have Stateful profile support");
    }
}
