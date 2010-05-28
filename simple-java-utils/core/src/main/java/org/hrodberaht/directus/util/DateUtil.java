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

package org.hrodberaht.i18n;

import org.hrodberaht.directus.exception.MessageRuntimeException;

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

    private static Locale locale = Locale.getDefault();

    private static String LONG_DATE = null;
    private static int LONG_DATE_LENGTH = -1;
    private static String SHORT_DATE = null;
    private static int SHORT_DATE_LENGTH = -1;

    private static Date systemOverriddenNow = null;

    static {
        updateFormatPatterns();
    }

    private DateUtil() {
    }

    /**
     * Utility method to change the locale of the DateUtil, affects the entire server as its static
     * Will call DateUtil.updateFormatPatterns after setting the locale
     *
     * @param locale the locale to change to
     */
    public static void setLocale(Locale locale) {
        DateUtil.locale = locale;
        updateFormatPatterns();
    }

    /**
     * Compare if the dates are less or equal
     *
     * @param theDate the date
     * @param compare the compare date for compare
     * @return result of compare for equals and DateUtil.before
     */
    public static boolean isLessOrEqual(Date theDate, Date compare) {
        if (theDate == null || compare == null) {
            throw new IllegalAccessError("Dates can not be null");
        }
        return DateUtil.equals(theDate, compare) || DateUtil.before(theDate, compare);
    }

    /**
     * Compare if the dates are more or equal
     *
     * @param theDate the date
     * @param compare the compare date for compare
     * @return result of compare for equals and DateUtil.after
     */
    public static boolean isMoreOrEqual(Date theDate, Date compare) {
        if (theDate == null || compare == null) {
            throw new IllegalAccessError("Dates can not be null");
        }
        return DateUtil.equals(theDate, compare) || DateUtil.after(theDate, compare);
    }

    /**
     * Compares if the date is after the compare date 
     *
     * @param theDate base date for compare
     * @param compare the compare date for compare
     * @return result of compare
     */
    public static boolean after(Date theDate, Date compare) {
        return theDate.getTime() > compare.getTime();
    }

    /**
     * Compares if the date is after the compare date
     *
     * @param theDate base date for compare
     * @param compare the compare date for compare
     * @return result of compare
     */
    public static boolean before(Date theDate, Date compare) {
        return theDate.getTime() < compare.getTime();
    }


    /**
     * Parse a date according to the provided pattern
     *
     * @param date the string to use for parsing
     * @param pattern the pattern to use for parsing
     * @return parsed date object
     */
    public static Date parseDate(String date, String pattern) {
        Format format = cacheSimpleDateFormat(pattern);
        try {
            return parseDate(date, format);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Reformat the dates according to pattern and then calls equals on them.
     * Useful for comparing if a date is on the same month or day even though there is more details
     *
     * @param date1 date for compare
     * @param date2 date for compare
     * @param pattern to apply before compare
     * @return results of compare
     */
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

    /**
     * A different version than the java version of Date.equals, it does not care about type.
     *
     * @param date1 date for compare
     * @param date2 date for compare
     * @return the result of date1.getTime() == date2.getTime()
     */
    public static boolean equals(Date date1, Date date2) {
        return date1.getTime() == date2.getTime();
    }

    /**
     * A simple parser that can find out what date/time type the selected inputted string is of and tries to parse that.
     * Uses the DateUtil.LONG_DATE and DateUtil.SHORT_DATE for its logic
     *
     * @param date the string to test for parsing
     * @return parsed date object
     */
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


    /**
     * Formatted version of selected locale (DateUtil#locale) timezone
     *
     * @param
     * @param theDate date to check timezone for
     * @return formatted timezone information for the date that is inout to method
     */
    public static String formatTimeZone(Date theDate) {
        TimeZone timezone = DateFormat.getDateInstance(DateFormat.SHORT, locale).getTimeZone();
        SimpleDateFormat format = new SimpleDateFormat("Z", locale);
        format.setTimeZone(timezone);
        return format.format(theDate);
    }

    /**
     * Formats the date according to selected locale pattern for short dates (no time).
     * Uses the Locale to figure out the pattern to use
     * then just calls the java.text.SimpleDateFormat#toLocalizedPattern().
     *
     * Long patters is created using DateFormat.getDateInstance(DateFormat.SHORT, locale)
     *
     * @param date date for formatting
     * @return formatted date according to pattern
     *
     * @see java.text.SimpleDateFormat#toLocalizedPattern()
     */
    public static String formatDate(Date date) {
        return formatDate(date, SHORT_DATE);
    }

    /**
     * Formats the date according to selected locale pattern for long dates (date & time).
     * Uses the Locale to figure out the pattern to use
     * then just calls the java.text.SimpleDateFormat#toLocalizedPattern().
     *
     * Long patters is created using DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM, locale)
     *
     * @param date date for formatting
     * @return formatted date according to pattern
     *
     * @see java.text.SimpleDateFormat#toLocalizedPattern()
     */
    public static String formatDateTime(Date date) {
        return formatDate(date, LONG_DATE);
    }

    /**
     * Formats the date according to set pattern, see normal dateformattingrules for pattern
     *
     * @param date date for formatting
     * @param pattern intended pattern
     * @return formatted date according to pattern
     * 
     * @see SimpleDateFormat for patterns
     */
    public static String formatDate(Date date, String pattern) {
        Format format = cacheSimpleDateFormat(pattern);
        return format.format(date);
    }

    /**
     * Uses a Calendar.add(Calendar.DAY_OF_YEAR, hours), but is usable from a Date as input.
     * The date returned is a new instance
     *
     * @param date date to roll
     * @param days amount of days to roll
     * @return the rolled date
     *
     * @see Calendar#add(int, int)
     */
    public static Date rollDays(Date date, int days) {
        Calendar calendar = Calendar.getInstance(locale);
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR, days);
        return calendar.getTime();
    }

    /**
     * Uses a Calendar.add(Calendar.MONTH, hours), but is usable from a Date as input.
     * The date returned is a new instance
     *
     * @param date date to roll
     * @param months amount of months to roll
     * @return the rolled date
     *
     * @see Calendar#add(int, int)
     */
    public static Date rollMonth(Date date, int months) {
        Calendar calendar = Calendar.getInstance(locale);
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, months);
        return calendar.getTime();
    }

    /**
     * Uses a Calendar.add(Calendar.HOUR_OF_DAY, hours), but is usable from a Date as input.
     * The date returned is a new instance
     *
     * @param date date to roll
     * @param hours amount of hours to roll
     * @return the rolled date
     * 
     * @see Calendar#add(int, int)
     */
    public static Date rollHours(Date date, int hours) {
        Calendar calendar = Calendar.getInstance(locale);
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, hours);
        return calendar.getTime();
    }

    /**
     * Sets the minimum possible time (hours, minutes, seconds, millis) and returns as a new instance of date.
     *
     * @param date date for minimum time settings
     * @return the new minimum times date (uses the incoming date as template)
     */
    public static Date setTimeToActualMinimum(Date date) {
        Calendar calendar = Calendar.getInstance(locale);
        calendar.setTime(date);
        setTimeToActualMinimum(calendar);
        return calendar.getTime();
    }

    /**
     * Sets the maximum possible time (hours, minutes, seconds, millis) and returns as a new instance of date.
     *
     * @param date date for maximum time settings
     * @return the new maximum times date (uses the incoming date as template)
     */
    public static Date setTimeToActualMaximum(Date date) {
        Calendar calendar = Calendar.getInstance(locale);
        calendar.setTime(date);
        setTimeToActualMaximum(calendar);
        return calendar.getTime();
    }


    /**
     * This is very useful if a developer needs to override what the time is for specific tests.
     * If all parts of the software uses this instead of "new Date" it will be possible to change time for specific
     *
     * @return the now date (replaces new Date), when used the TestUtilDateUtil class for date manipulation can be used.
     * @see org.hrodberaht.directus.tdd.TestUtilDateUtil#setNowDate
     */
    public static Date getNow() {
        if (systemOverriddenNow == null) {
            return new Date();
        }
        return systemOverriddenNow;
    }

    /**
     * Is the date the first in the current month, uses DateUtil.getNow for compare
     *
     * @param date
     * @return true if it is the first day in this, false if not
     * @see #getNow
     */
    public static boolean isFirstInThisMonth(Date date) {
        Calendar now = Calendar.getInstance(locale);
        now.setTime(getNow());
        Calendar calendar = Calendar.getInstance(locale);
        calendar.setTime(date);
        boolean isFirstDayInMonth = calendar.getActualMinimum(Calendar.DAY_OF_MONTH)
                == calendar.get(Calendar.DAY_OF_MONTH);
        return isFirstDayInMonth && (calendar.get(Calendar.MONTH) == now.get(Calendar.MONTH));
    }


    /**
     * Is the date the first in the month of its own definition.
     *
     * @param date
     * @return true if it is the first day, false if not
     */
    public static boolean isFirstInMonth(Date date) {
        Calendar calendar = Calendar.getInstance(locale);
        calendar.setTime(date);
        return calendar.getActualMinimum(Calendar.DAY_OF_MONTH) == calendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * Is the date the last in the month of its own definition.
     *
     * @param date
     * @return true if it is the last day, false if not
     */
    public static boolean isLastInMonth(Date date) {
        Calendar calendar = Calendar.getInstance(locale);
        calendar.setTime(date);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH) == calendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * Date is considered to be in between if it is equals to or less than toDate or equal to or more than fromDate
     *
     * @param theDate  is the date for comparison
     * @param fromDate is the start value
     * @param toDate   is the end value
     * @return true when considered in between, false otherwise
     */
    public static boolean isBetween(Date theDate, Date fromDate, Date toDate) {
        if (theDate == null || fromDate == null) {
            return false;
        }
        if (theDate.compareTo(fromDate) == 0) {
            return true;
        } else if (theDate.compareTo(toDate) == 0) {
            return true;
        } else if (theDate.after(fromDate) && toDate == null) {
            return true;
        } else if (theDate.after(fromDate)
                && theDate.before(toDate)) {
            return true;
        }
        return false;
    }


    private static void setTimeToActualMinimum(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, calendar.getActualMinimum(Calendar.HOUR_OF_DAY));
        calendar.set(Calendar.MINUTE, calendar.getActualMinimum(Calendar.MINUTE));
        calendar.set(Calendar.SECOND, calendar.getActualMinimum(Calendar.SECOND));
        calendar.set(Calendar.MILLISECOND, calendar.getActualMinimum(Calendar.MILLISECOND));
    }

    private static void setTimeToActualMaximum(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, calendar.getActualMaximum(Calendar.HOUR_OF_DAY));
        calendar.set(Calendar.MINUTE, calendar.getActualMaximum(Calendar.MINUTE));
        calendar.set(Calendar.SECOND, calendar.getActualMaximum(Calendar.SECOND));
        calendar.set(Calendar.MILLISECOND, calendar.getActualMaximum(Calendar.MILLISECOND));
    }

    private static Format cacheSimpleDateFormat(String pattern) {
        return new SimpleDateFormat(pattern);
    }


    private static Date parseDate(String date, Format format) throws ParseException {
        return (Date) format.parseObject(date);
    }

    private static void updateFormatPatterns() {
        SimpleDateFormat dateFormat = (SimpleDateFormat) DateFormat.getDateInstance(DateFormat.SHORT, locale);
        SHORT_DATE = dateFormat.toLocalizedPattern();
        SHORT_DATE_LENGTH = SHORT_DATE.length();

        SimpleDateFormat dateTimeFormat = (SimpleDateFormat) DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM, locale);
        LONG_DATE = dateTimeFormat.toLocalizedPattern();
        LONG_DATE_LENGTH = LONG_DATE.length();
    }


}