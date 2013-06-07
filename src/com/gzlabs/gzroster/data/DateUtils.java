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
	

}
