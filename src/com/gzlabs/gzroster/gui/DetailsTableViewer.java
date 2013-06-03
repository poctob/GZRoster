package com.gzlabs.gzroster.gui;

import java.util.ArrayList;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.TableColumn;

import com.gzlabs.gzroster.data.DataManager;

public class DetailsTableViewer extends TableViewer {

	private ArrayList<TableViewerColumn> l_columns;
	private ViewerCell c_selected;
	final private IDutyUpdater dutyupdater;

	final private String DIALOG_TITLE = "Confirm Delete";
	final private String DIALOG_MESSAGE = "Are you sure that you want to delete this item?";

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

	public void initiateColumns(ArrayList<String> columns) {
		for (String s : columns) {
			TableViewerColumn col = new TableViewerColumn(this, SWT.NONE);
			TableColumn tc = col.getColumn();
			tc.setWidth(100);
			tc.setText(s);
			l_columns.add(col);
		}
	}

	public void setLabelProvider(int col, ColumnLabelProvider provider) {
		l_columns.get(col).setLabelProvider(provider);
	}

	public void setEditingProvider(int col, EditingSupport provider) {
		l_columns.get(col).setEditingSupport(provider);
	}

	public void initiateData(ArrayList<String> data) {
		setInput(data);
	}

	@Override
	protected void hookEditingSupport(Control control) {

		final Action edit = new Action("Edit") {
			public void run() {
				ViewerCell cell = getC_selected();
				if(dutyupdater!=null && cell!=null)
				{
					dutyupdater.dutyUpdateRequest(cell.getText(), cell.getColumnIndex(), (String)cell.getElement());
					refresh();
				}
			}
		};

		final Action delete = new Action("Delete") {
			public void run() {
				ViewerCell cell = getC_selected();
				if (MessageDialog.openConfirm(null, DIALOG_TITLE,
						DIALOG_MESSAGE)) {
					if(dutyupdater!=null && cell!=null)
					{
						dutyupdater.dutyDeleteRequest(cell.getText(), cell.getColumnIndex(), (String)cell.getElement());
						refresh();
					}
				}
			}
		};

		final MenuManager menumgr = new MenuManager();
		menumgr.add(edit);
		menumgr.add(delete);
		control.setMenu(menumgr.createContextMenu(control));
		control.addMouseListener(new MouseAdapter() {
			public void mouseDown(MouseEvent e) {
				if (e.button == 3) {
					setC_selected(getCell(new Point(e.x, e.y)));
				}
			}
		});
	}

	public ViewerCell getC_selected() {
		return c_selected;
	}

	public void setC_selected(ViewerCell c_selected) {
		this.c_selected = c_selected;
	}

}
