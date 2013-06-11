package com.gzlabs.gzroster.gui;

public interface IPositionsManager {

	void setPositionDetails(String string);

	void processPositionData();

	void populateData();

	void deletePosition(String[] selection);

}
