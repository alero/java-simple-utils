package org.hrodberaht.inject.register.internal;

import org.hrodberaht.inject.internal.annotation.InjectionKey;

import java.lang.annotation.Annotation;

/**
 * Simple Java Utils - Container
 *
 * @author Robert Alexandersson
 *         2010-jun-03 17:53:13
 * @version 1.0
 * @since 1.0
 */
public class RegistrationInstanceAnnotation<T extends Registration> implements Registration {

    private Class theInterface;
    protected Class theService;
    protected String name;
    protected Class<? extends Annotation> annotation;

    public RegistrationInstanceAnnotation(Class theInterface) {
        this.theInterface = theInterface;
        this.theService = theInterface;
    }

    @SuppressWarnings(value = "unchecked")
    public T annotated(Class<? extends Annotation> annotation) {
        this.annotation = annotation;
        return (T) this;
    }

    @SuppressWarnings(value = "unchecked")
    public T namned(String named) {
        this.name = named;
        return (T) this;
    }

    public void with(Class theService) {
        this.theService = theService;
    }

    public InjectionKey getInjectionKey() {
        if(annotation != null){
            return new InjectionKey(annotation, theInterface);
        }else if(name != null){
            return new InjectionKey(name, theInterface);
        }
        return new InjectionKey(theInterface);
    }

    public Class getService() {
        return theService;
    }
}