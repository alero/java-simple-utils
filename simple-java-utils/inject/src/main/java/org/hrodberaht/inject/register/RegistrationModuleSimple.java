package org.hrodberaht.inject.register;

import org.hrodberaht.inject.register.internal.RegistrationExtended;
import org.hrodberaht.inject.register.internal.RegistrationInstanceSimple;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Simple Java Utils
 *
 * @author Robert Alexandersson
 *         2010-jun-17 19:11:31
 * @version 1.0
 * @since 1.0
 */
public abstract class RegistrationModuleSimple implements RegistrationModule{
    protected Map<RegistrationInstanceSimple, RegistrationInstanceSimple>
            registrations = new HashMap<RegistrationInstanceSimple, RegistrationInstanceSimple>();

    protected RegistrationModuleSimple() {
        registrations();
    }

    public RegistrationExtended register(Class anyThing) {
        RegistrationInstanceSimple instance = new RegistrationInstanceSimple(anyThing);
        registrations.put(instance, instance);
        return instance;
    }

    public Collection<RegistrationInstanceSimple> getRegistrations() {
        return registrations.values();
    }

    public abstract void registrations();
}
