package com.gzlabs.gzroster.gui;

import java.util.ArrayList;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.TableColumn;

/**
 * Customized table viewer.  Provides additional functionality for cell manipulation.
 * @author apavlune
 *
 */
public class DetailsTableViewer extends TableViewer {

	//Column labels
	private ArrayList<TableViewerColumn> l_columns;
	
	//Currently selected cell.
	private ViewerCell c_selected;
	
	//Data updater interface.
	final private IDutyUpdater dutyupdater;

	//Some constants
	final private String DIALOG_TITLE = "Confirm Delete";
	final private String DIALOG_MESSAGE = "Are you sure that you want to delete this item?";

	/**
	 * Default constructor.  Initializes member variables.
	 * @param parent Composite parent of this control.
	 * @param dutyupdater Data updater interface.
	 */
	public DetailsTableViewer(Composite parent, IDutyUpdater dutyupdater) {
		super(parent, SWT.FULL_SELECTION);
		this.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));
		getTable().setHeaderVisible(true);
		getTable().setLinesVisible(true);
		setContentProvider(new ArrayContentProvider());
		l_columns = new ArrayList<TableViewerColumn>();
		setC_selected(null);
		this.dutyupdater = dutyupdater;
	}

	/**
	 * Initializes column properties.
	 * @param columns Column label strings.
	 */
	public void initiateColumns(ArrayList<String> columns) {
		for (String s : columns) {
			TableViewerColumn col = new TableViewerColumn(this, SWT.NONE);
			TableColumn tc = col.getColumn();
			tc.setWidth(60);
			tc.setText(s);
			l_columns.add(col);
		}
	}
	
	public void removeAllColumns()
	{
	
		for(final TableColumn col:getTable().getColumns())
		{
			col.dispose();
		}
		for(int i=0; i<l_columns.size(); i++)
		{
			l_columns.remove(i);
		}
	}

	/**
	 * Sets a label provider for a column.
	 * @param col Column id.
	 * @param provider Provider to set.
	 */
	public void setLabelProvider(int col, ColumnLabelProvider provider) {
		l_columns.get(col).setLabelProvider(provider);
	}
	
	/**
	 * Initiates table data.
	 * @param data Data that will be placed in the table.
	 */
	public void initiateData(ArrayList<String> data) {
		setInput(data);
	}

	/**
	 * Add a right click context menu to a single cell.
	 */
	@Override
	protected void hookEditingSupport(Control control) {
		
		/*** Edit action definition.
		 */
		final Action _new = new Action("New") {
			public void run() {
				ViewerCell cell = getC_selected();
				if(dutyupdater!=null && cell!=null)
				{
					String col_label=getTable().getColumn(cell.getColumnIndex()).getText();
					dutyupdater.dutyNewRequest(col_label, (String)cell.getElement());
					refresh();
				}
			}
		};
		
		/**
		 * Edit action definition.
		 */
		final Action edit = new Action("Edit") {
			public void run() {
				ViewerCell cell = getC_selected();
				if(dutyupdater!=null && cell!=null)
				{
					String col_label=getTable().getColumn(cell.getColumnIndex()).getText();
					dutyupdater.dutyUpdateRequest(cell.getText(), col_label, (String)cell.getElement());
					refresh();
				}
			}
		};

		/**
		 * Delete action definition.
		 */
		final Action delete = new Action("Delete") {
			public void run() {
				ViewerCell cell = getC_selected();
				if (MessageDialog.openConfirm(null, DIALOG_TITLE,
						DIALOG_MESSAGE)) {
					if(dutyupdater!=null && cell!=null)
					{
						String col_label=getTable().getColumn(cell.getColumnIndex()).getText();
						dutyupdater.dutyDeleteRequest(cell.getText(), col_label, (String)cell.getElement());
						refresh();
					}
				}
			}
		};

		/**
		 * Context menu initialization.
		 */
		final MenuManager menumgr = new MenuManager();
		
		control.setMenu(menumgr.createContextMenu(control));
		
		/**
		 * Here we intercept mouse click and capture a cell that is being clicked.
		 */
		control.addMouseListener(new MouseAdapter() {
			public void mouseDown(MouseEvent e) {
				if (e.button == 3) {
					setC_selected(getCell(new Point(e.x, e.y)));
					menumgr.removeAll();
					
					//Our first column contains time labels so we ignore it.
					if(c_selected!=null && c_selected.getColumnIndex()>0)
					{
						//If cell has a name already, then we allow Edit/Delete ops.
						if(c_selected.getText().length()>1)
						{
							menumgr.add(edit);
							menumgr.add(delete);
						}
						//Otherwise it's only new operation that is allowed.
						else
						{
							menumgr.add(_new);
						}
					}
					
				}
			}
		});
	}

	/*************************************************************************/
	/**Accessors*/
	public ViewerCell getC_selected() {
		return c_selected;
	}

	public void setC_selected(ViewerCell c_selected) {
		this.c_selected = c_selected;
	}
	/*************************************************************************/

}
