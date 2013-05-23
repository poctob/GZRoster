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
public class DBPositionsList extends DBObjectList {

	public DBPositionsList(DBManager dbman, String table_name) {
		super(dbman, table_name);
		// TODO Auto-generated constructor stub
	}

	@Override
	void populateList() {
		ResultSet rs=this.getAllRecords();
		if(rs!=null)
		{
			try {
				while (rs.next())
				{
					DBObject person=new DBObject(dbman, table_name);					
					person.addProperty("PLACE_ID", rs.getString("PLACE_ID"));
					person.addProperty("PLACE_NAME", rs.getString("PLACE_NAME"));
					person.addProperty("NOTE", rs.getString("NOTE"));			
					objects.add(person);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}	
		
	}

	@Override
	ArrayList<String> getNames() {
		ArrayList<String> names=new ArrayList<String> ();
		for( DBObject obj : objects)
		{
			names.add(obj.getProperty("PLACE_NAME"));
		}
		return names;
	}

	@Override
	boolean recordExists(DBObject obj) {
		return obj.doesRecordExist("PLACE_NAME");
	}

	@Override
	DBObject getObjectByName(String name) {
		for( DBObject obj : objects)
		{
			if(obj.getProperty("PLACE_NAME").equals(name))
				return obj;
		}
		return null;
	}

	@Override
	void generateNewKey(DBObject obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	void generateNewPKID(DBObject obj) {
		obj.setNewPKID("PLACE_ID");
		
	}

	@Override
	boolean deleteObject(DBObject obj) {
		return obj.deleteRecord("PLACE_ID");
	}

	@Override
	boolean updateObject(DBObject obj) {
		return obj.updateRecord("PLACE_ID");
	}

	@Override
	DBObject getObjectByDetails(HashMap<String, String> details) {
		return getObjectByName(details.get("PLACE_NAME"));
	}

}
