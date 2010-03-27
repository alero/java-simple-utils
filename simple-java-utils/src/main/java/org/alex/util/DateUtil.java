package org.alex.util;

import org.alex.util.locale.LocaleProvider;

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

    private static final Locale locale = LocaleProvider.getSystemLocale();

    private static final String LONG_DATE = "yyyy-MM-dd HH:mm:ss";
    private static final String SHORT_DATE = "yyyy-MM-dd";


    private DateUtil() {
        // DateFormat.getDateInstance(DateFormat.SHORT, DateFormat.MEDIUM, locale).
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
        if (date.length() == 0) {
            return null;
        } else if (date.length() == 19) { // "yyyy-MM-dd HH:mm:ss"
            pattern = LONG_DATE;
        } else if (date.length() == 10) { // "yyyy-MM-dd"
            pattern = SHORT_DATE;
        } else {
            throw new RuntimeException("Unknown format " + date + " EVAL:" + date.length());
        }
        Format format = cacheSimpleDateFormat(pattern);
        try {
            return parseDate(date, format);
        } catch (ParseException e) {
            throw new RuntimeException(e);
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
        now.setTime(getNow());
        Calendar calendar = Calendar.getInstance(locale);
        calendar.setTime(date);
        boolean isFirstDayInMonth = calendar.getActualMinimum(Calendar.DAY_OF_MONTH)
                == calendar.get(Calendar.DAY_OF_MONTH);
        return isFirstDayInMonth && (calendar.get(Calendar.MONTH) == now.get(Calendar.MONTH));
    }

    private static Date getNow() {
        return new Date();
    }

    public static boolean isFirstInMonth(Date date) {
        Calendar calendar = Calendar.getInstance(locale);
        calendar.setTime(date);
        return calendar.getActualMinimum(Calendar.DAY_OF_MONTH) == calendar.get(Calendar.DAY_OF_MONTH);
    }


    public static boolean isBetween(Date theDate, Date fromDate, Date toDate) {
        if (theDate == null || fromDate == null) {
            return false;
        }
        Date date = theDate;
        if (date.compareTo(fromDate) == 0) { // Exactly the same
            return true;
        } else if (date.after(fromDate) && toDate == null) { // Latest version hit!
            return true;
        } else if (date.after(fromDate)
                && date.before(toDate)) { // Active Version hit!
            return true;
        }
        return false;
    }

}