package com.gzlabs.gzroster.gui;

import java.util.ArrayList;

public interface IDetailsManager {

	ArrayList<String> getPositions();

	String getCellLabelString(String element, String column_label);

}
