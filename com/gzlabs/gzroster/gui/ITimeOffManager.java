package com.gzlabs.gzroster.gui;

/**
 * Handles time off management
 * @author apavlune
 *
 */
public interface ITimeOffManager {

	/**
	 * Processes new time off
	 * @param start Time off start
	 * @param end Time off end
	 */
	void newTimeOffRequest(String start, String end);

	/**
	 * Handles delete time off request
	 * @param start Time off start
	 * @param end Time off end
	 * @return Success or not
	 */
	boolean deleteTimeOffRequest(String start, String end);

}
