package org.hrodberaht.inject.creators.annotation;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Simple Java Utils
 *
 * @author Robert Alexandersson
 *         2010-jun-03 17:49:42
 * @version 1.0
 * @since 1.0
 */
public abstract class RegistrationModule {

    private Map<Class, RegistrationInstance> registrations = new HashMap<Class, RegistrationInstance>();

    public RegistrationInstance register(Class anyThing){
        RegistrationInstance instance = new RegistrationInstance(anyThing);
        registrations.put(anyThing, instance);
        return instance;
    }

    public Collection<RegistrationInstance> getRegistrations(){
        return registrations.values();
    }

    public abstract void registrations();



}
