package com.gzlabs.gzroster.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import com.gzlabs.drosterheper.DBManager;

public class DBPersonList extends DBObjectList{

	
	public DBPersonList(DBManager dbman, String table_name) {
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
					person.addProperty("PERSON_ID", rs.getString("PERSON_ID"));
					person.addProperty("PERSON_NAME", rs.getString("PERSON_NAME"));
					person.addProperty("ADDRESS", rs.getString("ADDRESS"));
					person.addProperty("PHONE_HOME", rs.getString("PHONE_HOME"));
					person.addProperty("PHONE_MOBILE", rs.getString("PHONE_MOBILE"));
					person.addProperty("NOTE", rs.getString("NOTE"));
					person.addProperty("ACTIVE_PERSON", rs.getString("ACTIVE_PERSON"));
					person.addProperty("EMAIL_ADDRESS", rs.getString("EMAIL_ADDRESS"));
					person.addProperty("WEEKLY_TIME", rs.getString("WEEKLY_TIME"));
					person.addProperty("DAILY_TIME", rs.getString("DAILY_TIME"));
					person.addProperty("MOMTHLY_TIME", rs.getString("MOMTHLY_TIME"));
					person.addProperty("WEB_NAME", rs.getString("WEB_NAME"));
					person.addProperty("WEB_PASSWORD", rs.getString("WEB_PASSWORD"));
					person.addProperty("EMPLOYEE_KEY", rs.getString("EMPLOYEE_KEY"));					
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
			names.add(obj.getProperty("PERSON_NAME"));
		}
		return names;
	}
	@Override
	DBObject getObjectByName(String name) {
		for( DBObject obj : objects)
		{
			if(obj.getProperty("PERSON_NAME").equals(name))
				return obj;
		}
		return null;
	}
	@Override
	boolean recordExists(DBObject obj) {	
		return obj.doesRecordExist("PERSON_NAME");
			
	}
	@Override
	void generateNewKey(DBObject obj) {
		obj.generateNewKey("EMPLOYEE_KEY");
		
	}
	@Override
	void generateNewPKID(DBObject obj) {
		obj.setNewPKID("PERSON_ID");
		
	}
	@Override
	boolean deleteObject(DBObject obj) {	
		return obj.deleteRecord("PERSON_NAME");
	}
	@Override
	boolean updateObject(DBObject obj) {		
		return obj.updateRecord("EMPLOYEE_KEY");
	}
	@Override
	DBObject getObjectByDetails(HashMap<String, String> details) {
		// TODO Auto-generated method stub
		return getObjectByName(details.get("PERSON_NAME"));
	}
	
	

}
