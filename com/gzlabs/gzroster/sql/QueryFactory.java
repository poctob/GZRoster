package com.gzlabs.gzroster.sql;

import java.util.ArrayList;

import com.gzlabs.utils.WidgetUtilities;

/**
 * Provides query generation routines based on the currently used database types.
 * @author apavlune
 *
 */
public class QueryFactory {
	
	//Clauses
	protected final static String WHAT_CLAUSE="%WHAT%";
	protected final static String COL_CLAUSE="%COLS%";
	protected final static String VAL_CLAUSE="%VALS%";
	protected final static String FROM_CLAUSE="%FROM%";
	protected final static String WHERE_CLAUSE="%WHERE%";
	
	protected final static String AND_CLAUSE=" AND ";
	
	//Statements
	protected final static String UPDATE_STM="UPDATE "+FROM_CLAUSE+" SET "+WHAT_CLAUSE+" WHERE "+WHERE_CLAUSE;
	protected final static String DELETE_STM="DELETE FROM "+FROM_CLAUSE+" WHERE "+WHERE_CLAUSE;
	protected final static String INSERT_STM="INSERT INTO "+FROM_CLAUSE+" ("+COL_CLAUSE+") VALUES ("+VAL_CLAUSE+")";
	protected final static String SELECT_STM = "SELECT " + WHAT_CLAUSE + " FROM "+ FROM_CLAUSE + " WHERE " + WHERE_CLAUSE;
	protected final static String NEXT_PKID_STM ="SELECT NEXT VALUE FOR " + FROM_CLAUSE	+ "_ID_GEN FROM RDB$DATABASE";
	
	protected final static String CALL_PROC="CALL "+WHAT_CLAUSE+" ("+VAL_CLAUSE+")";

	
	/**
	 * Public access wrapper for the insert statement
	 * @param cols Column names
	 * @param vals Columns values
	 * @param table Table name
	 * @return Insert statement
	 */
	public static String getInsert(String cols, String vals, String table)
	{
		return get_insert_sql(cols, vals, table);
	}
	
	/**
	 * Generates MySQL Stored procedure execute clause.
	 * @param name Procedure name
	 * @param vals Variable list of parameters
	 * @return Call sproc statement
	 */
	public static String getProc(String name, String ... vals)
	{
		String args="";
		for(int i=0; i<vals.length; i++)
		{
			args+="'"+vals[i]+"',";
		}
		if(args.length()>0)
		{
			args=args.substring(0, args.length()-1);
		}
		
		String sql = CALL_PROC;
		sql=WidgetUtilities.safeStringReplace(sql, WHAT_CLAUSE, name);
		sql=WidgetUtilities.safeStringReplace(sql, VAL_CLAUSE, args);
		return sql;
	}
	
	/**
	 * Generates generic insert statement.
	 * @param cols Column name string
	 * @param vals Values name string
	 * @param table Name of the table
	 * @return Completed insert statement
	 */
	private static String get_insert_sql(String cols, String vals, String table)
	{
		String sql = INSERT_STM;
		sql=WidgetUtilities.safeStringReplace(sql, COL_CLAUSE, cols);
		sql=WidgetUtilities.safeStringReplace(sql, VAL_CLAUSE, vals);
		sql=WidgetUtilities.safeStringReplace(sql, FROM_CLAUSE, table);
		return sql;
	}	
	
	/**
	 * Public access wrapper for the delete statement
	 * @param cols Column names
	 * @param vals Columns values
	 * @param table Table name
	 * @return Delete statement
	 */
	public static String getDelete(String cols, String vals, String table)
	{
		return get_delete_sql(cols, vals, table);
	}
	
	/**
	 * Public access wrapper for the delete statement, convenience.
	 * @param cols Column names
	 * @param vals Columns values
	 * @param table Table name
	 * @return Delete statement
	 */
	public static String getDelete(String cols, int vals, String table)
	{
		return get_delete_sql(cols, Integer.toString(vals), table);
	}
	
	
	
