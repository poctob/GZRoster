package com.gzlabs.gzroster.data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.eclipse.swt.widgets.DateTime;

/**
 * Provides some utility functions related to date/time.
 * @author apavlune
 *
 */
public class DateUtils {
	
	/**
	 * Gets a date for the supplied weekday.
	 * @param weekday A day to get a day of the week for
	 * @return A string containing month and a day.
	 */
	public static String getWeekDayDate(int weekday)
	{
		Calendar cal=new GregorianCalendar();
		cal.setTime(new Date());
		int dofw=cal.get(Calendar.DAY_OF_WEEK);
		int daydiff=weekday-dofw;
		
		cal.add(Calendar.DAY_OF_MONTH, daydiff);
		
		String retval=cal.get(Calendar.MONTH)+"/"+cal.get(Calendar.DATE);
		return retval;
	}
	
	/**
	 * Gets a date from the supplied widget.
	 * @param date Date widget
	 * @param time Time widget (optional)
	 * @return String date/time representation.
	 */
	public static String dateStringFromWidget(DateTime date, DateTime time) {
		String retval = "";
		if (date != null) {
			String month =safeStringFormat(date.getMonth() + 1);
			String day = safeStringFormat(date.getDay());
			retval = date.getYear() + "-" + month + "-" + day;
		}

		if (time != null) {
			String hour = safeStringFormat(time.getHours());
			String minute = safeStringFormat( time.getMinutes());
			String second =safeStringFormat(time.getSeconds());

			retval += " " + hour + ":" + minute + ":" + second + ".0";
		}
		return retval;
	}
	
	/**
	 * Performs save string formatting.
	 * @param input Integer to format.
	 * @return formated string if there is not issues, empty string otherwise.
	 */
	private static String safeStringFormat(int input)
	{
		String retval="";
		try
		{
			retval=String.format("%02d", input);		
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}		
		return retval;
	}
	
