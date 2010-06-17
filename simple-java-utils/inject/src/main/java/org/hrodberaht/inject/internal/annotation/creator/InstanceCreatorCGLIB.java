package org.hrodberaht.inject.internal.annotation.creator;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * Simple Java Utils - Container
 *
 * There are no imports to net.sf.cglib, all usage is direct and this means that this class can be loaded without cglib present. 
 *
 * @author Robert Alexandersson
 *         2010-jun-05 23:25:22
 * @version 1.0
 * @since 1.0
 */
public class InstanceCreatorCGLIB implements InstanceCreator{

    private static final Map<Constructor, net.sf.cglib.reflect.FastConstructor> 
            cachedConstructs = new HashMap<Constructor, net.sf.cglib.reflect.FastConstructor>();

    public Object createInstance(Constructor constructor, Object... parameters) {
        net.sf.cglib.reflect.FastConstructor
                fastConstructor = findFastCreatorInstance(constructor);
        try {
            return fastConstructor.newInstance(parameters);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }


    }

    private static net.sf.cglib.reflect.FastConstructor findFastCreatorInstance(Constructor constructor) {
        if(!cachedConstructs.containsKey(constructor)){
            Class classToConstruct = constructor.getDeclaringClass();
            final net.sf.cglib.reflect.FastConstructor fastConstructor
                    = newFastClass(classToConstruct)
                    .getConstructor(constructor);
            cachedConstructs.put(constructor, fastConstructor);
            return fastConstructor;
        }
        return cachedConstructs.get(constructor);
    }

    // use fully-qualified names so imports don't need preprocessor statements
    private static final net.sf.cglib.core.NamingPolicy NAMING_POLICY
            = new net.sf.cglib.core.DefaultNamingPolicy() {
        @Override
        protected String getTag() {
            return "CreatedBySimpleInjection";
        }
    };

    private static net.sf.cglib.reflect.FastClass newFastClass(Class type) {
        net.sf.cglib.reflect.FastClass.Generator generator
                = new net.sf.cglib.reflect.FastClass.Generator();
        generator.setType(type);
        generator.setClassLoader(getClassLoader(type));
        generator.setNamingPolicy(NAMING_POLICY);
        return generator.create();
    }

    private static ClassLoader getClassLoader(Class<?> type) {
        return type.getClassLoader();
    }

}
