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
import org.hrodberaht.inject.internal.InjectionKey;
import org.hrodberaht.inject.internal.annotation.creator.InstanceCreator;
import org.hrodberaht.inject.internal.annotation.scope.ObjectAndScope;
import org.hrodberaht.inject.internal.exception.DuplicateRegistrationException;
import org.hrodberaht.inject.internal.stats.Statistics;
import org.hrodberaht.inject.register.VariableInjectionFactory;
import org.hrodberaht.inject.spi.InjectionPointFinder;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * Simple Java Utils - Container
 *
 * @author Robert Alexandersson
 *         2010-maj-28 20:26:43
 * @version 1.0
 * @since 1.0
 */
public class AnnotationInjection {


    private InjectionCacheHandler injectionCacheHandler;
    private AnnotationInjectionContainer injectionContainer;
    private SimpleInjection container;
    private InjectionFinder injectionFinder = InjectionPointFinder.getInjectionFinder();
    private InstanceCreator instanceCreator = InstanceCreatorFactory.getInstance();

    public AnnotationInjection(Map<InjectionKey, InjectionMetaData> injectionMetaDataCache
            , SimpleInjection container
            , AnnotationInjectionContainer injectionContainer) {
        injectionCacheHandler = new InjectionCacheHandler(injectionMetaDataCache);
        this.container = container;
        this.injectionContainer = injectionContainer;
        InjectionFinder injectionFinderFromContainer = injectionContainer.getInjectionFinder();
        if(injectionFinderFromContainer != null){
            this.injectionFinder = injectionFinderFromContainer;
        }
        InstanceCreator instanceCreatorFromContainer = injectionContainer.getInstanceCreator();
        if(instanceCreatorFromContainer != null){
            this.instanceCreator = instanceCreatorFromContainer;
        }
    }

    /**
     * Creates an instance for a service, uses {@link #findInjectionData(Class, InjectionKey)}
     *
     * @param serviceDefinition
     * @param key
     * @return a created object according to its bound scope {@link javax.inject.Scope}
     */
    public Object createInstance(Class<Object> serviceDefinition, InjectionKey key) {
        InjectionMetaData injectionMetaData =
                findInjectionData(serviceDefinition, key);
        return callConstructor(injectionMetaData);
    }

    public Object createInstance(Class serviceDefinition, InjectionKey key, Object variable) {
        InjectionMetaData variableInjectionMetaData = findInjectionData(serviceDefinition, key);
        Class serviceClass = variableInjectionMetaData.createVariableInstance(variable);        
        InjectionMetaData injectionMetaData = findInjectionData(serviceClass, new InjectionKey(serviceClass, false));
        return callConstructor(injectionMetaData);
    }

    /**
     * Will create a predefined service for later resolve.
     * Sets the predefined attribute in the InjectionMetaData to true
     *
     * @param service
     * @param key
     * @return a predefined services, not cached.
     */
    public InjectionMetaData createInjectionMetaData(Class service, InjectionKey key) {
        InjectionMetaData injectionMetaData = new InjectionMetaData(service, key, instanceCreator);
        if(service != null){
            Constructor constructor = InjectionUtils.findConstructor(service);
            injectionMetaData.setConstructor(constructor);
            injectionMetaData.setScopeHandler(InjectionUtils.getScopeHandler(injectionMetaData.getServiceClass()));
        }
        injectionMetaData.setPreDefined(true);
        injectionCacheHandler.put(injectionMetaData);
        return injectionMetaData;
    }

    /**
     * Checks the registry for the service class using a key, not null-safe.
     *
     * @param key the key to find a service class for
     * @return the found service class, null not accepted
     */
    public Class findServiceClass(InjectionKey key) {
        return this.injectionContainer.findService(key);
    }

    /**
     * Checks the registry for the service class using a key, not null-safe.
     *
     * @param key the key to find a service class for
     * @return the found service class, null not accepted
     */
    public Class findServiceClassAndRegister(InjectionKey key) {
        try {
            return this.injectionContainer.findServiceRegister(key.getServiceDefinition(), key).getService();
        } catch (DuplicateRegistrationException e) {
            return this.injectionContainer.findService(key);       
        }
    }

    /**
     * Search for injectiondata in the cache, if not found creates new injection data from scratch.
     *
     * @param service the service implementation to create
     * @param key     the injection key (named or annotated service definition)
     * @return a cached injection meta data, is not protected from manipulation
     */
    public InjectionMetaData findInjectionData(Class service, InjectionKey key) {
        InjectionMetaData cachedInjectionMetaData = injectionCacheHandler.find(
                new InjectionMetaData(service, key, instanceCreator));
        if (cachedInjectionMetaData != null) {
            if (cachedInjectionMetaData.isPreDefined()) {
                if(!cachedInjectionMetaData.getKey().isProvider()){
                    resolvePredefinedService(cachedInjectionMetaData);
                }                
            }
            return cachedInjectionMetaData;
        }

        InjectionMetaData injectionMetaData = new InjectionMetaData(service, key, instanceCreator);
        injectionMetaData.setScopeHandler(InjectionUtils.getScopeHandler(injectionMetaData.getServiceClass()));
        injectionCacheHandler.put(injectionMetaData);
        if (!injectionMetaData.getKey().isProvider()) {
            Constructor constructor = InjectionUtils.findConstructor(service);
            injectionMetaData.setConstructor(constructor);
            resolveService(injectionMetaData);
        }
        return injectionMetaData;
    }

