package com.gzlabs.gzroster.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.gzlabs.drosterheper.DBManager;

public class DB_Factory {

	// Clauses
	protected final static String WHAT_CLAUSE = " %WHAT% ";
	protected final static String COL_CLAUSE = " %COLS% ";
	protected final static String VAL_CLAUSE = " %VALS% ";
	protected final static String FROM_CLAUSE = " %FROM% ";
	protected final static String WHERE_CLAUSE = " %WHERE% ";

	// Statements
	protected final static String SELECT_STM = "SELECT" + WHAT_CLAUSE + "FROM"
			+ FROM_CLAUSE + "WHERE" + WHERE_CLAUSE;
	protected final static String UPDATE_STM = "UPDATE" + FROM_CLAUSE + "SET"
			+ WHAT_CLAUSE + "WHERE" + WHERE_CLAUSE;
	protected final static String DELETE_STM = "DELETE" + WHAT_CLAUSE + "FROM"
			+ FROM_CLAUSE + "WHERE" + WHERE_CLAUSE;
	protected final static String INSERT_STM = "INSERT" + "INT0" + FROM_CLAUSE
			+ " (" + COL_CLAUSE + ") VALUES (" + VAL_CLAUSE + ")";


	enum ObjectType {
		POSITION,
		PERSON,
		PERSON_TO_PLACE
	};

	public static DB_Object createObject(ObjectType type, DBManager dbman) {
		switch (type) {
		case POSITION:
			return new Position();
		case PERSON:
			return new Person();
		default:
			return null;
		}
	}

