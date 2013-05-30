package com.gzlabs.gzroster.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
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
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
		Calendar time_cal=new GregorianCalendar();
		Calendar start_cal=new GregorianCalendar();
		Calendar end_cal=new GregorianCalendar();

		for( DBObject obj : objects)
		{
			if(obj.getProperty("PLACE_ID").equals(position) && 
					obj.getProperty("PERSON_ID").equals(person))
			{
				try {
					start_cal.setTime(sdf.parse(obj.getProperty("DUTY_START_TIME")));
					end_cal.setTime(sdf.parse(obj.getProperty("DUTY_END_TIME")));
					time_cal.setTime(sdf.parse(starttime));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if(start_cal.equals(time_cal) || end_cal.equals(time_cal) || (start_cal.before(time_cal) && end_cal.after(time_cal)))
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
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
				Calendar start_cal=new GregorianCalendar();
				Calendar end_cal=new GregorianCalendar();
				Calendar time_cal=new GregorianCalendar();
				try {
					start_cal.setTime(sdf.parse(start));
					end_cal.setTime(sdf.parse(end));
					time_cal.setTime(sdf.parse(time));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if(start_cal.equals(time_cal) || end_cal.equals(time_cal) || (start_cal.before(time_cal) && end_cal.after(time_cal)))
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

}
