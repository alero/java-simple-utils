package org.hrodberaht.inject.internal.annotation;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * Simple Java Utils
 *
 * @author Robert Alexandersson
 *         2010-maj-28 21:24:31
 * @version 1.0
 * @since 1.0
 */
public class InjectionMetaData {


    private String qualifierName;
    private Class serviceClass;
    private boolean provider = false;

    private boolean isSingleton = false;
    private Object singleton = null;

    private boolean preDefined = false;

    private Constructor constructor;
    private List<InjectionMetaData> constructorDependencies;
    private List<InjectionPoint> injectionPoints;

    public InjectionMetaData(Class<Object> serviceClass, String qualifierName, boolean provider) {
        this.serviceClass = serviceClass;
        this.qualifierName = qualifierName;
        this.provider = provider;
    }

    public void setConstructor(Constructor constructor) {
        this.constructor = constructor;
    }

    public Constructor getConstructor() {
        return constructor;
    }

    public void setSingleton(boolean singleton) {
        isSingleton = singleton;
    }

    public boolean isSingleton() {
        return isSingleton;
    }

    public List<InjectionMetaData> getConstructorDependencies() {
        return constructorDependencies;
    }

    public void setConstructorDependencies(List<InjectionMetaData> constructorDependencies) {
        this.constructorDependencies = constructorDependencies;
    }

    public String getQualifierName() {
        return qualifierName;
    }

    public void setInjectionPoints(List<InjectionPoint> injectionPoints) {
        this.injectionPoints = injectionPoints;
    }

    public List<InjectionPoint> getInjectionPoints() {
        return injectionPoints;
    }


    public boolean isProvider() {
        return provider;
    }

    public boolean isPreDefined() {
        return preDefined;
    }

    public void setPreDefined(boolean preDefined) {
        this.preDefined = preDefined;
    }

    public Class getServiceClass() {
        return serviceClass;
    }


    public Object createInstance(Object[] parameters) {
        final boolean originalAccessible = constructor.isAccessible();
        constructor.setAccessible(true);

        try {
            if(isSingleton && singleton != null){
                return singleton;
            }
            final Object newInstance = constructor.newInstance(parameters);
            if(isSingleton){
                singleton = newInstance;   
            }
            return newInstance;
        }

        catch (final InstantiationException e) {
            throw new RuntimeException(e);
        }

        catch (final IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        catch (final InvocationTargetException e) {
            throw new RuntimeException(e);
        }

        finally {
           constructor.setAccessible(originalAccessible);
        }
    }

    public boolean canInject(final InjectionMetaData bean) {
        if (bean == null) {
            return false;
        }

        if (bean == this) {
            return true;
        }

        if (serviceClass.equals(bean.getServiceClass()) && qualifierName == null) {
            return true;
        }

        if (serviceClass.isAssignableFrom(bean.getServiceClass())) {
            if (qualifierName == null && bean.getQualifierName() == null) {
                return true;
            }

            else if (qualifierName != null && qualifierName.equals(bean.getQualifierName())) {
                return true;
            }
        }

        return false;
    }

    public Object getSingleton() {
        return singleton;
    }

    public void setSingleton(Object singleton) {
        this.singleton = singleton;
    }
}
