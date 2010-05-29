package org.hrodberaht.inject.internal.annotation;

import org.hrodberaht.inject.internal.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Simple Java Utils
 *
 * @author Robert Alexandersson
 *         2010-maj-28 20:26:43
 * @version 1.0
 * @since 1.0
 */
public class AnnotationInjection {

    

    private List<InjectionMetaData> injectionMetaDataCache = new ArrayList<InjectionMetaData>();
    private Map<InjectionMetaData, Object> serviceCache = new HashMap<InjectionMetaData, Object>();
    

    public Object createInstance(Class<Object> service) {
        InjectionMetaData injectionMetaData =
                findInjectionData(service, null, InjectionUtils.isProvider(service));

        return callConstructor(injectionMetaData);
    }

    public Object createInstance(Class<Object> service, String qualifier) {
        InjectionMetaData injectionMetaData =
                findInjectionData(service, qualifier, InjectionUtils.isProvider(service));

        return callConstructor(injectionMetaData);
    }

    private Object callConstructor(InjectionMetaData injectionMetaData) {

        Object service = serviceInCache(injectionMetaData);
        if(service != null){
            return service;
        }

        List<InjectionMetaData> dependencies = injectionMetaData.getConstructorDependencies();

        final Object[] beansForConstructor = new Object[dependencies.size()];

        for (int i = 0; i < dependencies.size(); i++) {
            final InjectionMetaData dependency = dependencies.get(i);
            final Object bean = innerCreateInstance(dependency);
            beansForConstructor[i] = bean;
        }
        service = injectionMetaData.createInstance(beansForConstructor);
        autoWireBean(service, injectionMetaData);
        addToServiceCache(injectionMetaData, service);
        return service;
    }

    private void autoWireBean(Object service, InjectionMetaData injectionMetaData) {
        List<InjectionPoint> injectionPoints = injectionMetaData.getInjectionPoints();

        for(InjectionPoint injectionPoint:injectionPoints){
            List<InjectionMetaData> dependencies = injectionPoint.getDependencies();
            Object[] serviceDependencies = new Object[dependencies.size()];
            int i = 0;
            for(InjectionMetaData dependence:dependencies){
                Object serviceDependence = innerCreateInstance(dependence);
                serviceDependencies[i] = serviceDependence;
                i++;
            }

            injectionPoint.inject(service, serviceDependencies);

        }
    }

    private Object innerCreateInstance(final InjectionMetaData dependency) {
        if (dependency.isProvider()) {            
            return new InjectionProvider(dependency.getServiceClass(), dependency.getQualifierName());
        }

        return createInstance(dependency.getServiceClass(), dependency.getQualifierName());
    }

    public InjectionMetaData findInjectionData(Class<Object> service, String qualifier, boolean provider) {
        InjectionMetaData cachedInjectionMetaData = injectionMetaDataInCache(new InjectionMetaData(service, qualifier, provider));
        if(cachedInjectionMetaData != null){
            return cachedInjectionMetaData;
        }

        InjectionMetaData injectionMetaData = new InjectionMetaData(service, qualifier, provider);
        addToInjectionMetaDataCache(injectionMetaData);
        Constructor constructor = InjectionUtils.findConstructor(service);
        injectionMetaData.setConstructor(constructor);
        injectionMetaData.setConstructorDependencies(findDependencies(constructor));
        injectionMetaData.setInjectionPoints(findAllInjectionPoints(service));
        return injectionMetaData;
    }

    private List<InjectionPoint> findAllInjectionPoints(Class<Object> service){
        final List<InjectionPoint> injectionPoints = new ArrayList<InjectionPoint>();

        final List<Method> allMethods = ReflectionUtils.findMethods(service);
        final List<Member> allMembers = ReflectionUtils.findMembers(service);

        for (final Member member : allMembers) {
            if (member instanceof Field) {
                final Field field = (Field) member;

                if (fieldNeedsInjection(field)) {

                    injectionPoints.add(new InjectionPoint(field, this));
                }
            }

            else if (member instanceof Method) {
                final Method method = (Method) member;

                if (methodNeedsInjection(method) && !ReflectionUtils.isOverridden(method, allMethods)) {

                    injectionPoints.add(new InjectionPoint(method, this));
                }
            }

            else {
                throw new UnsupportedOperationException("Unsupported member: " + member);
            }
        }

        return injectionPoints;        
    }

    private boolean methodNeedsInjection(Method method) {
        return false;
    }

    private boolean fieldNeedsInjection(Field field) {
        return !ReflectionUtils.isStatic(field)
             && !ReflectionUtils.isFinal(field)
             && field.isAnnotationPresent(InjectionUtils.INJECT);

    }

    private void addToInjectionMetaDataCache(InjectionMetaData injectionMetaData) {
        injectionMetaDataCache.add(injectionMetaData);
    }

    private InjectionMetaData injectionMetaDataInCache(InjectionMetaData injectionMetaData) {
        final Iterator<InjectionMetaData> iterator = injectionMetaDataCache.iterator();

        while (iterator.hasNext()) {
            final InjectionMetaData singleton = iterator.next();
            if (injectionMetaData.canInject(singleton) && injectionMetaData.isProvider() == singleton.isProvider()) {
                return singleton;
            }
        }
        return null;
    }

    private void addToServiceCache(InjectionMetaData injectionMetaData, Object service) {
        serviceCache.put(injectionMetaData, service);
    }

    private Object serviceInCache(InjectionMetaData injectionMetaData) {
        final Iterator<InjectionMetaData> iterator = serviceCache.keySet().iterator();

        while (iterator.hasNext()) {
            final InjectionMetaData singleton = iterator.next();
            if (injectionMetaData == singleton) {
                return serviceCache.get(injectionMetaData);
            }
        }
        return null;
    }

    private List<InjectionMetaData> findDependencies(Constructor constructor) {

        final Class[] parameterTypes = constructor.getParameterTypes();
        final Type[] genericParameterTypes = constructor.getGenericParameterTypes();
        final Annotation[][] parameterAnnotations = constructor.getParameterAnnotations();

        return InjectionUtils.findDependencies(parameterTypes, genericParameterTypes, parameterAnnotations, this);
    }


    public static boolean isProvider(Class<Object> service) {
        return InjectionUtils.isProvider(service);
    }
}
