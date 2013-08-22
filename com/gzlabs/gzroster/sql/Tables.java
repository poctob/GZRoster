package com.gzlabs.gzroster.sql;

/**
 * Provides SQL tables templates
 * @author apavlune
 *
 */
public class Tables
{
	//TmeOff table
	public static final String TIME_OFF_TABLE_NAME="VIEW_TIME_OFF";
	
	//Person to place table
	public static final String PERSON_TO_PLACE_TABLE_NAME="PERSON_TO_PLACE";
	
	//Positions table
	public static final String POSITION_TABLE_NAME="PLACE";
	
	//Person table
	public static final String PERSON_TABLE_NAME="PERSON";
	
	//Duty table
	public static final String DUTY_TABLE_NAME="DUTIES";
	
	public static final String [] DB_TYPES={"MySQL", "FireBird"};
	public static final String [] DB_DRIVERS={"com.mysql.jdbc.Driver",
		"org.firebirdsql.jdbc.FBDriver"};
	
	public static final String FB_DB_FLAG = "FireBird";
	
	public static final String PROC_TODAY_SCHEDULE="getTodaySchedule";
	public static final String PROC_CLOCKIN="ClockIn";
	public static final String PROC_CLOCKOUT="ClockOut";
	public static final String PROC_ISCLOCKEDIN="isClockedIn";
	public static final String PROC_TOTALSCHEDULEDHOURS="getTotalScheduledHours";
	public static final String PROC_WEEKLYWORKEDDHOURS="getWeeklyWorkedHours";
	public static final String PROC_REQUESTTIMEOFF="RequestTimeOff";
	public static final String PROC_SETPIN="setPin";
	public static final String PROC_GETPIN="getPin";
	public static final String PROC_UPDATEPERSON="updatePerson";
	public static final String PROC_GET_TIME_OFF_STATUS_OPTIONS = "getTimeOffStatusOptions";
	public static final String PROC_DELETE_TIME_OFFS = "deleteTimeOffs";
	public static final String PROC_GET_CLOCK_OUT_REASONS = "getClockOutReasons";
	public static final String PROC_GET_NEXT_SHIFT_DIFF = "getNextClockInTime";
	public static final String PROC_GET_SUPERVISOR_TYPE = "getSupervisorType";
	public static final String PROC_GET_PRIVILEGES = "getPrivileges";
	public static final String PROC_GET_PERSON_PRIVILEGES = "getPersonsPrivileges";
	public static final String PROC_ADD_PERSON_PRIVILEGE = "addPersonPrivileges";
	public static final String PROC_DELETE_PERSON_PRIVILEGE = "deletePersonPrivileges";
	public static final String PROC_GET_PERSON_POSITIONS = "getPersonsPositions";
	public static final String PROC_GET_POSITION = "getPosition";
	public static final String PROC_GET_DUTY = "getDuty";
	public static final String PROC_GET_PERSON = "getPerson";
	public static final String PROC_GET_TIME_OFF = "getTimeOff"
			;
	public static final String PROC_INSERT_DUTY = "insertDuty";
	public static final String PROC_UPDATE_DUTY = "updateDuty";
	public static final String PROC_DELETE_DUTY = "deleteDuty";

	public static final String PROC_UPDATE_PERSON = "updatePerson";

	public static final String PROC_DELETE_PERSON = "deletePerson";

	public static final String PROC_INSERT_PERSON = "insertPerson";

	public static final String PROC_DELETE_PERSON_POSITION = "deletePersonPositions";

	public static final String PROC_ADD_PERSON_POSITION = "addPersonPositions";

	public static final String PROC_INSERT_POSITION = "insertPosition";

	public static final String PROC_UPDATE_POSITION = "updatePosition";

	public static final String PROC_DELETE_POSITION = "deletePosition";

	public static final String PROC_INSERT_PRIVILEGE = "insertPrivilege";

	public static final String PROC_UPDATE_PRIVILEGE = "updatePrivilege";

	public static final String PROC_DELETE_PRIVILEGE = "deletePrivilege";
	
}
