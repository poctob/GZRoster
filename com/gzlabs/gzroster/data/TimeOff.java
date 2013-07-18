package com.gzlabs.gzroster.data;

import java.util.Date;

import com.gzlabs.utils.DateUtils;

/**
 * Time off object
 * @author apavlune
 *
 */
public class TimeOff{
	
	//Time off start time
	private Date start;
	
	//Time off end time
	private Date end;
	
	//request's status
	private String status;
	
	//person's name
	private String name;
	
	/**
	 * Default constructor initializes member variables
	 * @param start Start of the time off
	 * @param end End of the time off
	 */
	public TimeOff(Date start, Date end, String status, String name) {
		super();
		this.start = start;
		this.end = end;
		this.status=status;
		this.name=name;
	}
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
	
	public boolean setStart(String start)
	{
		Date st_date=DateUtils.StringToDate(start);
		if(st_date!=null)
		{
			this.start=st_date;
			return true;
		}
		return false;
		
	}
	
	public boolean setEnd(String end)
	{
		Date end_date=DateUtils.StringToDate(end);
		if(end_date!=null)
		{
			this.end=end_date;
			return true;
		}
		return false;		
	}
	
	public String getStatus()
	{
		return status;
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
