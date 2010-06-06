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
import org.hrodberaht.inject.internal.annotation.creator.InstanceCreator;
import org.hrodberaht.inject.internal.annotation.creator.InstanceCreatorCGLIB;
import org.hrodberaht.inject.internal.annotation.creator.InstanceCreatorDefault;
import org.hrodberaht.inject.internal.annotation.scope.ScopeHandler;

import java.lang.reflect.Constructor;
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
    private boolean preDefined = false;

    private ScopeHandler scopeHandler;

    private Constructor constructor;
    private List<InjectionMetaData> constructorDependencies;
    private List<InjectionPoint> injectionPoints;

    public InjectionMetaData(Class serviceClass, String qualifierName, boolean provider) {
        this.serviceClass = serviceClass;
        this.qualifierName = qualifierName;
        this.provider = provider;
    }

    

    public void setScopeHandler(ScopeHandler scopeHandler) {
        this.scopeHandler = scopeHandler;
    }

    public void setConstructor(Constructor constructor) {
        this.constructor = constructor;
    }

    public Constructor getConstructor() {
        return constructor;
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
        if(!originalAccessible){
            constructor.setAccessible(true);
        }

        try {
            Object scopedInstance = scopeHandler.getInstance();
            if (scopedInstance != null) {
                return scopedInstance;
            }

            Object newInstance = getInstanceCreator().createInstance(constructor, parameters);
            scopeHandler.addScope(newInstance);            
            return newInstance;
        }
        finally {
            if(!originalAccessible){
                constructor.setAccessible(originalAccessible);
            }
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
            } else if (qualifierName != null && qualifierName.equals(bean.getQualifierName())) {
                return true;
            }
        }

        return false;
    }

    private static InstanceCreator creator = null;
    public InstanceCreator getInstanceCreator() {
        if(creator == null){
            if(System.getProperty("simpleinjection.instancecreator.cglib") != null){
                creator = new InstanceCreatorCGLIB();
            } else {
                creator = new InstanceCreatorDefault();
            }
        }
        return creator;
    }

    public SimpleInjection.Scope getScope() {
        return scopeHandler.getScope();
    }
}
