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

package test.org.hrodberaht.config;

import org.hrodberaht.directus.config.ConfigBase;
import org.hrodberaht.directus.config.ConfigItem;
import org.hrodberaht.directus.config.MasterConfig;
import org.hrodberaht.directus.util.StringUtil;

import java.text.ParseException;
import java.util.Date;


/**
 * Simple Java Utils
 *
 * @author Robert Alexandersson
 * @version 1.0
 * @since 1.0
 */
public class AnyApplicationConfig extends ConfigBase {
    private static final String DEFAULT_CONFIG = "classpath:/test/org/hrodberaht/config/basicConfig.properties";

    public static void initConfig() throws ParseException {
        initConfig(DEFAULT_CONFIG);
    }

    public static void initConfig(String resource) throws ParseException {
        String externalConfig = System.getProperty("config.externalfile");
        if(!StringUtil.isBlank(externalConfig)){
            new AnyApplicationConfig(resource, externalConfig);
        }else{
            new AnyApplicationConfig(resource, null);
        }

    }


    public interface ApplicationState {
        ConfigItem<Boolean> A_BOOLEAN = new ConfigItem<Boolean>(Boolean.class, "anyapp.aboolean");
        ConfigItem<String> A_STRING = new ConfigItem<String>(String.class, "anyapp.astring");
        ConfigItem<Date> A_DATE = new ConfigItem<Date>(Date.class, "anyapp.adate");
        ConfigItem<Integer> A_INTEGER = new ConfigItem<Integer>(Integer.class, "anyapp.ainteger");
        ConfigItem<Long> A_LONG = new ConfigItem<Long>(Long.class, "anyapp.along");
    }


    private AnyApplicationConfig(String propertyPath, String customPropertyPath) throws ParseException {
        MasterConfig.registerConfig(this, propertyPath, customPropertyPath);
    }


}