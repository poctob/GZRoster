package com.gzlabs.gzroster.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.UUID;

import com.gzlabs.gzroster.sql.Tables;
import com.gzlabs.utils.DateUtils;
/**
 * Duty object representation.
 * @author apavlune
 *
 */
public class Duty extends DB_Object {

	/**
	 * Duty start
	 */
	private Calendar m_start;
	
	/**
	 * Duty end
	 */
	private Calendar m_end;
	
	/**
	 * Position assigned
	 */
	private String m_position;
	
	/**
	 * Person assigned to the position
	 */
	private String m_person;
	
	/**
	 * Unique identifier.  This is a legacy leftover from FB.
	 */
	private String m_uuid;
	
	
	 /*-------------------------------------------------------------------*/
	 /*  Constructors start
	 /*--------------------------------------------------------------------*/
	/**
	 * Default constructor.  Blank variables are assigned. Note that start and end
	 * dates are initialized to the current date.
	 */
	public Duty()
	{
		this(new GregorianCalendar(), new GregorianCalendar(), "", "", "");
	}
	
	/**
	 * Overloaded constructor, takes in all properties except for unique identifier.
	 * @param m_start Start date
	 * @param m_end End date
	 * @param m_position Position name
	 * @param m_person Person name
	 */
	public Duty(Calendar m_start, Calendar m_end, String m_position, String m_person) {
		this(m_start, m_end, m_position, m_person, "");
	}
	
	/**
	 * Overloaded constructor that takes in all variables for initialization.
	 * @param m_start Start date
	 * @param m_end End date
	 * @param m_position Position name
	 * @param m_person Person name
	 * @param u_uuid Unique identifier.
	 */
	public Duty(Calendar m_start, Calendar m_end, String m_position, String m_person, String u_uuid) {
		super();
		this.m_start = m_start;
		this.m_end = m_end;
		
		this.m_position =m_position;
		this.m_person = m_person;
		this.m_uuid=u_uuid;
	}	
	 /*-------------------------------------------------------------------*/
	 /*  Constructors End
	 /*--------------------------------------------------------------------*/

	 /*-------------------------------------------------------------------*/
	 /*  Setters and getters start
	 /*--------------------------------------------------------------------*/
	/**
	 * @return the m_start
	 */
	public Calendar getM_start() {
		return m_start;
	}

	/**
	 * @param m_start the m_start to set
	 */
	public void setM_start(Calendar m_start) {
		this.m_start = m_start;
	}

	/**
	 * @return the m_end
	 */
	public Calendar getM_end() {
		return m_end;
	}

	/**
	 * @param m_end the m_end to set
	 */
	public void setM_end(Calendar m_end) {
		this.m_end = m_end;
	}

	/**
	 * @return the m_position
	 */
	public String getM_position() {
		return m_position;
	}

	/**
	 * @param m_position the m_position to set
	 */
	public void setM_position(String m_position) {
		this.m_position = m_position;
	}

	/**
	 * @return the m_person
	 */
	public String getM_person() {
		return m_person;
	}

	/**
	 * @param m_person the m_person to set
	 */
	public void setM_person(String m_person) {
		this.m_person = m_person;
	}

	/**
	 * @return the m_uuid
	 */
	public String getM_uuid() {
		return m_uuid;
	}

	/**
	 * @param m_uuid the m_uuid to set
	 */
	public void setM_uuid(String m_uuid) {
		this.m_uuid = m_uuid;
	}
	
	/**
	 * @see DB_Object#getName()
	 */
	@Override
	public String getName() {
		return DateUtils.CalendarToString(m_start);
	}

	/**
	 * @see DB_Object#getPKID()
	 */
	@Override
	public int getPKID() {
		return 0;
	}
	
	/**
	 * @see DB_Object#getPKIDStr()
	 */
	@Override
	public String getPKIDStr() {
		return m_uuid;
	}
	/*-------------------------------------------------------------------*/
	/*  Setters and getters end
	/*--------------------------------------------------------------------*/

	
	/*-------------------------------------------------------------------*/
	/* SQL getters start
	/*--------------------------------------------------------------------*/
	/**
	 * @see DB_Object#getInsert_sql()
	 */
	@Override
	public String getInsert_sql() {
		m_uuid=UUID.randomUUID().toString();
		return Tables.PROC_INSERT_DUTY;
	}
	
	/**
	 * @see DB_Object#getUpdate_sql()
	 */
	@Override
	public String getUpdate_sql() {	
		return Tables.PROC_UPDATE_DUTY;
	}