	/**
	 * Gets all records from the database.
	 * 
	 * @param column
	 *            Column for selections purposes
	 * @param value
	 *            Value for selection purposes
	 * @return populated array list with DB_Objects
	 */
	public static ArrayList<DB_Object> getAllRecords(ObjectType type,
			DBManager dbman, String column, String value) {
		ResultSet records = runSQL(dbman, getSelect_sql(column, value, type),
				true);
		ArrayList<DB_Object> objects = new ArrayList<DB_Object>();
		try {
			while (records.next()) {
				DB_Object obj = createObject(type, dbman);
				obj.populateProperites(records);													
				objects.add(obj);
			}
			
			if(type==ObjectType.PERSON)
			{
				processPersons(objects, dbman);
			}		
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return objects;
	}
	
	private static void processPersons(ArrayList<DB_Object> persons, DBManager dbman)
	{
		for(DB_Object person:persons)
		{
			if(person!=null && dbman!=null)
			{
				ResultSet records = runSQL(dbman, getSelect_sql("1", "1", ObjectType.PERSON_TO_PLACE), true);
				((Person) person).populatePositions(records);
			}
		}	
	}

	/**
	 * Gets all records from the database. Convenience method that sets where
	 * clause to all.
	 * 
	 * @return populated array list with DB_Objects
	 */
	public static ArrayList<DB_Object> getAllRecords(ObjectType type,
			DBManager dbman) {
		return getAllRecords(type, dbman, "1", "1");
	}

	/**
	 * Inserts new database record
	 * @param type DB Object type to insert
	 * @param dbman Database Manager
	 * @param details Details of the record
	 * @return True if success, false otherwise
	 */
	public static boolean insertRecord(ObjectType type, DBManager dbman,
			ArrayList<String> details) {
		
		DB_Object obj = createObject(type, dbman);
		obj.populateProperties(details);
		
		String sql=obj.getInsert_sql(getNextPKID(dbman, obj.getTableName()));
		sql=sql.replace(FROM_CLAUSE, " "+obj.getTableName()+" ");
		
		ResultSet rs = runSQL(dbman, sql, false);

		if (rs == null) {
			return false;
		}
		return true;
	}

	public static boolean deleteRecord(ArrayList<DB_Object> objects, DBManager dbman,
			String name) {
		
		DB_Object obj=getObjectByName(objects, name);
		if(obj!=null)
		{
			String sql=obj.getDelete_sql();
			
			ResultSet rs = runSQL(dbman,sql, false);
			if (rs == null) {
				return false;
			}
			return true;
		}
		return false;
	}	
	
	/**
	 * Updates database record
	 * @param objects List of objects
	 * @param dbman Database Manager
	 * @param old_details Old details
	 * @param new_details New details
	 * @return True if success, false otherwise
	 */
	public static boolean updateRecord(ArrayList<DB_Object> objects, DBManager dbman,
			ArrayList<String> old_details, ArrayList<String> new_details) {
		for(DB_Object obj: objects)
		{
			if(obj!=null && obj.matches(old_details, false))
			{
				obj.populateProperties(new_details);
				String sql=obj.getUpdate_sql();
				ResultSet rs = runSQL(dbman,sql, false);
				if (rs == null) {
					return false;
				}
				return true;
			}
		}		
		return false;		
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
	private static String getSelect_sql(String column, 
			String value, ObjectType type) {	
		String table_name=null;
		switch(type)
		{
			case POSITION:table_name="PLACE";
					break;
			case PERSON:table_name="PERSON";
				break;
			case PERSON_TO_PLACE:table_name="PERSON_TO_PLACE";
				break;
			default:return null;
		}
		
		String sql = SELECT_STM;
		String where_cls = column + "=" + value;
		sql = sql.replace(WHAT_CLAUSE, " * ");
		sql = sql.replace(WHERE_CLAUSE, " " + where_cls + " ");
		sql = sql.replace(FROM_CLAUSE, " " + table_name + " ");
		return sql;
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
	private static ResultSet runSQL(DBManager dbman, String sql_str,
			boolean wantresult) {
		if (dbman != null) {
			return dbman.runQuery(sql_str, wantresult);
		}
		return null;
	}

	/**
	 * Retrieves next pkid from the database (Firebird crap)
	 * 
	 * @return next pkid
	 */
	private static int getNextPKID(DBManager dbman, String table_name) {
		ResultSet rs = dbman.runQuery("SELECT NEXT VALUE FOR " + table_name
				+ "_ID_GEN FROM RDB$DATABASE", true);
		try {
			if (rs.next()) {
				return rs.getInt("GEN_ID");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static DB_Object getObjectByName(ArrayList<DB_Object> objects, String name)
	{
		for(DB_Object obj: objects)
		{
			if(obj!=null)
			{
				if(obj.getName().equals(name))
				{
					return obj;
				}
			}
		}
		return null;
	}
	/**
	 * Convenience method to get names of the objects from the list.
	 * @return ArraList of names.
	 */
	public static ArrayList<String> getNames(ArrayList<DB_Object> objects)
	{
		ArrayList<String> names=new ArrayList<String>();
		for(DB_Object obj:objects)
		{
			if(obj!=null)
			{
				names.add(obj.getName());
			}
		}
		
		return names;
	}
	
	public static ArrayList<Integer> getPIKDs(ArrayList<DB_Object> objects)
	{
		ArrayList<Integer> pkids=new ArrayList<Integer>();
		for(DB_Object obj:objects)
		{
			if(obj!=null)
			{
				pkids.add(obj.getPKID());
			}
		}
		
		return pkids;
	}
	
	public static ArrayList<String> getPIKDsStr(ArrayList<DB_Object> objects)
	{
		ArrayList<Integer> pkids=getPIKDs(objects);
		ArrayList<String> pkids_str=new ArrayList<String>();
		for(Integer i:pkids)
		{
			pkids_str.add(i.toString());
		}
		
		return pkids_str;
	}
	/**
	 * Uses pkID to get an object from the list
	 * @param pkid pkID to use to retrieve an object.
	 * @return DBObject with a specified pkid;
	 */
	public static String getObjectNameByPKID(ArrayList<DB_Object> objects,int pkid)
	{
		for(DB_Object obj:objects)
		{
			if(obj!=null)
			{
				if(obj.getPKID()==pkid)
				{
					return obj.getName();
				}
			}
		}
		return null;
	}
	
	/**
	 * Uses pkID to get an object from the list
	 * @param pkid pkID to use to retrieve an object.
	 * @return DBObject with a specified pkid;
	 */
	
	public static int getObjectPKIDByName(ArrayList<DB_Object> objects,String name)
	{
		for(DB_Object obj:objects)
		{
			if(obj!=null)
			{
				if(obj.getName().equals(name))
				{
					return obj.getPKID();
				}
			}
		}
		return 0;
	}
	public static ArrayList<String> geDetaislByName(ArrayList<DB_Object> objects, String name) {
		for(DB_Object obj:objects)
		{
			if(obj!=null)
			{
				if(obj.getName().equals(name))
				{
					return obj.toSortedArray();
				}
			}
			
		}
		return null;
	}
}
