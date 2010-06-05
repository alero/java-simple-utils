package org.hrodberaht.inject.register.annotation;

import org.hrodberaht.inject.register.RegistrationModule;

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
public abstract class AnnotationRegistrationModule implements RegistrationModule {


    private Map<Class, AnnotationRegistrationInstance> registrations = new HashMap<Class, AnnotationRegistrationInstance>();

    protected AnnotationRegistrationModule() {
        registrations();
    }

    public AnnotationRegistrationInstance register(Class anyThing){
        AnnotationRegistrationInstance instance = new AnnotationRegistrationInstance(anyThing);
        registrations.put(anyThing, instance);
        return instance;
    }

    public Collection<AnnotationRegistrationInstance> getRegistrations(){
        return registrations.values();
    }

    public abstract void registrations();



}
