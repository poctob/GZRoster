package com.gzlabs.gzroster.gui.time_off;

import java.util.ArrayList;

public class TimeOffModelProvider {
	
	private ArrayList<Object> m_timeoff;
	
	public TimeOffModelProvider(ArrayList<Object> timeoff)
	{
		m_timeoff=timeoff;
	}
	
	public TimeOffModelProvider(TimeOffModelProvider clone)
	{
		if(clone!=null)
		{
			m_timeoff=new ArrayList<Object>(clone.getTimeOff());
		}
	}
	
	public ArrayList<Object>  getTimeOff()
	{
		return m_timeoff;
	}
}
