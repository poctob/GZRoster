package com.gzlabs.gzroster.gui;

import java.util.ArrayList;

public interface IShiftAdder {

	void isa_DateChaged(String new_date);

	ArrayList<String> getTimeSpan();

	void dutyDeleteRequest(String upd_person, String upd_position,
			String upd_start);

	void processDutyData();

	void isa_PositionChanged(String text);

	void isa_EmployeeChanged(String text);

}
