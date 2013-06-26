package com.gzlabs.gzroster.data;

import java.util.Date;

import com.gzlabs.utils.DateUtils;

/**
 * Time off object
 * @author apavlune
 *
 */
public class TimeOff {
	
	//Time off start time
	private Date start;
	
	//Time off end time
	private Date end;
	
	/**
	 * Default constructor initializes member variables
	 * @param start Start of the time off
	 * @param end End of the time off
	 */
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
	
	/**
	 * Returns start date as string
	 * @return Start date as string
	 */
	public String getStartStr()
	{
		return DateUtils.DateToString(start);
	}
	
	/**
	 * 
	 * @return End date as string
	 */
	public String getEndStr()
	{
		return DateUtils.DateToString(end);
	}
	
	/**
	 * Checks if supplied dates conflict with this object
	 * @param start Beginning of the period
	 * @param end End of the period
	 * @return True if there is a conflict, false otherwise
	 */
	public boolean isConflicting(String start, String end)
	{
		return DateUtils.isCalendarBetween(this.start, this.end, start, end, true);				
	}

}
