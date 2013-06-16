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
 * @author apavlune
 *
 */
public class AddShiftComposite extends Composite {
	
	/***************************************************************************/
	//Widgets	
	private DateTime datePicker;
	private Combo employeePicker;
	private Combo startPicker;
	private Combo endPicker;
	private Combo positionPicker;
	private Button addButton;
	private Button cancelButton;
	
	//Update place holders
	private String upd_person;
	private String upd_position;
	private String upd_start;
	/***************************************************************************/
	
	//Callback Interface
	final private IShiftAdder shiftadder;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public AddShiftComposite(Composite parent, int style, IShiftAdder sa) {
		super(parent, style);
		this.shiftadder = sa;
		
		datePicker = new DateTime(this, SWT.BORDER | SWT.DROP_DOWN);
		datePicker.setBounds(10, 10, 103, 30);
		
		datePicker.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(shiftadder != null)
				{
					shiftadder.isa_DateChaged(getSelectedDate());
				}
			}
		});
		
		employeePicker = new Combo(this, SWT.NONE);
		employeePicker.setBounds(10, 250, 150, 30);
		employeePicker.setEnabled(false);
		
		startPicker = new Combo(this, SWT.NONE);
		startPicker.setBounds(10, 130, 103, 30);
		
		startPicker.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				correlateEndTimeCombo();				
				shiftadder.isa_updateEmployeeList();
				upd_person=employeePicker.getText();
				employeePicker.setEnabled(true);
			}
		});
		
		endPicker = new Combo(this, SWT.NONE);
		endPicker.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				employeePicker.setEnabled(true);
				shiftadder.isa_updateEmployeeList();
				upd_person=employeePicker.getText();
			}
		});
		endPicker.setEnabled(false);
		endPicker.setBounds(10, 190, 103, 30);
		endPicker.setEnabled(false);
		
		positionPicker = new Combo(this, SWT.NONE);
		positionPicker.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {				
				upd_person=employeePicker.getText();	
				shiftadder.isa_updateEmployeeList();
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
		
		cancelButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				resetControls();
			}
		});
		
		cancelButton.setVisible(false);
		
		addButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(shiftadder!=null)
				{
					if(addButton.getText().equals("Update"))
					{			
						shiftadder.dutyDeleteRequest(upd_person,
								upd_position, 
								upd_start);
					}
					shiftadder.processDutyData();
				}
				resetControls();
				
			}
		});
	

	}

	public void resetControls()
	{
		employeePicker.deselectAll();
		employeePicker.setEnabled(false);
		positionPicker.deselectAll();
		endPicker.setEnabled(false);
		endPicker.deselectAll();
		startPicker.deselectAll();
		addButton.setText("Add");
		cancelButton.setVisible(false);
	}
	
	/**
	 * @return the upd_person
	 */
	public String getUpd_person() {
		return upd_person;
	}

	/**
	 * @param upd_person the upd_person to set
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
	 * @param upd_position the upd_position to set
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
	 * @param upd_start the upd_start to set
	 */
	public void setUpd_start(String upd_start) {
		this.upd_start = upd_start;
	}

	public void clearControls()
	{
		employeePicker.removeAll();
		positionPicker.removeAll();
		startPicker.removeAll();
		endPicker.removeAll();
		endPicker.setEnabled(false);
	}
	
	public void clearEmployees()
	{
		employeePicker.removeAll();
	}
	
	public void clearPositions()
	{
		positionPicker.removeAll();
	}
	/***************************************************************************/
	//Accessors follow
	public void addEmployee(ArrayList<String> data)
	{
		Collections.sort(data);
		for(String s:data)
		{
			employeePicker.add(s);
		}
	}
	public void addEmployee(String s)
	{
		employeePicker.add(s);
	}
	
	public String getSelectedEmployee()
	{
		return employeePicker.getText();
	}
	
	public void selectEmployee(String s)
	{
		safeComboSelect(s, employeePicker);
	}
	
	public void enableEmployeePicker()
	{
		employeePicker.setEnabled(true);
	}

	public void addPosition(ArrayList<String> data)
	{
		Collections.sort(data);
		for(String s:data)
		{
			positionPicker.add(s);
		}
	}
	
	public void addPosition(String s)
	{
		positionPicker.add(s);
	}
	
	public String getSelectedPosition()
	{
		return positionPicker.getText();
	}
	
	public void selectPosition(String s)
	{
		safeComboSelect(s, positionPicker);
	}
	
	public void addStart(String s)
	{
		startPicker.add(s);
	}
	
	public String getSelectedStart()
	{
		return startPicker.getText();
	}
	
	public void selectStart(String s)
	{
		safeComboSelect(s, startPicker);
	}
	
	public void selectEnd(String s)
	{
		safeComboSelect(s, endPicker);	
	}
	
	public String getSelectedEnd()
	{
		return endPicker.getText();
	}
	
	public void setEndEnabled(boolean enabled)
	{
		endPicker.setEnabled(enabled);
	}
	//End accessors	
	/***************************************************************************/

	public boolean checkTextLength()
	{
		return (startPicker.getText().length()>0 && 
				endPicker.getText().length()>0 &&
				positionPicker.getText().length()>0 && 
				employeePicker.getText().length()>0);
	}
	
	public void setButtonsToUpdate()
	{
		addButton.setText("Update");
		cancelButton.setVisible(true);
	}
	
	/**
	 * Updates end time combo to reflect a selection in start time combo.
	 * This is done to makes sure the end time picked is always after
	 * the start time.
	 */
	public void correlateEndTimeCombo()
	{
		if (endPicker != null && shiftadder != null) {
			endPicker.removeAll();
			endPicker.setEnabled(true);
			ArrayList<String> timespans = shiftadder.getTimeSpan();
			if(timespans == null)
			{
				return;
			}
			int index = startPicker.getSelectionIndex();

			for (int i = index + 1; i < timespans.size(); i++) {
				endPicker.add(timespans.get(i));
			}
			endPicker.select(0);
		}
	}
	
	public String getSelectedDate()
	{
		return DateUtils.dateStringFromWidget(datePicker, null);
	}
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
	
	private void safeComboSelect(String s, Combo c)
	{
		if(c != null && s != null)
		{
			c.select(c.indexOf(s));
		}
	}

}
