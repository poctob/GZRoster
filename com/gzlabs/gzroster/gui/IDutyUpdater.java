package com.gzlabs.gzroster.gui;

/**
 * Provides an interface for performing updates on the shifts in the table.
 * 
 * @author apavlune
 * 
 */
public interface IDutyUpdater {

	/**
	 * Handles new duty request
	 * 
	 * @param col_label
	 *            Position
	 * @param row_label
	 *            Time
	 */
	public void dutyNewRequest(String col_label, String row_label);

	/**
	 * Handles duty update request
	 * 
	 * @param label
	 *            Person
	 * @param col_label
	 *            Position
	 * @param row_label
	 *            Time
	 */
	public void dutyUpdateRequest(String label, String col_label,
			String row_label);

	/**
	 * Handles duty delete request
	 * 
	 * @param label
	 *            Person
	 * @param col_label
	 *            Position
	 * @param row_label
	 *            Time
	 */
	public void dutyDeleteRequest(String label, String col_label,
			String row_label, boolean update_request);

}
