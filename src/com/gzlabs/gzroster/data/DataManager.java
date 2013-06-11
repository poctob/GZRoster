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
	
	//Employees list
	private DBObjectList persons;
	
	//Positions list
	private DBObjectList positions;
	
	//Duties List
	private DBDutiesList duties;
	
	//Person to Positions mapping
	private DBPersonToPlace per_to_pos;
	
	/**
	 * Default constructor. Initializes member variables.
	 * @param pprop  Properties object.
	 * @param pids Status display
	 */
	public DataManager(IDisplayStatus pids) {
		positions=null;
		
		ids = pids;
		getProp();
		
		if(prop != null && !prop.isEmpty())
		{
			ids.DisplayStatus("Attempting to connect to the databse...");
	
			if (initDBMan()) {
				ids.DisplayStatus("Connected to the Database!");
				
				duties=new DBDutiesList(dbman, "DUTIES");
				duties.populateList();
				
				persons=new DBPersonList(dbman, "PERSON");
				persons.populateList();
				
				positions=new DBPositionsList(dbman, "PLACE");
				positions.populateList();
				
				per_to_pos=new DBPersonToPlace(dbman, "PERSON_TO_PLACE");
				per_to_pos.populateList();
				
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
		return persons.getNames();
	}
	
	/**
	 * Retrieves detailed information for a single employee.
	 * @param name Employee name to get the data for.
	 * @return ArrayList of employee details.
	 */
	public HashMap<String, String> getEmployeeDetails(String name)
	{		
		DBObject person=persons.getObjectByName(name);		
		return person==null?null:person.getCols();
	}
	
	/**
	 * Retrieves a list of positions stored in the database
	 * @return List of positions
	 */
	public ArrayList<String> getPositions()
	{
		return positions.getNames();
	}
	
	/**
	 * Retrieves a list of positions stored in the database
	 * @return List of positions
	 */
	public ArrayList<String> getPersonToPosMapping(String person_name)
	{
		String person_id=persons.getObjectByName(person_name).getProperty("PERSON_ID");
		ArrayList<String> position_ids=per_to_pos.getPositionsByPersonID(person_id);
		ArrayList<String> retval=new ArrayList<String>();
		for(String s : position_ids)
		{
			DBObject obj=positions.getObjectByPKID(s);
			if(obj!=null)
			{
				retval.add(obj.getProperty("PLACE_NAME"));
			}
		}
		
		return retval;
	}
	
	/**
	 * Retrieves detailed information for a single position.
	 * @param name Positions name to get the data for.
	 * @return ArrayList of positions details.
	 */
	public HashMap<String, String> getPositionDetails(String name)
	{		
		DBObject position=positions.getObjectByName(name);		
		return position==null?null:position.getCols();
	}
	
	/**
	 * Retrieves a list of employees stored in the database
	 * @return List of employees
	 */
	public ArrayList<String> getDuties()
	{	
		return duties.getNames();
	}
	
	/**
	 * Gets a list of people scheduled on a specified position and date/time
	 * @param date Date/time duty is scheduled on.
	 * @param postion_id Duty position.
	 * @return A List of people scheduled on that date.
	 */
	public ArrayList<String> isDutyOn(String date, String postion_id)
	{
		String pos_id=positions.getObjectByName(postion_id).getProperty("PLACE_ID");
		ArrayList<String> pers=duties.isOn(date, pos_id);
		ArrayList<String> retval=new ArrayList<String> ();
		for(String s: pers)
		{
			DBObject obj=persons.getObjectByPKID(s);
			if(obj!=null)
			{
				retval.add(obj.getProperty("PERSON_NAME"));
			}
		}
		return retval;
	}
	
	
	/**
	 * Adds new employee to the database, note that the name should be unique
	 * @param HashMap with the details to add to the database.
	 */
	public void addEmployee(HashMap<String, String> details)
	{
		if(persons.insertItem(details))
		{
			ids.DisplayStatus("Employee added!");	
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
	public void addDuty(HashMap<String, String> details)
	{
		DBObject employee=persons.getObjectByName(details.get("PERSON_ID"));
		details.put("PERSON_ID", employee.getProperty("PERSON_ID"));
		
		DBObject place=positions.getObjectByName(details.get("PLACE_ID"));
		details.put("PLACE_ID", place.getProperty("PLACE_ID"));
		
		if(duties.insertItem(details))
		{
			ids.DisplayStatus("Shift added! "+
			employee.getProperty("PERSON_NAME")
			+" - "+place.getProperty("PLACE_NAME")
			+":"+details.get("DUTY_START_TIME")+" - "
			+details.get("DUTY_END_TIME"));	
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
	public void deleteDuty(String person, String position, String datetime)
	{
		if(person.length()>1)
		{
			HashMap<String, String> details=getDutyDetails(person, position, datetime);
			if(duties.deleteObject(duties.getObjectByDetails(details)))
			{
				ids.DisplayStatus("Shift deleted!");	
			}
			else
			{
				ids.DisplayStatus("Failed to delete!");	
			}
		}
	}
	
	public String getDutyStart(String person, String position, String datetime)
	{
		String retstring="";
		if(person.length()>1)
		{
			HashMap<String, String> details=getDutyDetails(person, position, datetime);
			DBObject duty=duties.getObjectByDetails(details);
			retstring=duty.getProperty("DUTY_START_TIME");
			
		}
		return retstring;
	}
	
	public String getDutyEnd(String person, String position, String datetime)
	{
		String retstring="";
		if(person.length()>1)
		{
			HashMap<String, String> details=getDutyDetails(person, position, datetime);
			DBObject duty=duties.getObjectByDetails(details);
			retstring=duty.getProperty("DUTY_END_TIME");
		}
		return retstring;
	}
	
	private HashMap<String, String> getDutyDetails(String person, String position, String datetime)
	{
		HashMap<String, String> details=new HashMap<String, String> ();
	
		details.put("PLACE_ID", positions.getObjectByName(position).getProperty("PLACE_ID"));
		details.put("PERSON_ID", persons.getObjectByName(person).getProperty("PERSON_ID"));
		details.put("DUTY_START_TIME", datetime);
		
		return details;
	}
	/**
	 * Adds new position to the database, note that the name should be unique
	 * @param value Name to add.
	 */
	public void addPosition(HashMap<String, String> details)
	{		
		if(positions.insertItem(details))
		{
			ids.DisplayStatus("Positions added!");	
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
			success=persons.deleteItem(values[i]);
		}
		ids.DisplayStatus(success?"Record deleted!":"Unable to delete record!");
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
			success=positions.deleteItem(values[i]);
		}
		ids.DisplayStatus(success?"Record deleted!":"Unable to delete record!");
	}


	/**
	 * Updates record in the database.
	 * @param details Object properties.
	 */
	public void updateEmployee(HashMap<String, String> details) {
			if(persons.updateItem(details))
			{
				ids.DisplayStatus("Record updated!");
			}
			else
			{
				ids.DisplayStatus("Unable to update record!");
			}
	}
	
	/**
	 * Updates record in the database.
	 * @param details Object properties.
	 */
	public void updatePosition(HashMap<String, String> details) {
			if(positions.updateItem(details))
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
	public boolean checkDutyConflict(HashMap<String, String> details) {
		DBObject employee=persons.getObjectByName(details.get("PERSON_ID"));
		return duties.checkAvailability(employee.getProperty("PERSON_ID"), 
				details.get("DUTY_START_TIME"), details.get("DUTY_END_TIME"));
	}
	
	public String getTotalEmpoloyeeHours(String employee_name, String start, String end)
	{
		DBObject employee=persons.getObjectByName(employee_name);
		return duties.getTotalEmpoloyeeHours(employee.getProperty("PERSON_ID"), 
				start, end); 
	}
	
}
