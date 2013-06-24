package com.gzlabs.gzroster.data;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import com.gzlabs.drosterheper.IDisplayStatus;
import com.gzlabs.gzroster.sql.DBManager;
import com.gzlabs.gzroster.sql.DB_Factory;
import com.gzlabs.gzroster.sql.Tables;
import com.gzlabs.gzroster.sql.DB_Factory.ObjectType;

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
			safeDisplayStatus("Attempting to connect to the databse...");
	
			if (initDBMan()) {
				safeDisplayStatus("Connected to the Database!");
				
				updateDBObjects();
				
			} else {
				safeDisplayStatus("Database connection failed. Exiting...");
				return;
			}
		}
		else
		{
			safeDisplayStatus("Empty config file. Unable to continiue...");
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
		return name!=null?DB_Factory.geDetaislByName(db_persons, name):null;
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
	 * Retrieves a list of positions that this person is mapped to.
	 * @return List of position names
	 */
	public ArrayList<String> getPersonToPosMapping(String person_name)
	{	
		if(person_name!=null)
		{
			Person person=(Person)DB_Factory.getObjectByName(db_persons, person_name);
			
			if(person!=null)
			{
				ArrayList<Integer> position_ids=person.getM_positions();
				
				ArrayList<String> retval=new ArrayList<String>();
				for(Integer s : position_ids)
				{
					retval.add(DB_Factory.getObjectNameByPKID(db_positions, s));
				}
				
			return retval;
			}
		}
		return null;
	}
		
	/**
	 * Retrieves detailed information for a single position.
	 * @param name Positions name to get the data for.
	 * @return ArrayList of positions details.
	 */
	public ArrayList<String> getPositionDetailsByName(String name)
	{		
		return name!=null?DB_Factory.geDetaislByName(db_positions, name):null;
	}
	
	/**
	 * Retrieves a list of duty shifts stored in the database
	 * @return List of duties
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
		if(date!=null && postion_id != null)
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
		return null;
	}
	
	/**
	 * Adds new employee to the database, note that the name should be unique
	 * @param details ArrayList with the details to add to the database.
	 * @param position_boxes Names of the positions that are checked for this employee. 
	 */
	public void addEmployee(ArrayList<String>details, ArrayList<String> position_boxes)
	{
		if(details != null && position_boxes!=null)
		{
			boolean usingFB=prop.get("db_type").equals(Tables.FB_DB_FLAG);
			if(DB_Factory.insertRecord(ObjectType.PERSON, dbman, details,usingFB))
			{
				updatePersonToPositionMap(details.get(Tables.PERSON_NAME_INDEX), position_boxes);
				safeDisplayStatus("Employee added!");	
				updateDBObjects();
			}
			else
			{
				safeDisplayStatus("Unable to add employee!");
			}
		}
	}
	
	/**
	 * Adds new duty to the database, note that the name should be unique
	 * @param details ArrayList with the details to add to the database.
	 */
	public void addDuty(ArrayList<String> details)
	{		
		if(details != null)
		{
			if(DB_Factory.insertDuty(dbman, details, db_positions, db_persons))
			{
				safeDisplayStatus("Shift added!");
				updateDBObjects();
			}
			else
			{
				safeDisplayStatus("Unable to add shift!");
			}
		}
	}
	
	/**
	 * Deletes a duty from the database.
	 * @param details ArrayList with the details to add to the database.
	 */
	public void deleteDuty(ArrayList<String> details)
	{
		if(details!=null)
		{
			if(DB_Factory.deleteDuty(db_duties, dbman, details))
			{
				safeDisplayStatus("Shift deleted!");	
				updateDBObjects();
			}
			else
			{
				safeDisplayStatus("Failed to delete!");	
			}
		}
	}
	
	/**
	 * Finds start date/time for a specified duty.
	 * @param person Person name.
	 * @param position Position name.
	 * @param datetime Reference date/time, can be any time within the shift.
	 * @return String date/time representation fo the duty start.
	 */
	public String getDutyStart(String person, String position, String datetime)
	{
		Duty duty = getDutyByTime(person, position, datetime);
		if (duty != null) {
			return DateUtils.DateToString(duty.getM_start());
		}
		return null;
	}
	
	/**
	 * Finds end date/time for a specified duty.
	 * @param person Person name.
	 * @param position Position name.
	 * @param datetime Reference date/time, can be any time within the shift.
	 * @return String date/time representation fo the duty start.
	 */
	public String getDutyEnd(String person, String position, String datetime) {
		Duty duty = getDutyByTime(person, position, datetime);
		if (duty != null) {
			return DateUtils.DateToString(duty.getM_end());
		}
		return null;

	}
	
	/**
	 * Finds duty scheduled for specified time..
	 * @param person Person name.
	 * @param position Position name.
	 * @param datetime Reference date/time, can be any time within the shift.
	 * @return Duty scheduled for specified parameters.
	 */
	private Duty getDutyByTime(String person, String position, String datetime)
	{
		int person_id=DB_Factory.getObjectPKIDByName(db_persons, person);
		int position_id=DB_Factory.getObjectPKIDByName(db_positions, position);
		if(person!=null && person.length()>1)
		{
			return DB_Factory.findDutyByTime(db_duties, person_id, position_id, datetime);
		}
		return null;
	}
	
	/**
	 * Adds new position to the database, note that the name should be unique
	 * @param details Position information
	 */
	public void addPosition(ArrayList<String> details)
	{	
		if(details != null)
		{
			boolean usingFB=prop.get("db_type").equals(Tables.FB_DB_FLAG);
			if(DB_Factory.insertRecord(ObjectType.POSITION, dbman, details, usingFB))
			{
				safeDisplayStatus("Positions added!");	
				updateDBObjects();
			}
			else
			{
				safeDisplayStatus("Unable to add position!");
			}
		}
	}
	
	/**
	 * Deletes one or more employees from the database.
	 * @param values employees to delete.
	 */
	public void deleteEmployee(String [] values)
	{
		boolean success=false;
		if(values != null)
		{
			for(int i=0; i<values.length; i++)
			{
				success=DB_Factory.deleteRecord(db_persons,dbman,values[i]);
				if(success)
				{
					safeDisplayStatus("Record deleted!");
					updateDBObjects();
				}
				else
				{
					safeDisplayStatus("Unable to delete record!");
				}
			}	
		}
	}
	
	/**
	 * Deletes one or more position from from the database.
	 * @param values positions to delete.
	 */
	public void deletePosition(String [] values)
	{
		boolean success=false;
		if(values != null)
		{
			for(int i=0; i<values.length; i++)
			{
				success=DB_Factory.deleteRecord(db_positions,dbman,	values[i]);
				if(success)
				{
					safeDisplayStatus("Record deleted!");
					updateDBObjects();
				}
				else
				{
					safeDisplayStatus("Unable to delete record!");
				}
			}	
		}
	}

	/**
	 * Updates employee in the database.
	 * @param old_val Old values
	 * @param new_val New values
	 * @param position_boxes Positions list that this employee can take. 
	 */
	public void updateEmployee(ArrayList<String> old_val, ArrayList<String> new_val, ArrayList<String> position_boxes) {
		
		if(DB_Factory.updateRecord(db_persons, dbman, old_val, new_val))
		{
			updatePersonToPositionMap(new_val.get(Tables.PERSON_NAME_INDEX), position_boxes);
			safeDisplayStatus("Record updated!");
		}
		else
		{
			safeDisplayStatus("Unable to update record!");
		}	
			
	}
	
	/**
	 * Updates mapping of person to position
	 * @param person_name Employee name to update
	 * @param position_boxes Positions that this employee can take.
	 */
	private void updatePersonToPositionMap(String person_name, ArrayList<String> position_boxes)
	{
		if(person_name!=null & position_boxes!=null)
		{
			ArrayList<Integer> place_ids=new ArrayList<Integer>();
			
			for(String s:position_boxes)
			{
				int place_id=DB_Factory.getObjectPKIDByName(db_positions, s);
				place_ids.add(place_id);
			}
			
			DB_Factory.updatePersonToPosition(db_persons, place_ids, person_name, dbman);
		}
	}
	
	/**
	 * Updates position in the database.
	 * @param old_val Old values
	 * @param new_val New values
	 */
	public void updatePosition(ArrayList<String> old_val, ArrayList<String> new_val) {
			if(DB_Factory.updateRecord(db_positions, dbman, old_val, new_val))
			{
				updateDBObjects();
				safeDisplayStatus("Record updated!");
			}
			else
			{
				safeDisplayStatus("Unable to update record!");
			}
	}
	
	/**
	 * Fetches database data for local objects
	 */
	private void updateDBObjects()
	{
		boolean usingFB=prop.get("db_type").equals(Tables.FB_DB_FLAG);
		db_positions=DB_Factory.getAllRecords(ObjectType.POSITION, dbman, usingFB);	
		db_persons=DB_Factory.getAllRecords(ObjectType.PERSON, dbman, usingFB);	
		db_duties=DB_Factory.getAllDuties(dbman, db_persons, db_positions, usingFB);
		
	}
	
	/**
	 * Determines a label for specified cell.
	 * @param str_time Time of the shift (row label)
	 * @param column_label Position name (column label)
	 * @param string_date Date of the shift (currently selected date).
	 * @return Name of a person(s) scheduled for this shift.
	 */
	public String getCellLabelString(String str_time, String column_label, String string_date)
	{
		if(column_label!=null && string_date!=null && str_time!=null)
		{
							
			String date=string_date+" "+str_time+":00.0";
			
			ArrayList<String> persons_on_duty=null;
			persons_on_duty=isDutyOn(date, column_label);
			
			if(persons_on_duty!=null && persons_on_duty.size()>0)
			{
				return persons_on_duty.toString();
			}												
			return "";
		}
		return null;
	}
	
	/**
	 * Creates a list of times between starting and ending times 
	 * from the configuration, using an interval from the configuration.
	 * @return List of time spans.
	 */
	public ArrayList<String> getTimeSpan()
	{
		return DateUtils.getTimeSpan
				(prop.getProperty("day_start"), prop.getProperty("day_end"), prop.getProperty("interval_minutes"));		
	}
	
	/**
	 * Loads properties file.
	 * 
	 * @param ids
	 *            Information display interface.
	 * @return null if something went wrong. Properties loaded from file
	 *         otherwise.
	 */
	public Properties getProp() {
		safeDisplayStatus("Loading configuration...");
		prop = null;
		try {
			prop = new Properties();
			prop.load(new FileInputStream(CONFIG_FILE_PATH));
		} catch (Exception e) {
			safeDisplayStatus("Unable to load configuration file!");
		}
		safeDisplayStatus("Done...");
		return prop;
	}
	
	/**
	 *  Checks if an employee has already been scheduled.
	 * @param person_name Name of the employee
	 * @param start Shift start date/time
	 * @param end Shift end date/time
	 * @return true is person has a conflict, false otherwise.
	 */
	public boolean checkDutyConflict(String person_name, String start, String end) {
		
		int per_id=DB_Factory.getObjectPKIDByName(db_persons, person_name);
		
		for(DB_Object d:db_duties)
		{
			if(d!=null && ((Duty)d).personConflict(per_id, start, end))
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Calculates the total hours that an employee is scheduled for during a specified period.
	 * @param employee_name Name of the employee.
	 * @param start Start of the period.
	 * @param end End of the period.
	 * @return Total hours 
	 */
	public String getTotalEmpoloyeeHours(String employee_name, String start, String end)
	{
		int per_id=DB_Factory.getObjectPKIDByName(db_persons, employee_name);		
		double total_hours=0;
		
		for(DB_Object d: db_duties)
		{
			if(d!=null)
			{
				double hours=((Duty)d).getTotalEmpoloyeeHours(per_id, start, end);
				if(hours>0)
				{
					total_hours+=hours;
				}
			}
		}
		try
		{
			String retval=String.format("%.2f", total_hours);
			return retval;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	} 

	/**
	 * Gets a list of employees that are allowed to work specified positions for specified time span.
	 * @param col_label Position name
	 * @param start Start of the time span.
	 * @param end End of the time span.
	 * @return List of the employee names.
	 */
	public ArrayList<String> getAllowedEmployees(String col_label, String start, String end) {
		
		int place_id=DB_Factory.getObjectPKIDByName(db_positions, col_label);
		ArrayList<DB_Object> persons=new ArrayList<DB_Object>();
		for(DB_Object per:db_persons)
		{
			if(per!=null && ((Person)per).isActive() && ((Person)per).isPositionAllowed(place_id))
			{
				persons.add(per);
			}
		}
		ArrayList<String> retval=new ArrayList<String>();
		
		for(DB_Object per:persons)
		{
			if(per!=null && ((Person)per).isTimeAllowed(start, end))
			{				
				retval.add(per.getName());				
			}
		}
		
		return retval;

	}	

	/**
	 * Adds new time off request.
	 * @param start Time off start
	 * @param end Time off end
	 * @param nameText Name of the person.
	 * @return True if time off was added successfully, false otherwise
	 */
 	public boolean newTimeOffRequest(String start, String end, String nameText) {

		if(DB_Factory.addTimeOff(db_persons, start, end, nameText, dbman))
		{
			safeDisplayStatus("Time Off Added!");
			updateDBObjects();
			return true;
		}
		else
		{
			safeDisplayStatus("Unable to add Time Off!");
			return false;
		}
		
	}

 	/**
 	 * Fetches all time off requests for a specified employee.
 	 * @param string Employee name.
 	 * @return List of time off requests.
 	 */
	public ArrayList<String> getTimeOff(String string) {
		DB_Object person=DB_Factory.getObjectByName(db_persons, string);
		
		return person!=null?((Person)person).getTimesOff():null;
	}

	/**
	 * Deletes a time off request
	 * @param start Start of the request
	 * @param end End of the request
	 * @param nameText Name of the employee
	 * @return True is operation was successfull, false otherwise.
	 */
	public boolean deleteTimeOffRequest(String start, String end, String nameText) {
		if(DB_Factory.deleteTimeOff(db_persons, start, end, nameText, dbman))
		{
			safeDisplayStatus("Time Off Deleted!");
			updateDBObjects();
			return true;
		}
		else
		{
			safeDisplayStatus("Unable to delete Time Off!");
			return false;
		}
		
	}

	/**
	 * Fetches most recent records from the database for duties.
	 */
	public void refreshDutyList() {
		boolean usingFB=prop.get("db_type").equals(Tables.FB_DB_FLAG);
		db_duties=DB_Factory.getAllDuties(dbman, db_persons, db_positions, usingFB);
	}
	
	/**
	 * Saves properties into a file.
	 * 
	 * @param prop
	 *            Properties to save.
	 * @param ids
	 *            Information display interface.
	 */
	public void saveProp(Properties pprop) {
		if(pprop == null)
		{
			safeDisplayStatus("Properties are empty. Not saving.");
			return;
		}
		prop = pprop;
		safeDisplayStatus("Saving configuration...");
		FileOutputStream fout = null;
		try {
			fout = new FileOutputStream(CONFIG_FILE_PATH);
			prop.store(fout, "Auto-Save "
					+ DateUtils.getCurrentDateString());
		} catch (IOException e) {
			safeDisplayStatus(e.getMessage());
		} finally {
			try {
				fout.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		safeDisplayStatus("Done...");
	}
	
	/**
	 * Safety function for status display
	 * @param status Message to display.
	 */
	private void safeDisplayStatus(String status)
	{
		if(ids!=null)
		{
			ids.DisplayStatus(status);
		}
	}

	
}
