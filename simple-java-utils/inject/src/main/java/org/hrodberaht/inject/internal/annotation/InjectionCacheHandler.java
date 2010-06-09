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

import java.util.Iterator;
import java.util.List;

/**
 * Simple Java Utils - Container
 *
 * @author Robert Alexandersson
 *         2010-maj-29 15:25:40
 * @version 1.0
 * @since 1.0
 */
public class InjectionCacheHandler {


    private List<InjectionMetaData> injectionMetaDataCache = null;

    public InjectionCacheHandler(List<InjectionMetaData> injectionMetaDataCache) {
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

}
