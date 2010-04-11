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

package org.hrodberaht.directus.util;

import org.hrodberaht.directus.exception.MessageRuntimeException;
import org.hrodberaht.directus.util.locale.LocaleProvider;

import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Simple Java Utils
 *
 * @author Robert Alexandersson
 * @version 1.0
 * @since 1.0
 */
public final class DateUtil {

    private static Locale locale = LocaleProvider.getSystemLocale();

    private static String LONG_DATE = null;
    private static int LONG_DATE_LENGTH = -1;
    private static String SHORT_DATE = null;
    private static int SHORT_DATE_LENGTH = -1;

    private static Date systemOverriddenNow = null;

    static{
        updateFormatPatterns();
    }

    private static void updateFormatPatterns() {
        SimpleDateFormat dateFormat = (SimpleDateFormat) DateFormat.getDateInstance(DateFormat.SHORT, locale);
        SHORT_DATE = dateFormat.toLocalizedPattern();
        SHORT_DATE_LENGTH = SHORT_DATE.length();

        SimpleDateFormat dateTimeFormat = (SimpleDateFormat) DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM, locale);
        LONG_DATE = dateTimeFormat.toLocalizedPattern();
        LONG_DATE_LENGTH = LONG_DATE.length();
    }

    private DateUtil() {
    }

    public static void setLocale(Locale locale){
        DateUtil.locale = locale;
        updateFormatPatterns();
    }

    public static boolean isInInterval(Date startDate, Date endDate, Date compare) {
        if (startDate == null && endDate == null) {
            return true;
        } else if (startDate == null && isLessOrEqual(compare, endDate)) {
            return true;
        } else if (endDate == null && isMoreOrEqual(compare, startDate)) {
            return true;
        } else if (
                isMoreOrEqual(compare, startDate) && isLessOrEqual(compare, endDate)) {
            return true;
        }

        return false;
    }

    private static boolean isLessOrEqual(Date theDate, Date compare) {
        if (theDate == null || compare == null) {
            return false;
        }
        return theDate.equals(compare) || theDate.before(compare);
    }

    private static boolean isMoreOrEqual(Date theDate, Date compare) {
        if (theDate == null || compare == null) {
            return false;
        }
        return theDate.equals(compare) || theDate.after(compare);
    }

    public static boolean formattedDateEquals(Date date1, Date date2, String pattern) {
        Format format = cacheSimpleDateFormat(pattern);
        try {
            Date formattedDate1 = parseDate(format.format(date1), format);
            Date formattedDate2 = parseDate(format.format(date2), format);
            return formattedDate1.equals(formattedDate2);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }


    public static Date parseDate(String date, String pattern) {
        Format format = cacheSimpleDateFormat(pattern);
        try {
            return parseDate(date, format);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean equals(Date date1, Date date2) {
        return date1.getTime() == date2.getTime();
    }

    public static Date parseSimpleDate(String date) {
        String pattern;
        int lenght = date.length();
        if (lenght == 0) {
            return null;
        } else if (lenght >= LONG_DATE_LENGTH) {
            pattern = LONG_DATE;
        } else if (lenght >= SHORT_DATE_LENGTH) {
            pattern = SHORT_DATE;
        } else {
            throw new MessageRuntimeException("Unknown format for {0} ", date);
        }
        Format format = cacheSimpleDateFormat(pattern);
        try {
            return parseDate(date, format);
        } catch (ParseException e) {
            throw new MessageRuntimeException("Could not parse {0}", e, date);
        }

    }

    public static String formatDate(Date date) {
        return formatDate(date, SHORT_DATE);
    }

    public static String formatTimeZone(TimeZone date, Date theDate) {
        SimpleDateFormat format = new SimpleDateFormat("Z");
        format.setTimeZone(date);
        return format.format(theDate);
    }

    public static String formatDateTime(Date date) {
        return formatDate(date, LONG_DATE);
    }

    public static String formatDate(Date date, String pattern) {
        Format format = cacheSimpleDateFormat(pattern);
        return format.format(date);
    }

    private static Format cacheSimpleDateFormat(String pattern) {
        return new SimpleDateFormat(pattern);
    }


    private static Date parseDate(String date, Format format) throws ParseException {
        return (Date) format.parseObject(date);
    }


    public static Date rollDays(Date date, int days) {
        Calendar calendar = Calendar.getInstance(locale);
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR, days);
        return calendar.getTime();
    }

    public static Date rollMonth(Date date, int months) {
        Calendar calendar = Calendar.getInstance(locale);
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, months);
        return calendar.getTime();
    }

    private static Date rollHours(Date date, int hours) {
        Calendar calendar = Calendar.getInstance(locale);
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, hours);
        return calendar.getTime();
    }

    private static void setTimeToActualMinimum(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, calendar.getActualMinimum(Calendar.HOUR_OF_DAY));
        calendar.set(Calendar.MINUTE, calendar.getActualMinimum(Calendar.MINUTE));
        calendar.set(Calendar.SECOND, calendar.getActualMinimum(Calendar.SECOND));
        calendar.set(Calendar.MILLISECOND, calendar.getActualMinimum(Calendar.MILLISECOND));
    }


    public static Date rollMonthAndSetDayOfMonth(Date date, int monthRoll, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance(locale);
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, monthRoll);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        setTimeToActualMinimum(calendar);
        return calendar.getTime();
    }


    public static boolean isFirstInThisMonth(Date date) {
        Calendar now = Calendar.getInstance(locale);
        now.setTime(getNowDate());
        Calendar calendar = Calendar.getInstance(locale);
        calendar.setTime(date);
        boolean isFirstDayInMonth = calendar.getActualMinimum(Calendar.DAY_OF_MONTH)
                == calendar.get(Calendar.DAY_OF_MONTH);
        return isFirstDayInMonth && (calendar.get(Calendar.MONTH) == now.get(Calendar.MONTH));
    }

    /**
     * This is very useful if a developer needs to override what the time is for specific tests.
     * If all parts of the software uses this instead of "new Date" it will be possible to change time for specific
     * 
     * @return
     */
    public static Date getNowDate() {
        if(systemOverriddenNow != null){
            return new Date();
        }
        return systemOverriddenNow;
    }

    public static boolean isFirstInMonth(Date date) {
        Calendar calendar = Calendar.getInstance(locale);
        calendar.setTime(date);
        return calendar.getActualMinimum(Calendar.DAY_OF_MONTH) == calendar.get(Calendar.DAY_OF_MONTH);
    }

    public static boolean isLastInMonth(Date date) {
        Calendar calendar = Calendar.getInstance(locale);
        calendar.setTime(date);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH) == calendar.get(Calendar.DAY_OF_MONTH);
    }


    public static boolean isBetween(Date theDate, Date fromDate, Date toDate) {
        if (theDate == null || fromDate == null) {
            return false;
        }
        if (theDate.compareTo(fromDate) == 0) {
            return true;
        } else if (theDate.after(fromDate) && toDate == null) {
            return true;
        } else if (theDate.after(fromDate)
                && theDate.before(toDate)) {
            return true;
        }
        return false;
    }

}