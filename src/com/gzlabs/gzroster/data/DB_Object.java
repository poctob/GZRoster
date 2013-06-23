package com.gzlabs.gzroster.data;

import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * Database object base class
 * @author apavlune
 *
 */
public abstract class DB_Object{

	//Clauses
	protected final static String WHAT_CLAUSE="%WHAT%";
	protected final static String COL_CLAUSE="%COLS%";
	protected final static String VAL_CLAUSE="%VALS%";
	protected final static String FROM_CLAUSE="%FROM%";
	protected final static String WHERE_CLAUSE="%WHERE%";
	
	//Statements
	protected final static String UPDATE_STM="UPDATE "+FROM_CLAUSE+" SET "+WHAT_CLAUSE+" WHERE "+WHERE_CLAUSE;
	protected final static String DELETE_STM="DELETE FROM "+FROM_CLAUSE+" WHERE "+WHERE_CLAUSE;
	protected final static String INSERT_STM="INSERT INTO "+FROM_CLAUSE+" ("+COL_CLAUSE+") VALUES ("+VAL_CLAUSE+")";
	
	/**
	 * Generates Insert SQL statement
	 * 
	 * @return sql statement string.
	 */
	abstract String getInsert_sql();
	
	/**
	 * Generates Insert SQL statement
	 * 
	 * @return sql statement string.
	 */
	abstract String getUpdate_sql();
	
	/**
	 * Generates Delete SQL statement
	 * 
	 * @return sql statement string.
	 */
	abstract ArrayList<String> getDelete_sql();
	
	/**
	 * Populates object properties from the result set
	 * @param rs Result set to get data from
	 */
	abstract void populateProperites(ResultSet rs);
	
	/**
	 * Populates object properties using a string list
	 * @param details Strings to get data from
	 */
	abstract void populateProperties(ArrayList<String> details);
	
	/**
	 * Gets object's name.
	 * @return Object's name.
	 */
	abstract String getName();
	
	/**
	 * Gets object's name.
	 * @return Object's name.
	 */
	abstract int getPKID();
	
	/**
	 * Checks if the item matches to the supplied hash.
	 * @param details Hash with properties to match.
	 * @return True if matches, false otherwise.
	 */
	abstract boolean matches(ArrayList<String> details,boolean use_id);	

	/**
	 * Dumps object properties to string array
	 * @return ArrayList with object's properties.
	 */
	abstract ArrayList<String> toSortedArray();
	
	/**
	 * 
	 * @return Table name associated with this object.
	 */
	abstract String getTableName();
	
	protected String safeStringAssign(String variable)
	{
		return variable!=null?variable:"";
	}

}
