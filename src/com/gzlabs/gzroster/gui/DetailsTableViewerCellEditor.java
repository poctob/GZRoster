package com.gzlabs.gzroster.gui;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.EditingSupport;

import com.gzlabs.gzroster.data.DataManager;


public class DetailsTableViewerCellEditor extends EditingSupport{

	private final DetailsTableViewer viewer;
	private String label;
	private final String column_label;
	final private DataManager dman;
	final private IDateProvider dateprovider;
	
	
	public DetailsTableViewerCellEditor(DetailsTableViewer viewer) {
		super(viewer);
		this.viewer=viewer;
		dman=null;
		column_label=null;
		dateprovider=null;
	}
	
	public DetailsTableViewerCellEditor(DetailsTableViewer viewer, DataManager dmanager, String col_id, IDateProvider dateprovider) {
		super(viewer);
		this.viewer=viewer;
		dman=dmanager;
		column_label=col_id;
		this.dateprovider=dateprovider;
	}

	@Override
	protected CellEditor getCellEditor(Object element) {			
		return null;
	}

	@Override
	protected boolean canEdit(Object element) {		
		return true;
	}

	@Override
	protected Object getValue(Object element) {
		
		label=null;
		if(dman!=null && column_label!=null && dateprovider!=null)
			
		{
			label= dman.getCellLabelString((String)element, column_label, 
					dateprovider.getDateString());
		}
		return label;
	}

	@Override
	protected void setValue(Object element, Object value) {
		
		if(dman!=null)
		{
			dman.deleteDuty(label, column_label, dateprovider.getDateString()+" "+(String)element+":00.0");
			viewer.refresh();			
		}

	}
}
