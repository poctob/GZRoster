package com.gzlabs.gzroster.gui;

/**
 * Provides an interface for performing updates on the shifts in the table.
 * @author apavlune
 *
 */
public interface IDutyUpdater {
	
	public void dutyNewRequest(int col_id, String row_label);
	
	public void dutyUpdateRequest(String label, int col_id, String row_label);
	
	public void dutyDeleteRequest(String label, int col_id, String row_label);

}
