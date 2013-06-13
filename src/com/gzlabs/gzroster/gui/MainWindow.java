package com.gzlabs.gzroster.gui;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.HashMap;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.jface.dialogs.MessageDialog;
import com.gzlabs.drosterheper.IDisplayStatus;
import com.gzlabs.gzroster.data.DataManager;
import com.gzlabs.gzroster.data.Tables;

import org.eclipse.swt.widgets.MenuItem;

public class MainWindow implements IDisplayStatus, 
IDutyUpdater, IEHoursHandler, 
IShiftAdder, IPositionsManager,
IDetailsManager, IEmployeeManager, ITimeOffManager{

	protected Shell shell;
	private final FormToolkit formToolkit = new FormToolkit(
			Display.getDefault());
	private DataManager dman;

	/************************************************************/
	// Main Window Container
	private TabFolder tabFolder;		
	private Label lblStatus;
	private EmployeeHoursComposite employeeHoursComposite;
	private EmployeePositionComposite employeePositionComposite;
	private AddShiftComposite addShiftComposite;
	/************************************************************/

	/************************************************************/
	// Employee Tab
	private EmployeesWidget employeesWidget;
	private EmployeeTimeOffComposite employeeTimeOffComposite;
	/************************************************************/

	/************************************************************/
	// Positions Tab
	private TabItem tbtmDetails;	
	private PositionsWidget positionsWidget;

	/************************************************************/

	/************************************************************/
	// Details Tab
	private DetailsWidget detailsWidget;
	/************************************************************/
	
	/************************************************************/
	private Label lblAllowedPositions;
	
	/************************************************************/

	/**
	 * Launch the application.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			Display.setAppName("GZRoster");
			MainWindow window = new MainWindow();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setImage(SWTResourceManager.getImage(MainWindow.class, "/com/gzlabs/gzroster/gui/1370647295_60814.ico"));
		shell.setSize(1420, 928);
		shell.setText("GZ Roster");
		shell.setLayout(null);

		Menu menu = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menu);
		
		MenuItem fileMenu = new MenuItem(menu, SWT.CASCADE);
		fileMenu.setText("File");
		
		Menu menu_2 = new Menu(fileMenu);
		fileMenu.setMenu(menu_2);
		
		MenuItem quitMenuItem = new MenuItem(menu_2, SWT.NONE);
		quitMenuItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {			
				System.exit(0);
			}
		});
		quitMenuItem.setText("Quit");
		
		MenuItem editMenu = new MenuItem(menu, SWT.CASCADE);
		editMenu.setText("Edit");
		
		Menu menu_3 = new Menu(editMenu);
		editMenu.setMenu(menu_3);
		
		MenuItem configurationMenuItem = new MenuItem(menu_3, SWT.NONE);
		configurationMenuItem.setText("Configuration");
		
		MenuItem helpMenu = new MenuItem(menu, SWT.CASCADE);
		helpMenu.setText("Help");
		
		Menu menu_1 = new Menu(helpMenu);
		helpMenu.setMenu(menu_1);
		
		MenuItem helpAboutMenuItem = new MenuItem(menu_1, SWT.NONE);
		helpAboutMenuItem.setText("About");

		tabFolder = new TabFolder(shell, SWT.NONE);
		tabFolder.setBounds(10, 10, 900, 800);
		
		addShiftComposite = new AddShiftComposite(shell, SWT.NONE, this);
		addShiftComposite.setBounds(916, 38, 169, 336);
		formToolkit.adapt(addShiftComposite);
		formToolkit.paintBordersFor(addShiftComposite);

		createEmployeesTab();
		createPositionsTab();

		employeeHoursComposite = new EmployeeHoursComposite(shell, SWT.NONE, this);
		employeeHoursComposite.setBounds(1091, 38, 227, 529);
		formToolkit.adapt(employeeHoursComposite);
		formToolkit.paintBordersFor(employeeHoursComposite);			
		
		lblStatus = new Label(shell, SWT.NONE);
		lblStatus.setBounds(10, 816, 1398, 18);
		formToolkit.adapt(lblStatus, true, true);
		lblStatus.setText("Status");
		dman = new DataManager(this);
		createDetailsTab();
		populateData();
	}

	protected void resetScheduleControls() {				
		if(addShiftComposite!=null)
		{
			addShiftComposite.resetControls();
		}		
	}

	/*************************************************************************/
	/**
	 * Separated for readability.
	 */	

	private void createDetailsTab() {
		tbtmDetails = new TabItem(tabFolder, SWT.NONE);
		tbtmDetails.setText("Details");
		
		detailsWidget=new DetailsWidget(tabFolder,  SWT.NONE, this, this);
		detailsWidget.setLayout(new GridLayout(1, false));
		tbtmDetails.setControl(detailsWidget);
	}

	protected void updateDetailsData(ArrayList<String> data) {
		if (detailsWidget != null) {
			detailsWidget.initiateData(data);
		}
	}

	protected void createEmployeesTab() {

		TabItem tbtmEmployees = new TabItem(tabFolder, SWT.NONE);
		tbtmEmployees.setText("Employees");		

		Composite composite_1 = new Composite(tabFolder, SWT.NONE);
		tbtmEmployees.setControl(composite_1);
		composite_1.setBounds(0, 27, 900, 800);
		formToolkit.adapt(composite_1);
		formToolkit.paintBordersFor(composite_1);
		
		employeesWidget = new EmployeesWidget(composite_1, SWT.NONE, this);
		employeesWidget.setBounds(10, 10, 640, 376);
		formToolkit.adapt(employeesWidget);
		formToolkit.paintBordersFor(employeesWidget);

		employeePositionComposite = new EmployeePositionComposite(composite_1, SWT.NONE);
		employeePositionComposite.setBounds(680, 32, 182, 390);
		formToolkit.adapt(employeePositionComposite);
		formToolkit.paintBordersFor(employeePositionComposite);
		
		lblAllowedPositions = new Label(composite_1, SWT.CENTER);
		lblAllowedPositions.setBounds(701, 10, 130, 18);
		formToolkit.adapt(lblAllowedPositions, true, true);
		lblAllowedPositions.setText("Allowed Positions");
		
		employeeTimeOffComposite = new EmployeeTimeOffComposite(composite_1, SWT.NONE, this);
		employeeTimeOffComposite.setBounds(10, 428, 418, 303);
		formToolkit.adapt(employeeTimeOffComposite);
		formToolkit.paintBordersFor(employeeTimeOffComposite);
		
		Label lblTimeOff = new Label(composite_1, SWT.CENTER);
		lblTimeOff.setBounds(168, 404, 69, 18);
		formToolkit.adapt(lblTimeOff, true, true);
		lblTimeOff.setText("Time Off");
		toggleEmployeeEdit(false);
	}

	protected void createPositionsTab() {
		TabItem tbtmPositions = new TabItem(tabFolder, SWT.NONE);
		tbtmPositions.setText("Positions");
		positionsWidget = new PositionsWidget(tabFolder, SWT.NONE, this);
		tbtmPositions.setControl(positionsWidget);
		positionsWidget.setBounds(10, 10, 431, 341);
		formToolkit.adapt(positionsWidget);
		formToolkit.paintBordersFor(positionsWidget);
	}

	/*************************************************************************/

	/**
	 * Populates objects with data
	 */
	public void populateData() {
		ArrayList<String> employees = dman.getEmployees();
		
		if(addShiftComposite!=null && employeesWidget != null)
		{
			addShiftComposite.clearControls();
			addShiftComposite.addEmployee(employees);
			
			employeesWidget.clearEmployeesList();
			employeesWidget.addEmployee(employees);	
		
			Collections.sort(employees);
			for (String s : employees) {
							
				employeeHoursComposite.updateItem(s, 
						dman.getTotalEmpoloyeeHours(s, employeeHoursComposite.getStartDate(), 
								employeeHoursComposite.getEndDate()));
			}
		
			ArrayList<String> positions = dman.getPositions();
			
			if(positionsWidget != null)
			{
				positionsWidget.clearPositionsList();	
				Collections.sort(positions);
				for (String s : positions) {
					positionsWidget.addPosition(s);
					addShiftComposite.addPosition(s);
					
					if(employeePositionComposite!=null)
						employeePositionComposite.addButton(s);
				}
			}
		
			ArrayList<String> timespans = dman.getTimeSpan();
			for (String s : timespans) {
				addShiftComposite.addStart(s);
			}
			updateDetailsData(timespans);
		}
		
	}

	/**
	 * Displays current status in a text area window.
	 */
	@Override
	public void DisplayStatus(String status) {		
		lblStatus.setText(status);
	}

	/**
	 * Displays error dialog
	 */
	@Override
	public void ShowErrorBox(String error) {
		MessageDialog.openError(shell, "Error", error);
	}

	/**
	 * Enables and disables employee edit controls.
	 * 
	 * @param enable
	 *            Specify to enable or disable
	 */
	private void toggleEmployeeEdit(boolean enable) {
		//employeePositionComposite.setEnabled(enable);
	}


	/**
	 * Sets employee details to the specified HashMap values
	 * 
	 * @param details
	 *            HashMap with values.
	 */
	public void setEmployeeDetails(String string) {
		
		ArrayList<String> details=null;
		if(dman!=null)
		{
			details=dman.getEmployeeDetails(string);		
		if (details != null && employeesWidget!=null) {
			String value =details.get(Tables.PERSON_NAME_INDEX);
			if (value != null) {
				employeesWidget.setNameText(value);
			}

			value =details.get(Tables.PERSON_ADDRESS_INDEX);
			if (value != null) {
				employeesWidget.setAddressText(value);
			}

			value = details.get(Tables.PERSON_HPHONE_INDEX);
			if (value != null) {
				employeesWidget.setHomephoneText(value);
			}

			value = details.get(Tables.PERSON_MPHONE_INDEX);
			if (value != null) {
				employeesWidget.setMobilePhoneText(value);
			}

			value = details.get(Tables.PERSON_ACTIVE_INDEX);
			if (value != null && value.equals("1")) {
				employeesWidget.setActiveCheck(true);
			} else {
				employeesWidget.setActiveCheck(false);
			}
			
			if(employeePositionComposite!=null && dman!=null)
			{			
				employeePositionComposite.checkBoxes
				(dman.getPersonToPosMapping(employeesWidget.getNameText()));
			}
			
			if(employeeTimeOffComposite!=null)
			{
				employeeTimeOffComposite.toggleButtonVisibility(true);
				employeeTimeOffComposite.showTimeOff(dman.getTimeOff(string));
			}
		}

		}
	}

	/**
	 * Sets position details to the specified HashMap values
	 * 
	 * @param details
	 *            HashMap with values.
	 */
	public void setPositionDetails(String string) {
		
		if(dman != null)
		{
			ArrayList<String> details=dman.getPositionDetailsByName(string);
	
			if (details != null && positionsWidget!=null) {
				String value = details.get(Tables.PLACE_NAME_INDEX);
				if (value != null) {
					positionsWidget.setPositionNameText(value);
				}
	
				value = details.get(Tables.PLACE_NOTE_INDEX);
				if (value != null) {
					positionsWidget.setPositionNoteText(value);
				}
			}
		}
	}

	/**
	 * Updates existing employee or inserts new one.
	 */
	public void processEmployeeData() {
		if(employeesWidget!=null)
		{
			ArrayList<String> new_details = new ArrayList<String>();
			new_details.add("");
			new_details.add(employeesWidget.getNameText());
			new_details.add(employeesWidget.getAddressText());
			new_details.add(employeesWidget.getHomephoneText());
			new_details.add(employeesWidget.getMobilePhoneText());
			new_details.add("");
			new_details.add(employeesWidget.getActiveCheck() ? "1" : "0");
			new_details.add("");
			new_details.add("");
			
			ArrayList<String> old_details = new ArrayList<String>(Tables.PLACE_MAX_COLS);
			old_details.add("");
			old_details.add(employeesWidget.getOld_name());
			old_details.add(employeesWidget.getOld_address());
			old_details.add(employeesWidget.getOld_hphone());
			old_details.add(employeesWidget.getOld_mphone());
			old_details.add("");
			old_details.add(employeesWidget.getOld_active());
			old_details.add("");
			old_details.add("");
			
			ArrayList<String> position_boxes=null;
			if(employeePositionComposite!=null)
			{
				position_boxes=employeePositionComposite.getBoxes();
			}				
			
			if (employeesWidget.getSelectionIndex() >= 0) {
				dman.updateEmployee(old_details, new_details, position_boxes);
			} else {
				dman.addEmployee(new_details,position_boxes);
			}
			populateData();
		}
	}

	/**
	 * Updates existing duty or inserts new one.
	 */
	@Override
	public void processDutyData() {
		if (addShiftComposite!=null
				&& addShiftComposite.checkTextLength()) {
			HashMap<String, String> details = new HashMap<String, String>();
			details.put("DUTY_START_TIME",
					addShiftComposite.getSelectedDate() + " "
							+ addShiftComposite.getSelectedStart() + ":00.0");
			details.put("DUTY_END_TIME",
					addShiftComposite.getSelectedDate() + " "
							+ addShiftComposite.getSelectedEnd() + ":00.0");
			details.put("PLACE_ID", addShiftComposite.getSelectedPosition());
			details.put("PERSON_ID", addShiftComposite.getSelectedEmployee());
			details.put("APPROVED", "Y");

			if(dman.checkDutyConflict(details))
			{
				DisplayStatus("Scheduling conflict!");
				if(!MessageDialog.openConfirm(shell, "Schedule Conflict", 
						"This employee is already scheduled during this time period.  Add anyway?"))
				{
					DisplayStatus("Cancelled!");
					return;
				}
			}
			
			dman.addDuty(details);
			populateData();
		} else {
			DisplayStatus("Both Employee and Position need to be selected!  Both Start and End times are required!");
		}
	}

	/**
	 * Updates existing position or inserts new one.
	 */
	public void processPositionData() {
		
		if(positionsWidget != null)
		{
			ArrayList<String> new_details = new ArrayList<String>();
			new_details.add("");
			new_details.add(positionsWidget.getPositionNameText());
			new_details.add(positionsWidget.getPositionNoteText());
			
			ArrayList<String> old_details = new ArrayList<String>(Tables.PLACE_MAX_COLS);
			old_details.add("");
			old_details.add(positionsWidget.getOld_name());
			old_details.add(positionsWidget.getOld_note());
			
			
			if (positionsWidget.getSelectionIndex() >= 0) {
				dman.updatePosition(old_details,new_details);
			} else {
				dman.addPosition(new_details);
			}
		}
	}

	@Override
	public void dutyUpdateRequest(String label, String col_label, String row_label) {

		if(addShiftComposite != null && dman!=null)
		{
			addShiftComposite.setUpd_person(label);
			addShiftComposite.setUpd_position(col_label);			
			addShiftComposite.selectEmployee(label);
			
			ArrayList<String> allowed_positions=dman.getPersonToPosMapping(label);
			addShiftComposite.clearPositions();
			addShiftComposite.addPosition(allowed_positions);
			
			addShiftComposite.selectPosition(col_label);				
				
			String start_time=dman.getDutyStart(label, col_label, 
					addShiftComposite.getSelectedDate()+" "+row_label+":00.0");
			
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
			Calendar time_cal=new GregorianCalendar();
			
			String end_time=dman.getDutyEnd(label, col_label, 
					addShiftComposite.getSelectedDate()+" "+row_label+":00.0");
			try {
				String zminute="00";
				time_cal.setTime(sdf.parse(start_time));
				start_time=time_cal.get(Calendar.HOUR_OF_DAY)+":"+(time_cal.get(Calendar.MINUTE)==0?zminute:time_cal.get(Calendar.MINUTE));
				time_cal.setTime(sdf.parse(end_time));
				end_time=time_cal.get(Calendar.HOUR_OF_DAY)+":"+(time_cal.get(Calendar.MINUTE)==0?zminute:time_cal.get(Calendar.MINUTE));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			addShiftComposite.setEndEnabled(true);
			addShiftComposite.setUpd_start(start_time);
			addShiftComposite.selectStart(start_time);			
			
			addShiftComposite.correlateEndTimeCombo();
			addShiftComposite.selectEnd(end_time);	
			addShiftComposite.setButtonsToUpdate();
		}			
	}

	@Override
	public void dutyDeleteRequest(String label, String col_label, String row_label) {
		if(dman!=null)
		{
			dman.deleteDuty(label, col_label, 
					addShiftComposite.getSelectedDate()+" "+row_label+":00.0");
		}
		
	}

	@Override
	public void dutyNewRequest(String col_label, String row_label) {
		
		if(addShiftComposite != null && dman!=null)
		{
			//This is where we determine if an employee is allowed to work
			//this position
			ArrayList<String> allowed_empl=
					dman.getAllowedEmployees(col_label, addShiftComposite.getSelectedDate()+" "+row_label+":00.0");
			addShiftComposite.clearEmployees();
			addShiftComposite.addEmployee(allowed_empl);
			addShiftComposite.selectPosition(col_label);
			addShiftComposite.selectStart(row_label);
			addShiftComposite.correlateEndTimeCombo();
		}	
			
	}
		
	@Override
	public void rangeChanged(String dateStringFromWidget,
			String dateStringFromWidget2) {
		if(dman!=null)
		{
			ArrayList<String> employees = dman.getEmployees();
			for (String s : employees) {
				employeeHoursComposite.updateItem(s, 
						dman.getTotalEmpoloyeeHours(s, employeeHoursComposite.getStartDate(), 
								employeeHoursComposite.getEndDate()));
		}
		}
		
	}

	@Override
	public void isa_DateChaged(String new_date) {
		if(dman!=null && employeeHoursComposite!=null && addShiftComposite!=null)
		{
			updateDetailsData(dman.getTimeSpan());
			employeeHoursComposite.updateDates(new_date);
			ArrayList<String> allowed_empl=
					dman.getAllowedEmployees(addShiftComposite.getSelectedEmployee(), addShiftComposite.getSelectedDate()+" 00:00:00.0");
			addShiftComposite.clearEmployees();
			addShiftComposite.addEmployee(allowed_empl);
		}
		
	}

	@Override
	public ArrayList<String> getTimeSpan() {
		if(dman != null)
		{
			return dman.getTimeSpan();
		}
		return null;
	
	}


	@Override
	public void deletePosition(String[] selection) {
		if(dman!=null)
		{
			dman.deletePosition(selection);
		}
		
	}

	@Override
	public ArrayList<String> getPositions() {
		if(dman!=null)
		{
			return dman.getPositions();
		}
		return null;
	}

	@Override
	public String getCellLabelString(String element, String column_label) {
		if (dman != null && addShiftComposite!=null) {
			return dman.getCellLabelString((String) element,
					column_label,
					addShiftComposite.getSelectedDate());
		}
		return null;
	}



	@Override
	public void deleteEmployee(String[] selection) {
		if(dman!=null)
		{
			dman.deleteEmployee(selection);
		}
		
	}

	
	@Override
	public void isa_PositionChanged(String text) {
		if(addShiftComposite != null && dman!=null)
		{
			//This is where we determine if an employee is allowed to work
			//this position
			ArrayList<String> allowed_empl=dman.getAllowedEmployees(text, null);
			addShiftComposite.clearEmployees();
			addShiftComposite.addEmployee(allowed_empl);
			addShiftComposite.selectEmployee(addShiftComposite.getUpd_person());
		}	
		
	}

	@Override
	public void isa_EmployeeChanged(String text) {
		if(addShiftComposite != null && dman!=null)
		{
			//This is where we determine if an employee is allowed to work
			//this position
			ArrayList<String> allowed_empl=dman.getPersonToPosMapping(text);
			addShiftComposite.clearPositions();
			addShiftComposite.addPosition(allowed_empl);
			addShiftComposite.selectPosition(addShiftComposite.getUpd_position());

		}	
		
	}

	@Override
	public void newTimeOffRequest(String start, String end) {
		if(employeesWidget != null && dman!=null)
		{
			dman.newTimeOffRequest(start, end, employeesWidget.getNameText());
			employeeTimeOffComposite.showTimeOff(dman.getTimeOff(employeesWidget.getNameText()));
		}
		
	}

	@Override
	public boolean deleteTimeOffRequest(String start, String end) {
		if(employeesWidget != null && dman!=null)
		{
			return dman.deleteTimeOffRequest(start, end, employeesWidget.getNameText());
		}
		return false;
		
	}
}
