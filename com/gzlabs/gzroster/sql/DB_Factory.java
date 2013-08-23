package com.gzlabs.gzroster.sql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.gzlabs.gzroster.data.DB_Object;
import com.gzlabs.gzroster.data.Duty;
import com.gzlabs.gzroster.data.Person;
import com.gzlabs.gzroster.data.Position;
import com.gzlabs.gzroster.data.Privilege;

/**
 * Provides utilities for database Object generation and manipulation.
 * @author apavlune
 * 
 */
public class DB_Factory {

	//DB Manager, we need this guy to perform database operations.
	private DBManager dbman;
	
	//DB objects
	private ArrayList<DB_Object> db_positions;
	private ArrayList<DB_Object> db_persons;
	private ArrayList<DB_Object> db_duties;		
	private ArrayList<DB_Object> db_privileges;	
	
	/**
	 * Default constructor, initialized dbman object
	 * @param dbman
	 */
	public DB_Factory(DBManager dbman) {
		super();
		this.dbman = dbman;
		refreshData();
	}

	public void refreshData()
	{
		db_positions=getAllRecords(DBObjectType.POSITION);	
		db_persons=getAllRecords(DBObjectType.PERSON);
		db_duties=getAllRecords(DBObjectType.DUTY);
		db_privileges=getAllRecords(DBObjectType.PRIVILEGE);
	}
	/**
	 * Creates new database object based on the specified type.
	 * @param type Type of the object to create.  Comes from ObjectType enum.
	 * @return Newly created DB_Object.
	 */
	public DB_Object createObject(DBObjectType type) {
		if(type==null)
		{
			return null;
		}
		switch (type) {
		case POSITION:
			return new Position();
		case PERSON:
			return new Person();
		case DUTY:
			return new Duty();
		case PRIVILEGE:
			return new Privilege();
		default:
			return null;
		}
	}

