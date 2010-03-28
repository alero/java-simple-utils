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

import org.hrodberaht.directus.logging.SimpleLogger;
import org.hrodberaht.directus.util.NumberUtil;
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

    private SimpleLogger LOGGER = SimpleLogger.getInstance(this.getClass());
    private String propertyPath = null;
    private String customPropertyPath = null;

    private Map<ConfigItem, ConfigItem> configurations = new HashMap();

    private static final long RELOAD = 15000;
    private static long timestamp;

    private Properties origproperties = null;
    private Properties cproperties = null;
    private Properties properties = null;

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
            MasterConfig.registerConfig(config, property);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected void loadProperties() throws ParseException {
        long elapsedTime = System.currentTimeMillis() - timestamp;
        if (origproperties == null) {
            reloadProperties();
            logProperties();
        } else if ((timestamp == 0 || (elapsedTime > RELOAD))) {
            reloadProperties();
            timestamp = System.currentTimeMillis();
        }
    }

    private void reloadProperties() throws ParseException {
        origproperties = new Properties();
        cproperties = new Properties();
        loadProperties(origproperties, propertyPath);
        loadProperties(cproperties, customPropertyPath);
        mergeProperties();
        setValues();

    }

    private void setValues() throws ParseException {
        for (ConfigItem conf : configurations.keySet()) {
            String value = System.getProperty(conf.getName());
            if(StringUtil.isBlank(value)){
                value = properties.getProperty(conf.getName());                
            }
            if (conf.type().isAssignableFrom(Boolean.class)) {
                conf.setValue(Boolean.parseBoolean(value));
            } else if (conf.type().isAssignableFrom(Integer.class)) {
                conf.setValue(NumberUtil.parseInt(value));
            } else if (conf.type().isAssignableFrom(Long.class)) {
                conf.setValue(NumberUtil.parseLong(value));
            } else if (conf.type().isAssignableFrom(String[].class)) {
                conf.setValue(value.split(","));
            } else if (conf.type().isAssignableFrom(Date.class)) {
                conf.setValue(DateUtil.parseSimpleDate(value));
            } else {
                conf.setValue(value);
            }
            // conf.setValue(value);
        }
    }

    private void loadProperties(Properties props, String stream) {
        InputStream data = null;
        try {

            if (stream == null) {
                LOGGER.info("Property file not defined");
                return;
            }

            if (stream.startsWith("classpath:")) {
                String path = stream.replaceFirst("classpath:", "");
                data = ConfigBase.class.getResourceAsStream(path);
            } else if (stream.startsWith("file:")) {
                File file = new File(stream.replaceFirst("file:", ""));
                data = new FileInputStream(file);
            }
            if (data != null) {
                props.load(data);
            } else {
                LOGGER.info("Property file not found {0}", stream);
            }
        } catch (IOException e) {
            LOGGER.error(e);             
        } finally {
            close(data);
        }
    }


    private void mergeProperties() {
        properties = origproperties;
        if (cproperties != null) {
            Enumeration data = cproperties.keys();
            while (data.hasMoreElements()) {
                String key = (String) data.nextElement();
                String value = cproperties.getProperty(key);                
                properties.put(key, value);
            }
        }
    }

    private void logProperties() {
        LOGGER.info("Loading properties...");
        Enumeration keys = properties.keys();
        while (keys.hasMoreElements()) {
            String key = (String) keys.nextElement();            
            String value = System.getProperty(key);
            if(StringUtil.isBlank(value)){
                value = (String) properties.get(key);
            }
            LOGGER.info("Property key: {0}, value: {1}", key, value);
        }
        LOGGER.info("Properties loaded");
    }

    private static void close(InputStream data) {
        try {
            if (data != null) {
                data.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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
