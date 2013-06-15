package com.gzlabs.gzroster.data;

import java.util.Date;

public class TimeOff {
	
	private Date start;
	private Date end;
	
	public TimeOff(Date start, Date end) {
		super();
		this.start = start;
		this.end = end;
	}

	/**
	 * @return the start
	 */
	public Date getStart() {
		return start;
	}

	/**
	 * @param start the start to set
	 */
	public void setStart(Date start) {
		this.start = start;
	}

	/**
	 * @return the end
	 */
	public Date getEnd() {
		return end;
	}

	/**
	 * @param end the end to set
	 */
	public void setEnd(Date end) {
		this.end = end;
	}
	
	public String getStartStr()
	{
		return DateUtils.DateToString(start);
	}
	
	public String getEndStr()
	{
		return DateUtils.DateToString(end);
	}
	
	public boolean isConflicting(String start, String end)
	{
		return DateUtils.isCalendarBetween(this.start, this.end, start, end, true);				
	}

}