	/**
	 * Fetches all records from the database.
	 * @param type Type of the table to use.
	 * @param dbman Database manager
	 * @param column Column for the WHERE clause
	 * @param value value for the WHERE clause
	 * @param usingFB flag indicating if we are using FB database
	 * @return List of the database objects the were fetched.
	 */
	public ArrayList<DB_Object> getAllRecords(DBObjectType type) {
		
		if(dbman!=null)
		{
			ResultSet records =runSproc(getSelect_sql(type), true);
			ArrayList<DB_Object> objects = new ArrayList<DB_Object>();
			
			if(records!=null)
			{
				try {
					while (records.next()) {
						DB_Object obj = createObject(type);
						obj.populateProperites(records);	
						if(obj!=null)
						{							
							objects.add(obj);
						}
					}
					
					if(type==DBObjectType.PERSON)
					{
						processPersons(objects);
					}		
					
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return objects;
			}
		}
		return null;
	}
	
	/**
	 * Populates person objects with auxiliary data.
	 * @param persons List of the persons to process.
	 * @param dbman Database manager to use.
	 */
	private void processPersons(ArrayList<DB_Object> persons)
	{
		if(persons!=null)
		{
			for(DB_Object person:persons)
			{
				if(person!=null && dbman!=null)
				{
					((Person) person).setM_positions
					(runSprocList(Tables.PROC_GET_PERSON_POSITIONS, person.getName(), "PLACE"));
					
					ResultSet records = runSproc(Tables.PROC_GET_TIME_OFF, true, person.getName());						
					((Person) person).populateTimeOff(records);
					
					((Person)person).setM_privileges(
							runSprocList(Tables.PROC_GET_PERSON_PRIVILEGES, 
									((Person)person).getName(), 
									"PRIVILEGE"));	
				}
			}	
		}
	}

	/**
	 * Inserts new database record
	 * @param type DB Object type to insert
	 * @param dbman Database Manager
	 * @param details Details of the record
	 * @return True if success, false otherwise
	 */
	public  boolean insertRecord(DB_Object details, DBObjectType type) {
		if(details !=null)
		{
			DB_Object obj = createObject(type);
						
			if(obj!=null && dbman!=null)
			{
				obj.populateProperties(details);			
				ResultSet rs = runSproc(obj.getInsert_sql(), false, obj.toStringArray());
		
				if (rs != null) {
					refreshData();
					if(type==DBObjectType.PERSON)
					{

						updatePersonToPosition(obj);
						updatePersonToPrivilege(obj);
					}
					return true;
					
				}
			}
		}
		return false;
	}

	/**
	 * Deletes a record from the database
	 * @param objects Object collection that will be used to pick from.
	 * @param dbman Database manager to use.
	 * @param name Name of the object to delete.
	 * @return True if success, false otherwise.
	 */
	public boolean deleteRecord(ArrayList<DB_Object> objects, DB_Object obj) {
			if (objects!=null && obj != null && dbman != null) {

				for(DB_Object o: objects)
				{
					if(o.matches(obj, false))
					{
						ResultSet rs = runSproc(o.getDelete_sql(), true,o.getPKIDStr());
						if (rs == null) {
							return false;
						}
						refreshData();
						return true;
					}
				}
			}
		return false;
	}	
		
	
	/**
	 * Finds a specific duty using time as reference.
	 * @param person_id Person id in the duty
	 * @param position_id Position id in the duty
	 * @param datetime date to search.
	 * @return Duty if it's found, null if it's not.
	 */
	public Duty findDutyByTime(String person_id, String position_id, String datetime)
	{
		if(db_duties!=null)
		{
			for(DB_Object obj:db_duties)
			{
				if(obj!=null)
				{
					if(((Duty)obj).matches(person_id, position_id, datetime))
					{
						return (Duty) obj;
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * Updates database record
	 * @param objects List of objects
	 * @param dbman Database Manager
	 * @param old_details Old details
	 * @param new_details New details
	 * @return True if success, false otherwise
	 */
	public boolean updateRecord(ArrayList<DB_Object> objects,DB_Object old_details, DB_Object new_details) {
		
		if(objects!=null)
		{
			for(DB_Object obj: objects)
			{
				if(obj!=null && dbman!=null && obj.matches(old_details, false))
				{
					obj.populateProperties(new_details);
					ResultSet rs = runSproc(obj.getUpdate_sql(), false, obj.toStringArray());
					if (rs != null) {
						refreshData();
						return true;
					}
				}
			}		
		}
		return false;		
	}	
	
/**
 * Generates sql select statement.
 * @param column Select column
 * @param value Value for the select statement
 * @param type Table type
 * @return String containing sql statement
 */
	private String getSelect_sql(DBObjectType type) {	
		String sproc_name=null;
		if(type!=null)
		{
			switch(type)
			{
				case POSITION:sproc_name=Tables.PROC_GET_POSITION;
						break;
				case PERSON:sproc_name=Tables.PROC_GET_PERSON;
					break;
				case DUTY:sproc_name=Tables.PROC_GET_DUTY;
					break;
				case PRIVILEGE:sproc_name=Tables.PROC_GET_PRIVILEGES;
					break;
				default:
					return null;
			}		
		}
		return sproc_name;
	}

	/**
	 * Executes SQL statement.
	 * @param dbman Database manager to use.
	 * @param sql_str SQL string to execute.
	 * @param wantresult Flag if result is wanted.
	 * @return SQL result set.
	 */
	private ResultSet runSQL(String sql_str,boolean wantresult) {
		if (dbman != null) {
			return dbman.runQuery(sql_str, wantresult);
		}
		return null;
	}

	
	/**
	 * Convenience method to get names of the objects from the list.
	 * @return ArraList of names.
	 */
	public ArrayList<String> getNames(ArrayList<DB_Object> objects)
	{
		ArrayList<String> names=new ArrayList<String>();
		if(objects!=null)
		{
			for(DB_Object obj:objects)
			{
				if(obj!=null)
				{
					names.add(obj.getName());
				}
			}
		}
		return names;
	}
	
	
	/**
	 * Updates person to position map.
	 * @param place_ids List of positions
	 * @param nameText Name of a person
	 * @param dbman database manager
	 * @return True if a map was updated, false otherwise.
	 */
	public void updatePersonToPosition(DB_Object person)
	{
		if(person!=null)
		{
			Person pers=(Person)person;
			runSproc(Tables.PROC_DELETE_PERSON_POSITION, false, pers.getName());
			if(pers.getM_positions()!=null)
			{
				for(String s:pers.getM_positions())
				{
					runSproc(Tables.PROC_ADD_PERSON_POSITION, false, pers.getName(),s);
				}
				refreshData();
			}
		}
	}
	
	/**
	 * Updates person to position map.
	 * @param objects Person list
	 * @param place_ids List of positions
	 * @param nameText Name of a person
	 * @param dbman database manager
	 * @return True if a map was updated, false otherwise.
	 */
	public void updatePersonToPrivilege(DB_Object person)
	{
		if(person!=null)
		{
			Person pers=(Person)person;
			runSproc(Tables.PROC_DELETE_PERSON_PRIVILEGE, false, pers.getName());
			
			if(pers.getM_privileges()!=null)
			{
				for(String s:pers.getM_privileges())
				{
					runSproc(Tables.PROC_ADD_PERSON_PRIVILEGE, false, pers.getName(),s);
				}
				refreshData();
			}
		}
	}

		
	/**
	 * Get today's schedule for a specified employee.
	 * @param dbman Database manager
	 * @param name Employee's name
	 * @return List of database duties.
	 */
	public ArrayList<DB_Object> getTodaysDuty(String name) {
		if(dbman !=null && name!=null)
		{
			ResultSet records = runSproc(Tables.PROC_TODAY_SCHEDULE, true,name);
			ArrayList<DB_Object> objects = new ArrayList<DB_Object>();
			
			if(records!=null)
			{
				try {
					while (records.next()) {
						Duty obj = (Duty) createObject(DBObjectType.DUTY);
						obj.populateProperites(records);
						if(obj!=null)
						{
							objects.add(obj);
						}
					}
				records.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return objects;
			}
		}
		return null;		
	}
	
	/**
	 * Inserts clockin/out event.
	 * @param dbman Database manager
	 * @param name Person's name
	 * @param isClockIn Whether this is a clock in event.
	 * @return True is operation was a success.
	 */
	public boolean insertClockEvent(String name, boolean isClockIn, String reason)
	{
		boolean retval=false;
		if(dbman !=null && name!=null)
		{
			ResultSet records =runSproc(isClockIn?Tables.PROC_CLOCKIN:Tables.PROC_CLOCKOUT, true, name, reason); 
			return records!=null;
		}
		return retval;
	}
	
	/**
	 * Checks if a person is clocked in.
	 * @param dbman Database manager
	 * @param name Person's name
	 * @return True if person is clocked in.
	 */
	public boolean isClockedIn(String name)
	{
		return runSprocBoolean(Tables.PROC_ISCLOCKEDIN, name, "isClockedIN");
	}
	
	/**
	 * Fetches scheduled hours for current week.
	 * @param dbman Database manager
	 * @param name Person's name
	 * @return Scheduled hours for a week.
	 */
	public int getWeekScheduledHours(String name)
	{
		return runSprocInt(Tables.PROC_TOTALSCHEDULEDHOURS, name, "TOTAL_SCHEDULED");
	}
	
	/**
	 * Fetches worked hours for current week.
	 * @param dbman Database manager
	 * @param name Person's name
	 * @return Worked hours for a week.
	 */
	public int getWeekWorkeddHours(String name)
	{
		return runSprocInt(Tables.PROC_WEEKLYWORKEDDHOURS, name, "total_minutes");
	}
	
	public boolean requestTimeOff(String name, String start, String end, String status)
	{
		boolean retval=false;
		if(dbman !=null && name!=null)
		{
			ResultSet records =runSproc(Tables.PROC_REQUESTTIMEOFF, true, name, start, end, status); 
			refreshData();
			return records!=null;
		}
		return retval;
	}
	
	/**
	 * Runs stored procedure where a boolean is returned as a result
	 * @param dbman Database manager
	 * @param sproc_name Procedure's name
	 * @param args Procedure's arguments
	 * @param col_name Column name to return
	 * @return Boolean contained in the specified column
	 */
	private boolean runSprocBoolean(String sproc_name, String args, String col_name)
	{
		if(dbman !=null && sproc_name!=null && col_name !=null)
		{
			ResultSet records = runSproc(sproc_name, true, args);
			if(records!=null)
			{
				try {
					while (records.next()) {
						if(records.getInt(col_name)==1)
						{
							return true;
						}
					}
					
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return false;
	}
	
	/**
	 * Runs stored procedure where a single integer is returned as a result
	 * @param dbman Database manager
	 * @param sproc_name Procedure's name
	 * @param args Procedure's arguments
	 * @param col_name Column name to return
	 * @return Integer contained in the specified column
	 */
	private int runSprocInt(String sproc_name, String args, String col_name)
	{
		if(dbman !=null && sproc_name!=null && col_name !=null)
		{
			ResultSet records = runSproc(sproc_name, true, args);
			if(records!=null)
			{
				try {
					while (records.next()) {
						return records.getInt(col_name);
					}
					
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return 0;
	}
	
	
	/**
	 * Runs sql stored procedure
	 * @param dbman Database manager
	 * @param sproc_name Procedure's name
	 * @param args Procedure's arguments
	 * @param wantresult Whether a result is wanted
	 * @return Resultset is wanted, null otherwise.
	 */
	private ResultSet runSproc( String sproc_name, boolean wantresult, String ... args)
	{
		ResultSet retval=null;
		if(dbman !=null && sproc_name!=null)
		{
			String sql=QueryFactory.getProc(sproc_name, args);
			retval=runSQL(sql,	wantresult);
		}
		return retval;
	}

	public ArrayList<String> getActiveNames() {
		ArrayList<String> names=new ArrayList<String>();
		if(db_persons!=null)
		{
			for(DB_Object obj:db_persons)
			{
				if(obj!=null && ((Person)obj).isActive())
				{
					names.add(obj.getName());
				}
			}
		}
		return names;
	}

	public void setPin(String pin, String salt, String name) {
		runSproc(Tables.PROC_SETPIN, false, name, pin, salt); 
		
	}

	public ArrayList<String> getPin(String login) {
		return runSprocList(Tables.PROC_GETPIN, login, "pass","salt");			
	}
	
	public ArrayList<String> getTimeOffStatusOptions()
	{
		return runSprocList(Tables.PROC_GET_TIME_OFF_STATUS_OPTIONS, null, "Status");		
	}
	
	public ArrayList<String> getClockOutReasons()
	{
		return runSprocList(Tables.PROC_GET_CLOCK_OUT_REASONS, null, "Name");		
	}

	public void deleteTimeOffs() {
		runSproc(Tables.PROC_DELETE_TIME_OFFS, true); 
		
	}	
	
	/**
	 * Runs stored procedure where a list of strings is returned as a result
	 * @param dbman Database manager
	 * @param sproc_name Procedure's name
	 * @param col_name Column name(s) to return
	 * @return ArrayList of strings contained in the specified column
	 */
	private ArrayList<String> runSprocList(String sproc_name, String arg, String ...col_names)
	{
		ArrayList<String> retval=new ArrayList<String>();
		ResultSet records =arg!=null?runSproc(sproc_name, true, arg):runSproc(sproc_name, true);
		if(records!=null)
		{
			try {
				while (records.next()) {					
					for(int i=0; i<col_names.length; i++)
					{
						retval.add(records.getString(col_names[i]));		
					}
							
				}
				records.close();
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return retval;
	}

	public int getNextShiftDiff(String name) {
		return runSprocInt(Tables.PROC_GET_NEXT_SHIFT_DIFF, name, "Next_Shift_Diff");
	}

	public ArrayList<String> getSupervisorType(
			String type) {
		return runSprocList(Tables.PROC_GET_SUPERVISOR_TYPE, type, "PERSON_NAME");	
	}

	public ArrayList<Object> getAllTimesOff() {
		ArrayList<Object> retval=new ArrayList<Object>();
		for(DB_Object p : db_persons)
		{
			retval.addAll(((Person)p).getTimeOffs());
		}
		return retval;
	}
	/**
	 * Gets a list of employees that are allowed to work specified positions for specified time span.
	 * @param col_label Position name
	 * @param start Start of the time span.
	 * @param end End of the time span.
	 * @return List of the employee names.
	 */
	public ArrayList<String> getAllowedEmployees(String col_label, String start, String end) {
		
		ArrayList<DB_Object> persons=new ArrayList<DB_Object>();
		for(DB_Object per:db_persons)
		{
			if(per!=null && ((Person)per).isActive() && ((Person)per).isPositionAllowed(col_label))
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
				
			for(DB_Object d: db_duties)
			{
				String person_id=d==null?null:((Duty)d).isPersonOn(postion_id, date);
				if(person_id!=null)
				{
					retval.add(person_id);
				}
			}
			return retval;
		}
		return null;
	}

	/**
	 *  Checks if an employee has already been scheduled.
	 * @param person_name Name of the employee
	 * @param start Shift start date/time
	 * @param end Shift end date/time
	 * @return true is person has a conflict, false otherwise.
	 */
	public boolean checkDutyConflict(DB_Object duty) {		
		
		for(DB_Object d:db_duties)
		{
			if(d!=null && ((Duty)d).personConflict(duty))
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
		double total_hours=0;
		
		for(DB_Object d: db_duties)
		{
			if(d!=null)
			{
				double hours=((Duty)d).getTotalEmpoloyeeHours(employee_name, start, end);
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
	
	private DB_Object getElementByName(ArrayList<DB_Object> elements,
			String name) {
		if (name != null && elements != null) {
			for (DB_Object obj : elements) {
				if (obj.getName().equals(name)) {
					return obj;
				}
			}
		}
		return null;
	}
	
	public DB_Object getElementByName(String name, DBObjectType type) {
		return getElementByName(getElements(type), name);		
	}
	
	public ArrayList<String> getElementsNames(DBObjectType type) {		
		return getNames(getElements(type));
	}
	
	public boolean updateElement(DB_Object old_val, DB_Object new_val, DBObjectType type)
	{
		boolean retval=updateRecord(getElements(type), old_val, new_val);
		if(type==DBObjectType.PERSON)
		{
			updatePersonToPosition(new_val);
			updatePersonToPrivilege(new_val);
		}
		return retval;
	}
	
	public boolean deleteElement(DB_Object obj, DBObjectType type)
	{
		return deleteRecord(getElements(type),obj);
	}
	
	private ArrayList<DB_Object> getElements(DBObjectType type)
	{
		switch (type) {
		case PERSON:
			return db_persons;
		case POSITION:
			return db_positions;
		case DUTY:
			return db_duties;
		case PRIVILEGE:
			return db_privileges;
		default:
			return null;
		}
	}

}
