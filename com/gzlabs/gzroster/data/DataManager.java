package com.gzlabs.gzroster.data;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Properties;

import com.gzlabs.gzroster.gui.IConnectionStatus;
import com.gzlabs.gzroster.gui.IDisplayStatus;
import com.gzlabs.gzroster.sql.DBManager;
import com.gzlabs.gzroster.sql.DBObjectType;
import com.gzlabs.gzroster.sql.DB_Factory;
import com.gzlabs.utils.CryptoUtils;
import com.gzlabs.utils.DateUtils;

/**
 * Manages application data. Gets data from the database and populates objects
 * with it.
 * 
 * @author apavlune
 * 
 */
public class DataManager implements Runnable {

	private static final String CONFIG_FILE_PATH = "GZRoster.config";

	// Database manager.
	private DBManager dbman;

	// Application properties.
	private Properties prop = null;

	// Status display
	private IDisplayStatus ids = null;

	private IConnectionStatus iconn;

	// DB Factory
	private DB_Factory db_factory;
	
	//Config file
	String config_file;

	/**
	 * Default constructor. Initializes member variables.
	 * 
	 * @param pprop
	 *            Properties object.
	 * @param pids
	 *            Status display
	 * @param p_conf_file
	 * 			   Config file           
	 */
	public DataManager(IDisplayStatus pids, IConnectionStatus conn, String p_conf_file) {
		ids = pids;
		iconn = conn;
		config_file=p_conf_file!=null?p_conf_file:CONFIG_FILE_PATH;
	}

	/**
	 * Initialized database manager
	 * 
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
	 * 
	 * @return List of employees
	 */
	public ArrayList<String> getElementsNames(DBObjectType type) {
		return db_factory.getElementsNames(type);
	}

	/**
	 * Retrieves detailed information for a single element.
	 * 
	 * @param name
	 *            Employee name to get the data for.
	 * @param type
	 *            Type of the element
	 * @return ArrayList of employee details.
	 */
	public DB_Object getElementDetails(String name, DBObjectType type) {
		return name != null ? db_factory.getElementByName(name, type) : null;
	}

	/**
	 * Retrieves a list of positions that this person is mapped to.
	 * 
	 * @return List of position names
	 */
	public ArrayList<String> getPersonToPosMapping(String person_name) {
		if (person_name != null) {
			Person person = (Person) db_factory.getElementByName(person_name,
					DBObjectType.PERSON);

			if (person != null) {
				ArrayList<String> position_ids = person.getM_positions();
				ArrayList<String> retval = new ArrayList<String>();

				if (position_ids != null) {
					for (String s : position_ids) {
						retval.add(s);
					}
				}
				return retval;
			}
		}
		return null;
	}

	/**
	 * Gets a list of people scheduled on a specified position and date/time
	 * 
	 * @param date
	 *            Date/time duty is scheduled on.
	 * @param postion_id
	 *            Duty position.
	 * @return A List of people scheduled on that date.
	 */
	public ArrayList<String> isDutyOn(String date, String postion_id) {
		return db_factory.isDutyOn(date, postion_id);
	}

	/**
	 * Adds new record to the database
	 * 
	 * @param details
	 *            Object to add to the database.
	 * 
	 * @param type
	 *            Object type
	 */
	public void addRecord(DB_Object details, DBObjectType type) {
		if (details != null) {
			if (db_factory.insertRecord(details, type)) {
				safeDisplayStatus("Record added!");
			} else {
				safeDisplayStatus("Unable to add record!");
			}
		}
	}

	/**
	 * Finds start date/time for a specified duty.
	 * 
	 * @param person
	 *            Person name.
	 * @param position
	 *            Position name.
	 * @param datetime
	 *            Reference date/time, can be any time within the shift.
	 * @return String date/time representation of the duty start.
	 */
	public Calendar getDutyStart(String person, String position, String datetime) {
		Duty duty = getDutyByTime(person, position, datetime);
		if (duty != null) {
			return duty.getM_start();
		}
		return null;
	}

	/**
	 * Finds end date/time for a specified duty.
	 * 
	 * @param person
	 *            Person name.
	 * @param position
	 *            Position name.
	 * @param datetime
	 *            Reference date/time, can be any time within the shift.
	 * @return String date/time representation of the duty end.
	 */
	public Calendar getDutyEnd(String person, String position, String datetime) {
		Duty duty = getDutyByTime(person, position, datetime);
		if (duty != null) {
			return duty.getM_end();
		}
		return null;

	}

