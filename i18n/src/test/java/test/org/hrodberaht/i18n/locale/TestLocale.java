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

package test.org.hrodberaht.i18n.locale;

import org.hrodberaht.i18n.locale.LocaleProfile;
import org.hrodberaht.i18n.locale.LocaleProvider;
import org.junit.Before;
import org.junit.Test;

import java.util.Locale;

import static org.junit.Assert.assertEquals;

/**
 * Simple Java Utils
 *
 * @author Robert Alexandersson
 * @version 1.0
 * @since 1.0
 */
public class TestLocale {

    @Before
    public void init() {
        System.setProperty("localeprovide.locale", "sv_SE");
    }

    @Test
    public void simpleLocaleTest(){

        Locale testLocale = new Locale("sv","Se");
        Locale locale = LocaleProvider.getProfile().getLocale();
        assertEquals(locale, testLocale); 
    }

    @Test
    public void simpleSystemLocaleTest(){

        Locale testLocale = new Locale("sv","Se");
        Locale testProviderLocale = new Locale("en","US");

        // This mimics what can be done in a RequestFilter for example
        LocaleProvider.setThreadLocaleProvider();
        LocaleProvider.setProfile(new LocaleProfile(testProviderLocale));

        // While this can be done anywhere in the same JVM and on the same thread (inheritable threads supported).
        Locale locale = LocaleProvider.getSystemLocale();
        assertEquals(locale, testLocale);

        locale = LocaleProvider.getProfile().getLocale();
        assertEquals(locale, testProviderLocale);
        LocaleProvider.seDefaultLocaleProvider();
    }
}
