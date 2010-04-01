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

package org.hrodberaht.directus.config;

/**
 * Simple Java Utilts - Config
 *
 * @author Robert Alexandersson
 * @version 1.0
 * @since 1.0
 */
public class ConfigItem<T> {

    private String name;
    private T value;
    private Class clazz;

    protected ConfigItem() {        
    }

    public ConfigItem(Class clazz, String name) {
        this.name = name;
        this.clazz = clazz;
    }

    public String getName() {
        return name;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }


    public Class getType() {
        return clazz;
    }
}
