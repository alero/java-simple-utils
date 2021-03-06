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

package test.org.hrodberaht.directus.config;

import org.junit.BeforeClass;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.assertEquals;


/**
 * Simple Java Utils TestSuite
 *
 * @author Robert Alexandersson
 * @version 1.0
 * @since 1.0
 */
public class TestApplicationConfig {

    @BeforeClass
    public static void init() throws ParseException {
        AnyApplicationConfig.initConfig();
    }


    @Test
    public void testConfig() throws ParseException {
        assertEquals(true, AnyApplicationConfig.IS_ENABLED.getValue());
        assertEquals(true, AnyApplicationConfig.A_BOOLEAN.getValue());
        assertEquals("Hello", AnyApplicationConfig.ApplicationState.A_STRING.getValue());
        assertEquals(parseDate("2010-05-05"), AnyApplicationConfig.ApplicationState.A_DATE.getValue());
        assertEquals(new Integer(5), AnyApplicationConfig.ApplicationState.A_INTEGER.getValue());
        assertEquals(new Long(5050505055505000L), AnyApplicationConfig.ApplicationState.A_LONG.getValue());

    }

    @Test
    public void testCustomConfig() throws ParseException {
        /**
         * For information: The way this registration is done using System parameters is in no means mandatory,
         * it is just a simple way to prove how to support the custom configuration. Use any means needed to support this.
         */
        System.setProperty("config.externalfile", "classpath:/test/org/hrodberaht/directus/config/customConfig.properties");
        AnyApplicationConfig.initConfig();

        assertEquals(false, AnyApplicationConfig.A_BOOLEAN.getValue());
        assertEquals("Hello there", AnyApplicationConfig.ApplicationState.A_STRING.getValue());
        assertEquals(parseDate("2010-05-05"), AnyApplicationConfig.ApplicationState.A_DATE.getValue());
        assertEquals(new Integer(6), AnyApplicationConfig.ApplicationState.A_INTEGER.getValue());
        assertEquals(new Long(6060606066606000L), AnyApplicationConfig.ApplicationState.A_LONG.getValue());

    }

    private Date parseDate(String s) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.parse(s);
    }

}
