package com.gzlabs.gzroster.gui.time_off;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

import com.xpresstek.gzrosterdata.TimeOff;
import com.gzlabs.gzroster.gui.GZTableViewer;

public class StartDateEditingSupport extends TextEditingSupport{

	public StartDateEditingSupport(GZTableViewer viewer) {
		super(viewer);
	}

	@Override
	protected Object getValue(Object element) {
		return ((TimeOff)element).getStartStr();
	}

	@Override
	protected void setValue(Object element, Object value) {
		if(((TimeOff)element).setStart(String.valueOf(value)))
		{
			viewer.update(element, null);
		}
		else
		{
			MessageDialog.openError(new Shell(), "Format Error!", "Incorrect Date/Time Format!");
		}
	}

}
