package com.gzlabs.gzroster.gui;

import java.util.ArrayList;

public interface IShiftAdder {

	void isa_DateChaged(String new_date);

	ArrayList<String> getTimeSpan();

	void dutyDeleteRequest(String upd_person, String upd_position,
			String upd_start);

	void processDutyData();

}
