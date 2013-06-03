package com.gzlabs.gzroster.gui;

public interface IDutyUpdater {
	
	public void dutyUpdateRequest(String label, int col_id, String row_label);
	
	public void dutyDeleteRequest(String label, int col_id, String row_label);

}
