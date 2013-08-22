package com.gzlabs.gzroster.gui.time_off;

import java.util.ArrayList;

import com.gzlabs.gzroster.data.TimeOff;

public class TimeOffModelProvider {
	
	private ArrayList<Object> m_timeoff;
	
	public TimeOffModelProvider(ArrayList<Object> timeoff)
	{
		m_timeoff=new ArrayList<Object>();
		if(timeoff!=null)
		{
			for(Object to:timeoff)
			{
				if(to!=null && ((TimeOff)to).getStart()!=null)
				{
					m_timeoff.add(to);
				}
			}
		}
		
	}
	
	public TimeOffModelProvider(TimeOffModelProvider clone)
	{
		if(clone!=null)
		{
			m_timeoff=new ArrayList<Object>(clone.getTimeOff());
		}
	}
	
	public ArrayList<Object> getTimeOff()
	{
		return m_timeoff;
	}
}
