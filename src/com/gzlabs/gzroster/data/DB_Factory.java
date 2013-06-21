package com.gzlabs.gzroster.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.gzlabs.drosterheper.DBManager;

/**
 * Provides utilities for database Object generation and manipulation.
 * @author apavlune
 * 
 */
public class DB_Factory {

	// Clauses
	protected final static String WHAT_CLAUSE = " %WHAT% ";
	protected final static String FROM_CLAUSE = " %FROM% ";
	protected final static String WHERE_CLAUSE = " %WHERE% ";

	// Statements
	protected final static String SELECT_STM = "SELECT" + WHAT_CLAUSE + "FROM"
			+ FROM_CLAUSE + "WHERE" + WHERE_CLAUSE;

	/**
	 * Table names.  Nothing special.
	 * @author apavlune
	 *
	 */
	enum ObjectType {
		POSITION,
		PERSON,
		PERSON_TO_PLACE,
		PERSON_NA_AVAIL_HOURS,
		DUTIES
	};

	/**
	 * Creates new database object based on the specified type.
	 * @param type Type of the object to create.  Comes from ObjectType enum.
	 * @return Newly created DB_Object.
	 */
	public static DB_Object createObject(ObjectType type) {
		switch (type) {
		case POSITION:
			return new Position();
		case PERSON:
			return new Person();
		case DUTIES:
			return new Duty();
		default:
			return null;
		}
	}
	
