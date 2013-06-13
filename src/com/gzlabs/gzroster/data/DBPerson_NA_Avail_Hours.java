package com.gzlabs.gzroster.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import com.gzlabs.drosterheper.DBManager;

public class DBPerson_NA_Avail_Hours extends DBObjectList{

	public DBPerson_NA_Avail_Hours(DBManager dbman, String table_name) {
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
					person.addProperty("PERSON_NA_START_DATE_HOUR", rs.getString("PERSON_NA_START_DATE_HOUR"));		
					person.addProperty("PERSON_NA_END_DATE_HOUR", rs.getString("PERSON_NA_END_DATE_HOUR"));
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	boolean recordExists(DBObject obj) {
		ArrayList<DBObject> persons= getObjectByPropertyMulti("PERSON_ID", obj.getProperty("PERSON_ID"));
		for(DBObject objt:persons)
		{
			String start=objt.getProperty("PERSON_NA_START_DATE_HOUR");
			String end=objt.getProperty("PERSON_NA_END_DATE_HOUR");
			if(DateUtils.isCalendarBetween(start, end, obj.getProperty("PERSON_NA_START_DATE_HOUR"), true) || 
					DateUtils.isCalendarBetween(start, end, obj.getProperty("PERSON_NA_END_DATE_HOUR"), true))
			{
				return true;
			}
		}
		return false;
	}

	@Override
	DBObject getObjectByName(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	void generateNewKey(DBObject obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	void generateNewPKID(DBObject obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	boolean deleteObject(DBObject obj) {
		return obj.deleteRecord();
		
	}

	@Override
	boolean updateObject(DBObject obj) {
		return false;
	}

	@Override
	DBObject getObjectByDetails(HashMap<String, String> details) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	DBObject getObjectByPKID(String pkid) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public boolean isPersonAvailable(String person_id, String start_time)
	{
		if(start_time==null)
		{
			return true;
		}
		ArrayList<DBObject> persons= getObjectByPropertyMulti("PERSON_ID", person_id);
		
		for(DBObject obj:persons)
		{
			if(DateUtils.isCalendarBetween(obj.getProperty("PERSON_NA_START_DATE_HOUR"),
					obj.getProperty("PERSON_NA_END_DATE_HOUR"), start_time, true))
			{
				return false;
			}
		}
		return true;
	}

	public void insertItem(String start, String end, String person_id) {
		HashMap<String, String> details=new HashMap<String, String>();
		details.put("PERSON_NA_START_DATE_HOUR", start);
		details.put("PERSON_NA_END_DATE_HOUR", end);
		details.put("PERSON_ID", person_id);
		insertItem(details);				
	}

	@Override
	public String getObjectID(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	public ArrayList<String> getTimeOff(String person_id) {
		ArrayList<DBObject> persons= getObjectByPropertyMulti("PERSON_ID", person_id);
		ArrayList<String> data=new ArrayList<String>();
		for(DBObject obj:persons)
		{
			String timeoff="From: "+obj.getProperty("PERSON_NA_START_DATE_HOUR")+
					" To: "+obj.getProperty("PERSON_NA_END_DATE_HOUR");
			data.add(timeoff);
		}
		
		return data;
	}

	public boolean deleteTimeOff(String start, String end, String nameText) {
		ArrayList<DBObject> persons= getObjectByPropertyMulti("PERSON_ID", nameText);
		
		boolean retval=false;
		for(DBObject objt:persons)
		{
			if(objt.getProperty("PERSON_NA_START_DATE_HOUR").equals(start) && 
					objt.getProperty("PERSON_NA_END_DATE_HOUR").equals(end))
			{
				if(deleteObject(objt))
				{
					objects.remove(objt);
					retval=true;
				}
			}
		}
		return retval;

	}

}
