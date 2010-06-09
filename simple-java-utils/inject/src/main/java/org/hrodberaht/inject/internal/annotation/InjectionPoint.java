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

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Simple Java Utils
 *
 * @author Robert Alexandersson
 *         2010-maj-29 00:52:31
 * @version 1.0
 * @since 1.0
 */
public class InjectionPoint {
    private enum InjectionPointType{ METHOD, FIELD }

    private List<InjectionMetaData> dependencies;
    private InjectionPointType type;
    private Field field;
    private Method method;

    public InjectionPoint(Field field, AnnotationInjection annotationInjection) {
        type = InjectionPointType.FIELD;
        this.field = field;
        dependencies = new ArrayList<InjectionMetaData>(1);
        dependencies.add(findDependency(field, annotationInjection));
    }

    public InjectionPoint(Method method, AnnotationInjection annotationInjection) {
        type = InjectionPointType.METHOD;
        this.method = method;
        dependencies = findDependencies(method, annotationInjection);
    }


    public List<InjectionMetaData> getDependencies() {
        return dependencies;
    }

    public void inject(Object service, Object[] serviceDependencies) {
        if(type == InjectionPointType.FIELD){
            invokeField(service, serviceDependencies[0]);
        }else{
            invokeMethod(service, serviceDependencies); 
        }

    }

    private void invokeMethod(Object service, Object[] serviceDependency) {
        final boolean originalAccessible = method.isAccessible();
        method.setAccessible(true);

        try {
            method.invoke(service, serviceDependency);
        }

        catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }

        finally {
            method.setAccessible(originalAccessible);
        }
    }

    private void invokeField(Object service, Object serviceDependency) {
        final boolean originalAccessible = field.isAccessible();
        field.setAccessible(true);

        try {
            field.set(service, serviceDependency);
        }

        catch (final IllegalAccessException e) {
            throw new InjectRuntimeException(e);
        }

        finally {
            field.setAccessible(originalAccessible);
        }
    }

    private List<InjectionMetaData> findDependencies(final Method method, AnnotationInjection annotationInjection) {
        final Class<?>[] parameterTypes = method.getParameterTypes();
        final Type[] genericParameterTypes = method.getGenericParameterTypes();
        final Annotation[][] parameterAnnotations = method.getParameterAnnotations();

        return InjectionUtils.findDependencies(
                parameterTypes, genericParameterTypes, parameterAnnotations, annotationInjection
        );
    }

    private InjectionMetaData findDependency(final Field field, AnnotationInjection annotationInjection) {
        Class fieldBeanClass = field.getType();


        if (InjectionUtils.isProvider(fieldBeanClass)) {
            final Type genericType = field.getGenericType();
            final Class beanClassFromProvider = InjectionUtils.getClassFromProvider(field, genericType);
            InjectionKey key = AnnotationQualifierUtil.getQualifierKey(beanClassFromProvider, field.getAnnotations());
            if(key != null){
                Class serviceImpl = annotationInjection.findServiceClass(key);
                return annotationInjection.findInjectionData(serviceImpl, key, true);
            }else{
                return annotationInjection.findInjectionData(beanClassFromProvider, key, true);    
            }

        }

        else {
            InjectionKey qualifier = AnnotationQualifierUtil.getQualifierKey(fieldBeanClass, field.getAnnotations());
            return annotationInjection.findInjectionData(fieldBeanClass, qualifier, false);
        }
    }
}
