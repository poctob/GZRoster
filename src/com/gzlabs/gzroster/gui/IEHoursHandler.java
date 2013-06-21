package com.gzlabs.gzroster.gui;

/**
 * Interface to update total hours data
 * 
 * @author apavlune
 * 
 */
public interface IEHoursHandler {

	/**
	 * Handles changes in the date/time selectors
	 * 
	 * @param dateStringFromWidget
	 *            Start date
	 * @param dateStringFromWidget2
	 *            End date
	 */
	void rangeChanged(String dateStringFromWidget, String dateStringFromWidget2);

}
