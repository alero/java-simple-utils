package org.hrodberaht.inject.internal.annotation;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Simple Java Utils
 *
 * @author Robert Alexandersson
 *         2010-maj-29 15:25:40
 * @version 1.0
 * @since 1.0
 */
public class InjectionCacheHandler {


    private Map<InjectionMetaData, Object> serviceCache = null;
    private List<InjectionMetaData> injectionMetaDataCache = null;

    public InjectionCacheHandler(List<InjectionMetaData> injectionMetaDataCache) {
        this.serviceCache = new HashMap<InjectionMetaData, Object>();
        this.injectionMetaDataCache = injectionMetaDataCache;
    }

    public void put(InjectionMetaData injectionMetaData) {
        injectionMetaDataCache.add(injectionMetaData);
    }

    public InjectionMetaData find(InjectionMetaData injectionMetaData) {
        final Iterator<InjectionMetaData> iterator = injectionMetaDataCache.iterator();

        while (iterator.hasNext()) {
            InjectionMetaData singleton = iterator.next();
            if (injectionMetaData.canInject(singleton) && injectionMetaData.isProvider() == singleton.isProvider()) {
                return singleton;
            }
        }
        return null;
    }

    public void put(InjectionMetaData injectionMetaData, Object service) {
        serviceCache.put(injectionMetaData, service);
    }

    public Object findService(InjectionMetaData injectionMetaData) {
        final Iterator<InjectionMetaData> iterator = serviceCache.keySet().iterator();

        while (iterator.hasNext()) {
            final InjectionMetaData singleton = iterator.next();
            if (injectionMetaData.isSingleton() && injectionMetaData == singleton ) {
                return serviceCache.get(injectionMetaData);
            }
        }
        return null;
    }
}
