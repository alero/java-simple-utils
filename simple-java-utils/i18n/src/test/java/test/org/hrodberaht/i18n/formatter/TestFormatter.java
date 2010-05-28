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

package test.org.hrodberaht.i18n.formatter;

import org.hrodberaht.directus.exception.MessageRuntimeException;
import org.hrodberaht.i18n.formatter.DateFormatter;
import org.hrodberaht.i18n.formatter.FormatException;
import org.hrodberaht.i18n.formatter.Formatter;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.assertEquals;

/**
 * Simple Java Utils
 *
 * @author Robert Alexandersson
 * @version 1.0
 * @since 1.0
 */
public class TestFormatter {

    @BeforeClass
    public static void staticInit(){
        System.setProperty("localeprovide.locale", "sv_SE");
    }

    @AfterClass
    public static void staticDestroy(){
        System.clearProperty("localeprovide.locale");
    }

    @Test
    public void simpleDateParseFormat(){
        String testDate = "2010-01-01";
        Formatter<Date> formatter = Formatter.getFormatter(Date.class);
        Date aDate = formatter.convertToObject(testDate);
        String aStringDate = formatter.convertToString(aDate);
        assertEquals(testDate, aStringDate);
    }

    @Test(expected = FormatException.class)
    public void simpleBadDateParseFormat(){
        String testDate = "2010-01-01&";
        Formatter<Date> formatter = Formatter.getFormatter(Date.class);
        Date aDate = formatter.convertToObject(testDate);
        String aStringDate = formatter.convertToString(aDate);
        assertEquals(testDate, aStringDate);
    }

    @Test
    public void simpleDateTimeParse(){
        String testDate = "2010-01-01 00:00";
        Formatter<Date> formatter = Formatter.getFormatter(Date.class);
        Date aDate = formatter.convertToObject(testDate);
        assertEquals(testDateTime("2010-01-01 00:00"), aDate);
    }

    @Test
    public void simpleDateTimeFormat(){
        String testDate = "2010-01-01 00:00";
        Formatter<Date> formatter = Formatter.getFormatter(Date.class, DateFormatter.DateConvert.DateTime);
        String aDate = formatter.convertToString(testDateTime("2010-01-01 00:00"));
        assertEquals(testDate, aDate);
    }

    private Date testDate(String aDate){
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(aDate);
        } catch (ParseException e) {
            throw new MessageRuntimeException("Could not format date {0}", aDate);
        }
    }

    private Date testDateTime(String aDate){
        try {
            return new SimpleDateFormat("yyyy-MM-dd hh:mm").parse(aDate);
        } catch (ParseException e) {
            throw new MessageRuntimeException("Could not format datetime {0}", aDate);
        }
    }
}
