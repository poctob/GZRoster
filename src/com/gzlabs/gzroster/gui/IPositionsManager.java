package com.gzlabs.gzroster.gui;

/**
 * Handles position changes
 * @author apavlune
 *
 */
public interface IPositionsManager {

	/**
	 * Sets position details
	 * @param string Name of the position to 
	 */
	void setPositionDetails(String string);

	/**
	 * Processes changes in the position
	 */
	void processPositionData();

	/**
	 * Updates data
	 */
	void populateData();

	/**
	 * Deletes the position
	 * @param selection List of the position names to delete
	 */
	void deletePosition(String[] selection);

}
