package com.gzlabs.gzroster.data;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

import com.gzlabs.drosterheper.DBManager;

public abstract class DBObjectList {

	protected ArrayList <DBObject> objects;
	protected String key_column;
	protected DBManager dbman;
	protected String table_name;
	
	public DBObjectList(DBManager dbman, String table_name)
	{
		this.dbman=dbman;
		this.table_name=table_name;
		this.objects=new ArrayList<DBObject> ();
	}
	
	/**
	 * Gets all records from specified table
	 * @return ResultSet with the records.
	 */
	protected ResultSet getAllRecords()
	{
		String sql="SELECT * FROM "+table_name;
		if(dbman!=null)
		{
			return dbman.runQuery(sql,true);
		}
		return null;
	}
	
	/**
	 * Populates objects list.
	 */
	abstract void populateList();
	
	/**
	 * Convenience method to get names of the objects from the list.
	 * @return ArraList of names.
	 */
	abstract ArrayList<String> getNames();
	
	/**
	 * Checks if record exists in the database.
	 * @param obj Object to check.
	 * @return True if record exists, false otherwise.
	 */
	abstract boolean recordExists(DBObject obj);
	
	/**
	 * Returns a single object from the database with the specified name.
	 * @param name Name to search for.
	 * @return Null if object if not found, object otherwise.
	 */
	abstract DBObject getObjectByName(String name);
	
	/**
	 * Generates new unique key for the record.
	 * @param obj Record to generate the key for.
	 */
	abstract void generateNewKey(DBObject obj);
	
	/**
	 * Generates new pkID for the record.
	 * @param obj Record to generate the pkID for.
	 */
	abstract void generateNewPKID(DBObject obj);
	
	/**
	 * Deletes object from the database.
	 * @param obj Object to delete.
	 */
	abstract boolean deleteObject(DBObject obj);
	
	/**
	 * Updates object's data in the collection and database.
	 * @param obj Object to update.
	 * @return True if operation is successful, false otherwise
	 */
	abstract boolean updateObject(DBObject obj);
	
	/**
	 * Uses supplied properties to find object in the list.
	 * @param details Object's properties.
	 * @return DBObject with the specified properties.
	 */
	abstract DBObject getObjectByDetails(HashMap<String, String> details);
	
	/**
	 * Uses pkID to get an object from the list
	 * @param pkid pkID to use to retrieve an object.
	 * @return DBObject with a specified pkid;
	 */
	abstract DBObject getObjectByPKID(String pkid);
	/**
	 * Adds object to the database.
	 * @param item Item to add.
	 */
	public boolean insertItem(HashMap<String, String> details)
	{
		if(details != null && objects!=null)
		{
			DBObject obj=new DBObject(dbman, table_name, details);				
			if(!recordExists(obj))
			{
				objects.add(obj);
				generateNewKey(obj);
				generateNewPKID(obj);
				return obj.insertNew();
			}
		}
		return false;
	}
	
	public boolean updateItem(HashMap<String, String> details)
	{
		DBObject obj=getObjectByDetails(details);
		obj.setCols(details);
		return updateObject(obj);
	}
	/**
	 * Deletes item from the list and database.
	 * @param name Name of the object to delete.
	 * @return True if everything went OK, false otherwise.
	 */
	public boolean deleteItem(String name)
	{		
		DBObject obj=getObjectByName(name);
		boolean retval = deleteObject(obj);
		if (retval)
			objects.remove(obj);
		return retval;
	}
	
	/**
	 * Finds an object with specified property.  Only one object is returned.
	 * @param key Key of the property.
	 * @param value Value of the property.
	 * @return DBOject matching specified key/value pair.  Null if none is found.
	 */
	protected DBObject getObjectByProperty(String key, String value)
	{
		for( DBObject obj : objects)
		{
			if(obj.getProperty(key).equals(value))
				return obj;
		}
		return null;
	}	
	
}
