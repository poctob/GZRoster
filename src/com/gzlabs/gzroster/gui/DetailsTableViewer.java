package com.gzlabs.gzroster.gui;

import java.util.ArrayList;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableColumn;

public class DetailsTableViewer extends TableViewer{

	private ArrayList<TableViewerColumn> l_columns;
	
	public DetailsTableViewer(Composite parent) {
		super(parent);
		this.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));
		getTable().setHeaderVisible(true);
		getTable().setLinesVisible(true);
		setContentProvider(new ArrayContentProvider());
		l_columns=new ArrayList<TableViewerColumn>();
	}

	public void initiateColumns(ArrayList<String> columns)
	{
		 for(String s:columns)
		 {
			 TableViewerColumn col = new TableViewerColumn(this, SWT.NONE);
			 TableColumn tc = col.getColumn();
			 tc.setWidth(100);
			 tc.setText(s);
			 l_columns.add(col);
		 }		
	}
	
	public void setLabelProvider(int col, ColumnLabelProvider provider)
	{
		l_columns.get(col).setLabelProvider(provider);
	}
	
	public void initiateData(ArrayList<String> data)
	{		
		setInput(data);
	}

}
