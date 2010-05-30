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

import org.hrodberaht.inject.InjectRuntimeException;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Scope;
import javax.inject.Singleton;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Simple Java Utils
 *
 * @author Robert Alexandersson
 *         2010-maj-29 01:34:21
 * @version 1.0
 * @since 1.0
 */
public class InjectionUtils {
    
    public static final Class<Inject> INJECT = Inject.class;

    private static final Class<Scope> SCOPE = Scope.class;

    public static Class<Object> getClassFromProvider(final Object serviceClass, final Type serviceType) {
        if (serviceType instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) serviceType;
            Type[] typeArguments = parameterizedType.getActualTypeArguments();
            Class<Object> beanClassFromProvider = (Class<Object>) typeArguments[0];
            return beanClassFromProvider;
        }

        throw new IllegalArgumentException("Provider used without generic argument: " + serviceClass);
    }

    public static List<InjectionMetaData> findDependencies(
            Class[] parameterTypes, Type[] genericParameterType, Annotation[][] parameterAnnotation
            , AnnotationInjection annotationInjection
    ) {
        List<InjectionMetaData> injectionMetaData = new ArrayList<InjectionMetaData>(parameterTypes.length);

        for(int i=0;i<parameterTypes.length;i++){
            Class serviceClass = parameterTypes[i];
            Annotation[] annotations = parameterAnnotation[i];
            if (InjectionUtils.isProvider(serviceClass)) {
                Type serviceType = genericParameterType[i];
                Class<Object> beanClassFromProvider = InjectionUtils.getClassFromProvider(serviceClass, serviceType);

                addInjectMetaData(injectionMetaData, beanClassFromProvider, annotations, true, annotationInjection);
            }else{
                addInjectMetaData(injectionMetaData, serviceClass, annotations, false, annotationInjection);
            }


        }
        return injectionMetaData;
    }
    private static void addInjectMetaData(
            List<InjectionMetaData> injectionMetaData,
            Class serviceClass,
            Annotation[] annotations,
            boolean provider,
            AnnotationInjection annotationInjection) {
        injectionMetaData.add(
                annotationInjection.findInjectionData(
                        serviceClass,
                        AnnotationQualifierUtil.getQualifierName(serviceClass, annotations),
                        provider
                )
        );
    }

    public static Constructor findConstructor(final Class<Object> beanClass) {
        Constructor<?>[] declaredConstructors = beanClass.getDeclaredConstructors();
        List<Constructor<?>> annotatedConstructors = new ArrayList<Constructor<?>>();

        for (Constructor<?> constructor : declaredConstructors) {
            if (InjectionUtils.constructorNeedsInjection(constructor)) {
                annotatedConstructors.add(constructor);
            }
        }

        if (annotatedConstructors.size() == 0) {
            try {
                return beanClass.getDeclaredConstructor();
            }

            catch (NoSuchMethodException e) {
                throw new InjectRuntimeException(e);
            }
        }

        else if (annotatedConstructors.size() > 1) {
            throw new InjectRuntimeException(
                    "Several annotated constructors found for autowire {0} {1}", beanClass, annotatedConstructors);
        }

        return annotatedConstructors.get(0);
    }

    public static boolean constructorNeedsInjection(final Constructor<?> constructor) {
        return constructor.isAnnotationPresent(INJECT);
    }


    public static boolean isProvider(Class<Object> service) {
        return Provider.class.isAssignableFrom(service);
    }

    public static boolean isSingleton(final Class<?> beanClass) {
        Annotation scope = getScope(beanClass);
        if (scope instanceof Singleton) {
            return true;
        }
        if (scope == null) {
            return false;
        }
        throw new InjectRuntimeException("Unknown scope on {0} {1}", beanClass , scope);
    }

    private static Annotation getScope(final Class<?> beanClass) {
        List<Annotation> scopeAnnotations = new ArrayList<Annotation>();
        Annotation[] annotations = beanClass.getAnnotations();
        for (Annotation annotation : annotations) {
            if (annotation.annotationType().isAnnotationPresent(SCOPE)) {
                scopeAnnotations.add(annotation);
            }
        }

        if (scopeAnnotations.size() == 0) {
            return null;
        } else if (scopeAnnotations.size() > 1) {
            throw new InjectRuntimeException(
                    "More than one scope annotations found on {0} {1}", beanClass, scopeAnnotations);
        }

        return scopeAnnotations.get(0);
    }
}
