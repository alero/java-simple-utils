package org.hrodberaht.inject.internal.guice;

import org.hrodberaht.inject.SimpleInjection;
import org.hrodberaht.inject.internal.InjectionContainer;
import org.hrodberaht.inject.internal.InjectionContainerBase;
import org.hrodberaht.inject.internal.ServiceRegister;

/**
 * Simple Java Utils
 *
 * @author Robert Alexandersson
 *         2010-maj-31 22:14:05
 * @version 1.0
 * @since 1.0
 */
public class GuiceInjectionContainer extends InjectionContainerBase implements InjectionContainer {

    

    @Override
    public <T> T getService(Class<T> service, SimpleInjection.Scope forcedScope, String qualifier) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public <T> T getService(Class<T> service, SimpleInjection.Scope forcedScope) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void register(Class anInterface, Class<Object> service, SimpleInjection.Scope scope, SimpleInjection.RegisterType type) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void register(String namedInstance, Class<Object> service, SimpleInjection.Scope scope, SimpleInjection.RegisterType type) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected Object createInstance(ServiceRegister serviceRegister) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
