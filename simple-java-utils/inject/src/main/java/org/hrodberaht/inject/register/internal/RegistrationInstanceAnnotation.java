package org.hrodberaht.inject.register.internal;

import org.hrodberaht.inject.ScopeContainer;
import org.hrodberaht.inject.SimpleInjection;
import org.hrodberaht.inject.internal.InjectionKey;

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

    protected Class theInterface;
    protected Class theService;
    protected Object theInstance;
    protected String name;
    protected Class<? extends Annotation> annotation;
    protected SimpleInjection.Scope scope = null; // No default scope for registration

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
    public T named(String named) {
        this.name = named;
        return (T) this;
    }

    public void with(Class theService) {
        this.theService = theService;
    }

    public void withInstance(Object theInstance) {
        this.theInstance = theInstance;
        this.theService = theInstance.getClass();
        this.scope = ScopeContainer.Scope.SINGLETON;
    }

    public Class getService() {
        return theService;
    }

    public Object getTheInstance() {
        return theInstance;
    }

    public T scopeAs(ScopeContainer.Scope scope) {
        this.scope = scope;
        return (T) this;
    }

    public ScopeContainer.Scope getScope() {
        return scope;
    }


    public InjectionKey getInjectionKey() {
        if(annotation != null){
            return new InjectionKey(annotation, theInterface, false);
        }else if(name != null){
            return new InjectionKey(name, theInterface, false);
        }
        return new InjectionKey(theInterface, false);
    }
}