package com.gzlabs.gzroster.data;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Properties;

import com.gzlabs.drosterheper.DBManager;
import com.gzlabs.drosterheper.IDisplayStatus;
import com.gzlabs.gzroster.data.DB_Factory.ObjectType;

/**
 * Manages data.  Get data from the database and populates objects with it.
 * @author apavlune
 *
 */
public class DataManager {
	
	private static final String CONFIG_FILE_PATH = "GZRoster.config";

	//Database manager.
	private DBManager dbman;
	
	//Application properties.
	private Properties prop = null;
	
	//Status display
	private IDisplayStatus ids = null;
	
	//DB Objects
	private ArrayList<DB_Object> db_persons;
	
	private ArrayList<DB_Object> db_positions;
	
	private ArrayList<DB_Object> db_duties;

	
	/**
	 * Default constructor. Initializes member variables.
	 * @param pprop  Properties object.
	 * @param pids Status display
	 */
	public DataManager(IDisplayStatus pids) {

		
		ids = pids;
		getProp();
		
		if(prop != null && !prop.isEmpty())
		{
			ids.DisplayStatus("Attempting to connect to the databse...");
	
			if (initDBMan()) {
				ids.DisplayStatus("Connected to the Database!");
				
				db_positions=DB_Factory.getAllRecords(ObjectType.POSITION, dbman);	
				db_persons=DB_Factory.getAllRecords(ObjectType.PERSON, dbman);	
				db_duties=DB_Factory.getAllDuties(dbman, db_persons, db_positions);
				
			} else {
				ids.DisplayStatus("Database connection failed. Exiting...");
				return;
			}
		}
		else
		{
			ids.DisplayStatus("Empty config file. Unable to continiue...");
			return;
		}
	}
	
	/**
	 * Initialized database manager
	 * @return true if initialization was success, false otherwise
	 */
	private boolean initDBMan() {
		dbman = DBManager.getInstance(prop.getProperty("db_driver"),
				prop.getProperty("db_url"), prop.getProperty("db_username"),
				prop.getProperty("db_password"));
		return dbman.init();
	}
	
	/**
	 * Retrieves a list of employees stored in the database
	 * @return List of employees
	 */
	public ArrayList<String> getEmployees()
	{		
		return DB_Factory.getNames(db_persons);
	}
	
	/**
	 * Retrieves detailed information for a single employee.
	 * @param name Employee name to get the data for.
	 * @return ArrayList of employee details.
	 */
	public ArrayList<String> getEmployeeDetails(String name)
	{
		return DB_Factory.geDetaislByName(db_persons, name);
	}
	
	/**
	 * Retrieves a list of positions stored in the database
	 * @return List of positions
	 */
	public ArrayList<String> getPositions()
	{
		return DB_Factory.getNames(db_positions);
	}
	
	/**
	 * Retrieves a list of positions stored in the database
	 * @return List of positions
	 */
	public ArrayList<String> getPersonToPosMapping(String person_name)
	{		
		Person person=(Person)DB_Factory.getObjectByName(db_persons, person_name);
		ArrayList<Integer> position_ids=person.getM_positions();
		
		ArrayList<String> retval=new ArrayList<String>();
		for(Integer s : position_ids)
		{
			retval.add(DB_Factory.getObjectNameByPKID(db_positions, s));
		}
		
		return retval;
	}
		
	/**
	 * Retrieves detailed information for a single position.
	 * @param name Positions name to get the data for.
	 * @return ArrayList of positions details.
	 */
	public ArrayList<String> getPositionDetailsByName(String name)
	{		
		return DB_Factory.geDetaislByName(db_positions, name);
	}
	
	/**
	 * Retrieves a list of employees stored in the database
	 * @return List of employees
	 */
	public ArrayList<String> getDuties()
	{	
		return DB_Factory.getNames(db_duties);
	}
	
	/**
	 * Gets a list of people scheduled on a specified position and date/time
	 * @param date Date/time duty is scheduled on.
	 * @param postion_id Duty position.
	 * @return A List of people scheduled on that date.
	 */
	public ArrayList<String> isDutyOn(String date, String postion_id)
	{
		ArrayList<String> retval=new ArrayList<String> ();
		int place_id=DB_Factory.getObjectPKIDByName(db_positions, postion_id);
		
		for(DB_Object d: db_duties)
		{
			int person_id=d==null?0:((Duty)d).isPersonOn(place_id, date);
			if(person_id>0)
			{
				retval.add(DB_Factory.getObjectNameByPKID(db_persons,person_id));
			}
		}
		return retval;
	}
	
	
	/**
	 * Adds new employee to the database, note that the name should be unique
	 * @param position_boxes 
	 * @param HashMap with the details to add to the database.
	 */
	public void addEmployee(ArrayList<String>details, ArrayList<String> position_boxes)
	{

		if(DB_Factory.insertRecord(ObjectType.PERSON, dbman, details))
		{
			updatePersonToPositionMap(details.get(Tables.PERSON_NAME_INDEX), position_boxes);
			ids.DisplayStatus("Employee added!");	
			db_persons=DB_Factory.getAllRecords(ObjectType.PERSON, dbman);	
		}
		else
		{
			ids.DisplayStatus("Unable to add employee!");
		}
	}
	
