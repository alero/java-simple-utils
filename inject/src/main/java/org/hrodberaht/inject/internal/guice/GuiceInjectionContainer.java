package org.hrodberaht.inject.internal.guice;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import org.hrodberaht.inject.SimpleInjection;
import org.hrodberaht.inject.internal.InjectionContainer;

import java.lang.annotation.Annotation;

/**
 * Simple Java Utils - Container
 *
 * @author Robert Alexandersson
 *         2010-maj-31 22:14:05
 * @version 1.0
 * @since 1.0
 */
public class GuiceInjectionContainer implements InjectionContainer {

    private Injector injector = null;


    public <T> T getService(Class<T> service, SimpleInjection.Scope forcedScope, String qualifier) {
        return injector.getInstance(service);
    }


    public <T> T getService(
            Class<T> service, SimpleInjection.Scope forcedScope, Class<? extends Annotation> qualifier) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    
    public <T> T getService(Class<T> service, SimpleInjection.Scope forcedScope) {
        return injector.getInstance(service);
    }

    public void registerModule(Module... modules) {
        injector = Guice.createInjector(modules);
    }
}