    /**
     * @param dependency
     * @return
     */
    private Object innerCreateInstance(InjectionMetaData dependency) {
        if (dependency.getKey().isProvider()) {
            if(VariableInjectionFactory.SERVICE_NAME.equals(dependency.getKey().getName())){
                return new VariableInjectionProvider(container, dependency.getKey());                                
            }
            return new InjectionProvider(container, dependency.getKey());
        }
        return createInstance(dependency);
    }

    /**
     * Resolves/finds all injection needs (constructors and members)
     *
     * @param injectionMetaData the service ready for resolving
     */
    private void resolveService(InjectionMetaData injectionMetaData) {
        injectionMetaData.setConstructorDependencies(findDependencies(injectionMetaData.getConstructor()));
        injectionMetaData.setInjectionPoints(
                injectionFinder.findInjectionPoints(injectionMetaData.getServiceClass(), this)
        );
        injectionMetaData.setPostConstructMethod(
                injectionFinder.findPostConstruct(injectionMetaData.getServiceClass())
        );
    }

    /**
     * Will resolve the service and set the predefined to false
     *
     * @param cachedInjectionMetaData a predefined service
     */
    private void resolvePredefinedService(InjectionMetaData cachedInjectionMetaData) {
        resolveService(cachedInjectionMetaData);
        cachedInjectionMetaData.setPreDefined(false);
    }


    public void injectDependencies(Object service) {
        InjectionMetaData injectionMetaData =
                findInjectionData(service.getClass(),
                        new InjectionKey(service.getClass(), false)
                );
        injectFromInjectionPoints(service, injectionMetaData);
    }

    private void injectFromInjectionPoints(Object service, InjectionMetaData injectionMetaData) {
        List<InjectionPoint> injectionPoints = injectionMetaData.getInjectionPoints();
        for (InjectionPoint injectionPoint : injectionPoints) {
            List<InjectionMetaData> dependencies = injectionPoint.getDependencies();
            Object[] serviceDependencies = new Object[dependencies.size()];
            int i = 0;
            for (InjectionMetaData dependence : dependencies) {
                Object serviceDependence = innerCreateInstance(dependence);
                serviceDependencies[i] = serviceDependence;
                i++;
            }
            injectionPoint.inject(service, serviceDependencies);
        }
    }

    /**
     * Uses the injection points to create instances for all services intended.
     *
     * @param service
     * @param injectionMetaData
     */
    private void autoWireBean(Object service, InjectionMetaData injectionMetaData) {
        injectFromInjectionPoints(service, injectionMetaData);
    }


    private List<InjectionMetaData> findDependencies(Constructor constructor) {
        if(constructor == null){
            return null;
        }
        Class[] parameterTypes = constructor.getParameterTypes();
        Type[] genericParameterTypes = constructor.getGenericParameterTypes();
        Annotation[][] parameterAnnotations = constructor.getParameterAnnotations();
        return InjectionUtils.findDependencies(parameterTypes, genericParameterTypes, parameterAnnotations, this);
    }


    private Object createInstance(InjectionMetaData metaData) {
        return createInstance(metaData.getServiceClass(), metaData.getKey());
    }


    private Object callConstructor(InjectionMetaData injectionMetaData) {

        List<InjectionMetaData> dependencies = injectionMetaData.getConstructorDependencies();
        if(dependencies == null){ // no constructor was able to be defined, hopefully a scoped one is provided.
            ObjectAndScope service = injectionMetaData.createInstance();
            if(service.isInNeedOfInitialization()){
                return autowireAndPostConstruct(injectionMetaData, service.getInstance());
            }
            return service.getInstance();
        }
        Object[] servicesForConstructor = new Object[dependencies.size()];
        for (int i = 0; i < dependencies.size(); i++) {
            InjectionMetaData dependency = dependencies.get(i);
            Object bean = innerCreateInstance(dependency);
            servicesForConstructor[i] = bean;
            Statistics.addInjectConstructorCount();
        }
        ObjectAndScope service = injectionMetaData.createInstance(servicesForConstructor);
        if(service.isInNeedOfInitialization()){
            return autowireAndPostConstruct(injectionMetaData, service.getInstance());
        }
        return service.getInstance();
    }

    private Object autowireAndPostConstruct(InjectionMetaData injectionMetaData, Object service) {
        autoWireBean(service, injectionMetaData);
        // Performance wise this is a bit slow, look into caching the extended injection info injectionMetaData
        // TODO: for release 1.3 performance enhancements take a look at this
        injectionFinder.extendedInjection(service);
        Method postConstruct = injectionMetaData.getPostConstruct();
        if (postConstruct != null) {
            ReflectionUtils.invoke(postConstruct, service);
        }
        return service;
    }


    public void injectExtendedDependencies(Object service) {
        /*InjectionMetaData injectionMetaData =
                findInjectionData(service.getClass(),
                        new InjectionKey(service.getClass(), false)
                );
        List<InjectionPoint> injectionPoints = injectionMetaData.getInjectionPoints();
        for (InjectionPoint injectionPoint : injectionPoints) {
            List<InjectionMetaData> dependencies = injectionPoint.getDependencies();
            Object[] serviceDependencies = new Object[dependencies.size()];
            int i = 0;
            // for (InjectionMetaData dependence : dependencies) {
                // Object serviceDependence = innerCreateInstance(dependence);
                //  serviceDependencies[i] = serviceDependence;
                //i++;
            //}
            injectionFinder.extendedInjection(service); // inject services that are pre-created
            injectionPoint.inject(service, serviceDependencies);
        } */
    }
}
