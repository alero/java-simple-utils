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

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

/**
 * Simple Java Utilts - Config
 *
 * @author Robert Alexandersson
 * @version 1.0
 * @since 1.0
 */
public class ConfigRegister {

    public static enum Strategy {
        INHERIT, REPLACE
    }

    private Strategy customConfigurationStrategy;
    private Map<Class, ConfigBase> providedConfigurations = null;
    private Map<Class, ConfigBase> customConfigurations = null;


    private static ConfigRegister config = null;

    public static void registerConfig(ConfigBase config, String propertyPath, String customPropertyPath) {
        config.setPropertyPath(propertyPath);
        config.setCustomPropertyPath(customPropertyPath);
        if (ConfigRegister.config == null) {
            ConfigRegister.config = new ConfigRegister();
            Map<Class, ConfigBase> configurations = new HashMap<Class, ConfigBase>();
            ConfigRegister.config.setProvidedConfigurations(configurations);
            // TODO: make this useable
            Map<Class, ConfigBase> customConfigurations = new HashMap<Class, ConfigBase>();
            ConfigRegister.config.setCustomConfigurations(customConfigurations); // from local file
            ConfigRegister.config.setCustomConfigurationStrategy(ConfigRegister.Strategy.INHERIT);
        }

        ConfigRegister.config.providedConfigurations.put(config.getClass(), config);

        ConfigRegister.config.load();
    }

    public static void registerConfig(ConfigBase config, String property) {
        registerConfig(config, property, null);    
    }

    protected void load() {
        initiate(providedConfigurations);
        initiate(customConfigurations);        
    }

    private void initiate(Map<Class, ConfigBase> configuration) {
        if (configuration == null) {
            return;
        }
        for (ConfigBase config : configuration.values()) {
            try {
                config.initiate();
                config.loadProperties();
            } catch (IllegalAccessException e) {
                throw new ConfigException(e);
            } catch (ParseException e) {
                throw new ConfigException(e);
            }

        }
    }

    public void setProvidedConfigurations(Map<Class, ConfigBase> providedConfigurations) {
        this.providedConfigurations = providedConfigurations;
    }

    public Map<Class, ConfigBase> getCustomConfigurations() {
        return customConfigurations;
    }

    public void setCustomConfigurations(Map<Class, ConfigBase> customConfigurations) {
        this.customConfigurations = customConfigurations;
    }

    public Strategy getCustomConfigurationStrategy() {
        return customConfigurationStrategy;
    }

    public void setCustomConfigurationStrategy(Strategy customConfigurationStrategy) {
        this.customConfigurationStrategy = customConfigurationStrategy;
    }

}