	/**
	 * Converts string time into calendar.  Format is FireBird SQL time.
	 * @param time String to convert
	 * @return Calendar time representation.
	 */
	public static Calendar calendarFromString(String time)
	{
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
		Calendar retval=new GregorianCalendar();
		try {
			retval.setTime(sdf.parse(time));
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
		return retval;
		
	}
	
	/**
	 * Checks if a date falls within specified period.
	 * @param start Start of the period.
	 * @param end End of the period.
	 * @param ref Date that is being checked.
	 * @param inclusive Whether start and end dates will be included.
	 * @return True if the date is within the period false otherwise.
	 */
	public static boolean isCalendarBetween(Calendar start, Calendar end, Calendar ref, boolean inclusive)
	{
		if(ref==null)
		{
			return false;
		}
		if(inclusive)
		{
			if(ref.equals(start) || ref.equals(end))
			{
				return true;
			}			
		}
	
		if(ref.after(start) && ref.before(end))
		{
			return true;
		}				
		return false;
	}
	
	/**
	 * Convenience method, uses date string representation to check if it falls within specified period.
	 * @param start Start of the period.
	 * @param end End of the period.
	 * @param ref Date that is being checked.
	 * @param inclusive Whether start and end dates will be included.
	 * @return True if the date is within the period false otherwise.
	 */
	public static boolean isCalendarBetween(String start, String end, String ref, boolean inclusive)
	{
		Calendar start_cal=DateUtils.calendarFromString(start);
		Calendar end_cal=DateUtils.calendarFromString(end);
		Calendar time_cal=DateUtils.calendarFromString(ref);
		return  isCalendarBetween(start_cal, end_cal, time_cal, inclusive);
	}
	
	/**
	 * Returns a difference between two dates in minutes	
	 * @param start First date
	 * @param end Second date
	 * @return Difference between two dates
	 */
	public static double getSpanMinutes(String start, String end)
	{
		Calendar start_cal=DateUtils.calendarFromString(start);
		Calendar end_cal=DateUtils.calendarFromString(end);
		
		if(start_cal == null || end_cal ==null)
		{
			return 0;
		}
		
		if(end_cal.compareTo(start_cal)>0)
		{
			long span = end_cal.getTimeInMillis()-start_cal.getTimeInMillis();
			double seconds=span/1000;
			double minutes=seconds/60;
			return minutes;
		}
		else
		{
			return 0;
		}
	}
	
	/**
	 * Compares two dates
	 * @param start First date
	 * @param end Second date	
	 * @return 0 if dates are equal, -1 if first date is after the second on, 1 if second date is afer the first
	 */
	public static int compareToWidget(DateTime start, DateTime end)
	{
		if(start!=null && end!=null)
		{
			Calendar cal=new GregorianCalendar();
			cal.set(Calendar.YEAR, start.getYear());
			cal.set(Calendar.MONTH, start.getMonth());
			cal.set(Calendar.DAY_OF_MONTH, start.getDay());
			
			Calendar cal2=new GregorianCalendar();
			cal2.set(Calendar.YEAR, end.getYear());
			cal2.set(Calendar.MONTH, end.getMonth());
			cal2.set(Calendar.DAY_OF_MONTH, end.getDay());
			
			return cal.compareTo(cal2);
		}
		return 0;
	}
	
	/**
	 * Returns a date corresponding to the first day of the week.
	 * @param startSunday Whether the week starts on Sunday.
	 * @param date A date to calculate the start for.
	 * @return Calendar representation of the first day of the week.
	 */
	public static Calendar getWeekStart(boolean startSunday, Calendar date)
	{
		if(date==null)
		{
			date=new GregorianCalendar();
			date.setTime(new Date());
		}
		int current_dow=date.get(Calendar.DAY_OF_WEEK)-1;
		date.add(Calendar.DAY_OF_MONTH, startSunday?-current_dow:(-current_dow)+1);
		return date;
	}

	/**
	 * Returns a date corresponding to the last day of the week.
	 * @param startSunday Whether the week starts on Sunday.
	 * @param date A date to calculate the end for.
	 * @return Calendar representation of the last day of the week.
	 */	
	public static Calendar getWeekEnd(boolean startSunday, Calendar date)
	{
		if(date!=null)
		{
			Calendar start=getWeekStart(startSunday, date);
			start.add(Calendar.DAY_OF_MONTH, 6);
			return start;
		}
		return null;
	}
	
	/**
	 * Gets a week start day of the month
	 * @param startSunday Whether the week starts on Sunday.
	 * @param date A date to calculate the start for.
	 * @return Day of the month for the week start.
	 */	
	public static int getWeekStartDay(boolean startSunday, String date)
	{
		Calendar cal=date==null?null:calendarFromString(date+" 00:00:00.0");
		return getWeekStart(startSunday, cal).get(Calendar.DAY_OF_MONTH);
	}
	
	/**
	 * Gets a week end day of the month
	 * @param startSunday Whether the week starts on Sunday.
	 * @param date A date to calculate the end for.
	 * @return Day of the month for the week end.
	 */	
	public static int getWeekEndDay(boolean startSunday, String date)
	{
		Calendar cal=date==null?null:calendarFromString(date+" 00:00:00.0");
		Calendar weekEnd=getWeekEnd(startSunday, cal);
		if(weekEnd!=null)
		{
			return weekEnd.get(Calendar.DAY_OF_MONTH);
		}
		return 0;
		
	}
	
	/**
	 * Gets a week start month
	 * @param startSunday Whether the week starts on Sunday.
	 * @param date A date to calculate the start for.
	 * @return month for the week start.
	 */	
	public static int getWeekStartMonth(boolean startSunday, String date)
	{
		Calendar cal=date==null?null:calendarFromString(date+" 00:00:00.0");
		return getWeekStart(startSunday, cal).get(Calendar.MONTH);
	}
	
	/**
	 * Gets a week end month
	 * @param startSunday Whether the week starts on Sunday.
	 * @param date A date to calculate the end for.
	 * @return month for the week end.
	 */	
	public static int getWeekEndMonth(boolean startSunday, String date)
	{
		Calendar cal=date==null?null:calendarFromString(date+" 00:00:00.0");
		Calendar weekEnd=getWeekEnd(startSunday, cal);
		if(weekEnd!=null)
		{
			return weekEnd.get(Calendar.MONTH);
		}
		return 0;
	}
	
	/**
	 * Gets a week start year
	 * @param startSunday Whether the week starts on Sunday.
	 * @param date A date to calculate the start for.
	 * @return year for the week start.
	 */	
	public static int getWeekStartYear(boolean startSunday, String date)
	{
		Calendar cal=date==null?null:calendarFromString(date+" 00:00:00.0");
		return getWeekStart(startSunday, cal).get(Calendar.YEAR);
	}
	
	/**
	 * Gets a week end year
	 * @param startSunday Whether the week starts on Sunday.
	 * @param date A date to calculate the start for.
	 * @return year for the week end.
	 */	
	public static int getWeekEndYear(boolean startSunday, String date)
	{
		Calendar cal=date==null?null:calendarFromString(date+" 00:00:00.0");
		Calendar weekEnd=getWeekEnd(startSunday, cal);
		if(weekEnd!=null)
		{
			return weekEnd.get(Calendar.YEAR);
		}
		return 0;
	}
	
	/**
	 * Converts java.util.Date to SQL formatted string
	 * @param date Date to convert.
	 * @return SQL formatted string.
	 */
	public static String DateToString(Date date)
	{
		if(date!=null)
		{
			Calendar cal=new GregorianCalendar();
			cal.setTime(date);
			String retsring=String.format("%02d", cal.get(Calendar.YEAR))+"-";
		
			retsring+=safeStringFormat(cal.get(Calendar.MONTH)+1)+"-";
			retsring+=safeStringFormat(cal.get(Calendar.DAY_OF_MONTH))+" ";
			retsring+=safeStringFormat( cal.get(Calendar.HOUR_OF_DAY))+":";
			retsring+=safeStringFormat(cal.get(Calendar.MINUTE))+":";
			retsring+=safeStringFormat(cal.get(Calendar.SECOND))+".0"	;
			return retsring;
		}
		return null;
		
	}

	/**
	 * Convenience method, uses date representation to check if it falls within specified period.
	 * Works for two dates at a time.  Set the second one to null if not needed.
	 * @param start Start of the period.
	 * @param end End of the period.
	 * @param start2 First Date that is being checked.
	 * @param end2 Second Date that is being checked.
	 * @param inclusive Whether start and end dates will be included.
	 * @return True if the date is within the period false otherwise.
	 */
	public static boolean isCalendarBetween(Date start, Date end,
			String start2, String end2, boolean b) {
		if(start == null || end ==null)
		{
			return false;
		}
		boolean b1=false;
		boolean b2=false;
		
		Calendar start_cal=new GregorianCalendar();
		start_cal.setTime(start);
		
		Calendar end_cal=new GregorianCalendar();
		end_cal.setTime(end);
		
		if(start2!=null)
		{
			Calendar time_cal1=DateUtils.calendarFromString(start2);
			b1=isCalendarBetween(start_cal, end_cal, time_cal1, b);
		}
		
		if(end2!=null)
		{
			Calendar time_cal2=DateUtils.calendarFromString(end2);
			b2=isCalendarBetween(start_cal, end_cal, time_cal2, b);
		}
		return   b1 || b2;
	}

	/**
	 * Converts SQL formatted string to java.util.Date object.
	 * @param date String to convert
	 * @return Converted Date object.
	 */
	public static Date StringToDate(String date) {
		
		if(date!=null)
		{
			Calendar cal=calendarFromString(date);
			if(cal!=null)
			{
				return cal.getTime();
			}
		}
		return null;
		
	}

	/**
	 * Convenience method, returns a difference between two dates in minutes,
	 * takes java.util.Date as parameters.	
	 * @param m_start First date
	 * @param m_end Second date
	 * @return Difference between two dates
	 */
	public static double getSpanMinutes(Date m_start, Date m_end) {
		
		return getSpanMinutes(DateToString(m_start),DateToString(m_end));
	}
	
	/**
	 * Generates a list of strings that represent a period between two dates calculated 
	 * by using specified interval.
	 * @param start Start date.
	 * @param end End date.
	 * @param inter Interval
	 * @return List of string with time periods.
	 */
	public static ArrayList<String> getTimeSpan(String start, String end, String inter)
	{
		
		ArrayList<String> retval=new ArrayList<String> ();
		int start_time=safeParseInt(start);
		int end_time=safeParseInt(end);
		int interval=safeParseInt(inter);
		if(start_time<end_time)
		{			
			Calendar cal=new GregorianCalendar();
			cal.setTime(new Date());
			cal.set(Calendar.HOUR_OF_DAY, start_time);
			cal.set(Calendar.MINUTE, 0);
			retval.add(cal.get(Calendar.HOUR_OF_DAY)+":00");
			String zminute="00";
			
			while(cal.get(Calendar.HOUR_OF_DAY)<end_time)
			{
				cal.add(Calendar.MINUTE,interval);
				
				retval.add(cal.get(Calendar.HOUR_OF_DAY)+":"+(cal.get(Calendar.MINUTE)==0?zminute:cal.get(Calendar.MINUTE)));
			}
		}
		return retval;
	}
	
	/**
	 * Safe parsing of an integer from string
	 * @param input Input string to parse.
	 * @return parsed integer or 0 
	 */
	private static int safeParseInt(String input)
	{
		if(input!=null)
		{
			try
			{
				return Integer.parseInt(input);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		return 0;
	}

	/**
	 * Get raw current date/time string representation.
	 * @return String containing unformatted current date/time.
	 */
	public static String getCurrentDateString()
	{
		return new GregorianCalendar().getTime().toString();
	}
}
