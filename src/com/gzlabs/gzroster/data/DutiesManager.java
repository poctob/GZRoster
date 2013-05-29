package com.gzlabs.gzroster.data;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Properties;

public class DutiesManager {
	
	private int start_time;
	private int end_time;
	private int interval;
	
	public DutiesManager(Properties prop)
	{
		this.start_time=Integer.parseInt(prop.getProperty("day_start"));
		this.end_time=Integer.parseInt(prop.getProperty("day_end"));
		this.interval=Integer.parseInt(prop.getProperty("interval_minutes"));
	}
	
	public ArrayList<String> getTimeSpan()
	{
		ArrayList<String> retval=new ArrayList<String> ();
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
}
