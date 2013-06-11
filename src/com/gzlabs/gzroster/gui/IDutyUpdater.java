package com.gzlabs.gzroster.gui;

/**
 * Provides an interface for performing updates on the shifts in the table.
 * @author apavlune
 *
 */
public interface IDutyUpdater {
	
	public void dutyNewRequest(String col_label, String row_label);
	
	public void dutyUpdateRequest(String label, String col_label, String row_label);
	
	public void dutyDeleteRequest(String label, String col_label, String row_label);

}
