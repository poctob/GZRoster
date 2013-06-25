package com.gzlabs.gzroster.gui;

import java.util.ArrayList;
import java.util.Properties;

/**
 * Interface to manage Detail widget
 * @author apavlune
 *
 */
public interface IDetailsManager {

	/**
	 * Fetches a list of positions
	 * @return String list of positions
	 */
	ArrayList<String> getPositions();

	/**
	 * Fetches a label for table cell
	 * @param element Row label
	 * @param column_label Column label
	 * @return String containing cell label
	 */
	String getCellLabelString(String element, String column_label);
	
	/**
	 * Refreshes all data in the table
	 */
	void refreshData();
	
	/**
	 * Updates properties object
	 * @param prop properties object to update
	 */
	void updateProperties(Properties prop);

}
