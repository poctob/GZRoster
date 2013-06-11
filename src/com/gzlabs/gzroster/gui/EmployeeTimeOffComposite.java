package com.gzlabs.gzroster.gui;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Button;

public class EmployeeTimeOffComposite extends Composite {

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public EmployeeTimeOffComposite(Composite parent, int style) {
		super(parent, style);
		
		List list = new List(this, SWT.BORDER);
		list.setBounds(110, 80, 280, 210);
		
		DateTime dateTime = new DateTime(this, SWT.BORDER | SWT.DROP_DOWN);
		dateTime.setBounds(66, 10, 114, 29);
		
		DateTime dateTime_1 = new DateTime(this, SWT.BORDER | SWT.TIME);
		dateTime_1.setBounds(194, 10, 116, 29);
		
		Label lblFrom = new Label(this, SWT.NONE);
		lblFrom.setBounds(10, 10, 51, 18);
		lblFrom.setText("From:");
		
		Button btnAllDay = new Button(this, SWT.CHECK);
		btnAllDay.setBounds(316, 17, 80, 22);
		btnAllDay.setText("All Day");
		
		Label lblTo = new Label(this, SWT.NONE);
		lblTo.setText("To:");
		lblTo.setBounds(10, 45, 51, 18);
		
		DateTime dateTime_2 = new DateTime(this, SWT.BORDER | SWT.DROP_DOWN);
		dateTime_2.setBounds(66, 45, 114, 29);
		
		DateTime dateTime_3 = new DateTime(this, SWT.BORDER | SWT.TIME);
		dateTime_3.setBounds(194, 45, 116, 29);
		
		Button button = new Button(this, SWT.CHECK);
		button.setText("All Day");
		button.setBounds(316, 52, 80, 22);
		
		Button btnAdd = new Button(this, SWT.NONE);
		btnAdd.setBounds(10, 88, 88, 30);
		btnAdd.setText("Add");
		
		Button btnEdit = new Button(this, SWT.NONE);
		btnEdit.setBounds(10, 124, 88, 30);
		btnEdit.setText("Edit");
		
		Button btnDelete = new Button(this, SWT.NONE);
		btnDelete.setBounds(10, 161, 88, 30);
		btnDelete.setText("Delete");

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
