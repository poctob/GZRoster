package com.gzlabs.gzroster.gui;

public interface ITimeOffManager {

	void newTimeOffRequest(String start, String end);

	boolean deleteTimeOffRequest(String start, String end);

}
