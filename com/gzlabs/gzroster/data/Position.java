package com.gzlabs.gzroster.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.gzlabs.gzroster.sql.QueryFactory;
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
	private int m_id;
	private String m_name;
	private String m_note;

	/*************************************************************************/

	@Override
	public String getInsert_sql(int id) {
		m_id=id;
		String cols = "PLACE_NAME,NOTE";
		if(isUsingFB())
			cols+=",PLACE_ID";
		
		String vals = "'" + m_name + "','" + m_note + "'";
		if(isUsingFB())
			vals+=",'"+m_id+"'";
		
		return QueryFactory.getInsert(cols, vals, Tables.POSITION_TABLE_NAME);
	}

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

	@Override
	public String getName() {
		return m_name;
	}

	@Override
	public int getPKID() {
		return m_id;
	}

	@Override
	public boolean matches(ArrayList<String> details, boolean use_id) {
		if (details != null && details.size() == Tables.PLACE_MAX_COLS) {
			boolean id = true;
			if (use_id) {
				id = m_id == Integer.parseInt(details
						.get(Tables.PLACE_ID_INDEX));
			}
			return id && m_name.equals(details.get(Tables.PLACE_NAME_INDEX))
					&& m_note.equals(details.get(Tables.PLACE_NOTE_INDEX));
		}
		return false;
	}

	@Override
	public ArrayList<String> toSortedArray() {
		ArrayList<String> properties = new ArrayList<String>();
		properties.add(Integer.toString(m_id));
		properties.add(m_name);
		properties.add(m_note);
		return properties;
	}

	@Override
	public void populateProperties(ArrayList<String> details) {
		if (details == null || details.size() != Tables.PLACE_MAX_COLS) {
			return;
		}
		String id_str = details.get(Tables.PLACE_ID_INDEX);
		if (id_str.length() > 0) {
			m_id = Integer.parseInt(id_str);
		}

		m_name =safeStringAssign( details.get(Tables.PLACE_NAME_INDEX));
		m_note =safeStringAssign( details.get(Tables.PLACE_NOTE_INDEX));

	}

	@Override
	public ArrayList<String> getDelete_sql() {
		ArrayList<String> retval=new ArrayList<String>();	
		String sql = QueryFactory.getDelete
				("PLACE_ID", m_id, Tables.POSITION_TABLE_NAME);
		retval.add(sql);
		return retval;
	}

	@Override
	public String getUpdate_sql() {
		String what="PLACE_NAME='" + m_name + "',NOTE='"
				+ m_note + "'";
		return QueryFactory.getUpdate(what, "PLACE_ID", m_id, Tables.POSITION_TABLE_NAME);
	}

	@Override
	public String getNexPKID_sql() {		
		return QueryFactory.getNextPKIDFB(Tables.POSITION_TABLE_NAME);
	}

}
