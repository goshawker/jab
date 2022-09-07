package com.jab.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


// TODO: Auto-generated Javadoc
/**
 * @description: TODO
 * @author mike
 * @date 2022/9/7 14:18
 * @version 1.0
 */
public class DateUtils {

	/** The Constant FORMAT_DATE_SHORT. */
	public static final String FORMAT_DATE_SHORT = "yyyyMMdd";

	/** The Constant FORMAT_DATE_MIDDLE. */
	public static final String FORMAT_DATE_MIDDLE = "yyyy-MM-dd";

	/** The Constant FORMAT_DATE_TIME_SHORT. */
	public static final String FORMAT_DATE_TIME_SHORT = "yyyyMMddHHmmss";

	/** The Constant FORMAT_DATE_TIME_MIDDLE. */
	public static final String FORMAT_DATE_TIME_MIDDLE = "yyyy-MM-dd HH:mm:ss";

	/** The Constant FORMAT_TIME_SHORT. */
	public static final String FORMAT_TIME_SHORT = "HHmmss";

	/** The Constant FORMAT_TIME_MIDDLE. */
	public static final String FORMAT_TIME_MIDDLE = "HH:mm:ss";

	/**
	 * Date format short to mid (yyyy-mm-dd).
	 * 
	 * @param date  yyyymmdd
	 * @return the string
	 */
	public static String dateFormatShortToMid(String date) {
		String tempStr = date.substring(0, 4);
		String tempStr1 = date.substring(4, 6);
		String tempStr2 = date.substring(6, 8);
		return tempStr + "-" + tempStr1 + "-" + tempStr2;
	}

	/**
	 * Date format mid to short (yyyymmdd).
	 * 
	 * @param date yyyy-mm-dd
	 * @return the string
	 */
	public static String dateFormatMidToShort(String date) {
		String tempStr = date.substring(0, 4);
		String tempStr1 = date.substring(5, 7);
		String tempStr2 = date.substring(8, 10);
		return tempStr + tempStr1 + tempStr2;
	}

	/**
	 * Format.
	 * 
	 * @param calendar   the calendar
	 * @param formatStr   the format str
	 * @return the string
	 */
	public static String format(Calendar calendar, String formatStr) {
		if (calendar == null) {
			return null;
		}

		return format(calendar.getTime(), formatStr);
	}

	/**
	 * Format.
	 * 
	 * @param date
	 *            the date
	 * @param formatStr
	 *            the format str
	 * @return the string
	 */
	public static String format(Date date, String formatStr) {
		if (date == null) {
			return null;
		}

		return new SimpleDateFormat(formatStr).format(date);
	}

	/**
	 * Parses the.
	 * 
	 * @param dateString
	 *            the date string
	 * @param formatStr
	 *            the format str
	 * @return the java.util. date
	 */
	public static Date parse(String dateString, String formatStr) {
		try {
			return new SimpleDateFormat(formatStr).parse(dateString);
		} catch (ParseException ex) {
			ex.printStackTrace();
		}
		return null;
	}

