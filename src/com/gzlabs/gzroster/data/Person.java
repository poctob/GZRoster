package com.gzlabs.gzroster.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Data representation of a person object from the database.
 * 
 */
public class Person extends DB_Object {

	/**************************************************************************
	 * Member variables
	 */
	private int m_id;
	private String m_name;
	private String m_address;
	private String m_home_phone;
	private String m_mobile_phone;
	private String m_note;
	private boolean m_active;
	private String m_email;
	private String m_uuid;

	private ArrayList<TimeOff> m_times_off;
	private ArrayList<Integer> m_positions;

	/**
	 * @return the m_positions
	 */
	public ArrayList<Integer> getM_positions() {
		return m_positions;
	}

	/**
	 * @param m_positions the m_positions to set
	 */
	public void setM_positions(ArrayList<Integer> m_positions) {
		this.m_positions = m_positions;
	}
	
	public boolean isPositionAllowed(int pos_id)
	{
		for(int pos:m_positions)
		{
			if(pos==pos_id)
			{
				return true;
			}
		}
		return false;
	}

	/*************************************************************************/
	@Override
	String getInsert_sql(int pkid) {

		m_id = pkid;
		m_uuid=UUID.randomUUID().toString();
		String sql = INSERT_STM;
		String cols = "PERSON_ID," +
				"PERSON_NAME," +
				"ADDRESS,"+
				"PHONE_HOME,"+
				"PHONE_MOBILE,"+
				"NOTE,"+
				"ACTIVE_PERSON,"+
				"EMAIL_ADDRESS,"+
				"EMPLOYEE_KEY";
		
		int active=m_active?1:0;
		
		String vals = "'" + m_id + "','" +
				m_name + "','" +
				m_address + "','" +
				m_home_phone + "','" +
				m_mobile_phone + "','" +
				m_note + "','" +
				active + "','" +
				m_email + "','" +
				m_uuid+"'";

		sql = sql.replace(COL_CLAUSE, cols);
		sql = sql.replace(VAL_CLAUSE, vals);
		return sql;
	}

	@Override
	String getUpdate_sql() {
		String sql = UPDATE_STM;				
		sql=sql.replace(FROM_CLAUSE, getTableName());
		String active=m_active?"1":"0";
		sql=sql.replace(WHAT_CLAUSE, "PERSON_NAME='"+m_name+
				"',ADDRESS='"+m_address+
				"',PHONE_HOME='"+m_home_phone+
				"',PHONE_MOBILE='"+m_mobile_phone+
				"',NOTE='"+m_note+
				"',ACTIVE_PERSON='"+active+
				"',EMAIL_ADDRESS='"+m_email+
				"',EMPLOYEE_KEY='"+m_uuid+"'");
		sql=sql.replace(WHERE_CLAUSE, "PERSON_ID='"+m_id+"'");
		return sql;		
	}

	@Override
	String getDelete_sql() {
		String sql = DELETE_STM;						
		sql=sql.replace(WHERE_CLAUSE, "PERSON_ID='"+m_id+"'");
		return sql;		
		}

