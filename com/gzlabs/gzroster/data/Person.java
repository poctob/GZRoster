package com.gzlabs.gzroster.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

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
	private boolean m_active;
	private String m_email;

	private ArrayList<TimeOff> m_times_off;
	private ArrayList<String> m_positions;
	private ArrayList<String> m_privileges;

	/*************************************************************************/

	/*-------------------------------------------------------------------*/
	/*
	 * Constructors start
	 * /*--------------------------------------------------------------------
	 */
	/**
	 * Default constructor
	 */
	public Person() {
		this("", "", "", "", false, "", 0, null, null, null);
	}

	/**
	 * Overloaded constructor, takes in common properties
	 * 
	 * @param m_name
	 *            Name
	 * @param m_address
	 *            Address
	 * @param m_home_phone
	 *            Home phone
	 * @param m_mobile_phone
	 *            Mobiel phone
	 * @param m_active
	 *            Is person active?
	 * @param m_email
	 *            Email address
	 */
	public Person(String m_name, String m_address, String m_home_phone,
			String m_mobile_phone, boolean m_active, String m_email) {
		this(m_name, m_address, m_home_phone, m_mobile_phone, m_active,
				m_email, 0, null, null, null);
	}

	/**
	 * Overloaded constructor takes in all parameters
	 * 
	 * @param m_name
	 *            Name
	 * @param m_address
	 *            Address
	 * @param m_home_phone
	 *            Home phone
	 * @param m_mobile_phone
	 *            Mobiel phone
	 * @param m_active
	 *            Is person active?
	 * @param m_email
	 *            Email address
	 * @param p_pkid
	 *            Primary key
	 * @param p_times_off
	 *            Times off collection
	 * @param p_positions
	 *            positions collection
	 * @param p_privileges
	 *            Privileges collection
	 */
	public Person(String m_name, String m_address, String m_home_phone,
			String m_mobile_phone, boolean m_active, String m_email,
			int p_pkid, ArrayList<TimeOff> p_times_off,
			ArrayList<String> p_positions, ArrayList<String> p_privileges) {

		super();
		this.m_name = m_name;
		this.m_address = m_address;
		this.m_home_phone = m_home_phone;
		this.m_mobile_phone = m_mobile_phone;
		this.m_active = m_active;
		this.m_email = m_email;
		this.m_id = p_pkid;

		this.m_times_off = p_times_off != null ? p_times_off
				: new ArrayList<TimeOff>();
		this.m_positions = p_positions != null ? p_positions
				: new ArrayList<String>();
		this.m_privileges = p_privileges != null ? p_privileges
				: new ArrayList<String>();

	}

	/*-------------------------------------------------------------------*/
	/*
	 * Constructors End
	 * /*--------------------------------------------------------------------
	 */

	/*-------------------------------------------------------------------*/
	/*
	 * Setters and getters start
	 * /*--------------------------------------------------------------------
	 */
	/**
	 * Checks if this person is active
	 * 
	 * @return True or false
	 */
	public boolean isActive() {
		return m_active;
	}

	/**
	 * @return the m_positions
	 */
	public ArrayList<String> getM_positions() {
		return m_positions;
	}

	/**
	 * @param m_name
	 *            the m_name to set
	 */
	public void setM_name(String m_name) {
		this.m_name = m_name;
	}

	/**
	 * @return the m_address
	 */
	public String getM_address() {
		return m_address;
	}

	/**
	 * @return the m_home_phone
	 */
	public String getM_home_phone() {
		return m_home_phone;
	}

	/**
	 * @return the m_mobile_phone
	 */
	public String getM_mobile_phone() {
		return m_mobile_phone;
	}

	/**
	 * @return the m_email
	 */
	public String getM_email() {
		return m_email;
	}

	/**
	 * @return the m_privileges
	 */
	public ArrayList<String> getM_privileges() {
		return m_privileges;
	}

	/**
	 * @param m_positions
	 *            the m_positions to set
	 */
	public void setM_positions(ArrayList<String> m_positions) {
		this.m_positions = m_positions;
	}

	/**
	 * @param m_positions
	 *            the m_positions to set
	 */
	public void setM_privileges(ArrayList<String> m_privileges) {
		this.m_privileges = m_privileges;
	}

	/**
	 * @see DB_Object#getName()
	 */
	@Override
	public String getName() {
		return m_name;
	}

	/**
	 * @see DB_Object#getPKID()
	 */
	@Override
	public int getPKID() {
		return m_id;
	}

	/**
	 * @see DB_Object#getPKIDStr()
	 */
	@Override
	public String getPKIDStr() {
		return Integer.toString(m_id);
	}


	/**
	 * Times off collection accessor.
	 * @return Times off.
	 */
	public ArrayList<TimeOff> getTimeOffs() {
		return m_times_off;
	}
	/*-------------------------------------------------------------------*/
	/*
	 * SQL getters start
	 * /*--------------------------------------------------------------------
	 */
	/**
	 * @see DB_Object#getInsert_sql()
	 */
	@Override
	public String getInsert_sql() {
		return Tables.PROC_INSERT_PERSON;
	}

	/**
	 * @see DB_Object#getUpdate_sql()
	 */
	@Override
	public String getUpdate_sql() {
		return Tables.PROC_UPDATE_PERSON;
	}

	/**
	 * @see DB_Object#getDelete_sql()
	 */
	@Override
	public String getDelete_sql() {
		return Tables.PROC_DELETE_PERSON;
	}

	/*-------------------------------------------------------------------*/
	/*
	 * SQL getters end
	 * /*--------------------------------------------------------------------
	 */

	/**
	 * Checks if this person is allowed to work on the position
	 * 
	 * @param pos_id
	 *            Primary key of the position
	 * @return True is this person is allowed to work this position
	 */
	public boolean isPositionAllowed(String pos_id) {
		if (m_positions != null) {
			for (String pos : m_positions) {
				if (pos != null && pos.equals(pos_id)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * @see DB_Object#populateProperites(ResultSet)
	 */
	@Override
	public void populateProperites(ResultSet rs) {
		if (rs != null) {
			try {
				m_id = rs.getInt("PERSON_ID");
				m_name = safeStringAssign(rs.getString("PERSON_NAME"));
				m_address = safeStringAssign(rs.getString("ADDRESS"));
				m_home_phone = safeStringAssign(rs.getString("PHONE_HOME"));
				m_mobile_phone = safeStringAssign(rs.getString("PHONE_MOBILE"));
				m_active = rs.getInt("ACTIVE_PERSON") > 0;
				m_email = safeStringAssign(rs.getString("EMAIL_ADDRESS"));
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @see DB_Object#populateProperties(DB_Object)
	 */
	@Override
	public void populateProperties(DB_Object details) {
		if (details == null) {
			return;
		}
		Person person = (Person) details;
		if (person.getPKID() != 0) {
			m_id = person.getPKID();
		}
		m_name = person.getName();
		m_address = person.getM_address();
		m_home_phone = person.getM_home_phone();
		m_mobile_phone = person.getM_mobile_phone();
		m_active = person.isActive();
		m_email = person.getM_email();
		m_times_off = person.getTimeOffs();
		if (person.getM_positions() != null
				&& person.getM_positions().size() > 0) {
			m_positions = person.getM_positions();
		}

		if (person.getM_privileges() != null
				&& person.getM_privileges().size() > 0) {
			m_privileges = person.getM_privileges();
		}

	}

	/**
	 * @see DB_Object#matches(DB_Object, boolean)
	 */
	@Override
	public boolean matches(DB_Object details, boolean use_id) {
		if (details != null) {
			Person person = (Person) details;
			boolean id = true;
			if (use_id) {
				id = m_id == person.getPKID();
			}
			return id && m_name.equals(person.getName())
					&& m_address.equals(person.getM_address())
					&& m_home_phone.equals(person.getM_home_phone())
					&& m_mobile_phone.equals(person.getM_mobile_phone())
					&& m_active == person.isActive()
					&& m_email.equals(person.getM_email());
		}
		return false;
	}

	/**
	 * Populates a list of time offs from the SQL result set
	 * 
	 * @param rs
	 *            Result set with time off data
	 */
	public void populateTimeOff(ResultSet rs) {
		if (rs != null) {
			m_times_off = new ArrayList<TimeOff>();
			try {
				while (rs.next()) {
					String pers_name = rs.getString("PERSON_NAME");
					if (pers_name.equals(m_name)) {
						Calendar start=new GregorianCalendar();
						if(rs.getTimestamp("START")!=null)
						{
							start.setTime(rs.getTimestamp("START"));
						}
						
						Calendar end=new GregorianCalendar();
						if(rs.getTimestamp("END")!=null)
						{
							end.setTime(rs.getTimestamp("END"));
						}
						
						TimeOff timeOff = new TimeOff(start,
								end, rs.getString("STATUS"),
								pers_name);
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
					if (to.isConflicting(start_time, end_time)) {
						return false;
					}
				}
			}
		}
		return true;
	}

	/**
	 * @see DB_Object#toStringArray()
	 */
	@Override
	public String[] toStringArray() {
		String[] str_array = { m_name, m_address, m_home_phone, m_mobile_phone,
				m_active ? "1" : "0", m_email, getPKIDStr() };
		return str_array;
	}

}
