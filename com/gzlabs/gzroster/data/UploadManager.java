package com.gzlabs.gzroster.data;

import java.io.FileInputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;

import com.gzlabs.gzroster.sql.DBManager;
import com.gzlabs.gzroster.gui.IDisplayStatus;
import com.gzlabs.utils.ICalUtils;
import com.gzlabs.utils.SSHUtils;

/**
 * Manages data upload to the server.
 * 
 * @author apavlune
 * 
 */
public class UploadManager {
	
	/**
	 * Configuration file path
	 */
	private static final String CONFIG_FILE_PATH = "GZRoster.config";
	
	/**
	 * Database manager
	 */
	private DBManager dbman;
	
	/**
	 * Status display interface.
	 */
	private IDisplayStatus ids = null;
	
	/**
	 * Local properties object.
	 */
	private Properties prop = null;

	/**
	 * @return the ids
	 */
	public IDisplayStatus getIds() {
		return ids;
	}

	/**
	 * @param ids
	 *            the ids to set
	 */
	public void setIds(IDisplayStatus ids) {
		this.ids = ids;
	}	

	/**
	 * Default constructor with local variables initialization
	 * @param pprop Properties
	 * @param pids Display status interface.
	 */
	public UploadManager(Properties pprop, IDisplayStatus pids) {
		ids = pids;
		prop = pprop;

	}

	/**
	 * Performs data processing such as retrieving data from the db, purging if
	 * needed, creation and upload of the calendar.
	 * 
	 * @param password
	 *            SSH server password
	 */
	public void processData(String password) {
		ids.DisplayStatus("Attempting to connect to the databse...");

		//First step is to initialize database manager
		if (initDBMan()) {
			ids.DisplayStatus("Connected to the Database!");
		} else {
			ids.DisplayStatus("Database connection failed. Exiting...");
			return;
		}

		ids.DisplayStatus("Archving existing data...");
		
		//Attempt to copy data to the archive.
		if (archiveData()) {
			ids.DisplayStatus("Data archived successfully!");
		} else {
			ids.DisplayStatus("Failed to archive data...");
		}
		
		ids.DisplayStatus("Retrieving records...");
		
		//Get all records
		ResultSet rs = getDBData();

		if (rs == null) {
			ids.DisplayStatus("Something went wrong.  Result set is empty!");
		}

		ids.DisplayStatus("Attempting to write calendar...");
		
		//Try to make ICal file
		boolean success = makeICal(prop.getProperty("ical_file_path"), rs);

		if (!success) {
			ids.DisplayStatus("Something went wrong.  Unable to create calendar file!");
		}

		ids.DisplayStatus("Attempting to upload a file...");

		//Attempt to upload ICal to the server using SSH.
		success = uploadCalendar(prop.getProperty("ssh_host"),
				prop.getProperty("ssh_username"),
				password,
				Integer.parseInt(prop.getProperty("ssh_port")),
				prop.getProperty("ical_file_path"),
				prop.getProperty("ssh_destination"));

		if (!success) {
			ids.DisplayStatus("Something went wrong.  Unable to upload calendar file!");
		}
		dbman.disconnect();
		ids.DisplayStatus("Done...");
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
	 * Creates a copy of the data in the database.
	 * 
	 * @return true if copy is good, false otherwise.
	 */
	private boolean archiveData() {
		ids.DisplayStatus("Archiving data...");
		if (dbman == null) {
			if (!initDBMan())
				ids.DisplayStatus("Error archiving data!");
			return false;
		}		

		ResultSet rs = dbman.runQuery("SELECT DUTY_START_TIME, PLACE_ID, PERSON_ID, DUTY_END_TIME, DUTY_KEY FROM DUTIES", true);
		if (rs == null) {
			ids.DisplayStatus("No data to archive!");
			return true;
		}
		try {
			ArrayList<String> keys = new ArrayList<String>();
			while (rs.next()) {
				keys.add(rs.getString("DUTY_KEY"));
			}
			for (String s : keys) {
				ResultSet rs2 = dbman.runQuery("SELECT * FROM DUTIES_ARCHIVE "
						+ "WHERE DUTY_KEY='" + s + "'", true);
				if (rs2 != null) {
					int rs_count = 0;
					while (rs2.next()) {
						rs_count++;
					}
					if (rs_count == 0) {
						dbman.runQuery(
								"INSERT INTO DUTIES_ARCHIVE "
										+ "(DUTY_START_TIME, PLACE_ID, PERSON_ID, DUTY_END_TIME, DUTY_KEY) "
										+ "SELECT DUTY_START_TIME, PLACE_ID, PERSON_ID, DUTY_END_TIME, DUTY_KEY "
										+ "FROM DUTIES WHERE DUTY_KEY='" + s
										+ "'", false);
					}
				}
			}
		} catch (SQLException e) {
			ids.DisplayStatus("Error retrieving data to archive!");
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * Executes a query to get the data from the DB.
	 * @return null if something is wrong. Result set otherwise
	 */
	private ResultSet getDBData() {
		if (dbman == null) {
			if (!initDBMan())
				return null;
		}

		ResultSet rs = null;
		rs = dbman
				.runQuery(
						"SELECT DUTY_START_TIME, DUTY_END_TIME, PERSON_NAME, PLACE_NAME "
								+ "FROM DUTIES "
								+ "LEFT JOIN PERSON ON DUTIES.PERSON_ID=PERSON.PERSON_ID "
								+ "LEFT JOIN PLACE ON DUTIES.PLACE_ID=PLACE.PLACE_ID "
								+ "ORDER BY DUTY_START_TIME ASC", true);

		return rs;
	}

	/**
	 * Loads properties file.
	 * @return null if something went wrong. Properties loaded from file
	 *         otherwise.
	 */
	public Properties getProp() {
		ids.DisplayStatus("Loading configuration...");
		prop = null;
		try {
			prop = new Properties();
			prop.load(new FileInputStream(CONFIG_FILE_PATH));
		} catch (Exception e) {
			ids.DisplayStatus("Unable to load configuration file!");
			ids.DisplayStatus("Exiting...");
		}
		ids.DisplayStatus("Done...");
		return prop;
	}

	/**
	 * Saves properties into a file.
	 * 
	 * @param prop
	 *            Properties to save.
	 */
	public void saveProp(Properties pprop) {
		ids.DisplayStatus("Saving configuration...");
		prop = pprop;
		ids.DisplayStatus("Finished saving...");
	}

	/**
	 * Creates ICalendar out of the database data.
	 * 
	 * @param path
	 *            File path where to save the calendar.
	 * @param rs
	 *            Database data result set.
	 * @return true if everything is OK. False otherwise
	 */
	private boolean makeICal(String path, ResultSet rs) {
		ids.DisplayStatus("Creating calendar...");
		if (ICalUtils.makeICal(path, rs)) {
			ids.DisplayStatus("Done Saving ICal.");
			return true;
		}

		else {
			ids.DisplayStatus("Empty Result Set!");
			return false;
		}

	}

	/**
	 * Uploads calendar file to the server.
	 * 
	 * @param host
	 *            Server's host name.
	 * @param username
	 *            User name
	 * @param password
	 * @param port
	 * @param sourcefile
	 * @param destinationfile
	 * @return true if everything went OK. False otherwise.
	 */
	private boolean uploadCalendar(String host, String username,
			String password, int port, String sourcefile, String destinationfile) {
		boolean success = SSHUtils.sendFile(sourcefile, destinationfile,
				username, host, port, password);
		return success;
	}

}
