package com.gzlabs.gzroster.data;

import java.util.Calendar;

public class DailyDuty {
	
	private String position;
	private Calendar start;
	private Calendar end;
	
	public DailyDuty(String position, Calendar start, Calendar end)
	{
		this.position=position;
		this.start=start;
		this.end=end;		
	}
	
	public String getPosition()
	{
		return position;
	}

	public boolean isOn(Calendar time)
	{
		boolean retval=false;
		
		if(start!=null & end!=null)
		{			
			retval=start.equals(time) || end.equals(time);
			if(!retval)
			{
				retval=start.before(time) && end.after(time);
			}
		}
		return retval;
	}	
}
