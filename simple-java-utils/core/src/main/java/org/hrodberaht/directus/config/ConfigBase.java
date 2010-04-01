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

import org.hrodberaht.directus.exception.MessageRuntimeException;
import org.hrodberaht.directus.logging.SimpleLogger;
import org.hrodberaht.directus.util.NumberUtil;
import org.hrodberaht.directus.util.SocketCloser;
import org.hrodberaht.directus.util.StringUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Simple Java Utilts - Config
 *
 * @author Robert Alexandersson
 * @version 1.0
 * @since 1.0
 */
public abstract class ConfigBase {

    private static final long RELOAD_INTERVAL = 15000;
    private static long TIME_STAMP;
    private static boolean reloadEnabled = false;

    private SimpleLogger LOGGER = null;
    private String propertyPath = null;
    private String customPropertyPath = null;

    private Map<ConfigItem, ConfigItem> configurations = new HashMap<ConfigItem, ConfigItem>();

    private Properties properties = null;

    static{
        String value = System.getProperty("config.reload.enable");
        if("true".equals(value)){
            reloadEnabled = true;                
        }
    }

    protected void configure(Class clazz, String property) {
        configure(clazz, property, null);
    }

    protected void configure(Class clazz, String property, String customProperty) {
        LOGGER = SimpleLogger.getInstance(clazz);
        propertyPath = property;
        customPropertyPath = customProperty;
        try {
            ConfigBase config = (ConfigBase) clazz.newInstance();
            if(config.requiresValidation()){
                config.validate();                
            }
            ConfigRegister.registerConfig(config, property);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected void loadProperties() throws ParseException {
        long elapsedTime = System.currentTimeMillis() - TIME_STAMP;
        if (properties == null) {
            reloadProperties();
            logProperties();
        } else if (reloadEnabled && (TIME_STAMP == 0 || (elapsedTime > RELOAD_INTERVAL))) {
            reloadProperties();
            TIME_STAMP = System.currentTimeMillis();
        }
    }

    private void reloadProperties() throws ParseException {
        Properties origProperties = new Properties();
        Properties customProperties = new Properties();
        loadProperties(origProperties, propertyPath);
        loadProperties(customProperties, customPropertyPath);
        mergeProperties(origProperties, customProperties);
        populateConfigurationValues();

    }

    private void populateConfigurationValues() throws ParseException {
        for (ConfigItem config : configurations.keySet()) {
            // Makes it possible to override values Using System.setProperty();
            // {@link System#setProperty(String, String)}
            String value = System.getProperty(config.getName());
            if(StringUtil.isBlank(value)){
                value = properties.getProperty(config.getName());
            }

            if (config.getType().isAssignableFrom(Boolean.class)) {
                config.setValue(Boolean.parseBoolean(value));
            } else if (config.getType().isAssignableFrom(Integer.class)) {
                config.setValue(NumberUtil.parseInt(value));
            } else if (config.getType().isAssignableFrom(Long.class)) {
                config.setValue(NumberUtil.parseLong(value));
            } else if (config.getType().isAssignableFrom(String[].class)) {
                config.setValue(value.split(","));
            } else if (config.getType().isAssignableFrom(Date.class)) {
                config.setValue(DateUtil.parseSimpleDate(value));
            } else {
                config.setValue(value);
            }
        }
    }

    private void loadProperties(Properties props, String configPath) {
        InputStream data = null;
        try {

            if (configPath == null) {
                throw new RuntimeException("Property path not defined");
            }

            if (configPath.startsWith("classpath:")) {
                String path = configPath.replaceFirst("classpath:", "");
                data = ConfigBase.class.getResourceAsStream(path);
            } else if (configPath.startsWith("file:")) {
                File file = new File(configPath.replaceFirst("file:", ""));
                data = new FileInputStream(file);
            }
            if (data != null) {
                props.load(data);
            } else {
                throw MessageRuntimeException
                        .createError("Using property path {0} could not find a file")
                        .args(configPath);
            }
        } catch (IOException e) {
            LOGGER.error(e);             
        } finally {
            SocketCloser.close(data);
        }
    }


    private void mergeProperties(Properties origProperties, Properties customProperties) {
        properties = origProperties;
        if (customProperties != null) {
            Enumeration data = customProperties.keys();
            while (data.hasMoreElements()) {
                String key = (String) data.nextElement();
                String value = customProperties.getProperty(key);
                properties.remove(key);
                properties.put(key, value);                
            }
        }
    }

    private void logProperties() {
        LOGGER.info("Loggging properties ...");
        Enumeration keys = properties.keys();
        while (keys.hasMoreElements()) {
            String key = (String) keys.nextElement();            
            String value = System.getProperty(key);
            if(StringUtil.isBlank(value)){
                value = (String) properties.get(key);
            }
            LOGGER.info("Property key: {0}, value: {1}", key, value);
        }
        LOGGER.info("All properties logged");
    }


    private static class DateUtil {
        public static Date parseSimpleDate(String value) throws ParseException {
            if(value == null){
                return null;
            }
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            return dateFormat.parse(value);
        }
    }

    public void initiate() throws IllegalAccessException {        
        configurations.clear();
        checkForFields(this.getClass());
        Class[] interfaceses = this.getClass().getDeclaredClasses();
        checkInterfaces(interfaceses);
    }

    private void checkInterfaces(Class[] interfaceses) throws IllegalAccessException {
        for(Class ainterface:interfaceses){
            if(ainterface.isInterface()){
                checkForFields(ainterface);
                // Recursive
                Class[] innerinterfaceses = ainterface.getInterfaces();
                checkInterfaces(innerinterfaceses);
            }
        }
    }

    private void checkForFields(Class aClass) throws IllegalAccessException {
        Field[] fields = aClass.getDeclaredFields();
        for(Field field:fields){
            if(field.getType().isAssignableFrom(ConfigItem.class)){
                ConfigItem obj = (ConfigItem) field.get(new ConfigItem());
                configurations.put(obj, obj);
            }
        }
    }

    public void setPropertyPath(String path) {
        this.propertyPath = path;
    }

    public void setCustomPropertyPath(String path) {
        this.customPropertyPath = path;
    }

    public boolean requiresValidation() {
        return false;
    }

    public boolean validate() {
        return false;
    }
}
