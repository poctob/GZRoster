package com.gzlabs.gzroster.gui.time_off;

import java.util.ArrayList;

import com.gzlabs.gzroster.data.TimeOff;
import com.gzlabs.gzroster.gui.GZTableViewer;

public class PersonEditingSupport extends ComboEditingSupport{

	public PersonEditingSupport(GZTableViewer viewer, ArrayList<String> options) {
		super(viewer,options);
	}

	@Override
	protected Object getValue(Object element) {
		String status=((TimeOff)element).getName();
		
		return m_options.indexOf(status);
	}

	@Override
	protected void setValue(Object element, Object value) {
		((TimeOff)element).setName(m_options.get((int) value));		
		viewer.update(element, null);
	}

}
