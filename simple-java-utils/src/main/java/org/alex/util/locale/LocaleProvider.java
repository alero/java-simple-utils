package org.alex.util.locale;

import java.util.Locale;

/**
 * Simple Java Utils
 *
 * @author Robert Alexandersson
 * @version 1.0
 * @since 1.0
 */
public class LocaleProvider {
    public static LocaleProfile getProfile() {
        return new LocaleProfile(getSystemLocale());
    }

    public static Locale getSystemLocale() {
        return new Locale("sv","SE");
    }
}
