package com.gzlabs.gzroster.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import com.gzlabs.gzroster.sql.Tables;

/**
 * Position table representation.  Straight abstract class representation.
 * @author apavlune
 *
 */
public class Position extends DB_Object {

	/**************************************************************************
	 * Member variables
	 */
	
	/**
	 * Primary key.
	 */
	private int m_id;
	
	/**
	 * Position name.
	 */
	private String m_name;
	
	/**
	 * Note? Not sure if user ever.
	 */
	private String m_note;

	/*************************************************************************/
	
	/**
	 * Default constructor.
	 */
	public Position()
	{
		this("","",0);
	}
	
	/**
	 * Overloaded constructor.  Sets all variables sans the primary key.
	 * @param m_name Name of the position
	 * @param m_note Note
	 */
	public Position(String m_name, String m_note) {
		this(m_name, m_note, 0);
	}
	
	/**
	 * Overloaded constructor.  Sets all variables.
	 * @param m_name Name of the position
	 * @param m_note Note
	 * @param p_pkid Primary key.
	 */
	public Position(String m_name, String m_note, int p_pkid) {
		super();
		this.m_name = m_name;
		this.m_note = m_note;
		this.m_id=p_pkid;
	}
	

	/**
	 * @see DB_Object#populateProperites(ResultSet)
	 */
	@Override
	public void populateProperites(ResultSet rs) {
		try {
			m_id = rs.getInt("PLACE_ID");
 			m_name =safeStringAssign( rs.getString("PLACE_NAME"));
			m_note =safeStringAssign( rs.getString("NOTE"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	 /*-------------------------------------------------------------------*/
	 /*  Setters and getters start
	 /*--------------------------------------------------------------------*/

	/**
	 * Note getter.
	 * @return m_note variable
	 */
	public String getNote() {		
		return m_note;
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
	
	
	/*-------------------------------------------------------------------*/
	/*  Setters and getters end
	/*--------------------------------------------------------------------*/
	
	
	/*-------------------------------------------------------------------*/
	/* SQL getters start
	/*--------------------------------------------------------------------*/

	/**
	 * @see DB_Object#getDelete_sql()
	 */
	@Override
	public String getDelete_sql() {
		return Tables.PROC_DELETE_POSITION;
	}

	/**
	 * @see DB_Object#getUpdate_sql()
	 */
	@Override
	public String getUpdate_sql() {
		return Tables.PROC_UPDATE_POSITION;
	}

	/**
	 * @see DB_Object#getInsert_sql()
	 */
	@Override
	public String getInsert_sql() {
		return Tables.PROC_INSERT_POSITION;
	}
	/*-------------------------------------------------------------------*/
	/* SQL getters end
	/*--------------------------------------------------------------------*/
	
	/**
	 * @see DB_Object#matches(DB_Object, boolean)
	 */
	@Override
	public boolean matches(DB_Object details, boolean use_id) {
		if (details != null) {
			boolean id = true;
			Position position=(Position)details;
			if (use_id) {
				id = m_id == position.getPKID();
			}
			return id && m_name.equals(position.getName())
					&& m_note.equals(position.getNote());
		}
		return false;
	}

	/**
	 * @see DB_Object#populateProperites(ResultSet)
	 */
	@Override
	public void populateProperties(DB_Object details) {
		if (details == null) {
			return;
		}
		Position position=(Position)details;
		if(position.getPKID()!=0)
		{
			m_id = position.getPKID();
		}
		m_name =position.getName();
		m_note =position.getNote();

	}

	/**
	 * @see DB_Object#toStringArray()
	 */
	@Override
	public String[] toStringArray() {
		String[] str_array=	{m_name,
				m_note,
				getPKIDStr()};
		return str_array;
	}
	


}
