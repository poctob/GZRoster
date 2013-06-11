package com.gzlabs.gzroster.gui;

import java.util.ArrayList;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import com.gzlabs.gzroster.data.DateUtils;

public class EmployeeHoursComposite extends Composite {
	private Table hoursTable;
	private ArrayList<TableItem> tableItems;
	private IEHoursHandler hours_handler;
	final DateTime startDate;
	final DateTime endDate;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public EmployeeHoursComposite(Composite parent, int style, IEHoursHandler handler) {
		super(parent, style);
		hours_handler=handler;
		startDate = new DateTime(this, SWT.BORDER | SWT.DROP_DOWN);		
		startDate.setBounds(57, 31, 119, 29);
		
		endDate = new DateTime(this, SWT.BORDER | SWT.DROP_DOWN);
		updateDates(null);
		
		endDate.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int date_check=DateUtils.compareToWidget(endDate, startDate);
				if(hours_handler!=null && date_check>=0)
				{
					hours_handler.rangeChanged(DateUtils.dateStringFromWidget(startDate, null),
							DateUtils.dateStringFromWidget(endDate, null));
				}
			}
		});
		endDate.setBounds(57, 66, 119, 29);
		
		startDate.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int date_check=DateUtils.compareToWidget(endDate, startDate);
				if(hours_handler!=null && date_check>=0)
				{
					hours_handler.rangeChanged(DateUtils.dateStringFromWidget(startDate, null),
							DateUtils.dateStringFromWidget(endDate, null));
				}
			}
		});
		
		updateDates(null);
		
		Label lblFrom = new Label(this, SWT.NONE);
		lblFrom.setBounds(10, 31, 47, 18);
		lblFrom.setText("From:");
		
		Label lblTo = new Label(this, SWT.NONE);
		lblTo.setBounds(24, 66, 152, 18);
		lblTo.setText("To:");
		
		hoursTable = new Table(this, SWT.BORDER | SWT.FULL_SELECTION);
		hoursTable.setBounds(10, 105, 205, 416);
		hoursTable.setHeaderVisible(true);
		hoursTable.setLinesVisible(true);
		
		TableColumn tblclmnEmployee = new TableColumn(hoursTable, SWT.NONE);
		tblclmnEmployee.setWidth(120);
		tblclmnEmployee.setText("Employee");
		
		TableColumn tblclmnHours = new TableColumn(hoursTable, SWT.CENTER);
		tblclmnHours.setWidth(74);
		tblclmnHours.setText("Hours");
		
		Label lblTotalEmployeeHours = new Label(this, SWT.NONE);
		lblTotalEmployeeHours.setBounds(24, 7, 142, 18);
		lblTotalEmployeeHours.setText("Total Employee Hours");
		
		tableItems=new ArrayList<TableItem>();

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
	
	public void clearTable()
	{
		hoursTable.clearAll();
	}
	
	public void addItem(String name, String hours)
	{
		TableItem ti=new TableItem(hoursTable, SWT.NONE);
		ti.setText(0, name);
		ti.setText(1, hours);
		tableItems.add(ti);
	}
	
	public void updateItem(String name, String new_hours)
	{
		for(TableItem item : tableItems)
		{
			if(item.getText(0).equals(name))
			{
				item.setText(1, new_hours);
				return;			
			}
		}
		addItem(name, new_hours);
	}
	
	public void removeItem(String name)
	{
		for(int i=0; i< tableItems.size(); i++)
		{
			if(tableItems.get(i).getText(0).equals(name))
			{
				hoursTable.remove(i);
				tableItems.remove(i);
				return;
			}
		}
	}

	public String getStartDate()
	{
		return DateUtils.dateStringFromWidget(startDate, null)+" 00:00:00.0";
	}
	
	public String getEndDate()
	{
		return DateUtils.dateStringFromWidget(endDate, null)+" 23:59:59.0";
	}

	public void updateDates(String dateStringFromWidget) {
		
		if(startDate!=null && endDate !=null && hours_handler!=null)
		{
			startDate.setDay(DateUtils.getWeekStartDay(false, dateStringFromWidget));
			startDate.setMonth(DateUtils.getWeekStartMonth(false, dateStringFromWidget));
			startDate.setYear(DateUtils.getWeekStartYear(false, dateStringFromWidget));

			endDate.setDay(DateUtils.getWeekEndDay(false, dateStringFromWidget));
			endDate.setMonth(DateUtils.getWeekEndMonth(false, dateStringFromWidget));
			endDate.setYear(DateUtils.getWeekEndYear(false, dateStringFromWidget));

			hours_handler.rangeChanged(DateUtils.dateStringFromWidget(startDate, null),
					DateUtils.dateStringFromWidget(endDate, null));
		}
	}
}