	/**
	 * Finds duty scheduled for specified time..
	 * 
	 * @param person
	 *            Person name.
	 * @param position
	 *            Position name.
	 * @param datetime
	 *            Reference date/time, can be any time within the shift.
	 * @return Duty scheduled for specified parameters.
	 */
	private Duty getDutyByTime(String person, String position, String datetime) {
		if (person != null && person.length() > 1) {
			return db_factory.findDutyByTime(person, position, datetime);
		}
		return null;
	}

	/**
	 * Deletes a record from the database.
	 * 
	 * @param obj
	 *            Element to delete.
	 * @param type
	 * 			  Element type.
	 */
	public void deleteRecord(DB_Object obj, DBObjectType type) {
		boolean success = false;

		success = db_factory.deleteElement(obj, type);
		if (success) {
			safeDisplayStatus("Record deleted!");
		} else {
			safeDisplayStatus("Unable to delete record!");
		}
	}

	/**
	 * Updates object in the database.
	 * 
	 * @param oldObj
	 *            Old object
	 * @param newObj
	 *            New object
	 *            
	 *@param type
	 *			Object type.            
	 */
	public void updateRecord(DB_Object oldObj, DB_Object newObj,
			DBObjectType type) {
		if (db_factory.updateElement(oldObj, newObj, type)) {
			safeDisplayStatus("Record updated!");
		} else {
			safeDisplayStatus("Unable to update record!");
		}
	}

	/**
	 * Determines a label for specified cell.
	 * 
	 * @param str_time
	 *            Time of the shift (row label)
	 * @param column_label
	 *            Position name (column label)
	 * @param string_date
	 *            Date of the shift (currently selected date).
	 * @return Name of a person(s) scheduled for this shift.
	 */
	public String getCellLabelString(String str_time, String column_label,
			String string_date) {
		if (column_label != null && string_date != null && str_time != null) {

			String date = string_date + " " + str_time + ":00.0";

			ArrayList<String> persons_on_duty = null;
			persons_on_duty = isDutyOn(date, column_label);

			if (persons_on_duty != null && persons_on_duty.size() > 0) {
				return persons_on_duty.toString();
			}
			return "";
		}
		return null;
	}

	/**
	 * Creates a list of times between starting and ending times from the
	 * configuration, using an interval from the configuration.
	 * 
	 * @return List of time spans.
	 */
	public ArrayList<Object> getTimeSpan() {
		return DateUtils.getTimeSpan(prop.getProperty("day_start"),
				prop.getProperty("day_end"),
				prop.getProperty("interval_minutes"));
	}

	/**
	 * Loads properties file.
	 * 
	 * @return null if something went wrong. Properties loaded from file
	 *         otherwise.
	 */
	public Properties getProp() {
		safeDisplayStatus("Loading configuration...");
		prop = null;
		try {
			prop = new Properties();
			prop.load(new FileInputStream(config_file));
		} catch (Exception e) {
			safeDisplayStatus("Unable to load configuration file!");
		}
		safeDisplayStatus("Done...");
		return prop;
	}

	/**
	 * Checks if an employee has already been scheduled.
	 * 
	 * @param duty
	 *           Duty object to check.
	 * @return true is person has a conflict, false otherwise.
	 */
	public boolean checkDutyConflict(DB_Object duty) {
		return db_factory.checkDutyConflict(duty);
	}

	/**
	 * Calculates the total hours that an employee is scheduled for during a
	 * specified period.
	 * 
	 * @param employee_name
	 *            Name of the employee.
	 * @param start
	 *            Start of the period.
	 * @param end
	 *            End of the period.
	 * @return Total hours
	 */
	public String getTotalEmpoloyeeHours(String employee_name, String start,
			String end) {

		return db_factory.getTotalEmpoloyeeHours(employee_name, start, end);
	}

	/**
	 * Gets a list of employees that are allowed to work specified positions for
	 * specified time span.
	 * 
	 * @param col_label
	 *            Position name
	 * @param start
	 *            Start of the time span.
	 * @param end
	 *            End of the time span.
	 * @return List of the employee names.
	 */
	public ArrayList<String> getAllowedEmployees(String col_label,
			String start, String end) {
		return db_factory.getAllowedEmployees(col_label, start, end);
	}

	/**
	 * Fetches most recent records from the database.
	 */
	public void refreshData() {
		db_factory.refreshData();
	}

