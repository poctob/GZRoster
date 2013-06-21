package com.gzlabs.gzroster.gui;

import java.util.ArrayList;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.swt.widgets.Composite;

/**
 * Widget displaying details table
 * 
 * @author apavlune
 * 
 */
public class DetailsWidget extends Composite {

	private IDetailsManager idm;
	/************************************************************/
	// Details Tab
	private DetailsTableViewer tv;

	/************************************************************/

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public DetailsWidget(Composite parent, int style, IDutyUpdater dutyupdater,
			IDetailsManager dm) {
		super(parent, style);
		if (dm == null || dutyupdater == null) {
			return;
		}
		idm = dm;
		tv = new DetailsTableViewer(this, dutyupdater);
		final ArrayList<String> cols = idm.getPositions();
		if (cols == null) {
			return;
		}

		cols.add(0, "Time");
		tv.initiateColumns(cols);
		tv.setLabelProvider(0, new ColumnLabelProvider() {
			public String getText(Object element) {

				return (String) element;
			}
		});

		for (int i = 1; i < cols.size(); i++) {
			final int col_id = i;

			tv.setLabelProvider(i, new ColumnLabelProvider() {
				public String getText(Object element) {
					String retstr = null;
					String column_label = tv.getTable().getColumn(col_id)
							.getText();
					retstr = idm.getCellLabelString((String) element,
							column_label);
					if (retstr != null && retstr.length() > 1) {
						retstr = retstr.substring(0, retstr.length() - 1);
						retstr = retstr.substring(1, retstr.length());
					}

					return retstr;
				}
			});
		}

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	/**
	 * Public wrapper for data initialization.
	 * 
	 * @param data
	 *            Data to pass to the table viewer
	 */
	public void initiateData(ArrayList<String> data) {
		if (tv != null) {
			tv.initiateData(data);
		}

	}

}
