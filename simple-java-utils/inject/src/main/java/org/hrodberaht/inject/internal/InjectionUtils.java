package org.hrodberaht.inject.internal;

import org.hrodberaht.inject.SPIRuntimeException;

import javax.inject.Inject;
import javax.inject.Provider;
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

    public static Class<Object> getClassFromProvider(final Object serviceClass, final Type serviceType) {
        if (serviceType instanceof ParameterizedType) {
            final ParameterizedType parameterizedType = (ParameterizedType) serviceType;
            final Type[] typeArguments = parameterizedType.getActualTypeArguments();
            final Class<Object> beanClassFromProvider = (Class<Object>) typeArguments[0];

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
                throw new SPIRuntimeException(e);
            }
        }

        else if (annotatedConstructors.size() > 1) {
            throw new SPIRuntimeException(
                    "Several annotated constructors found for autowiring " + beanClass + " " + annotatedConstructors);
        }

        return annotatedConstructors.get(0);
    }

    public static boolean constructorNeedsInjection(final Constructor<?> constructor) {
        return constructor.isAnnotationPresent(INJECT);
    }


    public static boolean isProvider(Class<Object> service) {
        return Provider.class.isAssignableFrom(service);
    }
}
