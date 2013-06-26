package com.gzlabs.gzroster.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.gzlabs.gzroster.sql.QueryFactory;
import com.gzlabs.gzroster.sql.Tables;

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

	private ArrayList<TimeOff> m_times_off;
	private ArrayList<Integer> m_positions;

	/*************************************************************************/

	/**
	 * Checks if this person is active
	 * @return True or false
	 */
	boolean isActive()
	{
		return m_active;
	}
	/**
	 * @return the m_positions
	 */
	public ArrayList<Integer> getM_positions() {
		return m_positions;
	}

	/**
	 * @param m_positions
	 *            the m_positions to set
	 */
	public void setM_positions(ArrayList<Integer> m_positions) {
		this.m_positions = m_positions;
	}

	/**
	 * Checks if this person is allowed to work on the position
	 * 
	 * @param pos_id
	 *            Primary key of the position
	 * @return True is this person is allowed to work this position
	 */
	public boolean isPositionAllowed(int pos_id) {
		if (m_positions != null) {
			for (int pos : m_positions) {
				if (pos == pos_id) {
					return true;
				}
			}
		}
		return false;
	}

	/*************************************************************************/
	@Override
	public
	String getInsert_sql(int pkid) {	
		m_id=pkid;
		String cols = "PERSON_NAME," + "ADDRESS,"
				+ "PHONE_HOME," + "PHONE_MOBILE," + "NOTE," + "ACTIVE_PERSON,"
				+ "EMAIL_ADDRESS";
		
		if(isUsingFB())
			cols+=",PERSON_ID";
		
		int active = m_active ? 1 : 0;

		String vals = "'" + m_name + "','" + m_address + "','"
				+ m_home_phone + "','" + m_mobile_phone + "','" + m_note
				+ "','" + active + "','" + m_email + "'";
		
		if(isUsingFB())
			vals+=",'"+m_id+"'";

		return QueryFactory.getInsert(cols, vals, Tables.PERSON_TABLE_NAME);
	}

	@Override
	public String getUpdate_sql() {	
		String active = m_active ? "1" : "0";
		String what = "PERSON_NAME='" + m_name + "',ADDRESS='"
				+ m_address + "',PHONE_HOME='" + m_home_phone
				+ "',PHONE_MOBILE='" + m_mobile_phone + "',NOTE='" + m_note
				+ "',ACTIVE_PERSON='" + active + "',EMAIL_ADDRESS='" + m_email
				+ "'";
		return QueryFactory.getUpdate(what, "PERSON_ID", m_id, Tables.PERSON_TABLE_NAME);
	}

	@Override
	public ArrayList<String> getDelete_sql() {
		ArrayList<String> retval = new ArrayList<String>();
		String sql = QueryFactory.getDelete
				("PERSON_ID", m_id, Tables.PERSON_TABLE_NAME);
		retval.add(sql);
		return retval;
	}

	@Override
	public
	void populateProperites(ResultSet rs) {
		if (rs != null) {
			try {
				m_id = rs.getInt("PERSON_ID");
				m_name=safeStringAssign( rs.getString("PERSON_NAME"));
				m_address=safeStringAssign(rs.getString("ADDRESS"));
				m_home_phone=safeStringAssign(rs.getString("PHONE_HOME"));
				m_mobile_phone=safeStringAssign(rs.getString("PHONE_MOBILE"));
				m_note=safeStringAssign(rs.getString("NOTE"));
				m_active = rs.getInt("ACTIVE_PERSON") > 0;
				m_email=safeStringAssign(rs.getString("EMAIL_ADDRESS"));
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public
	void populateProperties(ArrayList<String> details) {
		if (details == null || details.size() != Tables.PERSON_MAX_COLS) {
			return;
		}
		String id_str = details.get(Tables.PERSON_ID_INDEX);
		if (id_str.length() > 0) {
			m_id = Integer.parseInt(id_str);
		}

		m_name=safeStringAssign(details.get(Tables.PERSON_NAME_INDEX));
		m_address=safeStringAssign(details.get(Tables.PERSON_ADDRESS_INDEX));
		m_home_phone=safeStringAssign(details.get(Tables.PERSON_HPHONE_INDEX));
		m_mobile_phone=safeStringAssign(details.get(Tables.PERSON_MPHONE_INDEX));
		m_note=safeStringAssign(details.get(Tables.PERSON_NOTE_INDEX));
		m_active = details.get(Tables.PERSON_ACTIVE_INDEX).equals("1");
		m_email=safeStringAssign(details.get(Tables.PERSON_EMAIL_INDEX));
	}

	/**
	 * Populates allowed positions list from the SQL result set
	 * 
	 * @param rs
	 *            Result set with position's ids
	 */
	public void populatePositions(ResultSet rs) {
		if (rs != null) {
			m_positions = new ArrayList<Integer>();
			try {
				while (rs.next()) {
					int pers_id = rs.getInt("PERSON_ID");
					if (pers_id == m_id) {
						m_positions.add(rs.getInt("PLACE_ID"));
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public
	String getName() {
		return m_name;
	}

	@Override
	public int getPKID() {
		return m_id;
	}

	@Override
	public boolean matches(ArrayList<String> details, boolean use_id) {
		if (details != null && details.size() == Tables.PERSON_MAX_COLS) {
			boolean id = true;
			boolean uuid = true;
			if (use_id) {
				id = m_id == Integer.parseInt(details
						.get(Tables.PERSON_ID_INDEX));
			}
			boolean active = details.get(Tables.PERSON_ACTIVE_INDEX)
					.equals("1");
			return id
					&& m_name.equals(details.get(Tables.PERSON_NAME_INDEX))
					&& m_address.equals(details
							.get(Tables.PERSON_ADDRESS_INDEX))
					&& m_home_phone.equals(details
							.get(Tables.PERSON_HPHONE_INDEX))
					&& m_mobile_phone.equals(details
							.get(Tables.PERSON_MPHONE_INDEX))
					&& m_note.equals(details.get(Tables.PERSON_NOTE_INDEX))
					&& m_active == active
					&& m_email.equals(details.get(Tables.PERSON_EMAIL_INDEX))
					&& uuid;
		}
		return false;
	}

	@Override
	public
	ArrayList<String> toSortedArray() {
		ArrayList<String> properties = new ArrayList<String>();
		String active = m_active ? "1" : "0";
		properties.add(Integer.toString(m_id));
		properties.add(m_name);
		properties.add(m_address);
		properties.add(m_home_phone);
		properties.add(m_mobile_phone);
		properties.add(m_note);
		properties.add(active);
		properties.add(m_email);
		return properties;
	}

	/**
	 * Populates a list of time offs from the sql result set
	 * 
	 * @param rs
	 *            Result set with time off data
	 */
	public void populateTimeOff(ResultSet rs) {
		if (rs != null) {
			m_times_off = new ArrayList<TimeOff>();
			try {
				while (rs.next()) {
					int pers_id = rs.getInt("PERSON_ID");
					if (pers_id == m_id) {
						TimeOff timeOff = new TimeOff(
								rs.getTimestamp("PERSON_NA_START_DATE_HOUR"),
								rs.getTimestamp("PERSON_NA_END_DATE_HOUR"));
						m_times_off.add(timeOff);
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * Returns a list of time offs in a string list format
	 * 
	 * @return List of times off
	 */
	public ArrayList<String> getTimesOff() {
		ArrayList<String> retval = new ArrayList<String>();
		if (m_times_off != null) {
			for (TimeOff to : m_times_off) {
				if (to != null) {
					String str_to = "From:";
					str_to += to.getStartStr();
					str_to += " To:";
					str_to += to.getEndStr();

					retval.add(str_to);
				}

			}
		}
		return retval;
	}

	/**
	 * Checks if this employee is allowed to work at the specified time period
	 * 
	 * @param start_time
	 *            Starting time of a shift
	 * @param end_time
	 *            Ending time of a shift
	 * @return True if this person is allowed to work this shift
	 */
	public boolean isTimeAllowed(String start_time, String end_time) {
		if (start_time != null && end_time != null && m_times_off != null) {
			for (TimeOff to : m_times_off) {
				if (to != null) {
					return !(to.isConflicting(start_time, end_time));
				}

			}
		}
		return true;
	}

	/**
	 * Convenience method to get insert SQL statement for the time off table.
	 * 
	 * @param start
	 *            Start of the time off
	 * @param end
	 *            End of the time off
	 * @return String containing insert SQL statement.
	 */
	public String getTimeOffInsertSql(String start, String end) {

		String cols = "PERSON_ID," + "PERSON_NA_START_DATE_HOUR,"
				+ "PERSON_NA_END_DATE_HOUR";

		String vals = "'" + m_id + "','" + start + "','" + end + "'";

		return QueryFactory.getInsert(cols, vals, Tables.TIME_OFF_TABLE_NAME);
	}

	/**
	 * Delete time off SQL statement
	 * 
	 * @param start
	 *            Start of the time off
	 * @param end
	 *            End of the time off
	 * @return String containing sql statement.
	 */
	public String getDeleteTimeOffSql(String start, String end) {
		ArrayList<String> cols=new ArrayList<String>();
		cols.add("PERSON_ID");
		cols.add("PERSON_NA_START_DATE_HOUR");
		cols.add("PERSON_NA_END_DATE_HOUR");
		
		ArrayList<String> vals=new ArrayList<String>();
		vals.add(Integer.toString(m_id));
		vals.add(start);
		vals.add(end);
		
		return QueryFactory.getDelete(cols, vals, Tables.TIME_OFF_TABLE_NAME);		
	}

	/**
	 * Generates a list of SQL statement to insert person to position mappings
	 * 
	 * @return SQL statement
	 */
	public ArrayList<String> getPersonToPositionsInsertSql() {
		ArrayList<String> retval = new ArrayList<String>();

		if (m_positions != null) {
			for (Integer i : m_positions) {
				String cols = "PERSON_ID," + "PLACE_ID";
				String vals = "";
				vals = "'" + m_id + "','" + i + "'";
				retval.add(QueryFactory.getInsert(cols, vals, Tables.PERSON_TO_PLACE_TABLE_NAME));
			}
		}
		return retval;
	}

	/**
	 * Generates SQL statement to delete person to position mappings.
	 * @return SQL statement.
	 */
	public String getPersonToPositionsDeleteSql() {
		return QueryFactory.getDelete
				("PERSON_ID", m_id, Tables.PERSON_TO_PLACE_TABLE_NAME);
	}
	@Override
	public String getNexPKID_sql() {
		return QueryFactory.getNextPKIDFB(Tables.PERSON_TABLE_NAME);
	}

}