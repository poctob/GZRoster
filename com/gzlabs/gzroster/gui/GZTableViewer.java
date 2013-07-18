package com.gzlabs.gzroster.gui;

import java.util.ArrayList;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableColumn;

/**
 * Customized table viewer. Provides additional functionality for cell
 * manipulation.
 * 
 * @author apavlune
 * 
 */
public abstract class GZTableViewer extends TableViewer {

	// Column labels
	private ArrayList<TableViewerColumn> l_columns;

	// Currently selected cell.
	private ViewerCell c_selected;
	
	//table comparator
	protected ViewerComparator comparator;

	/**
	 * Default constructor. Initializes member variables.
	 * 
	 * @param parent
	 *            Composite parent of this control.
	 * @param dutyupdater
	 *            Data updater interface.
	 */
	public GZTableViewer(Composite parent) {
		super(parent, SWT.FULL_SELECTION);
		this.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));
		getTable().setHeaderVisible(true);
		getTable().setLinesVisible(true);
		setContentProvider(new ArrayContentProvider());
		l_columns = new ArrayList<TableViewerColumn>();
		setC_selected(null);
	}
	
	/**
	 * Initializes column properties.
	 * 
	 * @param columns
	 *            Column label strings.
	 */
	public void initiateColumns(ArrayList<String> columns, int width) {
		if (columns != null && l_columns!=null) {
			for (String s : columns) {
				TableViewerColumn col = new TableViewerColumn(this, SWT.NONE);
				TableColumn tc = col.getColumn();
			
				if(tc!=null)
				{
					tc.setWidth(width);
					tc.setText(s);
					l_columns.add(col);
				}
			}
		}
	}

	/**
	 * Sets a label provider for a column.
	 * 
	 * @param col
	 *            Column id.
	 * @param provider
	 *            Provider to set.
	 */
	public void setLabelProvider(int col, ColumnLabelProvider provider) {
		if(l_columns!=null && provider!=null)
		{
			l_columns.get(col).setLabelProvider(provider);
		}
	}

	/**
	 * Initiates table data.
	 * 
	 * @param data
	 *            Data that will be placed in the table.
	 */
	public void initiateData(ArrayList<Object> data) {
		setInput(data);
	}

	/*************************************************************************/
	/** Accessors */
	public ViewerCell getC_selected() {
		return c_selected;
	}

	public void setC_selected(ViewerCell c_selected) {
		this.c_selected = c_selected;
	}
	/*************************************************************************/

	public TableViewerColumn createTableViewerColumn(String title, 
			int bound, 
			final int colNumber, 
			ColumnLabelProvider labelProvider,
			EditingSupport editing_support) {
	    final TableViewerColumn viewerColumn = new TableViewerColumn(this,
	        SWT.NONE);
	    final TableColumn column = viewerColumn.getColumn();
	    column.setText(title);
	    column.setWidth(bound);
	    column.setResizable(true);
	    column.setMoveable(true);
	    column.addSelectionListener(getSelectionAdapter(column, colNumber));	    
	    viewerColumn.setLabelProvider(labelProvider);	    
	    viewerColumn.setEditingSupport(editing_support);
	    return viewerColumn;
	  }
	
	 protected abstract SelectionAdapter getSelectionAdapter(final TableColumn column,
		      final int index);
}
