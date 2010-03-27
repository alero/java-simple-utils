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

package test.org.alex.util;

import org.alex.exception.MessageRuntimeException;
import org.alex.util.DateUtil;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

/**
 * Simple Java Utils
 *
 * @author Robert Alexandersson
 * @version 1.0
 * @since 1.0
 */
public class TestDateUtil {

    @BeforeClass
    public static void initStatic(){
        setTestLocale();
    }

    @Before
    public void init(){
        DateUtil.setLocale(getTestLocale());
    }

    private static Locale getTestLocale(){
        return new Locale("sv","SE");
    }

    private static void setTestLocale() {
        System.setProperty("localeprovide.locale", "sv_SE");
    }


    @Test
    public void testDateEquals(){
        Date aDate = testDate("2010-01-01");
        Timestamp aTimestamp = new Timestamp(aDate.getTime());
        assertTrue( DateUtil.equals(aDate, aTimestamp) );
    }

    @Test
    public void testDateParseSimple(){
        Date aDate = DateUtil.parseSimpleDate("2010-01-01");
        assertEquals(testDate("2010-01-01"), aDate);
    }

    @Test
    public void testDateFormatDefault(){
        DateUtil.setLocale(new Locale("en","US"));
        Date aDate = DateUtil.parseSimpleDate("01/01/2010");
        assertEquals(testDate("2010-01-01"), aDate);
    }

    @Test (expected = MessageRuntimeException.class)
    public void testDateFormatDefaultFail(){
        DateUtil.setLocale(new Locale("en","US"));
        Date aDate = DateUtil.parseSimpleDate("2010-01-01");
        assertEquals(testDate("2010-01-01"), aDate);
    }

    @Test
    public void testDateRollDays(){
        Date aDate = DateUtil.rollDays(testDate("2010-01-01"), 1);
        assertEquals(testDate("2010-01-02"), aDate);
    }

    @Test
    public void testDateRollMonth(){
        Date aDate = DateUtil.rollMonth(testDate("2010-01-01"), 1);
        assertEquals(testDate("2010-02-01"), aDate);
    }

    @Test
    public void testDateParseDateWithPattern(){
        Date aDate = DateUtil.parseDate("2010-01-01", "yyyy-MM-dd");
        assertEquals(testDate("2010-01-01"), aDate);
    }

    @Test
    public void testDateFormat(){
        String aDate = DateUtil.formatDate(testDate("2010-01-01"));
        assertEquals("2010-01-01", aDate);
    }

    @Test
    public void testDateFormatCustom(){
        String aDate = DateUtil.formatDate(testDate("2010-01-01"), "yy-MM-dd");
        assertEquals("10-01-01", aDate);
    }

    @Test
    public void testDateFormatTimeZone(){
        TimeZone zone = DateFormat.getDateInstance(DateFormat.SHORT, getTestLocale()).getTimeZone();

        String vinterTime = DateUtil.formatTimeZone(zone, testDateTime("2010-01-01 12:00:00"));
        assertEquals("+0100", vinterTime);

        String summerTime = DateUtil.formatTimeZone(zone, testDateTime("2010-06-01 12:00:00"));
        assertEquals("+0200", summerTime);
    }

    @Test
    public void testDateFormattedDateEquals(){
        Date date1 = testDateTime("2010-01-01 12:00:00");
        Date date2 = testDateTime("2010-01-01 13:00:00");
        assertTrue(DateUtil.formattedDateEquals(date1, date2, "yyyy-MM-dd"));
    }

    @Test
    public void testDateIsBetween(){
        Date date1 = testDateTime("2010-01-01 12:00:00");
        Date date2 = testDateTime("2010-01-01 13:00:00");
        Date date3 = testDateTime("2010-01-01 14:00:00");
        assertTrue(DateUtil.isBetween(date2, date1, date3));

    }


    private Date testDate(String aDate){
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(aDate);
        } catch (ParseException e) {
            throw MessageRuntimeException.createError("Could not format date {0}").args(aDate);
        }
    }

    private Date testDateTime(String aDate){
        try {
            return new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(aDate);
        } catch (ParseException e) {
            throw MessageRuntimeException.createError("Could not format datetime {0}").args(aDate);
        }
    }

}
