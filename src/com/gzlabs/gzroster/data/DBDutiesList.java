package com.gzlabs.gzroster.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import com.gzlabs.drosterheper.DBManager;

public class DBDutiesList extends DBObjectList{

	public DBDutiesList(DBManager dbman, String table_name) {
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
					DBObject duty=new DBObject(dbman, table_name);					
					duty.addProperty("DUTY_START_TIME", rs.getString("DUTY_START_TIME"));
					duty.addProperty("PLACE_ID", rs.getString("PLACE_ID"));
					duty.addProperty("PERSON_ID", rs.getString("PERSON_ID"));
					duty.addProperty("DUTY_END_TIME", rs.getString("DUTY_END_TIME"));
					duty.addProperty("DUTY_NOTE", rs.getString("DUTY_NOTE"));
					duty.addProperty("RULE_IDS_VIOLATION", rs.getString("RULE_IDS_VIOLATION"));
					duty.addProperty("APPROVED", rs.getString("APPROVED"));
					duty.addProperty("APPROVED_TIME", rs.getString("APPROVED_TIME"));
					duty.addProperty("DUTY_KEY", rs.getString("DUTY_KEY"));		
					objects.add(duty);
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
			names.add(obj.getProperty("DUTY_START_TIME"));
		}
		return names;
	}

	@Override
	boolean recordExists(DBObject obj) {
		if(obj.getProperty("DUTY_KEY")==null)
		{
			return false;
		}
		
		return obj.doesRecordExist("DUTY_KEY");
	}

	@Override
	DBObject getObjectByName(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	void generateNewKey(DBObject obj) {
		obj.generateNewKey("DUTY_KEY");
		
	}

	@Override
	void generateNewPKID(DBObject obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	boolean deleteObject(DBObject obj) {
		boolean retval = obj.deleteRecord("DUTY_KEY");
		objects.remove(obj);
		return retval;
	}

	@Override
	boolean updateObject(DBObject obj) {
		return obj.updateRecord("DUTY_KEY");
	}

	@Override
	DBObject getObjectByDetails(HashMap<String, String> details) {
		DBObject retval=null;
		String position=details.get("PLACE_ID");
		String person=details.get("PERSON_ID");
		String starttime=details.get("DUTY_START_TIME");

		for( DBObject obj : objects)
		{
			if(obj.getProperty("PLACE_ID").equals(position) && 
					obj.getProperty("PERSON_ID").equals(person))
			{
				if(DateUtils.isCalendarBetween(obj.getProperty("DUTY_START_TIME"), 
						obj.getProperty("DUTY_END_TIME"), starttime, true))
				{
					return obj;
				}

			}
		}
		return retval;
	}
	
	public ArrayList<String> isOn(String time, String position)
	{
		ArrayList<String> retval=new  ArrayList<String>();
		for( DBObject obj : objects)
		{
			if(obj.getProperty("PLACE_ID").equals(position))
			{
				String start=obj.getProperty("DUTY_START_TIME");
				String end=obj.getProperty("DUTY_END_TIME");
				
				if(DateUtils.isCalendarBetween(start, end, time, true))
				{
					retval.add(obj.getProperty("PERSON_ID"));
				}		
			}
				
		}
		return retval;
	}

	@Override
	DBObject getObjectByPKID(String pkid) {
		// TODO Auto-generated method stub
		return null;
	}	

	/**
	 * Checks if an employee is already scheduled for the specified time period.
	 * @param employee_id  Employee id
	 * @param start Shift start
	 * @param end	Shift end
	 * @return True if employee is scheduled within specified period, false otherwise.
	 */
	boolean checkAvailability(String employee_id, String start, String end)
	{	
		for( DBObject obj : objects)
		{
			if(obj.getProperty("PERSON_ID").equals(employee_id))
			{
				String obj_start=obj.getProperty("DUTY_START_TIME");
				String obj_end=obj.getProperty("DUTY_END_TIME");
				
				boolean start_match=DateUtils.isCalendarBetween(obj_start, obj_end, start, true);
				boolean end_match=DateUtils.isCalendarBetween(obj_start, obj_end, end, true);
				
				if(start_match || end_match)
				{
					return true;
				}				
			}
		}
		return false;
	}
	
	public String getTotalEmpoloyeeHours(String employee_id, String start, String end)
	{
		double hours=0;
		
		for( DBObject obj : objects)
		{
			if(obj.getProperty("PERSON_ID").equals(employee_id))
			{
				String obj_start=obj.getProperty("DUTY_START_TIME");
				boolean start_match=DateUtils.isCalendarBetween(start, end, obj_start, true);
				if(start_match)
				{
					String obj_end=obj.getProperty("DUTY_END_TIME");
					hours+=DateUtils.getSpanMinutes(obj_start, obj_end);
				}
			}
		}
		double minutes=hours/60;
		return String.format("%.2f", minutes);
	}

	@Override
	public String getObjectID(String name) {
		// TODO Auto-generated method stub
		return null;
	}
}