	/**
	 * Adds new duty to the database, note that the name should be unique
	 * @param HashMap with the details to add to the database.
	 */
	public void addDuty(ArrayList<String> details)
	{				
		if(DB_Factory.insertDuty(dbman, details, db_positions, db_persons))
		{
			ids.DisplayStatus("Shift added!");
		}
		else
		{
			ids.DisplayStatus("Unable to add shift!");
		}
	}
	
	/**
	 * Deletes a duty from the database.
	 * @param person Person scheduled for this duty.
	 * @param position Position id.
	 * @param datetime Date/time of the duty.
	 */
	public void deleteDuty(ArrayList<String> details)
	{
			if(DB_Factory.deleteDuty(db_duties, dbman, details))
			{
				ids.DisplayStatus("Shift deleted!");	
			}
			else
			{
				ids.DisplayStatus("Failed to delete!");	
			}
	}

	
	public String getDutyStart(String person, String position, String datetime)
	{
		int person_id=DB_Factory.getObjectPKIDByName(db_persons, person);
		int position_id=DB_Factory.getObjectPKIDByName(db_positions, position);
		if(person.length()>1)
		{
			Duty duty=DB_Factory.findDutyByTime(db_duties, dbman, person_id, position_id, datetime);
			return DateUtils.DateToString(duty.getM_start());
		}
		return null;
	}
	
	public String getDutyEnd(String person, String position, String datetime)
	{
		int person_id=DB_Factory.getObjectPKIDByName(db_persons, person);
		int position_id=DB_Factory.getObjectPKIDByName(db_positions, position);
		if(person.length()>1)
		{
			Duty duty=DB_Factory.findDutyByTime(db_duties, dbman, person_id, position_id, datetime);
			return DateUtils.DateToString(duty.getM_end());
		}
		return null;
	}
	
	private HashMap<String, String> getDutyDetails(String person, String position, String datetime)
	{
		HashMap<String, String> details=new HashMap<String, String> ();
	
		int place_id=DB_Factory.getObjectPKIDByName(db_positions, position);
		details.put("PLACE_ID", Integer.toString(place_id));
		
		int person_id=DB_Factory.getObjectPKIDByName(db_persons, person);
		details.put("PERSON_ID",  Integer.toString(person_id));
		details.put("DUTY_START_TIME", datetime);
		
		return details;
	}
	/**
	 * Adds new position to the database, note that the name should be unique
	 * @param value Name to add.
	 */
	public void addPosition(ArrayList<String> details)
	{		
		if(DB_Factory.insertRecord(ObjectType.POSITION, dbman, details))
		{
			ids.DisplayStatus("Positions added!");	
			db_positions=DB_Factory.getAllRecords(ObjectType.POSITION, dbman);	
		}
		else
		{
			ids.DisplayStatus("Unable to add position!");
		}
	}
	
	/**
	 * Deletes employee from the database.
	 * @param value employee to delete.
	 */
	public void deleteEmployee(String [] values)
	{
		boolean success=false;
		for(int i=0; i<values.length; i++)
		{
			success=DB_Factory.deleteRecord(db_persons,dbman,values[i]);
			if(success)
			{
				ids.DisplayStatus("Record deleted!");
				db_persons=DB_Factory.getAllRecords(ObjectType.PERSON, dbman);	
			}
			else
			{
				ids.DisplayStatus("Unable to delete record!");
			}
		}		
	}
	
	/**
	 * Deletes position from the database.
	 * @param value employee to delete.
	 */
	public void deletePosition(String [] values)
	{
		boolean success=false;
		for(int i=0; i<values.length; i++)
		{
			success=DB_Factory.deleteRecord(db_positions,dbman,	values[i]);
			if(success)
			{
				ids.DisplayStatus("Record deleted!");
				db_positions=DB_Factory.getAllRecords(ObjectType.POSITION, dbman);	
			}
			else
			{
				ids.DisplayStatus("Unable to delete record!");
			}
		}		
	}


	/**
	 * Updates record in the database.
	 * @param details Object properties.
	 * @param position_boxes 
	 */
	public void updateEmployee(ArrayList<String> old_val, ArrayList<String> new_val, ArrayList<String> position_boxes) {
		
		if(DB_Factory.updateRecord(db_persons, dbman, old_val, new_val))
		{
			ids.DisplayStatus("Record updated!");
		}
		else
		{
			ids.DisplayStatus("Unable to update record!");
		}	
			
	}
	
	private void updatePersonToPositionMap(String person_name, ArrayList<String> position_boxes)
	{
		Person person=(Person)DB_Factory.getObjectByName(db_persons, person_name);	
		ArrayList<Integer> place_ids=new ArrayList<Integer>();
		
		for(String s:position_boxes)
		{
			int place_id=DB_Factory.getObjectPKIDByName(db_positions, s);
			place_ids.add(place_id);
		}
		person.setM_positions(place_ids);
	}
	
