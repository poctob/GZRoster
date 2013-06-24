package com.gzlabs.gzroster.data;

import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * Database object base class
 * @author apavlune
 *
 */
public abstract class DB_Object{
	
	private boolean usingFB;
	/**
	 * Generates Insert SQL statement
	 * 
	 * @return sql statement string.
	 */
	public abstract String getInsert_sql(int pkid);
	
	/**
	 * Generate nextPKID SQL statement
	 * @return sql statement string.
	 */
	public abstract String getNexPKID_sql();
	
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
	public abstract ArrayList<String> getDelete_sql();
	
	/**
	 * Populates object properties from the result set
	 * @param rs Result set to get data from
	 */
	public abstract void populateProperites(ResultSet rs);
	
	/**
	 * Populates object properties using a string list
	 * @param details Strings to get data from
	 */
	public abstract void populateProperties(ArrayList<String> details);
	
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
	 * Checks if the item matches to the supplied hash.
	 * @param details Hash with properties to match.
	 * @return True if matches, false otherwise.
	 */
	public abstract boolean matches(ArrayList<String> details,boolean use_id);	

	/**
	 * Dumps object properties to string array
	 * @return ArrayList with object's properties.
	 */
	public abstract ArrayList<String> toSortedArray();

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
	 * @return the usingFB
	 */
	public boolean isUsingFB() {
		return usingFB;
	}

	/**
	 * @param usingFB the usingFB to set
	 */
	public void setUsingFB(boolean usingFB) {
		this.usingFB = usingFB;
	}

}
