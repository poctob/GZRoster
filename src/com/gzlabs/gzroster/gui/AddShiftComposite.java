package com.gzlabs.gzroster.gui;

import java.util.ArrayList;
import java.util.Collections;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Button;

import com.gzlabs.gzroster.data.DateUtils;

/**
 * Widget for adding new shift.
 * 
 * @author apavlune
 * 
 */
public class AddShiftComposite extends Composite {

	/***************************************************************************/
	// Widgets
	private DateTime datePicker;
	private Combo employeePicker;
	private Combo startPicker;
	private Combo endPicker;
	private Combo positionPicker;
	private Button addButton;
	private Button cancelButton;

	// Update place holders
	private String upd_person;
	private String upd_position;
	private String upd_start;
	/***************************************************************************/

	// Callback Interface
	final private IShiftAdder shiftadder;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public AddShiftComposite(Composite parent, int style, IShiftAdder sa) {
		super(parent, style);
		this.shiftadder = sa;

		datePicker = new DateTime(this, SWT.BORDER | SWT.DROP_DOWN);
		datePicker.setBounds(10, 10, 103, 30);

		/**
		 * Triggers a callback method when date has been changed.
		 */
		datePicker.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (shiftadder != null) {
					shiftadder.isa_DateChaged(getSelectedDate());
				}
			}
		});

		employeePicker = new Combo(this, SWT.NONE);
		employeePicker.setBounds(10, 250, 150, 30);
		employeePicker.setEnabled(false);

		startPicker = new Combo(this, SWT.NONE);
		startPicker.setBounds(10, 130, 103, 30);

		/**
		 * When start date is picked, the end date is correlated, callback is
		 * triggered to update a list of available employees and employee picker
		 * gets enabled.
		 */
		startPicker.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (shiftadder != null && employeePicker != null) {
					correlateEndTimeCombo();
					shiftadder.isa_updateEmployeeList();
					upd_person = employeePicker.getText();
					employeePicker.setEnabled(true);
				}
			}
		});

		endPicker = new Combo(this, SWT.NONE);

		/**
		 * Employee picker gets enabled, callback is triggered to update a list
		 * of available employees
		 */
		endPicker.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (employeePicker != null && shiftadder != null) {
					employeePicker.setEnabled(true);
					shiftadder.isa_updateEmployeeList();
					upd_person = employeePicker.getText();
				}
			}
		});
		endPicker.setEnabled(false);
		endPicker.setBounds(10, 190, 103, 30);
		endPicker.setEnabled(false);

		positionPicker = new Combo(this, SWT.NONE);

		/**
		 * Callback is triggered to update a list of available employees
		 */
		positionPicker.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (employeePicker != null && shiftadder != null) {
					upd_person = employeePicker.getText();
					shiftadder.isa_updateEmployeeList();
				}
			}
		});
		positionPicker.setBounds(10, 70, 150, 30);

		Label label = new Label(this, SWT.NONE);
		label.setText("Position");
		label.setBounds(10, 46, 69, 18);

		Label label_1 = new Label(this, SWT.NONE);
		label_1.setText("Employee");
		label_1.setBounds(10, 226, 69, 18);

		Label label_2 = new Label(this, SWT.NONE);
		label_2.setText("Start Time");
		label_2.setBounds(10, 106, 69, 18);

		Label label_3 = new Label(this, SWT.NONE);
		label_3.setText("End Time");
		label_3.setBounds(10, 166, 69, 18);

		addButton = new Button(this, SWT.NONE);
		addButton.setText("Add");
		addButton.setBounds(10, 291, 69, 30);

		cancelButton = new Button(this, SWT.NONE);
		cancelButton.setBounds(85, 291, 82, 30);
		cancelButton.setText("Cancel");

		/**
		 * Resets all controls
		 */
		cancelButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				resetControls();
			}
		});

		cancelButton.setVisible(false);

		/**
		 * Initiates a callback to add new duty, if it's an update request, duty
		 * is deleted first.
		 */
		addButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (shiftadder != null) {
					if (addButton.getText().equals("Update")) {
						shiftadder.dutyDeleteRequest(upd_person, upd_position,
								upd_start);
					}
					shiftadder.processDutyData();
				}
				resetControls();

			}
		});

	}

	/**
	 * Resets all controls.
	 */
	public void resetControls() {
		employeePicker.deselectAll();
		employeePicker.setEnabled(false);
		positionPicker.deselectAll();
		endPicker.setEnabled(false);
		endPicker.deselectAll();
		startPicker.deselectAll();
		WidgetUtilities.safeButtonSet(addButton, "Add");		
		cancelButton.setVisible(false);
	}

	/**
	 * @return the upd_person
	 */
	public String getUpd_person() {
		return upd_person;
	}

	/**
	 * @param upd_person
	 *            the upd_person to set
	 */
	public void setUpd_person(String upd_person) {
		this.upd_person = upd_person;
	}

	/**
	 * @return the upd_position
	 */
	public String getUpd_position() {
		return upd_position;
	}

	/**
	 * @param upd_position
	 *            the upd_position to set
	 */
	public void setUpd_position(String upd_position) {
		this.upd_position = upd_position;
	}

	/**
	 * @return the upd_start
	 */
	public String getUpd_start() {
		return upd_start;
	}

	/**
	 * @param upd_start
	 *            the upd_start to set
	 */
	public void setUpd_start(String upd_start) {
		this.upd_start = upd_start;
	}

	/**
	 * Clears all data from the controls.
	 */
	public void clearControls() {
		employeePicker.removeAll();
		positionPicker.removeAll();
		startPicker.removeAll();
		endPicker.removeAll();
		endPicker.setEnabled(false);
	}

	/**
	 * Clears everything from the employees picker
	 */
	public void clearEmployees() {
		employeePicker.removeAll();
	}

	/**
	 * Clears everything from the positions picker
	 */
	public void clearPositions() {
		positionPicker.removeAll();
	}

	/***************************************************************************/
	// Accessors follow
	public void addEmployee(ArrayList<String> data) {
		Collections.sort(data);
		for (String s : data) {
			WidgetUtilities.safeComboAdd(employeePicker, s);
		}
	}

	public void addEmployee(String s) {
		WidgetUtilities.safeComboAdd(employeePicker, s);
	}

	public String getSelectedEmployee() {
		return employeePicker.getText();
	}

	public void selectEmployee(String s) {
		WidgetUtilities.safeComboSelect(employeePicker, s);
	}

	public void enableEmployeePicker() {
		employeePicker.setEnabled(true);
	}

	public void addPosition(ArrayList<String> data) {
		Collections.sort(data);
		for (String s : data) {
			WidgetUtilities.safeComboAdd(positionPicker, s);
		}
	}

	public void addPosition(String s) {
		WidgetUtilities.safeComboAdd(positionPicker, s);
	}

	public String getSelectedPosition() {
		return positionPicker.getText();
	}

	public void selectPosition(String s) {
		WidgetUtilities.safeComboSelect(positionPicker, s);
	}

	public void addStart(String s) {
		WidgetUtilities.safeComboAdd(startPicker, s);
	}

	public String getSelectedStart() {
		return startPicker.getText();
	}

	public void selectStart(String s) {
		WidgetUtilities.safeComboSelect(startPicker, s);
	}

	public void selectEnd(String s) {
		WidgetUtilities.safeComboSelect(endPicker, s);
	}

	public String getSelectedEnd() {
		return endPicker.getText();
	}

	public void setEndEnabled(boolean enabled) {
		endPicker.setEnabled(enabled);
	}

	// End accessors
	/***************************************************************************/

	/**
	 * Checks if all controls have been selected
	 * 
	 * @return True if all controls are good
	 */
	public boolean checkTextLength() {

		if (startPicker != null && endPicker != null && positionPicker != null
				&& employeePicker != null) {
			return (startPicker.getText().length() > 0
					&& endPicker.getText().length() > 0
					&& positionPicker.getText().length() > 0 && employeePicker
					.getText().length() > 0);
		}
		return false;
	}

	/**
	 * Changes Add button text to update
	 */
	public void setButtonsToUpdate() {
		if (addButton != null && cancelButton != null) {
			WidgetUtilities.safeButtonSet(addButton, "Update");
			cancelButton.setVisible(true);
		}
	}

	/**
	 * Updates end time combo to reflect a selection in start time combo. This
	 * is done to makes sure the end time picked is always after the start time.
	 */
	public void correlateEndTimeCombo() {
		if (endPicker != null && shiftadder != null) {
			endPicker.removeAll();
			endPicker.setEnabled(true);
			ArrayList<String> timespans = shiftadder.getTimeSpan();
			if (timespans == null) {
				return;
			}
			int index = startPicker.getSelectionIndex();

			for (int i = index + 1; i < timespans.size(); i++) {
				WidgetUtilities.safeComboAdd(endPicker, timespans.get(i));
			}
			endPicker.select(0);
		}
	}

	/**
	 * Returns currently selected date as String
	 * 
	 * @return String representation of the currently selected date.
	 */
	public String getSelectedDate() {
		return DateUtils.dateStringFromWidget(datePicker, null);
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

}
