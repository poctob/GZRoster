package com.gzlabs.gzroster.gui.time_off;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TextCellEditor;

import com.gzlabs.gzroster.gui.GZTableViewer;

public abstract class TextEditingSupport extends EditingSupport{

	protected final GZTableViewer viewer;
	public TextEditingSupport(GZTableViewer viewer) {
		super(viewer);
		this.viewer=viewer;
	}

	@Override
	protected CellEditor getCellEditor(Object element) {		
		return new TextCellEditor(viewer.getTable());
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
