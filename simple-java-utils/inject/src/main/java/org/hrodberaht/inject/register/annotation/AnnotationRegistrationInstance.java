package org.hrodberaht.inject.register.annotation;

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
public class AnnotationRegistrationInstance {

    private Class theInterface;
    private Class theService;
    private String name;
    private Class<? extends Annotation> annotation;

    public AnnotationRegistrationInstance(Class theInterface) {
        this.theInterface = theInterface;
        this.theService = theInterface;
    }

    public AnnotationRegistrationInstance annotated(Class<? extends Annotation> annotation) {
        this.annotation = annotation;
        return this;
    }

    public AnnotationRegistrationInstance namned(String named) {
        this.name = named;
        return this;
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