	/**
	 * Parses the.
	 * 
	 * @param sqlDate
	 *            the sql date
	 * @return the java.util. date
	 */
	public static Date parse(java.sql.Date sqlDate) {
		try {
			return new Date(sqlDate.getTime());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	/**
	 * Parses the calendar.
	 * 
	 * @param dateString
	 *            the date string
	 * @param formatStr
	 *            the format str
	 * @return the calendar
	 */
	public static Calendar parseCalendar(String dateString, String formatStr) {
		return toCalendar(parse(dateString, formatStr));
	}

	/**
	 * Parses the timestamp.
	 * 
	 * @param dateString
	 *            the date string
	 * @param formatStr
	 *            the format str
	 * @return the timestamp
	 */
	public static Timestamp parseTimestamp(String dateString, String formatStr) {
		return new Timestamp(parse(formatStr, dateString).getTime());
	}

	/**
	 * To calendar.
	 * 
	 * @param date
	 *            the date
	 * @return the calendar
	 */
	public static Calendar toCalendar(Date date) {
		if (date != null) {
			Calendar cl = Calendar.getInstance();
			cl.setTime(date);
			return cl;
		}

		return null;
	}

	/**
	 * To date.
	 * 
	 * @param calendar
	 *            the calendar
	 * @return the java.util. date
	 */
	public static Date toDate(Calendar calendar) {
		return calendar != null ? calendar.getTime() : null;
	}

	/**
	 * Gets the current time.
	 * 
	 * @param formatStr
	 *            the format str
	 * @return the current time
	 */
	public static String getCurrentTime(String formatStr) {
		Calendar cal = Calendar.getInstance();
		return format(cal, formatStr);
	}

	/**
	 * Gets the hour.
	 * 
	 * @param cal
	 *            the cal
	 * @return the hour
	 */
	public static String getHour(Calendar cal) {
		int hour = cal.get(Calendar.HOUR);
		if (hour < 10) {
			return "0" + String.valueOf(hour);
		}

		return String.valueOf(hour);
	}

	/**
	 * Gets the minute.
	 * 
	 * @param cal
	 *            the cal
	 * @return the minute
	 */
	public static String getMinute(Calendar cal) {
		int minute = cal.get(Calendar.MINUTE);
		if (minute < 10) {
			return "0" + String.valueOf(minute);
		}

		return String.valueOf(minute);
	}

	/**
	 * Gets the second.
	 * 
	 * @param cal
	 *            the cal
	 * @return the second
	 */
	public static String getSecond(Calendar cal) {
		int second = cal.get(Calendar.SECOND);
		if (second < 10) {
			return "0" + String.valueOf(second);
		}

		return String.valueOf(second);
	}

	/**
	 * Format date short (yyyyMMdd).
	 * 
	 * @param calendar  the calendar
	 * @return the string
	 */
	public static String formatDateShort(Calendar calendar) {
		return format(calendar, "yyyyMMdd");
	}

	/**
	 * Format date short(yyyyMMdd).
	 * 
	 * @param date  the date
	 * @return the string
	 */
	public static String formatDateShort(Date date) {
		return format(date, "yyyyMMdd");
	}

	/**
	 * Format date mid (yyyy-MM-dd).
	 * 
	 * @param calendar
	 *            the calendar
	 * @return the string
	 */
	public static String formatDateMid(Calendar calendar) {
		return format(calendar, "yyyy-MM-dd");
	}

	/**
	 * Format date mid (yyyy-MM-dd).
	 * 
	 * @param date   the date
	 * @return the string
	 */
	public static String formatDateMid(Date date) {
		return format(date, "yyyy-MM-dd");
	}

	/**
	 * Format datetime short(yyyyMMddHHmmss).
	 * 
	 * @param calendar
	 *            the calendar
	 * @return the string
	 */
	public static String formatDatetimeShort(Calendar calendar) {
		return format(calendar, "yyyyMMddHHmmss");
	}

	/**
	 * Format datetime short (yyyyMMddHHmmss).
	 * 
	 * @param date
	 *            the date
	 * @return the string
	 */
	public static String formatDatetimeShort(Date date) {
		return format(date, "yyyyMMddHHmmss");
	}

	/**
	 * Format datetime mid(yyyy-MM-dd HH:mm:ss).
	 * 
	 * @param calendar
	 *            the calendar
	 * @return the string
	 */
	public static String formatDatetimeMid(Calendar calendar) {
		return format(calendar, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * Format datetime mid(yyyy-MM-dd HH:mm:ss).
	 * 
	 * @param date
	 *            the date
	 * @return the string
	 */
	public static String formatDatetimeMid(Date date) {
		return format(date, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * Format time short(HHmmss).
	 * 
	 * @param calendar
	 *            the calendar
	 * @return the string
	 */
	public static String formatTimeShort(Calendar calendar) {
		return format(calendar, "HHmmss");
	}

	/**
	 * Format time short(HHmmss).
	 * 
	 * @param date
	 *            the date
	 * @return the string
	 */
	public static String formatTimeShort(Date date) {
		return format(date, "HHmmss");
	}

	/**
	 * Format time mid(HH:mm:ss).
	 * 
	 * @param calendar
	 *            the calendar
	 * @return the string
	 */
	public static String formatTimeMid(Calendar calendar) {
		return format(calendar, "HH:mm:ss");
	}

	/**
	 * Format time mid(HH:mm:ss).
	 * 
	 * @param date
	 *            the date
	 * @return the string
	 */
	public static String formatTimeMid(Date date) {
		return format(date, "HH:mm:ss");
	}

	// =======

	/**
	 * _new date.
	 * 
	 * @return the string
	 */
	public static String newDate() {
		return SimpleDateFormat.getDateInstance(
				SimpleDateFormat.MEDIUM).format(new Date());
	}

	/**
	 * _new date.
	 * 
	 * @param offset
	 *            the offset
	 * @return the string
	 */
	public static String newDate(int offset) {

		Calendar __calendar = Calendar.getInstance();
		__calendar.setTime(new Date());
		__calendar.set(Calendar.DAY_OF_MONTH,
				__calendar.get(Calendar.DAY_OF_MONTH) + offset);
		return SimpleDateFormat.getDateInstance(
				SimpleDateFormat.MEDIUM).format(__calendar.getTime());
	}

	/**
	 * _parse date time.
	 * 
	 * @param datetime
	 *            the datetime
	 * @return the date
	 * @throws ParseException
	 *             the parse exception
	 */
	public static Date parseDateTime(String datetime) throws ParseException {
		return SimpleDateFormat.getDateTimeInstance(
				SimpleDateFormat.MEDIUM, SimpleDateFormat.MEDIUM).parse(
				datetime);
	}

	/**
	 * _parse date.
	 * 
	 * @param date
	 *            the date
	 * @return the date
	 * @throws ParseException
	 *             the parse exception
	 */
	public static Date parseDate(String date) throws ParseException {
		return SimpleDateFormat.getDateInstance(
				SimpleDateFormat.MEDIUM).parse(date);
	}

	/**
	 * _new date time.
	 * 
	 * @return the string
	 */
	public static String newDateTime() {
		return SimpleDateFormat.getDateTimeInstance(
				SimpleDateFormat.MEDIUM, SimpleDateFormat.MEDIUM).format(
				new Date());
	}

	/**
	 * _new date time.
	 * 
	 * @param offset
	 *            the offset
	 * @return the string
	 */
	public static String newDateTime(int offset) {
		Calendar __calendar = Calendar.getInstance();
		__calendar.setTime(new Date());
		__calendar.set(Calendar.DAY_OF_MONTH,
				__calendar.get(Calendar.DAY_OF_MONTH) + offset);
		return SimpleDateFormat.getDateTimeInstance(
				SimpleDateFormat.MEDIUM, SimpleDateFormat.MEDIUM).format(
				__calendar.getTime());
	}

	/**
	 * _compare date.
	 * 
	 * @param date
	 *            the date
	 * @return the int
	 * @throws ParseException
	 *             the parse exception
	 */
	public static int compareDate(String date) throws ParseException {

		return SimpleDateFormat.getDateTimeInstance().parse(date)
				.compareTo(new Date());
	}

	/**
	 * _compare date.
	 * 
	 * @param date
	 *            the date
	 * @return the int
	 * @throws ParseException
	 *             the parse exception
	 */
	public static int compareDate(Date date) throws ParseException {
		return date.compareTo(new Date());
	}
}