	/**
	 * @see DB_Object#getDelete_sql()
	 */
	@Override
	public String getDelete_sql() {			
		return Tables.PROC_DELETE_DUTY;		
	}
	/*-------------------------------------------------------------------*/
	/* SQL getters end
	/*--------------------------------------------------------------------*/
	
	/**
	 * @see DB_Object#matches(DB_Object, boolean)
	 */
	@Override
	public
	boolean matches(DB_Object details, boolean use_id) {
		if(details != null)
		{
			Duty duty=(Duty) details;
			boolean id=true;
			if(use_id)
			{
				id= m_uuid==duty.getM_uuid();
			}

			return id &&
					m_start.equals(duty.getM_start()) &&
					m_end.equals(duty.getM_end()) &&
					m_person.equals(duty.getM_person()) &&
				    m_position.equals(duty.getM_position());			
		}
		return false;
	}

	/**
	 * @see DB_Object#populateProperites(ResultSet)
	 */
	@Override
	public void populateProperites(ResultSet rs) {
		try {
			
			if(rs.getTimestamp("DUTY_START_TIME")!=null)
			{
				m_start.setTime(rs.getTimestamp("DUTY_START_TIME"));
			}
			
			if(rs.getTimestamp("DUTY_END_TIME")!=null)
			{
				m_end.setTime(rs.getTimestamp("DUTY_END_TIME"));
			}		
			
			
			m_person=rs.getString("PERSON_NAME");
			m_position=rs.getString("PLACE_NAME");
			m_uuid=rs.getString("DUTY_KEY");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Checks if a person is scheduled to work on specified date
	 * @param place_id Positions id
	 * @param date Date
	 * @return Person id if it's found, 0 if not
	 */
	public String isPersonOn(String place_id, String date)
	{
		boolean start_b=DateUtils.isCalendarBetween(m_start, m_end, date, null, true);
		if(m_position!=null && m_person!=null && place_id.equals(m_position) && start_b)
		{
			return m_person;
		}
		return null;
	}

	/**
	 * @see DB_Object#populateProperties(DB_Object)
	 */
	@Override
	public void populateProperties(DB_Object details) {
		if(details!=null)
		{
			Duty duty=(Duty) details;
			m_uuid=duty.getM_uuid();
			m_start=duty.getM_start();
			m_end=duty.getM_end();
			m_person=duty.getM_person();
			m_position=duty.getM_position();
			
		}		
	}
	
	/**
	 * Checks if a conflict exists for a person in a specified period.
	 * @param person_id Person id to check
	 * @param start Duty start
	 * @param end Duty end
	 * @return True if there is a conflict, false otherwise
	 */
	public boolean personConflict(DB_Object duty)
	{
		if(m_person != null && duty!=null && ((Duty)duty).getM_person().equals(m_person))
		{
			boolean start_match=DateUtils.isCalendarBetween(m_start, m_end, ((Duty)duty).getM_start(), null, true);
			boolean end_match=DateUtils.isCalendarBetween(m_start, m_end, ((Duty)duty).getM_end(), null, true);
			
			if(start_match || end_match)
			{
				return true;
			}	
			
		}
		return false;
	}
	
	/**
	 * Calculates persons hours for a specified period
	 * @param employee_id Employee to calculate hours for
	 * @param start Start of a period
	 * @param end End of a period
	 * @return Total number of employee hours
	 */
	public double getTotalEmpoloyeeHours(String employee_id, String start, String end)
	{
		double hours = 0;

		if (m_person !=null && m_start!=null && m_end!=null && m_person.equals(employee_id)) {
		
			String start_str=DateUtils.CalendarToString(m_start);
			boolean start_match = DateUtils.isCalendarBetween(start, end,
					start_str, true);
			if (start_match) {
				hours += DateUtils.getSpanMinutes(m_start, m_end);
			}
		} 
		return hours / 60;
	}

	/**
	 * Checks if a specified data matches member variables
	 * @param person_id Person id to check
	 * @param position_id Position id to check
	 * @param datetime Date/time to check
	 * @return True if there is a match, false otherwise.
	 */
	public boolean matches(String person_id, String position_id, String datetime) {
		if(m_person.equals(person_id) && m_position.equals(position_id) && 
				DateUtils.isCalendarBetween(m_start, m_end, datetime, null, true))
		{
			return true;
		}
		return false;
	}
	
	/**
	 * @see DB_Object#toStringArray()
	 */
	@Override
	public String[] toStringArray() {
		String[] str_array=	{DateUtils.CalendarToString(m_start),
		m_position,
		m_person,
		DateUtils.CalendarToString(m_end),
		m_uuid};
		return str_array;
	}
	
	

}
