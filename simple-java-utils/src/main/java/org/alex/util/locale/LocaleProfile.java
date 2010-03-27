package org.alex.util.locale;

import java.util.Locale;

/**
 * Simple Java Utils
 *
 * @author Robert Alexandersson
 * @version 1.0
 * @since 1.0
 */
public class LocaleProfile {
    
    private Locale locale;

    public LocaleProfile(Locale locale) {
        this.locale = locale;
    }

    public Locale getLocale() {
        return locale;
    }

}
