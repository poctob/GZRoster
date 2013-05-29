package com.gzlabs.gzroster.data;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtils {
	
	public String getWeekDayDate(int weekday)
	{
		Calendar cal=new GregorianCalendar();
		cal.setTime(new Date());
		int dofw=cal.get(Calendar.DAY_OF_WEEK);
		int daydiff=weekday-dofw;
		
		cal.add(Calendar.DAY_OF_MONTH, daydiff);
		
		String retval=cal.get(Calendar.MONTH)+"/"+cal.get(Calendar.DATE);
		return retval;
	}

}
