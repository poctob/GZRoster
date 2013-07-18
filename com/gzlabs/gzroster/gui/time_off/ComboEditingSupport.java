package com.gzlabs.gzroster.gui.time_off;

import java.util.ArrayList;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.EditingSupport;

import com.gzlabs.gzroster.gui.GZTableViewer;

public abstract class ComboEditingSupport extends EditingSupport{

	protected final ArrayList<String> m_options;
	protected final GZTableViewer viewer;
	public ComboEditingSupport(GZTableViewer viewer, ArrayList<String> options) {
		super(viewer);
		this.viewer=viewer;
		this.m_options=options;
	}

	@Override
	protected CellEditor getCellEditor(Object element) {
		String[] arr=m_options.toArray(new String[m_options.size()]);
		return new ComboBoxCellEditor(viewer.getTable(), arr);
	}

	@Override
	protected boolean canEdit(Object element) {		
		return true;
	}

	@Override
	protected abstract Object getValue(Object element);

	@Override
	protected abstract void setValue(Object element, Object value);

}
