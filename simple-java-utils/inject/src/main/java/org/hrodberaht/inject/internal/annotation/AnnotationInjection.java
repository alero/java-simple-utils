/*
 * ~ Copyright (c) 2010.
 *   ~ Licensed under the Apache License, Version 2.0 (the "License");
 *   ~ you may not use this file except in compliance with the License.
 *   ~ You may obtain a copy of the License at
 *   ~
 *   ~        http://www.apache.org/licenses/LICENSE-2.0
 *   ~
 *   ~ Unless required by applicable law or agreed to in writing, software
 *   ~ distributed under the License is distributed on an "AS IS" BASIS,
 *   ~ WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   ~ See the License for the specific language governing permissions and limitations under the License.
 */

package org.hrodberaht.inject.internal.annotation;

import org.hrodberaht.inject.SimpleInjection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Simple Java Utils
 *
 * @author Robert Alexandersson
 *         2010-maj-28 20:26:43
 * @version 1.0
 * @since 1.0
 */
public class AnnotationInjection {

    
    private InjectionCacheHandler injectionCacheHandler = null;
    private SimpleInjection container;
    

    public AnnotationInjection(List<InjectionMetaData> injectionMetaDataCache, SimpleInjection container) {
        injectionCacheHandler = new InjectionCacheHandler(injectionMetaDataCache);
        this.container = container;
    }


    public Object createInstance(Class service) {
        InjectionMetaData injectionMetaData =
                findInjectionData(service, null, InjectionUtils.isProvider(service));

        return callConstructor(injectionMetaData);
    }

    @SuppressWarnings(value = "unchecked")
    private Object createInstance(InjectionMetaData metaData) {
        return createInstance(metaData.getServiceClass(), metaData.getQualifierName());
    }

    private Object createInstance(Class<Object> service, String qualifier) {
        InjectionMetaData injectionMetaData =
                findInjectionData(service, qualifier, InjectionUtils.isProvider(service));

        return callConstructor(injectionMetaData);
    }

    private Object callConstructor(InjectionMetaData injectionMetaData) {

        List<InjectionMetaData> dependencies = injectionMetaData.getConstructorDependencies();
        Object[] servicesForConstructor = new Object[dependencies.size()];

        for (int i = 0; i < dependencies.size(); i++) {
            InjectionMetaData dependency = dependencies.get(i);
            Object bean = innerCreateInstance(dependency);
            servicesForConstructor[i] = bean;
        }
        Object service = injectionMetaData.createInstance(servicesForConstructor);
        autoWireBean(service, injectionMetaData);        
        return service;
    }

    /**
     * Uses the injection points to create instances for all services intended.
     * @param service
     * @param injectionMetaData
     */
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

    private Object innerCreateInstance(InjectionMetaData dependency) {
        if (dependency.isProvider()) {
            InjectionMetaData injectionMetaData = findInjectionData(dependency, false);
            return new InjectionProvider(container, injectionMetaData.getServiceClass(), injectionMetaData.getQualifierName());
        }
        return createInstance(dependency);
    }

    public InjectionMetaData createInjectionMetaData(Class service, String qualifier, boolean provider) {
        InjectionMetaData injectionMetaData = new InjectionMetaData(service, qualifier, provider);       
        Constructor constructor = InjectionUtils.findConstructor(service);
        injectionMetaData.setConstructor(constructor);
        injectionMetaData.setScopeHandler(InjectionUtils.getScopeHandler(injectionMetaData.getServiceClass()));
        injectionMetaData.setPreDefined(true);
        return injectionMetaData;
    }

    @SuppressWarnings(value = "unchecked")
    private InjectionMetaData findInjectionData(InjectionMetaData metaData, boolean provider) {
        return findInjectionData(metaData.getServiceClass(), metaData.getQualifierName(), provider);           
    }

    public InjectionMetaData findInjectionData(Class service, String qualifier, boolean provider) {
        InjectionMetaData cachedInjectionMetaData = injectionCacheHandler.find(
                new InjectionMetaData(service, qualifier, provider));
        if(cachedInjectionMetaData != null){
            if(cachedInjectionMetaData.isPreDefined()){
                resolvePredefinedService(cachedInjectionMetaData);
            }
            return cachedInjectionMetaData;
        }

        InjectionMetaData injectionMetaData = new InjectionMetaData(service, qualifier, provider);
        injectionMetaData.setScopeHandler(InjectionUtils.getScopeHandler(injectionMetaData.getServiceClass()));
        injectionCacheHandler.put(injectionMetaData);
        Constructor constructor = InjectionUtils.findConstructor(service);
        injectionMetaData.setConstructor(constructor);        
        resolveService(injectionMetaData);
        return injectionMetaData;
    }

    private void resolveService(InjectionMetaData injectionMetaData) {
        injectionMetaData.setConstructorDependencies(findDependencies(injectionMetaData.getConstructor()));
        injectionMetaData.setInjectionPoints(
                findAllInjectionPoints(injectionMetaData.getServiceClass())
        );
    }

    private void resolvePredefinedService(InjectionMetaData cachedInjectionMetaData) {
        resolveService(cachedInjectionMetaData);
        cachedInjectionMetaData.setPreDefined(false);
    }

    private List<InjectionPoint> findAllInjectionPoints(Class service){
        List<InjectionPoint> injectionPoints = new ArrayList<InjectionPoint>();
        List<Method> allMethods = ReflectionUtils.findMethods(service);
        List<Member> allMembers = ReflectionUtils.findMembers(service);
        for (Member member : allMembers) {
            if (member instanceof Field) {
                Field field = (Field) member;
                if (fieldNeedsInjection(field)) {
                    injectionPoints.add(new InjectionPoint(field, this));
                }
            }

            else if (member instanceof Method) {
                Method method = (Method) member;
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
        return !ReflectionUtils.isStatic(method) && method.isAnnotationPresent(InjectionUtils.INJECT);
    }

    private boolean fieldNeedsInjection(Field field) {
        return !ReflectionUtils.isStatic(field)
             && !ReflectionUtils.isFinal(field)
             && field.isAnnotationPresent(InjectionUtils.INJECT);
    }

    private List<InjectionMetaData> findDependencies(Constructor constructor) {
        Class[] parameterTypes = constructor.getParameterTypes();
        Type[] genericParameterTypes = constructor.getGenericParameterTypes();
        Annotation[][] parameterAnnotations = constructor.getParameterAnnotations();
        return InjectionUtils.findDependencies(parameterTypes, genericParameterTypes, parameterAnnotations, this);
    }


}