	/**
	 * Generates generic delete statement.
	 * @param cols Column name string
	 * @param vals Values name string
	 * @param table Name of the table
	 * @return Completed delete statement
	 */
	private static String get_delete_sql(String cols, String vals, String table)
	{
		String sql = DELETE_STM;
		sql=WidgetUtilities.safeStringReplace(sql, FROM_CLAUSE, table);
		sql=WidgetUtilities.safeStringReplace(sql, WHERE_CLAUSE, cols+"='" +vals + "'");
		return sql;
	}
	
	/**
	 * Public access wrapper for the delete statement, uses multiple clauses
	 * @param cols Column names
	 * @param vals Columns values
	 * @param table Table name
	 * @return Delete statement
	 */
	public static String getDelete(ArrayList<String> cols, ArrayList<String> vals, String table)
	{
		return get_delete_sql(cols, vals, table);
	}
	
	
	/**
	 * Generates generic delete statement, uses multiple clauses
	 * @param cols Column name string
	 * @param vals Values name string
	 * @param table Name of the table
	 * @return Completed delete statement
	 */
	private static String get_delete_sql(ArrayList<String> cols, ArrayList<String> vals, String table)
	{
		if(cols==null || vals==null || cols.size()!=vals.size())
		{
			return null;
		}
		String sql = DELETE_STM;	
		String where="";
		
		for(int i=0; i<cols.size(); i++)
		{
			where+=cols.get(i)+"='"+vals.get(i)+"'"+AND_CLAUSE;			
		}
		where=where.substring(0, where.length()-AND_CLAUSE.length());
		sql=WidgetUtilities.safeStringReplace(sql, FROM_CLAUSE, table);
		sql=WidgetUtilities.safeStringReplace(sql, WHERE_CLAUSE, where);
		return sql;
	}
	
	/**
	 * Public access wrapper for the update statement.
	 * @param what New values
	 * @param col Column names
	 * @param val Columns values
	 * @param table Table name
	 * @return Update statement
	 */
	public static String getUpdate(String what, String col, String val,String table)
	{
		return get_update_sql(what, col, val, table);
	}
	
	/**
	 * Public access wrapper for the update statement, convenience.
	 * @param what New values
	 * @param col Column names
	 * @param val Columns values
	 * @param table Table name
	 * @return Update statement
	 */
	public static String getUpdate(String what, String col, int val,String table)
	{
		return get_update_sql(what, col, Integer.toString(val), table);
	}
		
	/**
	 * Generates generic update statement.
	 * @param what New values
	 * @param col Column name string
	 * @param val Values name string
	 * @param table Name of the table
	 * @return Completed update statement
	 */
	private static String get_update_sql(String what, String col, String val,String table)
	{
		String sql = UPDATE_STM;
		sql = WidgetUtilities.safeStringReplace(sql, FROM_CLAUSE, table);
		sql = WidgetUtilities.safeStringReplace(sql, WHAT_CLAUSE, what);
		sql = WidgetUtilities.safeStringReplace(sql, WHERE_CLAUSE, col+"='" + val + "'");
		return sql;
	}
	
	/**
	 * Public access wrapper for the select statement.
	 * @param what what to select
	 * @param col Column names
	 * @param val Columns values
	 * @param table Table name
	 * @return Select statement
	 */
	public static String getSelect(String what, String col, String val,String table)
	{
		return get_select_sql(what, col, val, table);
	}
		
	/**
	 * Generates generic select statement.
	 * @param what What to select
	 * @param col Column name string
	 * @param val Values name string
	 * @param table Name of the table
	 * @return Completed select statement
	 */
	private static String get_select_sql(String what, String col, String val,String table)
	{
		
		String sql = SELECT_STM;
		sql = WidgetUtilities.safeStringReplace(sql, WHAT_CLAUSE, what);
		sql = WidgetUtilities.safeStringReplace(sql, WHERE_CLAUSE, col+"='" + val + "'");
		sql = WidgetUtilities.safeStringReplace(sql, FROM_CLAUSE, table);
		return sql;
	}
	
	/**
	 * Next PKID statement for FireBird database
	 * @param table Table name
	 * @return next pkidStatement
	 */
	public static String getNextPKIDFB(String table)
	{
		String sql = NEXT_PKID_STM;
		sql = WidgetUtilities.safeStringReplace(sql, FROM_CLAUSE, table);
		return sql;
	}
	
}