	/**
	 * Updates record in the database.
	 * @param details Object properties.
	 */
	public void updatePosition(ArrayList<String> old_val, ArrayList<String> new_val) {
			if(DB_Factory.updateRecord(db_positions, dbman, old_val, new_val))
			{
				ids.DisplayStatus("Record updated!");
			}
			else
			{
				ids.DisplayStatus("Unable to update record!");
			}
	}
	
	public String getCellLabelString(String str_time, String column_label, String string_date)
	{
		if(column_label!=null && string_date!=null)
		{
							
			String date=string_date+" "+str_time+":00.0";
			
			ArrayList<String> persons_on_duty=null;
			persons_on_duty=isDutyOn(date, column_label);
			
			if(persons_on_duty!=null && persons_on_duty.size()>0)
			{
				return persons_on_duty.toString();
			}												
			return "X";
		}
		return null;
	}
	
	public ArrayList<String> getTimeSpan()
	{
		
		ArrayList<String> retval=new ArrayList<String> ();
		int start_time=Integer.parseInt(prop.getProperty("day_start"));
		int end_time=Integer.parseInt(prop.getProperty("day_end"));
		int interval=Integer.parseInt(prop.getProperty("interval_minutes"));
		if(start_time<end_time)
		{			
			Calendar cal=new GregorianCalendar();
			cal.setTime(new Date());
			cal.set(Calendar.HOUR_OF_DAY, start_time);
			cal.set(Calendar.MINUTE, 0);
			retval.add(cal.get(Calendar.HOUR_OF_DAY)+":00");
			String zminute="00";
			
			while(cal.get(Calendar.HOUR_OF_DAY)<end_time)
			{
				cal.add(Calendar.MINUTE,interval);
				
				retval.add(cal.get(Calendar.HOUR_OF_DAY)+":"+(cal.get(Calendar.MINUTE)==0?zminute:cal.get(Calendar.MINUTE)));
			}
		}
		return retval;
	}
	
	/**
	 * Loads properties file.
	 * 
	 * @param ids
	 *            Information display interface.
	 * @return null if something went wrong. Properties loaded from file
	 *         otherwise.
	 */
	private Properties getProp() {
		ids.DisplayStatus("Loading configuration...");
		prop = null;
		try {
			prop = new Properties();
			prop.load(new FileInputStream(CONFIG_FILE_PATH));
		} catch (Exception e) {
			ids.DisplayStatus("Unable to load configuration file!");
		}
		ids.DisplayStatus("Done...");
		return prop;
	}

	
	/**
	 * Checks if an employee has already been scheduled.
	 * @param details Employee details map.
	 * @return True is there is a conflict, false otherwise.
	 */
	public boolean checkDutyConflict(String person_name, String start, String end) {
		
		int per_id=DB_Factory.getObjectPKIDByName(db_persons, person_name);
		
		for(DB_Object d:db_duties)
		{
			if(((Duty)d).personConflict(per_id, start, end))
			{
				return true;
			}
		}
		return false;
	}
	
	public String getTotalEmpoloyeeHours(String employee_name, String start, String end)
	{
		int per_id=DB_Factory.getObjectPKIDByName(db_persons, employee_name);		
		String ret_str="0.0";
		
		for(DB_Object d: db_duties)
		{
			if(d!=null && !((Duty)d).getTotalEmpoloyeeHours(per_id, start, end).equals(ret_str))
			{
				return ((Duty)d).getTotalEmpoloyeeHours(per_id, start, end);
			}
		}
		return ret_str;
	}

	public ArrayList<String> getAllowedEmployees(String col_label, String start, String end) {
		
		int place_id=DB_Factory.getObjectPKIDByName(db_positions, col_label);
		ArrayList<DB_Object> persons=new ArrayList<DB_Object>();
		for(DB_Object per:db_persons)
		{
			if(((Person)per).isPositionAllowed(place_id))
			{
				persons.add(per);
			}
		}
		ArrayList<String> retval=new ArrayList<String>();
		
		for(DB_Object per:persons)
		{
			if(((Person)per).isTimeAllowed(start, end))
			{				
				retval.add(per.getName());				
			}
		}
		
		return retval;

	}	

 	public boolean newTimeOffRequest(String start, String end, String nameText) {

		if(DB_Factory.addTimeOff(db_persons, start, end, nameText, dbman))
		{
			ids.DisplayStatus("Time Off Added!");
			return true;
		}
		else
		{
			ids.DisplayStatus("Unable to add Time Off!");
			return false;
		}
		
	}

	public ArrayList<String> getTimeOff(String string) {
		DB_Object person=DB_Factory.getObjectByName(db_persons, string);
		return ((Person)person).getTimesOff();
	}

	public boolean deleteTimeOffRequest(String start, String end, String nameText) {
		if(DB_Factory.deleteTimeOff(db_persons, start, end, nameText, dbman))
		{
			ids.DisplayStatus("Time Off Added!");
			return true;
		}
		else
		{
			ids.DisplayStatus("Unable to add Time Off!");
			return false;
		}
		
	}
	
}
