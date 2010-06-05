package org.hrodberaht.inject.internal.guice;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import org.hrodberaht.inject.SimpleInjection;
import org.hrodberaht.inject.register.annotation.AnnotationRegistrationModule;
import org.hrodberaht.inject.internal.InjectionContainer;
import org.hrodberaht.inject.internal.InjectionContainerBase;
import org.hrodberaht.inject.internal.ServiceRegister;
import org.hrodberaht.inject.internal.annotation.InjectionKey;

import java.lang.annotation.Annotation;

/**
 * Simple Java Utils
 *
 * @author Robert Alexandersson
 *         2010-maj-31 22:14:05
 * @version 1.0
 * @since 1.0
 */
public class GuiceInjectionContainer extends InjectionContainerBase implements InjectionContainer {

    Injector injector = null;

    @Override
    public <T> T getService(Class<T> service, SimpleInjection.Scope forcedScope, String qualifier) {
        return injector.getInstance(service);
    }

    @Override
    public <T> T getService(Class<T> service, SimpleInjection.Scope forcedScope, Class<? extends Annotation> qualifier) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public <T> T getService(Class<T> service, SimpleInjection.Scope forcedScope) {
        return injector.getInstance(service);
    }

    @Override
    public void register(Class anInterface, Class service, SimpleInjection.Scope scope, SimpleInjection.RegisterType type) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void register(InjectionKey key, Class service, SimpleInjection.Scope scope, SimpleInjection.RegisterType type) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void register(AnnotationRegistrationModule... modules) {
        //To change body of implemented methods use File | Settings | File Templates.
    }


    @Override
    protected Object createInstance(ServiceRegister serviceRegister) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void registerModule(Module... modules) {
        injector = Guice.createInjector(modules);
    }
}