	/**
	 * Saves properties into a file.
	 * 
	 * @param prop
	 *            Properties to save.
	 */
	public void saveProp(Properties pprop) {
		if (pprop == null) {
			safeDisplayStatus("Properties are empty. Not saving.");
			return;
		}
		prop = pprop;
		safeDisplayStatus("Saving configuration...");
		FileOutputStream fout = null;
		try {
			fout = new FileOutputStream(config_file);
			prop.store(fout, "Auto-Save " + DateUtils.getCurrentDateString());
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
	 * 
	 * @param status
	 *            Message to display.
	 */
	private void safeDisplayStatus(String status) {
		if (ids != null && status!=null) {
			ids.DisplayStatus(status);
		}
	}

	/**
	 * Intializes database connection in a separate thread.
	 */
	@Override
	public void run() {

		if (iconn == null) {
			safeDisplayStatus("Main window is not available! Exiting...");
			System.exit(0);
		}

		getProp();

		if (prop != null && !prop.isEmpty()) {
			safeDisplayStatus("Attempting to connect to the databse...");

			if (initDBMan()) {
				safeDisplayStatus("Connected to the Database!");
				db_factory = new DB_Factory(dbman);
				db_factory.refreshData();

			} else {
				safeDisplayStatus("Database connection failed. Exiting...");
				iconn.setError();
				iconn.setInitialized();

				return;
			}
		} else {
			safeDisplayStatus("Empty config file. Unable to continiue...");
			iconn.setError();
			iconn.setInitialized();

			return;
		}

		iconn.setInitialized();
		return;

	}

	/**
	 * Sets employees pin using SHA512 hash.
	 * @param pin Pin to hash.
	 * @param name Employee's name.
	 */
	public void setPin(String pin, String name) {
		String salt = CryptoUtils.generateRandomSalt(120, 32);
		String hash = CryptoUtils.hashPasswordSHA512(pin, salt);
		db_factory.setPin(hash, salt, name);

	}

	/**
	 * Fetches all time offs from the database.
	 * @return Time Offs collection.
	 */
	public ArrayList<Object> getAllTimesOff() {
		return db_factory.getAllTimesOff();
	}

	/**
	 * Fetches all available time off status label.
	 * @return Time off status labels
	 */
	public ArrayList<String> getTimeOffStatusOptions() {
		return db_factory.getTimeOffStatusOptions();
	}

	/**
	 * Retrieves a list of Active employees stored in the database
	 * 
	 * @return List of employees
	 */
	public ArrayList<String> getActiveEmployees() {
		return db_factory.getActiveNames();
	}

	/**
	 * Updates time off object in the database.
	 * @param timeOff Time off object to update.
	 */
	public void updateTimesOff(ArrayList<Object> timeOff) {
		
		if(timeOff!=null)
		{
			//All time offs are deleted initially
			db_factory.deleteTimeOffs();	
			
			//then they are re-added.
			for (Object obj : timeOff) {
				
				if(obj!=null && obj instanceof TimeOff)
				{
					TimeOff toff = (TimeOff) obj;
									
					db_factory.requestTimeOff(toff.getName(), toff.getStartStr(),
							toff.getEndStr(), toff.getStatus());
				}
			}
		}
	}

	/**
	 * Fetches a list of privileges mapped for a specified person
	 * @param person_name Name of person to fetch.
	 * @return Collection of privileges.
	 */
	public ArrayList<String> getPersonToPrivilegesMapping(String person_name) {
		if (person_name != null) {
			Person person = (Person) db_factory.getElementByName(person_name,
					DBObjectType.PERSON);

			if (person != null) {
				return person.getM_privileges();
			}
		}
		return null;
	}

	/**
	 * Deletes a duty from the database.
	 * @param start_date Duty start
	 * @param end_date Duty end
	 * @param position Position
	 * @param person Person
	 */
	public void deleteDuty(Calendar start_date, Calendar end_date,
			String position, String person) {
		boolean success = false;
		Duty duty = new Duty(start_date, end_date, position, person);
		success = db_factory.deleteElement(duty, DBObjectType.DUTY);
		if (success) {
			safeDisplayStatus("Record deleted!");
		} else {
			safeDisplayStatus("Unable to delete record!");
		}
	}

	/**
	 * Fetches database object based on its name.
	 * @param name Name to use
	 * @param type Object type.
	 * @return Database object that matches parameters.
	 */
	public DB_Object getObjectByName(String name, DBObjectType type) {
		return db_factory.getElementByName(name, type);
	}
}