	@Override
	void populateProperites(ResultSet rs) {
		if (rs != null) {
			try {
				m_id=rs.getInt("PERSON_ID");
				m_name=rs.getString("PERSON_NAME");
				m_address=rs.getString("ADDRESS");
				m_home_phone=rs.getString("PHONE_HOME");
				m_mobile_phone=rs.getString("PHONE_MOBILE");
				m_note=rs.getString("NOTE");
				m_active=rs.getInt("ACTIVE_PERSON")>0;
				m_email=rs.getString("EMAIL_ADDRESS");
				m_uuid=rs.getString("EMPLOYEE_KEY");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	void populateProperties(ArrayList<String> details) {
		if(details == null || details.size() != Tables.PERSON_MAX_COLS)
		{
			return;
		}
		String id_str=details.get(Tables.PERSON_ID_INDEX);
		if(id_str.length()>0)
		{
			m_id=Integer.parseInt(id_str);
		}
		
		m_name=details.get(Tables.PERSON_NAME_INDEX);		
		m_address=details.get(Tables.PERSON_ADDRESS_INDEX);
		m_home_phone=details.get(Tables.PERSON_HPHONE_INDEX);
		m_mobile_phone=details.get(Tables.PERSON_MPHONE_INDEX);
		m_note=details.get(Tables.PERSON_NOTE_INDEX);
		m_active=details.get(Tables.PERSON_ACTIVE_INDEX).equals("1");
		m_email=details.get(Tables.PERSON_EMAIL_INDEX);
		m_uuid=details.get(Tables.PLACE_UUID_INDEX);

	}

	public void populatePositions(ResultSet rs)
	{
		if(rs!=null)
		{
			m_positions=new ArrayList<Integer>();
			try {
				while (rs.next())
				{
					int pers_id=rs.getInt("PERSON_ID");
					if(pers_id==m_id)
					{
						m_positions.add(rs.getInt("PLACE_ID"));
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}	
	}
	@Override
	String getName() {
		return m_name;
	}

	@Override
	int getPKID() {
		return m_id;
	}

	@Override
	boolean matches(ArrayList<String> details, boolean use_id) {
		if(details != null && details.size() == Tables.PERSON_MAX_COLS)
		{
			boolean id=true;
			boolean uuid=true;
			if(use_id)
			{
				id= m_id==Integer.parseInt(details.get(Tables.PERSON_ID_INDEX));
				uuid=m_uuid.equals(details.get(Tables.PLACE_UUID_INDEX));
			}
			boolean active=details.get(Tables.PERSON_ACTIVE_INDEX).equals("1");
			return id &&
					m_name.equals(details.get(Tables.PERSON_NAME_INDEX)) &&
					m_address.equals(details.get(Tables.PERSON_ADDRESS_INDEX)) &&
					m_home_phone.equals(details.get(Tables.PERSON_HPHONE_INDEX)) &&
					m_mobile_phone.equals(details.get(Tables.PERSON_MPHONE_INDEX)) &&
					m_note.equals(details.get(Tables.PERSON_NOTE_INDEX)) &&
					m_active==active &&
					m_email.equals(details.get(Tables.PERSON_EMAIL_INDEX)) &&
					uuid;			
		}
		return false;
	}

	@Override
	ArrayList<String> toSortedArray() {
		ArrayList<String> properties=new ArrayList<String>();
		String active=m_active?"1":"0";
		properties.add(Integer.toString(m_id));
		properties.add(m_name);
		properties.add(m_address);
		properties.add(m_home_phone);
		properties.add(m_mobile_phone);
		properties.add(m_note);
		properties.add(active);
		properties.add(m_email);
		properties.add(m_uuid);
		return properties;
	}

	@Override
	String getTableName() {		
		return "PERSON";
	}

	public void populateTimeOff(ResultSet rs) {
		if(rs!=null)
		{
			m_times_off=new ArrayList<TimeOff>();
			try {
				while (rs.next())
				{
					int pers_id=rs.getInt("PERSON_ID");
					if(pers_id==m_id)
					{
						TimeOff timeOff=new TimeOff(rs.getDate("PERSON_NA_START_DATE_HOUR"),
								rs.getDate("PERSON_NA_END_DATE_HOUR"));
						m_times_off.add(timeOff);
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}	
		
	}

	public ArrayList<String> getTimesOff()
	{
		ArrayList<String> retval=new ArrayList<String>();
		for(TimeOff to:m_times_off)
		{
			if(to!=null)
				{
				String str_to="From:";
				str_to+=to.getStartStr();
				str_to+=" To:";
				str_to+=to.getEndStr();
				
				retval.add(str_to);
			}
			
		}
		return retval;
	}
	
	public boolean isTimeAllowed(String start_time, String end_time)
	{
		if(start_time!=null && end_time!=null)
		{
			for(TimeOff to:m_times_off)
			{
				if(to!=null)
				{
					return !(to.isConflicting(start_time, end_time));
				}
		
			}
		}
		return true;
	}

	public String getTimeOffInsertSql(String start, String end) {
		String sql = INSERT_STM;
		String cols = "PERSON_ID," +
				"PERSON_NA_START_DATE_HOUR," +
				"PERSON_NA_END_DATE_HOUR";
		
		String vals = "'" + m_id + "','" +
				start + "','" +
				end +"'";

		sql = sql.replace(COL_CLAUSE, cols);
		sql = sql.replace(VAL_CLAUSE, vals);
		return sql;
	}

	public String getDeleteTimeOffSql(String start, String end) {
		String sql = DELETE_STM;						
		sql=sql.replace(WHERE_CLAUSE, "PERSON_ID='"+m_id+"' AND PERSON_NA_START_DATE_HOUR='"+start+
				"' AND PERSON_NA_END_DATE_HOUR='"+end+"'");
		return sql;		
	}	
	
	public ArrayList<String> getPersonToPositionsInsertSql() {
		ArrayList<String> retval=new ArrayList<String>();

		for(Integer i:m_positions)
		{
			String sql = INSERT_STM;
			String cols = "PERSON_ID," +
					"PLACE_ID";
			String vals="";
			vals = "'"+ m_id + "','" + i +"'";
			sql = sql.replace(COL_CLAUSE, cols);
			sql = sql.replace(VAL_CLAUSE, vals);
			
			retval.add(sql);
		}

		
		return retval;
	}

	public String getPersonToPositionsDeleteSql() {
		String sql = DELETE_STM;						
		sql=sql.replace(WHERE_CLAUSE, "PERSON_ID='"+m_id+"'");
		return sql;		
	}	

}
