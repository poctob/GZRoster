package com.gzlabs.gzroster.data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
			String month = String.format("%02d", date.getMonth() + 1);
			String day = String.format("%02d", date.getDay());
			retval = date.getYear() + "-" + month + "-" + day;
		}

		if (time != null) {
			String hour = String.format("%02d", time.getHours());
			String minute = String.format("%02d", time.getMinutes());
			String second = String.format("%02d", time.getSeconds());

			retval += " " + hour + ":" + minute + ":" + second + ".0";
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
			// TODO Auto-generated catch block
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
	
	public static Calendar getWeekEnd(boolean startSunday, Calendar date)
	{
		Calendar start=getWeekStart(startSunday, date);
		start.add(Calendar.DAY_OF_MONTH, 6);
		return start;
	}
	
	
	public static int getWeekStartDay(boolean startSunday, String date)
	{
		Calendar cal=date==null?null:calendarFromString(date+" 00:00:00.0");
		return getWeekStart(startSunday, cal).get(Calendar.DAY_OF_MONTH);
	}
	
	public static int getWeekEndDay(boolean startSunday, String date)
	{
		Calendar cal=date==null?null:calendarFromString(date+" 00:00:00.0");
		return getWeekEnd(startSunday, cal).get(Calendar.DAY_OF_MONTH);
	}
	
	public static int getWeekStartMonth(boolean startSunday, String date)
	{
		Calendar cal=date==null?null:calendarFromString(date+" 00:00:00.0");
		return getWeekStart(startSunday, cal).get(Calendar.MONTH);
	}
	
	public static int getWeekEndMonth(boolean startSunday, String date)
	{
		Calendar cal=date==null?null:calendarFromString(date+" 00:00:00.0");
		return getWeekEnd(startSunday, cal).get(Calendar.MONTH);
	}
	
	public static int getWeekStartYear(boolean startSunday, String date)
	{
		Calendar cal=date==null?null:calendarFromString(date+" 00:00:00.0");
		return getWeekStart(startSunday, cal).get(Calendar.YEAR);
	}
	
	public static int getWeekEndYear(boolean startSunday, String date)
	{
		Calendar cal=date==null?null:calendarFromString(date+" 00:00:00.0");
		return getWeekEnd(startSunday, cal).get(Calendar.YEAR);
	}
	
	public static String DateToString(Date date)
	{
		Calendar cal=new GregorianCalendar();
		cal.setTime(date);
		String retsring=String.format("%02d", cal.get(Calendar.YEAR))+"-";
	
		retsring+=String.format("%02d", cal.get(Calendar.MONTH)+1)+"-";
		retsring+=String.format("%02d", cal.get(Calendar.DAY_OF_MONTH))+" ";
		retsring+=String.format("%02d", cal.get(Calendar.HOUR_OF_DAY))+":";
		retsring+=String.format("%02d", cal.get(Calendar.MINUTE))+":";
		retsring+=String.format("%02d", cal.get(Calendar.SECOND))+".0"	;;
		return retsring;
		
	}



	public static boolean isCalendarBetween(Date start, Date end,
			String start2, String end2, boolean b) {
		
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



	public static Date StringToDate(String date) {
		
		Calendar cal=calendarFromString(date);
		return cal.getTime();
		
	}



	public static double getSpanMinutes(Date m_start, Date m_end) {
		
		return getSpanMinutes(DateToString(m_start),DateToString(m_end));
	}


}
