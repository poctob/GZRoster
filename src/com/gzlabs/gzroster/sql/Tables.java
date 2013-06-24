package com.gzlabs.gzroster.sql;

/**
 * Provides SQL tables templates
 * @author apavlune
 *
 */
public class Tables
{
	//Positions table
	public static final int PLACE_ID_INDEX = 0;
	public static final int PLACE_NAME_INDEX = 1;
	public static final int PLACE_NOTE_INDEX = 2;
	public static final int PLACE_MAX_COLS = 3;
	
	//Person table
	public static final int PERSON_MAX_COLS=9;
	public static final int PERSON_ID_INDEX = 0;
	public static final int PERSON_NAME_INDEX = 1;
	public static final int PERSON_ADDRESS_INDEX = 2;
	public static final int PERSON_HPHONE_INDEX = 3;
	public static final int PERSON_MPHONE_INDEX = 4;
	public static final int PERSON_NOTE_INDEX = 5;
	public static final int PERSON_ACTIVE_INDEX = 6;
	public static final int PERSON_EMAIL_INDEX = 7;
	
	//Duties table
	public static final int DUTIES_MAX_COLS=5;
	public static final int DUTIES_START_INDEX=0;
	public static final int DUTIES_PLACE_ID_INDEX=1;
	public static final int DUTIES_PERSON_ID_INDEX=2;
	public static final int DUTIES_END_INDEX=3;
	public static final int DUTIES_KEY_INDEX=4;
	
	//TmeOff table
	public static final String TIME_OFF_TABLE_NAME="PERSON_NA_AVAIL_HOURS";
	
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
}
