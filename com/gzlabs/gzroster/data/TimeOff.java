package com.gzlabs.gzroster.data;

import java.util.Calendar;
import com.gzlabs.utils.DateUtils;

/**
 * Time off object
 * @author apavlune
 *
 */
public class TimeOff{
	
	//Time off start time
	private Calendar start;
	
	//Time off end time
	private Calendar end;
	
	//request's status
	private String status;
	
	//person's name
	private String name;
	
	/**
	 * Default constructor initializes member variables
	 * @param start Start of the time off
	 * @param end End of the time off
	 * @param status Time off status
	 * @param name name of a person this time off applie to.
	 */
	public TimeOff(Calendar start, Calendar end, String status, String name) {
		super();
		this.start = start;
		this.end = end;
		this.status=status;
		this.name=name;
	}
	

	/*-------------------------------------------------------------------*/
	/*
	 * Setters and getters start
	 * /*--------------------------------------------------------------------
	 */
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the start
	 */
	public Calendar getStart() {
		return start;
	}

	/**
	 * @param start the start to set
	 */
	public void setStart(Calendar start) {
		this.start = start;
	}

	/**
	 * @return the end
	 */
	public Calendar getEnd() {
		return end;
	}

	/**
	 * @param end the end to set
	 */
	public void setEnd(Calendar end) {
		this.end = end;
	}
	
	public void setStatus(String status)
	{
		this.status=status;
	}
	
	/**
	 * Returns start date as string
	 * @return Start date as string
	 */
	public String getStartStr()
	{
		return DateUtils.CalendarToString(start);
	}
	
	/**
	 *  Returns end date as string
	 * @return End date as string
	 */
	public String getEndStr()
	{
		return DateUtils.CalendarToString(end);
	}
	
	/**
	 * Sets start value from string
	 * @param start String date.
	 * @return true if this is a valid date.
	 */
	public boolean setStart(String start)
	{
		Calendar st_date=DateUtils.calendarFromString(start);
		if(st_date!=null)
		{
			this.start=st_date;
			return true;
		}
		return false;
		
	}
	
	/**
	 * Sets end value from string
	 * @param endt String date.
	 * @return true if this is a valid date.
	 */
	public boolean setEnd(String end)
	{
		Calendar end_date=DateUtils.calendarFromString(end);
		if(end_date!=null)
		{
			this.end=end_date;
			return true;
		}
		return false;		
	}
	
	/**
	 * Retrieves status variable.
	 * @return Time off status.
	 */
	public String getStatus()
	{
		return status;
	}

	/*-------------------------------------------------------------------*/
	/*
	 * Setters and getters End
	 * /*--------------------------------------------------------------------
	 */
	
	
	/**
	 * Checks if supplied dates conflict with this object
	 * @param start Beginning of the period
	 * @param end End of the period
	 * @return True if there is a conflict, false otherwise
	 */
	public boolean isConflicting(String start, String end)
	{
		if(status!=null)
		{
			return status.equals("Approved") && DateUtils.isCalendarBetween(this.start, this.end, start, end, true);
		}
		return false;
	}

}
