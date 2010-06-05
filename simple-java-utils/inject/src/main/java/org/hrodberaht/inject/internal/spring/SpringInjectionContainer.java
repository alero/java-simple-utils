package org.hrodberaht.inject.internal.spring;

import org.hrodberaht.inject.SimpleInjection;
import org.hrodberaht.inject.creators.annotation.AnnotationRegistrationModule;
import org.hrodberaht.inject.internal.InjectionContainer;
import org.hrodberaht.inject.internal.InjectionContainerBase;
import org.hrodberaht.inject.internal.ServiceRegister;
import org.hrodberaht.inject.internal.annotation.InjectionKey;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.lang.annotation.Annotation;

/**
 * Simple Java Utils
 *
 * @author Robert Alexandersson
 *         2010-maj-31 22:03:37
 * @version 1.0
 * @since 1.0
 */
public class SpringInjectionContainer extends InjectionContainerBase implements InjectionContainer {

    // private Map<Class, Class> serviceRegister = new HashMap<Class, Class>();
    private ApplicationContext context = null;

    public SpringInjectionContainer() {

        /*String[] beannames = context.getBeanDefinitionNames();
        for(String beanname:beannames){
            if(!beanname.startsWith("org.springframework")){
                Object o = context.getBean(beanname);
                Class[] _interfaces = o.getClass().getInterfaces();
                if(_interfaces.length == 1){
                    serviceRegister.put(_interfaces[0], null);
                }
            }
        }*/
    }

    public void registerConfigResource(String... locations) {
        context = new ClassPathXmlApplicationContext(locations);
    }

    @Override
    public <T> T getService(Class<T> service, SimpleInjection.Scope forcedScope, String qualifier) {
        return context.getBean(qualifier, service);
    }

    @Override
    public <T> T getService(Class<T> service, SimpleInjection.Scope forcedScope, Class<? extends Annotation> qualifier) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public <T> T getService(Class<T> service, SimpleInjection.Scope forcedScope) {
        return context.getBean(service);
    }

    @Override
    public void register(Class anInterface, Class service, SimpleInjection.Scope scope, SimpleInjection.RegisterType type) {
        // TODO: implements Spring registration support
    }

    @Override
    public void register(InjectionKey key, Class service, SimpleInjection.Scope scope, SimpleInjection.RegisterType type) {
        // TODO: implements Spring registration support
    }

    @Override
    public void register(AnnotationRegistrationModule... module) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected Object createInstance(ServiceRegister serviceRegister) {
        // This will never be called for Spring
        return null;
    }
}
