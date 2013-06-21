package com.gzlabs.gzroster.gui;

import java.util.ArrayList;

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

/**
 * Widget to display total employee hours
 * @author apavlune
 *
 */
public class EmployeeHoursComposite extends Composite {
	
	//Member variables
	private Table hoursTable;
	private ArrayList<TableItem> tableItems;
	private IEHoursHandler hours_handler;
	final DateTime startDate;
	final DateTime endDate;

	/**
	 * Creates widget
	 * @param parent Parent composite
	 * @param style Composite style
	 * @param handler Callback interface object
	 */
	public EmployeeHoursComposite(Composite parent, int style, IEHoursHandler handler) {
		super(parent, style);
		startDate = new DateTime(this, SWT.BORDER | SWT.DROP_DOWN);		
		endDate = new DateTime(this, SWT.BORDER | SWT.DROP_DOWN);
		
		if(handler == null)
			return;
		
		hours_handler=handler;
		
		startDate.setBounds(57, 31, 119, 29);
		
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
	
	/**
	 * Clears all data from teh table
	 */
	public void clearTable()
	{
		hoursTable.clearAll();
	}
	
	/**
	 * Adds item to the table
	 * @param name Name of the person
	 * @param hours Person's hours
	 */
	public void addItem(String name, String hours)
	{
		TableItem ti=new TableItem(hoursTable, SWT.NONE);
		WidgetUtilities.safeTableItemSet(ti, 0, name);
		WidgetUtilities.safeTableItemSet(ti, 1, hours);
		tableItems.add(ti);
	}
	
	/**
	 * Updates an item in the table
	 * @param name Name of the person
	 * @param new_hours New value for hours
	 */
	public void updateItem(String name, String new_hours)
	{
		for(TableItem item : tableItems)
		{
			if(item!=null && item.getText(0).equals(name))
			{
				WidgetUtilities.safeTableItemSet(item, 1, new_hours);				
				return;			
			}
		}
		addItem(name, new_hours);
	}
	
	/**
	 * Removes item from the list
	 * @param name Employee name to remove
	 */
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

	/**
	 * Fetches current start date
	 * @return Start date as string
	 */
	public String getStartDate()
	{
		return DateUtils.dateStringFromWidget(startDate, null)+" 00:00:00.0";
	}
	
	/**
	 * Fetches current end date
	 * @return End date as string
	 */
	public String getEndDate()
	{
		return DateUtils.dateStringFromWidget(endDate, null)+" 23:59:59.0";
	}

	/**
	 * Updates start and end dates based on another datetime widget
	 * @param dateStringFromWidget DateTime widget to use
	 */
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
