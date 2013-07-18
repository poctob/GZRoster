package com.gzlabs.gzroster.gui.time_off;

import java.util.ArrayList;

/**
 * Handles time off management
 * @author apavlune
 *
 */
public interface ITimeOffManager {

	ArrayList<String> getTimeOffStatusOptions();
	
	ArrayList<String> getNameOptions();

	ArrayList<Object> getTimeSpan();

	void updateTimesOff(ArrayList<Object> timeOff);

}
