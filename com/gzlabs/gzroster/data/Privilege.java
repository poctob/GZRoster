package com.gzlabs.gzroster.data;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.gzlabs.gzroster.sql.Tables;

/**
 * Representation of the privilege object.  
 * @author apavlune
 *
 */
public class Privilege extends DB_Object {
	/**************************************************************************
	 * Member variables
	 */
	
	/**
	 * primary key
	 */
	private int m_id;
	
	/**
	 * Privilege name
	 */
	private String m_name;
	/*************************************************************************/
	
	/**
	 * Default constructor
	 */
	public Privilege()
	{
		this("",0);
	}
	
	/**
	 * Overloaded constructor takes name as a parameter
	 * @param m_name Privilege name.
	 */
	public Privilege(String m_name) {
		this(m_name, 0);
	}

	/**
	 * Overloaded constructor, initializes all variables.
	 * @param p_name Name of the object.
	 * @param p_pkid Primary key.
	 */
	public Privilege(String p_name, int p_pkid)
	{
		super();
		this.m_name=p_name;
		this.m_id=p_pkid;
	}
	
	/*-------------------------------------------------------------------*/
	/* SQL getters start
	/*--------------------------------------------------------------------*/
	/**
	 * @see DB_Object#getInsert_sql()
	 */
	@Override
	public String getInsert_sql() {
		return Tables.PROC_INSERT_PRIVILEGE;
	}

	/**
	 * @see DB_Object#getUpdate_sql()
	 */
	@Override
	public String getUpdate_sql() {
		return Tables.PROC_UPDATE_PRIVILEGE;
	}

	/**
	 * @see DB_Object#getDelete_sql()
	 */
	@Override
	public String getDelete_sql() {
		return Tables.PROC_DELETE_PRIVILEGE;
	}
	/*-------------------------------------------------------------------*/
	/* SQL getters end
	/*--------------------------------------------------------------------*/
	
	
	/**
	 * @see DB_Object#populateProperites(ResultSet)
	 */
	@Override
	public void populateProperites(ResultSet rs) {
		try {
			m_id = rs.getInt("pkid");
 			m_name =safeStringAssign( rs.getString("Name"));
		} catch (SQLException e) {
			e.printStackTrace();
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
		Privilege privilege=(Privilege)details;
		if(privilege.getPKID()!=0)
		{
			m_id = privilege.getPKID();
		}
		m_name =privilege.getName();
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
	 * @see DB_Object#matches(DB_Object, boolean)
	 */
	@Override
	public boolean matches(DB_Object details, boolean use_id) {
		if (details != null) {
			boolean id = true;
			Privilege privilege=(Privilege)details;
			if (use_id) {
				id = m_id == privilege.getPKID();
			}
			return id && m_name.equals(privilege.getName())	;
		}
		return false;
	}

	/**
	 * @see DB_Object#toStringArray()
	 */
	@Override
	public String[] toStringArray() {
		String[] str_array=	{m_name,
				getPKIDStr()};
		return str_array;
	}

}
