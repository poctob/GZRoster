package com.gzlabs.gzroster.data;
import java.util.ArrayList;
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
	
	//Database manager.
	private DBManager dbman;
	
	//Application properties.
	private Properties prop = null;
	
	//Status display
	private IDisplayStatus ids = null;
	
	//Current error message
	private String error_msg=null;
	
	//Employees list
	private DBObjectList persons;
	
	//Positions list
	private DBObjectList positions;
	
	//Duties List
	private DBDutiesList duties;
	
	/**
	 * Default constructor. Initializes member variables.
	 * @param pprop  Properties object.
	 * @param pids Status display
	 */
	public DataManager(Properties pprop, IDisplayStatus pids) {
		positions=null;
		
		ids = pids;
		prop = pprop;
		
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
	
	public ArrayList<String> isDutyOn(String date, String postion_id)
	{
		ArrayList<String> pers=duties.isOn(date, postion_id);
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
		String pers=person.substring(0, person.length()-1);
		pers=pers.substring(1, pers.length());
		
		details.put("PLACE_ID", position);
		details.put("PERSON_ID", persons.getObjectByName(pers).getProperty("PERSON_ID"));
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

}
