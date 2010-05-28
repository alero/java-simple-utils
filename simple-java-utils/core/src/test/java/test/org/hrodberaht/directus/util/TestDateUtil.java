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

package test.org.hrodberaht.directus.util;

import org.hrodberaht.directus.exception.MessageRuntimeException;
import org.hrodberaht.directus.tdd.TestUtilDateUtil;
import org.hrodberaht.directus.util.DateUtil;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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
        JavaContainerRegister.registerDefault(ProviderInterface.class, SimpleLocaleProvider.class);

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
    public void testDateEquals_False(){
        Date aDate = testDate("2010-01-01");
        Timestamp aTimestamp = new Timestamp(aDate.getTime()+1);
        assertTrue(! DateUtil.equals(aDate, aTimestamp) );
    }

    @Test
    public void testDateParseSimple(){
        Date aDate = DateUtil.parseSimpleDate("2010-01-01");
        assertEquals(testDate("2010-01-01"), aDate);
    }

    @Test
    public void testDateParseSimple_PatternFail(){

        try {
            Date aDate = DateUtil.parseSimpleDate("10-01-01");
            assertEquals("Dont get here", null);
        } catch (MessageRuntimeException e) {
            assertEquals("Unknown format for 10-01-01 ", e.getMessage());
        }
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

    @Test(expected = RuntimeException.class)
    public void testParseDateError(){
        DateUtil.setLocale(new Locale("en","US"));
        Date aDate = DateUtil.parseDate("01/01/2010", "yyyy-mmm.dd");
        
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
    public void testDateRollHours(){
        Date aDate = DateUtil.rollHours(testDateTime("2010-01-01 14:00:00"), 1);
        assertEquals(testDateTime("2010-01-01 15:00:00"), aDate);
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
    public void testDateTimeFormat(){
        String aDate = DateUtil.formatDateTime(testDateTime("2010-01-01 12:00:00"));
        assertEquals("2010-01-01 12:00:00", aDate);
    }

    @Test
    public void testDateFormatTimeZone(){


        String vinterTime = DateUtil.formatTimeZone(testDateTime("2010-01-01 12:00:00"));
        assertEquals("+0100", vinterTime);

        String summerTime = DateUtil.formatTimeZone(testDateTime("2010-06-01 12:00:00"));
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
        Date compareDate = testDateTime("2010-01-01 13:00:00");
        Date date1 = testDateTime("2010-01-01 12:00:00");
        Date date2 = testDateTime("2010-01-01 14:00:00");
        assertTrue(DateUtil.isBetween(compareDate, date1, date2));
    }

    @Test
    public void testDateIsBetween_SameAsStart(){
        Date compareDate = testDateTime("2010-01-01 12:00:00");
        Date date1 = testDateTime("2010-01-01 12:00:00");
        Date date2 = testDateTime("2010-01-01 14:00:00");
        assertTrue(DateUtil.isBetween(compareDate, date1, date2));
    }

    @Test
    public void testDateIsBetween_SameAsEnd(){
        Date compareDate = testDateTime("2010-01-01 14:00:00");
        Date date1 = testDateTime("2010-01-01 12:00:00");
        Date date2 = testDateTime("2010-01-01 14:00:00");
        assertTrue(DateUtil.isBetween(compareDate, date1, date2));
    }

    @Test
    public void testDateIsLessOrEqual_Less(){
        Date date1 = testDateTime("2010-01-01 12:00:00");
        Date date2 = testDateTime("2010-01-01 13:00:00");
        assertTrue(DateUtil.isLessOrEqual(date1, date2));
    }
    @Test
    public void testDateIsLessOrEqual_Equals(){
        Date date1 = testDateTime("2010-01-01 12:00:00");
        Date date2 = testDateTime("2010-01-01 12:00:00");
        assertTrue(DateUtil.isLessOrEqual(date1, date2));
    }

    @Test
    public void testDateIsLessOrEqual_More(){
        Date date1 = testDateTime("2010-01-01 13:00:00");
        Date date2 = testDateTime("2010-01-01 12:00:00");
        assertTrue(!DateUtil.isLessOrEqual(date1, date2));
    }

    @Test
    public void testDateIsLessOrEqual_Null(){
        try {
            Date date1 = testDateTime("2010-01-01 13:00:00");
            DateUtil.isLessOrEqual(date1, null);
        } catch (IllegalAccessError e) {
            assertEquals("Dates can not be null", e.getMessage());
        }
    }

    @Test
    public void testDateIsMoreOrEqual_More(){
        Date date1 = testDateTime("2010-01-01 13:00:00");
        Date date2 = testDateTime("2010-01-01 12:00:00");
        assertTrue(DateUtil.isMoreOrEqual(date1, date2));
    }
    @Test
    public void testDateIsMoreOrEqual_Equals(){
        Date date1 = testDateTime("2010-01-01 12:00:00");
        Date date2 = testDateTime("2010-01-01 12:00:00");
        assertTrue(DateUtil.isMoreOrEqual(date1, date2));
    }
    @Test
    public void testDateIsMoreOrEqual_Less(){
        Date date1 = testDateTime("2010-01-01 12:00:00");
        Date date2 = testDateTime("2010-01-01 13:00:00");
        assertTrue(!DateUtil.isMoreOrEqual(date1, date2));
    }

    @Test
    public void testDateIsMoreOrEqual_Null(){
        try {
            Date date1 = testDateTime("2010-01-01 13:00:00");
            DateUtil.isMoreOrEqual(date1, null);
        } catch (IllegalAccessError e) {
            assertEquals("Dates can not be null", e.getMessage());
        }
    }

    @Test
    public void testDateUtilNow(){
        Date testNowDate = testDateTime("2010-01-01 12:00:00");
        TestUtilDateUtil.setNowDate(testNowDate);
        assertEquals(testNowDate, DateUtil.getNow());

        TestUtilDateUtil.clearNowDate();
        // verify the reset of system date now
        assertTrue(!DateUtil.equals(testNowDate, DateUtil.getNow()));
    }

    @Test
    public void testTimeToMinimum(){
        Date aDate = testDateTime("2010-01-01 12:00:00");
        aDate = DateUtil.setTimeToActualMinimum(aDate);
        assertEquals(testDateTime("2010-01-01 00:00:00"), aDate);
    }

    @Test
    public void testTimeToMaximum(){
        Date aDate = testDateTime("2010-01-01 12:00:00");
        aDate = DateUtil.setTimeToActualMaximum(aDate);
        assertEquals(testDateTimeMillis("2010-01-01 23:59:59:999"), aDate);
    }

    @Test
    public void testIsFirstInThisMonth(){
        TestUtilDateUtil.setNowDate(testDateTime("2010-01-01 12:00:00"));

        Date aDate = testDateTime("2010-01-01 12:00:00");
        assertTrue(DateUtil.isFirstInThisMonth(aDate));

        TestUtilDateUtil.clearNowDate();
    }

    @Test
    public void testIsFirstInThisMonth_False(){
        TestUtilDateUtil.setNowDate(testDateTime("2010-03-01 12:00:00"));

        Date aDate = testDateTime("2010-01-01 12:00:00");
        assertTrue(!DateUtil.isFirstInThisMonth(aDate));

        TestUtilDateUtil.clearNowDate();
    }

    @Test
    public void testIsLastInThisMonth(){
        Date aDate = testDateTime("2010-01-31 12:00:00");
        assertTrue(DateUtil.isLastInMonth(aDate));
    }

    @Test
    public void testIsLastInThisMonth_False(){
        Date aDate = testDateTime("2010-01-30 12:00:00");
        assertTrue(!DateUtil.isLastInMonth(aDate));
    }

    @Test
    public void testIsFirstMonth(){
        Date aDate = testDateTime("2010-01-01 12:00:00");
        assertTrue(DateUtil.isFirstInMonth(aDate));
    }

    @Test
    public void testIsFirstMonth_False(){
        Date aDate = testDateTime("2010-01-02 12:00:00");
        assertTrue(!DateUtil.isFirstInMonth(aDate));
    }

    private Date testDateTimeMillis(String aDate){
        try {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS").parse(aDate);
        } catch (ParseException e) {
            throw new MessageRuntimeException("Could not format datetime {0}", aDate);
        }
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
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(aDate);
        } catch (ParseException e) {
            throw new MessageRuntimeException("Could not format datetime {0}", aDate);
        }
    }

}
