package com.gzlabs.gzroster.data;

/**
 * Provides SQL tables templates
 * @author apavlune
 *
 */

public class Tables
{
	public static final int PLACE_ID_INDEX = 0;
	public static final int PLACE_NAME_INDEX = 1;
	public static final int PLACE_NOTE_INDEX = 2;
	public static final int PLACE_MAX_COLS = 3;
	
	public static final int PERSON_MAX_COLS=9;
	public static final int PERSON_ID_INDEX = 0;
	public static final int PERSON_NAME_INDEX = 1;
	public static final int PERSON_ADDRESS_INDEX = 2;
	public static final int PERSON_HPHONE_INDEX = 3;
	public static final int PERSON_MPHONE_INDEX = 4;
	public static final int PERSON_NOTE_INDEX = 5;
	public static final int PERSON_ACTIVE_INDEX = 6;
	public static final int PERSON_EMAIL_INDEX = 7;
	public static final int PLACE_UUID_INDEX = 8;
	
	public static final int DUTIES_MAX_COLS=5;
	public static final int DUTIES_START_INDEX=0;
	public static final int DUTIES_PLACE_ID_INDEX=1;
	public static final int DUTIES_PERSON_ID_INDEX=2;
	public static final int DUTIES_END_INDEX=3;
	public static final int DUTIES_KEY_INDEX=4;
	
}
