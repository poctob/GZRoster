package com.gzlabs.gzroster.gui;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.TableColumn;

/**
 * Customized table viewer. Provides additional functionality for cell
 * manipulation.
 * 
 * @author apavlune
 * 
 */
public class DetailsTableViewer extends GZTableViewer {
	
	// Data updater interface.
	final private IDutyUpdater dutyupdater;

	// Some constants
	final private String DIALOG_TITLE = "Confirm Delete";
	final private String DIALOG_MESSAGE = "Are you sure that you want to delete this item?";

	/**
	 * Default constructor. Initializes member variables.
	 * 
	 * @param parent
	 *            Composite parent of this control.
	 * @param dutyupdater
	 *            Data updater interface.
	 */
	public DetailsTableViewer(Composite parent, IDutyUpdater dutyupdater) {
		super(parent);
		this.dutyupdater = dutyupdater;
	}


	/**
	 * Add a right click context menu to a single cell.
	 */	
	@Override
	protected void hookEditingSupport(Control control) {

		/***
		 * Edit action definition.
		 */
		final Action _new = new Action("New") {
			public void run() {
				ViewerCell cell = getC_selected();
				if (dutyupdater != null && cell != null) {
					String col_label = getTable().getColumn(
							cell.getColumnIndex()).getText();
					dutyupdater.dutyNewRequest(col_label,
							(String) cell.getElement());
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
				if (dutyupdater != null && cell != null) {
					String col_label = getTable().getColumn(
							cell.getColumnIndex()).getText();
					dutyupdater.dutyUpdateRequest(cell.getText(), col_label,
							(String) cell.getElement());
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
					if (dutyupdater != null && cell != null) {
						String col_label = getTable().getColumn(
								cell.getColumnIndex()).getText();
						dutyupdater.dutyDeleteRequest(cell.getText(),
								col_label, (String) cell.getElement(), false);
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
		 * Here we intercept mouse click and capture a cell that is being
		 * clicked.
		 */
		control.addMouseListener(new MouseAdapter() {
			public void mouseDown(MouseEvent e) {
				if (e.button == 3) {
					setC_selected(getCell(new Point(e.x, e.y)));
					menumgr.removeAll();

					// Our first column contains time labels so we ignore it.
					if (getC_selected() != null && getC_selected().getColumnIndex() > 0) {
						// If cell has a name already, then we allow Edit/Delete
						// ops.
						if (getC_selected().getText().length() > 1) {
							menumgr.add(edit);
							menumgr.add(delete);
						}
						// Otherwise it's only new operation that is allowed.
						else {
							menumgr.add(_new);
						}
					}

				}
			}
		});
	}


	@Override
	protected SelectionAdapter getSelectionAdapter(TableColumn column, int index) {
		// TODO Auto-generated method stub
		return null;
	}


}
