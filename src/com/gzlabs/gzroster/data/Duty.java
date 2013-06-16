package com.gzlabs.gzroster.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class Duty extends DB_Object {

	/**
	 * @return the m_start
	 */
	public Date getM_start() {
		return m_start;
	}

	/**
	 * @param m_start the m_start to set
	 */
	public void setM_start(Date m_start) {
		this.m_start = m_start;
	}

	/**
	 * @return the m_end
	 */
	public Date getM_end() {
		return m_end;
	}

	/**
	 * @param m_end the m_end to set
	 */
	public void setM_end(Date m_end) {
		this.m_end = m_end;
	}

	/**
	 * @return the m_position
	 */
	public Position getM_position() {
		return m_position;
	}

	/**
	 * @param m_position the m_position to set
	 */
	public void setM_position(Position m_position) {
		this.m_position = m_position;
	}

	/**
	 * @return the m_person
	 */
	public Person getM_person() {
		return m_person;
	}

	/**
	 * @param m_person the m_person to set
	 */
	public void setM_person(Person m_person) {
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

	private Date m_start;
	private Date m_end;
	private Position m_position;
	private Person m_person;
	private String m_uuid;

	@Override
	String getInsert_sql(int pkid) {
		m_uuid=UUID.randomUUID().toString();
		String sql = INSERT_STM;
		String cols="DUTY_START_TIME," +
				"PLACE_ID," +
				"PERSON_ID,"+
				"DUTY_END_TIME,"+
				"DUTY_KEY,"+
				"APPROVED";
		;
		String vals="'"+DateUtils.DateToString(m_start)+"','"+
				m_position.getPKID()+"','"+
				m_person.getPKID()+"','"+
				DateUtils.DateToString(m_end)+"','"+
				m_uuid+"','Y'";
		
		sql=sql.replace(COL_CLAUSE, cols);
		sql=sql.replace(VAL_CLAUSE, vals);
		return sql;
	}

	@Override
	String getUpdate_sql() {
		String sql = UPDATE_STM;				
		sql=sql.replace(FROM_CLAUSE, getTableName());
		sql=sql.replace(WHAT_CLAUSE, "DUTY_START_TIME='"+DateUtils.DateToString(m_start)+
				"',PLACE_ID='"+m_position.getPKID()+
				"',PERSON_ID='"+m_person.getPKID()+
				"',DUTY_END_TIME='"+DateUtils.DateToString(m_end)+
				"',DUTY_KEY='"+m_uuid+"'");
		sql=sql.replace(WHERE_CLAUSE, "DUTY_KEY='"+m_uuid+"'");
		return sql;		
	}

	@Override
	String getDelete_sql() {
		String sql = DELETE_STM;						
		sql=sql.replace(WHERE_CLAUSE, "DUTY_KEY='"+m_uuid+"'");
		return sql;		
	}

	public Duty populateProperites(ResultSet rs, ArrayList<DB_Object> postions,  ArrayList<DB_Object> persons) {
		try {
			m_start = rs.getDate("DUTY_START_TIME");
			m_end = rs.getDate("DUTY_END_TIME");
			m_uuid = rs.getString("DUTY_KEY");
			
			int person_id = rs.getInt("PERSON_ID");
			int place_id = rs.getInt("PLACE_ID");	
			
			m_person=(Person) DB_Factory.getObjectByPKID(persons, person_id);
			m_position=(Position) DB_Factory.getObjectByPKID(postions, place_id);
			
			if(m_person == null || m_position == null)
			{
				return null;
			}
			return this;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;

	}

	void populateProperties(ArrayList<String> details, ArrayList<DB_Object> postions,  ArrayList<DB_Object> persons) {
		if(details == null || details.size() != Tables.DUTIES_MAX_COLS)
		{
			return;
		}
		
		String start_str=details.get(Tables.DUTIES_START_INDEX);
		if(start_str.length()>0)
		{
			m_start=DateUtils.StringToDate(start_str);
		}
		
		String end_str=details.get(Tables.DUTIES_END_INDEX);
		if(end_str.length()>0)
		{
			m_end=DateUtils.StringToDate(end_str);
		}
		m_uuid=details.get(Tables.DUTIES_KEY_INDEX);
		
		String person_id=details.get(Tables.DUTIES_PERSON_ID_INDEX);
		m_person=(Person) DB_Factory.getObjectByName(persons, person_id);
		
		String place_id=details.get(Tables.DUTIES_PLACE_ID_INDEX);
		m_position=(Position)DB_Factory.getObjectByName(postions, place_id);
	}

	@Override
	String getName() {
		return DateUtils.DateToString(m_start);
	}

	@Override
	int getPKID() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	boolean matches(ArrayList<String> details, boolean use_id) {
		if(details != null && details.size() == Tables.DUTIES_MAX_COLS)
		{
			boolean id=true;
			if(use_id)
			{
				id= m_uuid==details.get(Tables.DUTIES_KEY_INDEX);
			}
			
			String start_str=DateUtils.DateToString(m_start);
			String end_str=DateUtils.DateToString(m_end);
			return id &&
					start_str.equals(details.get(Tables.DUTIES_START_INDEX)) &&
					end_str.equals(details.get(Tables.DUTIES_END_INDEX)) &&
					m_person.getName().equals(details.get(Tables.DUTIES_PERSON_ID_INDEX)) &&
				    m_position.getName().equals(details.get(Tables.DUTIES_PLACE_ID_INDEX));			
		}
		return false;
	}

	@Override
	ArrayList<String> toSortedArray() {
		ArrayList<String> properties=new ArrayList<String>();
		properties.add(DateUtils.DateToString(m_start));
		properties.add(Integer.toString(m_position.getPKID()));
		properties.add(Integer.toString(m_person.getPKID()));
		properties.add(DateUtils.DateToString(m_end));
		properties.add(m_uuid);
		return properties;
	}

	@Override
	String getTableName() {
		return "DUTIES";
	}

	@Override
	void populateProperites(ResultSet rs) {
		// TODO Auto-generated method stub
		
	}
	
	public int isPersonOn(int place_id, String date)
	{
		boolean start_b=DateUtils.isCalendarBetween(m_start, m_end, date, null, true);
		if( place_id==m_position.getPKID() && start_b)
		{
			return m_person.getPKID();
		}
		return 0;
	}

	@Override
	void populateProperties(ArrayList<String> details) {
		// TODO Auto-generated method stub
		
	}
	
	public boolean personConflict(int person_id, String start, String end)
	{
		if(person_id==m_person.getPKID())
		{
			boolean start_match=DateUtils.isCalendarBetween(m_start, m_end, start, null, true);
			boolean end_match=DateUtils.isCalendarBetween(m_start, m_end, end, null, true);
			
			if(start_match || end_match)
			{
				return true;
			}	
			
		}
		return false;
	}
	
	public String getTotalEmpoloyeeHours(int employee_id, String start, String end)
	{
		double hours = 0;

		if (m_person.getPKID()==employee_id) {
		
			String start_str=DateUtils.DateToString(m_start);
			boolean start_match = DateUtils.isCalendarBetween(start, end,
					start_str, true);
			if (start_match) {
				hours += DateUtils.getSpanMinutes(m_start, m_end);
			}
		}

		double minutes = hours / 60;
		return String.format("%.2f", minutes);
	}

	public boolean matches(int person_id, int position_id, String datetime) {
		if(m_person.getPKID()==person_id && m_position.getPKID()==position_id && 
				DateUtils.isCalendarBetween(m_start, m_end, datetime, null, true))
		{
			return true;
		}
		return false;
	}

}
