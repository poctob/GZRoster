package com.gzlabs.gzroster.gui;

import java.util.EventObject;

import com.gzlabs.gzroster.sql.DBObjectType;

public class ElementsChangedEvent extends EventObject {

	private DBObjectType m_type;
	/**
	 * 
	 */
	private static final long serialVersionUID = 7766984642129822796L;

	public ElementsChangedEvent(Object source, DBObjectType type) {
		super(source);
		m_type=type;
	}
	
	public DBObjectType getType()
	{
		return m_type;
	}

}
