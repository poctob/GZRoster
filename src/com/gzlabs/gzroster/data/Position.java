package com.gzlabs.gzroster.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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
	String getInsert_sql() {

		String sql = INSERT_STM;
		String cols = "PLACE_ID,PLACE_NAME,NOTE";
		String vals = "'" + m_id + "','" + m_name + "','" + m_note + "'";

		sql = sql.replace(COL_CLAUSE, cols);
		sql = sql.replace(VAL_CLAUSE, vals);
		return sql;
	}

	@Override
	void populateProperites(ResultSet rs) {
		try {
			m_id = rs.getInt("PLACE_ID");
			m_name =safeStringAssign( rs.getString("PLACE_NAME"));
			m_note =safeStringAssign( rs.getString("NOTE"));
		} catch (SQLException e) {
			e.printStackTrace();
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
	ArrayList<String> toSortedArray() {
		ArrayList<String> properties = new ArrayList<String>();
		properties.add(Integer.toString(m_id));
		properties.add(m_name);
		properties.add(m_note);
		return properties;
	}

	@Override
	void populateProperties(ArrayList<String> details) {
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
	String getTableName() {
		return "PLACE";
	}

	@Override
	ArrayList<String> getDelete_sql() {
		ArrayList<String> retval=new ArrayList<String>();	

		String sql = DELETE_STM;
		sql = sql.replace(FROM_CLAUSE, getTableName());
		sql = sql.replace(WHERE_CLAUSE, "PLACE_ID='" + m_id + "'");
		retval.add(sql);
		return retval;
	}

	@Override
	String getUpdate_sql() {
		String sql = UPDATE_STM;
		sql = sql.replace(FROM_CLAUSE, getTableName());
		sql = sql.replace(WHAT_CLAUSE, "PLACE_NAME='" + m_name + "',NOTE='"
				+ m_note + "'");
		sql = sql.replace(WHERE_CLAUSE, "PLACE_ID='" + m_id + "'");
		return sql;
	}

}
