package org.hrodberaht.inject.internal.spring;

import org.hrodberaht.inject.SimpleInjection;
import org.hrodberaht.inject.internal.InjectionContainer;
import org.hrodberaht.inject.internal.ServiceRegister;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.lang.annotation.Annotation;
import java.util.Collection;

/**
 * Simple Java Utils - Container
 *
 * @author Robert Alexandersson
 *         2010-maj-31 22:03:37
 * @version 1.0
 * @since 1.0
 */
public class SpringInjectionContainer implements InjectionContainer {

    private ApplicationContext context = null;

    public void registerConfigResource(String... locations) {
        context = new ClassPathXmlApplicationContext(locations);
    }


    public <T> T getService(Class<T> service, SimpleInjection.Scope forcedScope, String qualifier) {
        return context.getBean(qualifier, service);
    }


    public <T> T getService(
            Class<T> service, SimpleInjection.Scope forcedScope, Class<? extends Annotation> qualifier) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public <T> T getService(Class<T> service, SimpleInjection.Scope forcedScope) {
        return context.getBean(service);
    }

    public Collection<ServiceRegister> getServiceRegister() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }


}