	/**
	 * Retrieves all duty objects from the database
	 * @param dbman Database manager to use.
	 * @param persons List of the persons to use for reference.
	 * @param positions List of the positions to use for reference.
	 * @return List of the duties in the database.
	 */
	public static ArrayList<DB_Object> getAllDuties(DBManager dbman, ArrayList<DB_Object> persons, ArrayList<DB_Object> positions) {
		if(dbman !=null && persons!=null && positions !=null)
		{
			ResultSet records = runSQL(dbman, getSelect_sql("1","1",ObjectType.DUTIES),
					true);
			ArrayList<DB_Object> objects = new ArrayList<DB_Object>();
			
			if(records!=null)
			{
				try {
					while (records.next()) {
						Duty obj = (Duty) createObject(ObjectType.DUTIES);
						obj=obj.populateProperites(records, positions, persons);
						if(obj!=null)
						{
							objects.add(obj);
						}
					}
					
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return objects;
			}
		}
		return null;
		
	}

	/**
	 * Fetches all records from the database.
	 * @param type Type of the table to use.
	 * @param dbman Database manager
	 * @param column Column for the WHERE clause
	 * @param value value for the WHERE clause
	 * @return List of the database objects the were fetched.
	 */
	public static ArrayList<DB_Object> getAllRecords(ObjectType type,
			DBManager dbman, String column, String value) {
		
		if(dbman!=null)
		{
			ResultSet records = runSQL(dbman, getSelect_sql(column, value, type),
					true);
			ArrayList<DB_Object> objects = new ArrayList<DB_Object>();
			
			if(records!=null)
			{
				try {
					while (records.next()) {
						DB_Object obj = createObject(type);
						obj.populateProperites(records);	
						
						if(obj!=null)
						{
							objects.add(obj);
						}
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
		}
		return null;
	}
	
	/**
	 * Populates person objects with auxiliary data.
	 * @param persons List of the persons to process.
	 * @param dbman Database manager to use.
	 */
	private static void processPersons(ArrayList<DB_Object> persons, DBManager dbman)
	{
		if(persons!=null)
		{
			for(DB_Object person:persons)
			{
				if(person!=null && dbman!=null)
				{
					ResultSet records = runSQL(dbman, getSelect_sql("1", "1", ObjectType.PERSON_TO_PLACE), true);
					((Person) person).populatePositions(records);
					
					records = runSQL(dbman, getSelect_sql("1", "1", ObjectType.PERSON_NA_AVAIL_HOURS), true);
					((Person) person).populateTimeOff(records);
				}
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
		if(details !=null)
		{
			DB_Object obj = createObject(type);
			obj.populateProperties(details);
			
			if(obj!=null && dbman!=null)
			{
				String sql=obj.getInsert_sql(getNextPKID(dbman, obj.getTableName()));
				sql=sql.replace(FROM_CLAUSE, " "+obj.getTableName()+" ");
				
				ResultSet rs = runSQL(dbman, sql, false);
		
				if (rs != null) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Deletes a record from the database
	 * @param objects Object collection that will be used to pick from.
	 * @param dbman Database manager to use.
	 * @param name Name of the object to delete.
	 * @return True if success, false otherwise.
	 */
	public static boolean deleteRecord(ArrayList<DB_Object> objects, DBManager dbman,
			String name) {
		
		if(objects!=null)
		{
			DB_Object obj=getObjectByName(objects, name);
			if(obj!=null && dbman !=null)
			{
				ArrayList<String> stmts=obj.getDelete_sql();
				
				if(stmts!=null)
				{
					for(String sql:stmts)
					{
						ResultSet rs = runSQL(dbman,sql, false);
						if (rs == null) {
							return false;
						}
					}
					return true;
				}
			}
		}
		return false;
	}	
	
	/**
	 * Specific method to delete a shift duty from the database. 
	 * @param objects List of duties to pick from.
	 * @param dbman Database manager to use.
	 * @param details Specific duty details for reference.
	 * @return True if success, false otherwise.
	 */
	public static boolean deleteDuty(ArrayList<DB_Object> objects, DBManager dbman,
			ArrayList<String> details) {
		
		if(objects!=null)
		{
			for(DB_Object obj:objects)
			{
				if(obj!=null && dbman!=null && ((Duty)obj).matches(details, false))
				{
					ArrayList<String> stmts=obj.getDelete_sql();
					{
						if(stmts!=null)
						{
							for(String sql:stmts)
							{
								ResultSet rs = runSQL(dbman,sql, false);
								if (rs == null) {
									return false;
								}
							}
							return true;
						}
						
					}					
				}
			}
		}
		return false;
	}	
	
	/**
	 * Finds a specific duty using time as reference.
	 * @param objects List of duty objects to search.
	 * @param person_id Person id in the duty
	 * @param position_id Position id in the duty
	 * @param datetime date to search.
	 * @return Duty if it's found, null if it's not.
	 */
	public static Duty findDutyByTime(ArrayList<DB_Object> objects, int person_id, int position_id, String datetime)
	{
		if(objects!=null)
		{
			for(DB_Object obj:objects)
			{
				if(obj!=null)
				{
					if(((Duty)obj).matches(person_id, position_id, datetime))
					{
						return (Duty) obj;
					}
				}
			}
		}
		return null;
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
		
		if(objects!=null)
		{
			for(DB_Object obj: objects)
			{
				if(obj!=null && dbman!=null && obj.matches(old_details, false))
				{
					obj.populateProperties(new_details);
					String sql=obj.getUpdate_sql();
					ResultSet rs = runSQL(dbman,sql, false);
					if (rs != null) {
						return true;
					}
				}
			}		
		}
		return false;		
	}	
	
/**
 * Generates sql select statement.
 * @param column Select column
 * @param value Value for the select statement
 * @param type Table type
 * @return String containing sql statement
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
			case PERSON_NA_AVAIL_HOURS:table_name="PERSON_NA_AVAIL_HOURS";
				break;
			case DUTIES:table_name="DUTIES";
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
	 * Executes SQL statement.
	 * @param dbman Database manager to use.
	 * @param sql_str SQL string to execute.
	 * @param wantresult Flag if result is wanted.
	 * @return SQL result set.
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
	 * @param dbman Database manager to use.
	 * @param table_name Name of the table to get next id for.
	 * @return next pkid
	 */
	private static int getNextPKID(DBManager dbman, String table_name) {
		
		if(dbman!=null && table_name!=null)
		{
			ResultSet rs = dbman.runQuery("SELECT NEXT VALUE FOR " + table_name
					+ "_ID_GEN FROM RDB$DATABASE", true);
			try {
				if (rs.next()) {
					return rs.getInt("GEN_ID");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return 0;
	}

	/**
	 * Finds a database object in the list by using a name.
	 * @param objects List of the objects to search.
	 * @param name Name reference.
	 * @return DB_Object if found, null if not.
	 */
	public static DB_Object getObjectByName(ArrayList<DB_Object> objects, String name)
	{
		if(objects!=null)
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
		}
		return null;
	}

	/**
	 * Finds an object using pkid.
	 * @param objects List of objects to search.
	 * @param pkid Primary key to look for
	 * @return object if it has been found, null if not.
	 */
	public static DB_Object getObjectByPKID(ArrayList<DB_Object> objects, int pkid)
	{
		if(objects!=null)
		{
			for(DB_Object obj: objects)
			{
				if(obj!=null)
				{
					if(obj.getPKID()==pkid)
					{
						return obj;
					}
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
		if(objects!=null)
		{
			for(DB_Object obj:objects)
			{
				if(obj!=null)
				{
					names.add(obj.getName());
				}
			}
		}
		return names;
	}
	
	/**
	 * Retrieves primary keys from the objects and returns them as a list.
	 * @param objects Objects to search.
	 * @return List of the primary keys.
	 */
	public static ArrayList<Integer> getPIKDs(ArrayList<DB_Object> objects)
	{
		ArrayList<Integer> pkids=new ArrayList<Integer>();
		if(objects!=null)
		{
			for(DB_Object obj:objects)
			{
				if(obj!=null)
				{
					pkids.add(obj.getPKID());
				}
			}
		}
		return pkids;
	}
	
	/**
	 * Returns primary keys from the objects as strings
	 * @param objects Objects to search
	 * @return List of the primary keys.
	 */
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
	 * @param objects List of objects to search
	 * @param pkid pkID to use to retrieve an object.
	 * @return DBObject with a specified pkid;
	 */
	public static String getObjectNameByPKID(ArrayList<DB_Object> objects,int pkid)
	{
		if(objects!=null)
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
		}
		
		return null;
	}
	
	/**
	 * Finds a primary key from an object using it's name.
	 * @param objects List of objects to search.
	 * @param name Reference name of the object
	 * @return Object's primary key
	 */
	public static int getObjectPKIDByName(ArrayList<DB_Object> objects,String name)
	{
		if(objects!=null)
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
		}
		return 0;
	}
	
	/**
	 * Fetches object's properties using it's name
	 * @param objects List of the objects to search.
	 * @param name object's name 
	 * @return List of object properties as strings.
	 */
	public static ArrayList<String> geDetaislByName(ArrayList<DB_Object> objects, String name) {
		if(objects!=null)
		{
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
		}
		return null;
	}
	
	/**
	 * Adds a time off for a person.
	 * @param objects List of persons
	 * @param start Time off start
	 * @param end Time off end
	 * @param nameText Name of the person
	 * @param dbman Database manager to use
	 * @return True if time off was added, false otherwise
	 */
	public static boolean addTimeOff(ArrayList<DB_Object> objects, String start, String end, String nameText, DBManager dbman)
	{
		if(objects!=null && start!=null && end!=null && nameText!=null && dbman!=null)
		{
			DB_Object person=getObjectByName(objects, nameText);
			if(dbman!=null && person !=null && ((Person)person).isTimeAllowed(start, end))
			{
				String sql=((Person)person).getTimeOffInsertSql(start, end);
				sql=sql.replace(FROM_CLAUSE, " PERSON_NA_AVAIL_HOURS ");
				
				ResultSet rs = runSQL(dbman, sql, false);
	
				if (rs != null) {
					return true;
				}				
			}
		}
		return false;
	}
	
	/**
	 * Updates person to position map.
	 * @param objects Person list
	 * @param place_ids List of positions
	 * @param nameText Name of a person
	 * @param dbman database manager
	 * @return True if a map was updated, false otherwise.
	 */
	public static boolean updatePersonToPosition(ArrayList<DB_Object> objects, ArrayList<Integer> place_ids, String nameText, DBManager dbman)
	{
		if(objects!=null && place_ids!=null && nameText != null && dbman!=null)
		{
			DB_Object person=getObjectByName(objects, nameText);
			if(dbman!=null && person !=null)
			{
				((Person)person).setM_positions(place_ids);
				String sql=((Person)person).getPersonToPositionsDeleteSql();
				sql=sql.replace(FROM_CLAUSE, " PERSON_TO_PLACE ");			
				
				ResultSet rs = runSQL(dbman,sql, false);
				if (rs == null) {
					return false;
				}
				
				ArrayList<String> insert_sql=((Person)person).getPersonToPositionsInsertSql();
				for(String s: insert_sql)
				{
					s=s.replace(FROM_CLAUSE, " PERSON_TO_PLACE ");	
					rs = runSQL(dbman,s, false);
					if (rs == null) {
						return false;
					}
				}
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Deletes a time off for a person
	 * @param objects List of persons
	 * @param start Time off start
	 * @param end time off end
	 * @param nameText Name of a person
	 * @param dbman DB manager to use
	 * @return True if time off was deleted, false otherwise.
	 */
	public static boolean deleteTimeOff(ArrayList<DB_Object> objects, String start, String end, String nameText, DBManager dbman)
	{
		if(objects!=null && start!=null && end!=null && nameText!=null && dbman!=null)
		{
			DB_Object person=getObjectByName(objects, nameText);
			if(dbman!=null && person !=null)
			{		
					String sql=((Person)person).getDeleteTimeOffSql(start, end);
					sql=sql.replace(FROM_CLAUSE, " PERSON_NA_AVAIL_HOURS ");
					
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
	 * Adds new duty to the database.
	 * @param dbman Database manager to use.
	 * @param details Duty properties
	 * @param db_positions list of positions
	 * @param db_persons list of persons
	 * @return True if duty was added, false otherwise.
	 */
	public static boolean insertDuty(DBManager dbman,ArrayList<String> details,
			ArrayList<DB_Object> db_positions, ArrayList<DB_Object> db_persons) {
			
		if(dbman!=null && details!=null && db_positions!=null && db_persons!=null)
		{
			DB_Object obj = createObject(ObjectType.DUTIES);
			((Duty)obj).populateProperties(details, db_positions, db_persons);
			
			String sql=obj.getInsert_sql(0);
			sql=sql.replace(FROM_CLAUSE, " "+obj.getTableName()+" ");
			
			ResultSet rs = runSQL(dbman, sql, false);
	
			if (rs != null) {
				return true;
			}
		}
		return false;
	}
	
	
}
