package com.gzlabs.gzroster.data;

import java.sql.ResultSet;

/**
 * Database object base class
 * @author apavlune
 *
 */
public abstract class DB_Object{
	
	/**
	 * Generates Insert SQL statement
	 * 
	 * @return sql statement string.
	 */
	public abstract String getInsert_sql();
	
	/**
	 * Generates Insert SQL statement
	 * 
	 * @return sql statement string.
	 */
	public abstract String getUpdate_sql();
	
	/**
	 * Generates Delete SQL statement
	 * 
	 * @return sql statement string.
	 */
	public abstract String getDelete_sql();
	
	/**
	 * Populates object properties from the result set
	 * @param rs Result set to get data from
	 */
	public abstract void populateProperites(ResultSet rs);
	
	/**
	 * Populates object properties using a string list
	 * @param details Strings to get data from
	 */
	public abstract void populateProperties(DB_Object details);
	
	/**
	 * Gets object's name.
	 * @return Object's name.
	 */
	public abstract String getName();
	
	/**
	 * Gets object's name.
	 * @return Object's name.
	 */
	public abstract int getPKID();
	
	/**
	 * String representation of the primary key.
	 * @return Primary key as string.
	 */
	public String getPKIDStr()
	{
		return Integer.toString(getPKID()); 
	}
	
	/**
	 * Checks if the item matches to the supplied hash.
	 * @param details Hash with properties to match.
	 * @return True if matches, false otherwise.
	 */
	public abstract boolean matches(DB_Object details,boolean use_id);	

	/**
	 * Set a sting to the value, if the value is null, string is set to empty.
	 * @param variable Value to set the string to.
	 * @return Assigned string.
	 */
	protected String safeStringAssign(String variable)
	{
		return variable!=null?variable:"";
	}
	
	/**
	 * Converts object to string array.  Sort of a serialization.
	 * @return String array containing object's properties.
	 */
	public abstract String [] toStringArray();

}
