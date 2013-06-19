package com.gzlabs.gzroster.gui;

import java.util.ArrayList;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import com.gzlabs.gzroster.data.DateUtils;
import org.eclipse.wb.swt.SWTResourceManager;

public class EmployeeTimeOffComposite extends Composite {

	private DateTime dateTime;
	private DateTime dateTime_1;
	private DateTime dateTime_2;
	private DateTime dateTime_3;
	private Button btnAllDay;
	private Button button;
	private Button btnAdd;
	private Button btnEdit;
	private Button btnDelete;
	private List list;
	
	
	private ITimeOffManager itof;
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public EmployeeTimeOffComposite(Composite parent, int style, ITimeOffManager it) {		
		super(parent, style);
		
		if(it == null)
		{
			return;
		}
		itof=it;
		
		list = new List(this, SWT.BORDER);
		list.setFont(SWTResourceManager.getFont("Arial", 8, SWT.NORMAL));
		list.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				toggleButtonVisibility(true);
			}
		});
		list.setBounds(110, 80, 289, 210);
		
		dateTime = new DateTime(this, SWT.BORDER | SWT.DROP_DOWN);
		dateTime.setBounds(66, 10, 114, 29);
		
		dateTime_1 = new DateTime(this, SWT.BORDER | SWT.TIME);
		dateTime_1.setBounds(194, 10, 116, 29);
		
		Label lblFrom = new Label(this, SWT.NONE);
		lblFrom.setBounds(10, 10, 51, 18);
		lblFrom.setText("From:");
		
		btnAllDay = new Button(this, SWT.CHECK);
		btnAllDay.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {			
				dateTime_1.setVisible(!btnAllDay.getSelection());
				if(btnAllDay.getSelection())
				{
					dateTime_1.setHours(0);
					dateTime_1.setMinutes(0);	
					dateTime_1.setSeconds(0);
				}
			}
		});
		btnAllDay.setBounds(316, 17, 80, 22);
		btnAllDay.setText("All Day");
		
		Label lblTo = new Label(this, SWT.NONE);
		lblTo.setText("To:");
		lblTo.setBounds(10, 45, 51, 18);
		
		dateTime_2 = new DateTime(this, SWT.BORDER | SWT.DROP_DOWN);
		dateTime_2.setBounds(66, 45, 114, 29);
		
		dateTime_3 = new DateTime(this, SWT.BORDER | SWT.TIME);
		dateTime_3.setBounds(194, 45, 116, 29);
		
		button = new Button(this, SWT.CHECK);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				dateTime_3.setVisible(!button.getSelection());
				
				if(button.getSelection())
				{
					dateTime_3.setHours(23);
					dateTime_3.setMinutes(59);	
					dateTime_3.setSeconds(59);
				}
			}
		});
		button.setText("All Day");
		button.setBounds(316, 52, 80, 22);
		
		btnAdd = new Button(this, SWT.NONE);
		btnAdd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				addEntry();				
			}
		});
		btnAdd.setBounds(10, 88, 88, 30);
		btnAdd.setText("Add");
		
		btnEdit = new Button(this, SWT.NONE);
		btnEdit.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				deleteEntry();
				addEntry();	
			}
		});
		btnEdit.setBounds(10, 124, 88, 30);
		btnEdit.setText("Update");
		
		btnDelete = new Button(this, SWT.NONE);
		btnDelete.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				deleteEntry();
			}
		});
		btnDelete.setBounds(10, 161, 88, 30);
		btnDelete.setText("Delete");
		toggleButtonVisibility(false);
	}

	public void toggleButtonVisibility(boolean visible)
	{
		btnAdd.setVisible(visible);
		
		if(list.getItemCount()==0)
		{
			btnEdit.setVisible(false);
			btnDelete.setVisible(false);
		}
		else
		{
			btnEdit.setVisible(true);
			btnDelete.setVisible(true);
		}
	}
	
	private void deleteEntry()
	{
		String selection=list.getItem(list.getSelectionIndex());
		
		String start=selection.substring(5, 27);
		String end=selection.substring(30, selection.length());
		if(itof.deleteTimeOffRequest(start, end))
		{
			list.remove(list.getSelectionIndex());
		}
	}
	
	private void addEntry()
	{
		String start=DateUtils.dateStringFromWidget(dateTime, dateTime_1);
		String end=DateUtils.dateStringFromWidget(dateTime_2, dateTime_3);
		itof.newTimeOffRequest(start, end);
	}
	public void showTimeOff(ArrayList<String> data)
	{
		list.removeAll();
		for(String s:data)
		{
			list.add(s);
		}
		
	}
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
