package com.gzlabs.gzroster.gui;

import java.util.ArrayList;
import java.util.Properties;

public interface IDetailsManager {

	ArrayList<String> getPositions();

	String getCellLabelString(String element, String column_label);
	
	void refreshData();
	
	void updateProperties(Properties prop);

}
