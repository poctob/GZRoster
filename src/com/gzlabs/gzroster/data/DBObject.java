package com.gzlabs.gzroster.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import com.gzlabs.drosterheper.DBManager;

/**
 * Superclass for the database objects.
 * 
 * @author apavlune
 * 
 */
public class DBObject {

	/**
	 * SQL statements
	 */
	protected String table_name;

	/**
	 * Columns and values
	 */
	protected HashMap<String, String> cols;

	/**
	 * Database manager object.
	 */
	private DBManager dbman;

	/**
	 * Error tracker
	 */
	private String last_error;

	/**
	 * Default constructor. Initializes member variables.
	 * 
	 * @param dbman
	 *            Database manager.
	 * @param table
	 *            Table name.
	 */
	public DBObject(DBManager dbman, String table) {
		this.dbman = dbman;
		this.cols = new HashMap<String, String>();
		this.table_name = table;
	}

	/**
	 * Overloaded constructor. Adds properties to this object.
	 * 
	 * @param dbman
	 *            Database manager.
	 * @param table
	 *            Table name.
	 * @param detail
	 *            Hashmap with the properties
	 */
	public DBObject(DBManager dbman, String table,
			HashMap<String, String> details) {
		this.dbman = dbman;
		this.cols = details;
		this.table_name = table;
	}

	/***************************************************************/
	// Getters and setters
	/***************************************************************/
	public DBManager getDbman() {
		return dbman;
	}

	public void setDbman(DBManager dbman) {
		this.dbman = dbman;
	}

	public String getLast_error() {
		return last_error;
	}

	public HashMap<String, String> getCols() {
		return cols;
	}

	public void setCols(HashMap<String, String> details) {
		Object[] keys = details.keySet().toArray();
		for (Object s : keys) {
			cols.put((String) s, details.get(s));
		}
	}

	/***************************************************************/
	/**
	 * Adds a property to the list. Only non-null values for a key are allowed.
	 * 
	 * @param key
	 *            Property key.
	 * @param value
	 *            Property value.
	 */
	public void addProperty(String key, String value) {
		if (key != null) {
			cols.put(key, value);
		}
	}

	/**
	 * Accessor to return specified property.
	 * 
	 * @param key
	 *            Key to look for.
	 * @return Property value is returned.
	 */
	public String getProperty(String key) {
		return cols.get(key);
	}

	/**
	 * Convenience method to get values from the hashmap in the ArrayList
	 * format.
	 * 
	 * @return arraylist of hashmap values.
	 */
	public ArrayList<String> getValues() {
		ArrayList<String> values = new ArrayList<String>();
		Object[] keys = cols.keySet().toArray();
		for (Object s : keys) {
			values.add(cols.get(s));
		}
		return values;
	}

	/**
	 * Generates Insert SQL statement
	 * 
	 * @return sql statement string.
	 */
	private String getInsert_sql() {
		Object[] keys = cols.keySet().toArray();
		String sql = "INSERT INTO " + this.table_name + "(";
		for (Object s : keys) {
			sql += (String) s + ",";
		}
		sql = sql.substring(0, sql.length() - 1);
		sql += ") VALUES (";

		for (Object s : keys) {
			sql += "'" + cols.get(s) + "',";
		}
		sql = sql.substring(0, sql.length() - 1);
		sql += ")";
		return sql;
	}

	/**
	 * Generates update sql syntax
	 * 
	 * @param column
	 *            Column that will be used in WHERE clause
	 * @return a String containing the sql statement.
	 */
	private String getUpdate_sql(String column) {
		Object[] keys = cols.keySet().toArray();
		String sql = "UPDATE " + this.table_name + " SET ";
		for (Object s : keys) {
			if (cols.get(s) != null) {
				sql += (String) s + "='" + cols.get(s) + "',";
			}
		}
		sql = sql.substring(0, sql.length() - 1);
		sql += " WHERE " + column + "='" + cols.get(column) + "'";
		return sql;
	}

	/**
	 * Generates Select sql statement
	 * 
	 * @param column
	 *            Column with a parameter.
	 * @param value
	 *            Value of the parameter.
	 * @return sql statement string.
	 */
	private String getSelect_sql(String column, String value) {
		String sql = "SELECT * FROM " + this.table_name + " WHERE " + column
				+ "='" + value + "'";
		return sql;
	}

	/**
	 * Generates Delete sql statement.
	 * 
	 * @param column
	 *            that contains delete parameter.
	 * @return sql statement string.
	 */
	private String getDelete_sql(String column) {
		String value = cols.get(column);
		String sql = "DELETE FROM " + this.table_name + " WHERE " + column
				+ "='" + value + "'";
		return sql;
	}

	/**
	 * Creates new UUID for the primary key field.
	 */
	public void generateNewKey(String column) {
		cols.put(column, UUID.randomUUID().toString());
	}

	/**
	 * Checks if this record exists in the database.
	 * 
	 * @param column
	 *            Column when value is expected.
	 * @return true if it does, false if it doesn't.
	 */
	public boolean doesRecordExist(String column) {
		ResultSet rs = runSQL(getSelect_sql(column, cols.get(column)), true);
		try {
			if (rs == null || rs.isClosed() || !rs.next()) {
				return false;
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * Inserts new record to the database.
	 * 
	 * @return true is operation is success, false otherwise.
	 */
	public boolean insertNew() {
		ResultSet rs = runSQL(getInsert_sql(), false);

		if (rs == null) {
			last_error = "Unable to insert record!";
			return false;
		}
		return true;
	}

	public boolean updateRecord(String column) {
		ResultSet rs = runSQL(getUpdate_sql(column), false);
		if (rs == null) {
			last_error = "Unable to update record!";
			return false;
		}
		return true;
	}

	/**
	 * Deletes record from the database.
	 * 
	 * @return true is operation is success, false otherwise.
	 */
	public boolean deleteRecord(String column) {
		ResultSet rs = runSQL(getDelete_sql(column), false);
		if (rs == null) {
			last_error = "Unable to delete record!";
			return false;
		}
		return true;
	}

	/**
	 * Runs sql statement
	 * 
	 * @param sql_str
	 *            SQL statement to run
	 * @param wantresult
	 *            Whether the results are expected back.
	 * @return ResultSet
	 */
	private ResultSet runSQL(String sql_str, boolean wantresult) {
		if (dbman != null) {
			return dbman.runQuery(sql_str, wantresult);
		}
		return null;
	}

	/**
	 * Fetches and sets next pkID to the specified column.
	 * 
	 * @param key
	 *            Column for the pkID
	 */
	public void setNewPKID(String key) {
		if (key != null) {
			cols.put(key, getNextPKID());
		}
	}

	/**
	 * Retrieves next pkid from the database (Firebird crap)
	 * 
	 * @return next pkid
	 */
	private String getNextPKID() {
		ResultSet rs = dbman.runQuery("SELECT NEXT VALUE FOR " + table_name
				+ "_ID_GEN FROM RDB$DATABASE", true);
		try {
			if (rs.next()) {
				return rs.getString("GEN_ID");
			}
		} catch (SQLException e) {
			last_error = "Dabase Error! " + e.getMessage();
			e.printStackTrace();
		}
		return "0";
	}
}
