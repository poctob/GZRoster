package com.gzlabs.gzroster.gui;

import java.util.ArrayList;

/**
 * Handles shifts
 * @author apavlune
 *
 */
public interface IShiftAdder {

	/**
	 * Date has been changed
	 * @param new_date New date
	 */
	void isa_DateChaged(String new_date);

	/**
	 * Fetches time spans for the shifts
	 * @return List of times
	 */
	ArrayList<Object> getTimeSpan();

	/**
	 * Handles duty delete operation
	 * @param upd_person Person
	 * @param upd_position Position
	 * @param upd_start Duty start time
	 */
	void dutyDeleteRequest(String upd_person, String upd_position,
			String upd_start, boolean update_request);	

	/**
	 * Handles duty update requests
	 */
	void processDutyData();
	
	/**
	 * Refreshes list of available employees
	 */
	void isa_updateEmployeeList();

}
