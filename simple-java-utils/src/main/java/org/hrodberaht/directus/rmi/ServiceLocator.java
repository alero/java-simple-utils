/*
 * Copyright (c) 2010.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package org.hrodberaht.directus.rmi;

import org.hrodberaht.directus.logging.SimpleLogger;
import org.hrodberaht.directus.util.ReflectUtil;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * Simple Java Utils
 *
 * @author Robert Alexandersson
 * @version 1.0
 * @since 1.0
 */
public class ServiceLocator {

    private ServiceLocator(){        
    }

    private static final SimpleLogger LOGGER = SimpleLogger.getInstance(ServiceLocator.class);

    private static Map<Class, Object> services = new HashMap<Class, Object>();

    public static <T> T getService(Class<T> theServiceClass) {
        try {
            return (T) getEJBService(theServiceClass);
        } catch (Exception e) {
            LOGGER.error(e);            
            throw new RuntimeException(e);            
        }
    }

    private static Object getEJBService(Class theService) {
        Class theHomeClass = null;
        try {
            boolean isEntityBean = false;
            if(theService.getName().endsWith("Home")) {
                theHomeClass = Class.forName(theService.getName());
                isEntityBean = true;
            } else {
                theHomeClass = Class.forName(theService.getName() + "Home");
            }
            LOGGER.info("Creating "+theService.getName());
            Object theHome = null;
            if (services.containsKey(theHomeClass)) {
                LOGGER.info("Cached Home Creating {0}", theService.getName());
                theHome = services.get(theHomeClass);
            } else {
                LOGGER.debug("New Home Creating {0}", theService.getName());
                String JNDI_NAME = getStaticValue("JNDI_NAME", theHomeClass);
                theHome = RMILookup.findHome(theHomeClass, JNDI_NAME);
                services.put(theHomeClass, theHome);
            }

            if (isEntityBean) {
                return theHome;
            } else {
                LOGGER.info("Home Creating {0}", theService.getName());

                try {
                    return callMethod(theHome, "create");
                } catch (Throwable e) {
                    LOGGER.error(e);
                    e.printStackTrace(System.out);
                    throw e;
                }
            }
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static Object callMethod(Object obj, String method)
            throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        return ReflectUtil.getInstance().invokeMethod(obj, method);
    }

    private static String getStaticValue(String attriute, Class theClass)
            throws IllegalAccessException, NoSuchFieldException {
        Field field = theClass.getField(attriute);
        return String.valueOf(field.get(null));

    }
}

