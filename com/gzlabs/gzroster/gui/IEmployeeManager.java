package com.gzlabs.gzroster.gui;

/**
 * Manages employee widget
 * @author apavlune
 *
 */
public interface IEmployeeManager {

	/**
	 * Updates employee details
	 * @param string Details to set data to
	 */
	void setEmployeeDetails(String string);

	/**
	 * Handles employee data changes
	 */
	void processEmployeeData();

	/**
	 * Deletes employee
	 * @param selection Array of the employee names
	 */
	void deleteEmployee(String[] selection);

	/**
	 * Refreshes data
	 */
	void populateData();

	void setPin(String pin, String name);

}
