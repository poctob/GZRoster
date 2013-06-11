/**
 * 
 */
package com.gzlabs.gzroster.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import com.gzlabs.drosterheper.DBManager;

/**
 * @author apavlune
 *
 */
public class DBPersonToPlace extends DBObjectList {

	/**
	 * @param dbman
	 * @param table_name
	 */
	public DBPersonToPlace(DBManager dbman, String table_name) {
		super(dbman, table_name);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see com.gzlabs.gzroster.data.DBObjectList#populateList()
	 */
	@Override
	void populateList() {
		ResultSet rs=this.getAllRecords();
		if(rs!=null)
		{
			try {
				while (rs.next())
				{
					DBObject person=new DBObject(dbman, table_name);					
					person.addProperty("PERSON_ID", rs.getString("PERSON_ID"));
					person.addProperty("PLACE_ID", rs.getString("PLACE_ID"));			
					objects.add(person);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}	
	}

	/* (non-Javadoc)
	 * @see com.gzlabs.gzroster.data.DBObjectList#getNames()
	 */
	@Override
	ArrayList<String> getNames() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.gzlabs.gzroster.data.DBObjectList#recordExists(com.gzlabs.gzroster.data.DBObject)
	 */
	@Override
	boolean recordExists(DBObject obj) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see com.gzlabs.gzroster.data.DBObjectList#getObjectByName(java.lang.String)
	 */
	@Override
	DBObject getObjectByName(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.gzlabs.gzroster.data.DBObjectList#generateNewKey(com.gzlabs.gzroster.data.DBObject)
	 */
	@Override
	void generateNewKey(DBObject obj) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.gzlabs.gzroster.data.DBObjectList#generateNewPKID(com.gzlabs.gzroster.data.DBObject)
	 */
	@Override
	void generateNewPKID(DBObject obj) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.gzlabs.gzroster.data.DBObjectList#deleteObject(com.gzlabs.gzroster.data.DBObject)
	 */
	@Override
	boolean deleteObject(DBObject obj) {		
		return obj.deleteRecord();
	}

	/* (non-Javadoc)
	 * @see com.gzlabs.gzroster.data.DBObjectList#updateObject(com.gzlabs.gzroster.data.DBObject)
	 */
	@Override
	boolean updateObject(DBObject obj) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see com.gzlabs.gzroster.data.DBObjectList#getObjectByDetails(java.util.HashMap)
	 */
	@Override
	DBObject getObjectByDetails(HashMap<String, String> details) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.gzlabs.gzroster.data.DBObjectList#getObjectByPKID(java.lang.String)
	 */
	@Override
	DBObject getObjectByPKID(String pkid) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public ArrayList<String> getPositionsByPersonID(String person_id)
	{
		ArrayList<DBObject> loc_objects=getObjectByPropertyMulti("PERSON_ID", person_id);
		ArrayList<String> retval=new ArrayList<String>();
		
		for(DBObject obj : loc_objects)
		{
			retval.add(obj.getProperty("PLACE_ID"));
		}
		
		return retval;
	}

}
