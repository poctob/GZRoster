package com.gzlabs.gzroster.gui;

import java.util.ArrayList;

import com.gzlabs.gzroster.sql.DBObjectType;

public interface IItemsManager {

	ArrayList<String> getData(DBObjectType type);

	void deleteItem(Object item, DBObjectType type);

	Object getItem(String selection, DBObjectType type);
	
	void updateObject(Object oldObj, Object newObj, DBObjectType type);
	
	void insertObject(Object newObj, DBObjectType type);

}
