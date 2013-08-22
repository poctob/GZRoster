package com.gzlabs.gzroster.gui;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Properties;

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
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.jface.dialogs.MessageDialog;

import com.gzlabs.gzroster.data.DB_Object;
import com.gzlabs.gzroster.data.UploadManager;
import com.gzlabs.gzroster.data.DataManager;
import com.gzlabs.gzroster.gui.person.EmployeeHoursComposite;
import com.gzlabs.gzroster.gui.person.EmployeesWidget;
import com.gzlabs.gzroster.gui.person.IEmployeeManager;
import com.gzlabs.gzroster.gui.time_off.ITimeOffManager;
import com.gzlabs.gzroster.gui.time_off.TimeOffWidget;
import com.gzlabs.gzroster.sql.DBObjectType;
import com.gzlabs.utils.WidgetUtilities;

import org.eclipse.swt.widgets.MenuItem;

/**
 * Main window for the application
 * 
 * @author apavlune
 * 
 */
public class MainWindow implements IDisplayStatus, IDutyUpdater,
		IEHoursHandler, IShiftAdder, IDetailsManager,
		IEmployeeManager, ITimeOffManager, IConnectionStatus {

	protected Shell shell;
	private final FormToolkit formToolkit = new FormToolkit(
			Display.getDefault());
	private DataManager dman;
	private UploadManager drh;

	/************************************************************/
	// Main Window Container
	private TabFolder tabFolder;
	private Label lblStatus;
	private EmployeeHoursComposite employeeHoursComposite;
	private AddShiftComposite addShiftComposite;
	/************************************************************/

	/************************************************************/
	// Employee Tab
	private EmployeesWidget employeesWidget;
	/************************************************************/

	/************************************************************/
	// Positions Tab
	private TabItem tbtmDetails;

	/************************************************************/

	/************************************************************/
	// Details and Timeoff Tab
	private DetailsWidget detailsWidget;
	private TimeOffWidget timeOffWidget;

	/************************************************************/
	
	private volatile boolean isInitialized;
	private volatile boolean isError;

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
		isInitialized=false;	
		isError=false;
		
		dman = new DataManager(this, this, null);
		Thread worker=new Thread(dman);
		worker.setName("DataManager");
		worker.start();
		
		SplashShell splash=new SplashShell(display);
		splash.open();
		splash.layout();
		while (!isInitialized) {
			if (!display.readAndDispatch()) {

				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
			}
		}
		
		if(isError)
		{
			splash.activateError();
			while (!splash.isDisposed()) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}
		}
		else
		{
			splash.close();		
			drh = new UploadManager(dman.getProp(), this);
			createContents();
			shell.open();
			shell.layout();
			shell.setCursor(new Cursor(display, SWT.CURSOR_ARROW));
			while (!shell.isDisposed()) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setBackground(SWTResourceManager.getColor(51, 153, 153));
		shell.setImage(SWTResourceManager.getImage(MainWindow.class,
				"/com/gzlabs/gzroster/gui/1370647295_60814.ico"));
		shell.setSize(1420, 928);
		shell.setText("GZ Roster");
		shell.setLayout(null);

		Menu menu = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menu);

		MenuItem fileMenu = new MenuItem(menu, SWT.CASCADE);
		fileMenu.setText("File");

		Menu menu_2 = new Menu(fileMenu);
		fileMenu.setMenu(menu_2);

		MenuItem mntmUpload = new MenuItem(menu_2, SWT.NONE);

		mntmUpload.setText("Upload");

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

		MenuItem mntmPurge = new MenuItem(menu_3, SWT.NONE);

		MenuItem helpMenu = new MenuItem(menu, SWT.CASCADE);
		helpMenu.setText("Help");

		Menu menu_1 = new Menu(helpMenu);
		helpMenu.setMenu(menu_1);

		MenuItem helpAboutMenuItem = new MenuItem(menu_1, SWT.NONE);
		helpAboutMenuItem.setText("About");

		tabFolder = new TabFolder(shell, SWT.NONE);
		tabFolder.setBackground(SWTResourceManager.getColor(51, 153, 204));
		tabFolder.setBounds(10, 10, 900, 800);

		addShiftComposite = new AddShiftComposite(shell, SWT.NO_MERGE_PAINTS, this);
		addShiftComposite.setBounds(916, 38, 191, 336);
		formToolkit.adapt(addShiftComposite);
		formToolkit.paintBordersFor(addShiftComposite);

		createEmployeesTab();

		employeeHoursComposite = new EmployeeHoursComposite(shell, SWT.NONE,
				this);
		employeeHoursComposite.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_BACKGROUND_GRADIENT));
		employeeHoursComposite.setBounds(1120, 38, 227, 529);
		formToolkit.adapt(employeeHoursComposite);
		formToolkit.paintBordersFor(employeeHoursComposite);

		lblStatus = new Label(shell, SWT.NONE);
		lblStatus.setBackground(SWTResourceManager.getColor(SWT.COLOR_LIST_SELECTION));
		lblStatus.setBounds(10, 816, 1398, 18);
		formToolkit.adapt(lblStatus, true, true);
		lblStatus.setText("Status");
	
		createDetailsTab();
		populateData();

		final PurgeDataDialog pdd = new PurgeDataDialog(shell, SWT.NONE, drh,
				this);

		mntmPurge.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (pdd != null) {
					pdd.open();
				}
			}
		});

		final ConfigurationDialog conf_dialog = new ConfigurationDialog(shell,
				SWT.NONE, dman.getProp(), this);

		configurationMenuItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (conf_dialog != null) {
					conf_dialog.open();
				}
			}
		});
		mntmPurge.setText("Purge");

		mntmUpload.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				if (dman != null) {
					SSHStatusShell.launch(dman.getProp(),shell);
				}
			}
		});
		isInitialized=true;
	}

	/**
	 * Resets add shift controls
	 */
	protected void resetScheduleControls() {
		if (addShiftComposite != null) {
			addShiftComposite.resetControls();
		}
	}

	/*************************************************************************/
	/**
	 * Separated for readability.
	 */

	private void createDetailsTab() {
		tbtmDetails = new TabItem(tabFolder, SWT.NONE);
		tbtmDetails.setText("Schedule");

		detailsWidget = new DetailsWidget(tabFolder, SWT.NONE, this, this);
		detailsWidget.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_BACKGROUND_GRADIENT));
		detailsWidget.setLayout(new GridLayout(1, false));
		tbtmDetails.setControl(detailsWidget);
		
		TabItem tbtmTimeOff = new TabItem(tabFolder, SWT.NONE);
		tbtmTimeOff.setText("Time Off");
		
		timeOffWidget = new TimeOffWidget(tabFolder, SWT.NONE, this);
		timeOffWidget.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_BACKGROUND_GRADIENT));
		timeOffWidget.setLayout(new GridLayout(1, false));
		tbtmTimeOff.setControl(timeOffWidget);
	}

	/**
	 * Initiates details data
	 * 
	 * @param data
	 *            List of duties
	 */
	protected void updateDetailsData(ArrayList<Object> data) {
		if (detailsWidget != null && data != null) {
			detailsWidget.initiateData(data);
		}
	}
	
	protected void updateTimeOffData(ArrayList<Object> data)
	{
		if (timeOffWidget != null && data != null) {
			timeOffWidget.initiateData(data);
		}
	}

	/**
	 * Creates employees tab.
	 */
	protected void createEmployeesTab() {

		TabItem tbtmEmployees = new TabItem(tabFolder, SWT.NONE);
		tbtmEmployees.setText("Employees");

		Composite composite_1 = new Composite(tabFolder, SWT.NONE);
		composite_1.setBackground(SWTResourceManager.getColor(SWT.COLOR_LIST_SELECTION));
		tbtmEmployees.setControl(composite_1);
		composite_1.setBounds(0, 27, 900, 800);
		formToolkit.adapt(composite_1);
		formToolkit.paintBordersFor(composite_1);

		employeesWidget = new EmployeesWidget(composite_1, SWT.NONE, this);
		employeesWidget.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_BACKGROUND_GRADIENT));
		employeesWidget.setBounds(10, 10, 843, 408);
		
		formToolkit.adapt(employeesWidget);
		formToolkit.paintBordersFor(employeesWidget);
		
		employeesWidget.addElementsChangedListener(new ElementsChangedListener()
		{
			@Override
			public void elementsChanged(ElementsChangedEvent event) {	
				
				if(event.getType()==DBObjectType.POSITION)
				{
					rebuildDetailsWidget();
				}
				populateData();
			}
			
		});
		
	}

	/*************************************************************************/

	/**
	 * Populates objects with data
	 */
	public void populateData() {
		ArrayList<String> employees = dman.getElementsNames(DBObjectType.PERSON);

		if (addShiftComposite != null 
				&& employees != null) {
			addShiftComposite.clearControls();
			addShiftComposite.addEmployee(employees);
			
			employeeHoursComposite.clearTable();
			ArrayList<String> active_employees = dman.getActiveEmployees();
			for (String s : active_employees) {

				if (employeeHoursComposite != null) {					
					employeeHoursComposite.updateItem(s, dman
							.getTotalEmpoloyeeHours(s,
									employeeHoursComposite.getStartDate(),
									employeeHoursComposite.getEndDate()));
				}
			}

			ArrayList<String> positions = dman.getElementsNames(DBObjectType.POSITION);

			if (positions!=null) {

				Collections.sort(positions);
				for (String s : positions) {
					addShiftComposite.addPosition(s);			
				} 
			}
			
			ArrayList<Object> timespans = dman.getTimeSpan();
			
			if(timespans!=null)
			{
				for (Object s : timespans) {
					addShiftComposite.addStart((String)s);
				}
			}
			updateDetailsData(timespans);
			updateTimeOffData(dman.getAllTimesOff());
		}

	}

	/**
	 * Displays current status in a text area window.
	 */
	@Override
	public void DisplayStatus(String status) {		
		WidgetUtilities.safeLabelSet(lblStatus, status);
		System.out.println(status);
	}

	/**
	 * Displays error dialog
	 */
	@Override
	public void ShowErrorBox(String error) {
		MessageDialog.openError(shell, "Error", error);
	}

	/**
	 * Updates existing duty or inserts new one.
	 */
	@Override
	public void processDutyData() {
		if (addShiftComposite != null && addShiftComposite.checkTextLength() && dman!=null) {			

			if (dman.checkDutyConflict(addShiftComposite.getDutyDetails())) {
				DisplayStatus("Scheduling conflict!");
				if (!MessageDialog
						.openConfirm(shell, "Schedule Conflict",
								"This employee is already scheduled during this time period.  Add anyway?")) {
					DisplayStatus("Cancelled!");
					return;
				}
			}
			dman.addRecord(addShiftComposite.getDutyDetails(), DBObjectType.DUTY);
			populateData();
		} else {
			DisplayStatus("Both Employee and Position need to be selected!  Both Start and End times are required!");
		}
	}

	@Override
	public void dutyUpdateRequest(String label, String col_label,
			String row_label) {

		if (addShiftComposite != null && dman != null) {
			addShiftComposite.setUpd_person(label);
			addShiftComposite.setUpd_position(col_label);
			addShiftComposite.selectEmployee(label);
			addShiftComposite.selectPosition(col_label);

			Calendar start_time = dman.getDutyStart(label, col_label,
					addShiftComposite.getSelectedDate() + " " + row_label
							+ ":00.0");

			String start_str = null;

			Calendar end_time = dman.getDutyEnd(label, col_label,
					addShiftComposite.getSelectedDate() + " " + row_label
							+ ":00.0");

			String end_str = null;

			String zminute = "00";

			start_str = start_time.get(Calendar.HOUR_OF_DAY)
					+ ":"
					+ (start_time.get(Calendar.MINUTE) == 0 ? zminute : start_time
							.get(Calendar.MINUTE));

			end_str = end_time.get(Calendar.HOUR_OF_DAY)
					+ ":"
					+ (end_time.get(Calendar.MINUTE) == 0 ? zminute : end_time
							.get(Calendar.MINUTE));
			addShiftComposite.setEndEnabled(true);
			addShiftComposite.setUpd_start(start_str);
			addShiftComposite.selectStart(start_str);

			addShiftComposite.correlateEndTimeCombo();
			addShiftComposite.selectEnd(end_str);
			addShiftComposite.setButtonsToUpdate();
			addShiftComposite.enableEmployeePicker();
			isa_updateEmployeeList();
		}
	}

	@Override
	public void dutyDeleteRequest(String person, String position,
			String row_label, boolean update_request) {
		if (dman != null && addShiftComposite!=null) {
			Calendar start_date = dman.getDutyStart(person, position,
					addShiftComposite.getSelectedDate() + " " + row_label
							+ ":00.0");

			Calendar end_date = dman.getDutyEnd(person, position,
					addShiftComposite.getSelectedDate() + " " + row_label
							+ ":00.0");
			dman.deleteDuty(start_date, end_date, position, person);
			populateData();
		}

	}

	@Override
	public void dutyNewRequest(String col_label, String row_label) {

		if (addShiftComposite != null) {
			addShiftComposite.selectPosition(col_label);
			addShiftComposite.selectStart(row_label);
			addShiftComposite.correlateEndTimeCombo();
			isa_updateEmployeeList();
		}
	}

	@Override
	public void rangeChanged(String dateStringFromWidget,
			String dateStringFromWidget2) {
		if (dman != null) {
			ArrayList<String> employees = dman.getElementsNames(DBObjectType.PERSON);
			
			if(employees!=null && employeeHoursComposite!=null )
			{
				for (String s : employees) {
					employeeHoursComposite.updateItem(s, dman
							.getTotalEmpoloyeeHours(s,
									employeeHoursComposite.getStartDate(),
									employeeHoursComposite.getEndDate()));
				}
			}
		}

	}

	@Override
	public void isa_DateChaged(String new_date) {
		if (dman != null && employeeHoursComposite != null
				&& addShiftComposite != null) {
			updateDetailsData(dman.getTimeSpan());
			employeeHoursComposite.updateDates(new_date);
			isa_updateEmployeeList();
		}

	}

	@Override
	public ArrayList<Object> getTimeSpan() {
		if (dman != null) {
			return dman.getTimeSpan();
		}
		return null;

	}

	/**
	 * Refreshes details widget with new data
	 */
	private void rebuildDetailsWidget()
	{
		if(tbtmDetails!=null)
		{
			detailsWidget = new DetailsWidget(tabFolder, SWT.NONE, this, this);
			detailsWidget.setLayout(new GridLayout(1, false));
			tbtmDetails.setControl(detailsWidget);
		}
	}

	@Override
	public ArrayList<String> getPositions() {
		if (dman != null) {
			return dman.getElementsNames(DBObjectType.POSITION);
		}
		return null;
	}

	@Override
	public String getCellLabelString(String element, String column_label) {
		if (dman != null && addShiftComposite != null) {
			return dman.getCellLabelString((String) element, column_label,
					addShiftComposite.getSelectedDate());
		}
		return null;
	}

	@Override
	public void deleteItem(Object item, DBObjectType type) {
		if(item instanceof DB_Object)
		{
			if (dman != null) {
				dman.deleteRecord((DB_Object) item, type);
				employeeHoursComposite.removeItem(((DB_Object) item).getName());
			}
		}
	}

	@Override
	public void isa_updateEmployeeList() {
		if (addShiftComposite != null && dman != null) {
			// This is where we determine if an employee is allowed to work
			// this position
			String position = addShiftComposite.getSelectedPosition();
			String start = addShiftComposite.getSelectedStart();
			if (start!=null && start.length() == 0) {
				start = "00:00";
			}
			String end = addShiftComposite.getSelectedEnd();
			if (end!=null && end.length() == 0) {
				end = "00:00";
			}

			String start_dt = addShiftComposite.getSelectedDate() + " " + start
					+ ":00.0";
			String end_dt = addShiftComposite.getSelectedDate() + " " + end
					+ ":00.0";

			ArrayList<String> allowed_empl = dman.getAllowedEmployees(position,
					start_dt, end_dt);
			addShiftComposite.clearEmployees();
			addShiftComposite.addEmployee(allowed_empl);
			addShiftComposite.enableEmployeePicker();
			addShiftComposite.selectEmployee(addShiftComposite.getUpd_person());

		}
	}

	@Override
	public void refreshData() {
		if (dman != null) {
			dman.refreshData();
		}
		populateData();

	}

	@Override
	public void updateProperties(Properties prop) {
		if (dman != null && prop!=null) {
			dman.saveProp(prop);
			refreshData();
		}

	}

	@Override
	public synchronized void setInitialized()
	{
		isInitialized=true;
	}

	@Override
	public synchronized void setError() {
		isError=true;
		
	}

	@Override
	public void setPin(String pin, String name)
	{
		dman.setPin(pin, name);
	}

	@Override
	public ArrayList<String> getTimeOffStatusOptions() {
		if(dman!=null)
		{
			return dman.getTimeOffStatusOptions();
		}
		return null;
	}

	@Override
	public ArrayList<String> getNameOptions() {
		if(dman!=null)
		{
			return dman.getActiveEmployees();
		}
		return null;
	}

	@Override
	public void updateTimesOff(ArrayList<Object> timeOff) {
		if(dman!=null)
		{
			dman.updateTimesOff(timeOff);
		}
		
	}

	@Override
	public ArrayList<String> getData(DBObjectType type) {
		if(dman!=null)
		{
			return dman.getElementsNames(type);
		}
		return null;
	}

	@Override
	public Object getItem(String selection,  DBObjectType type) {		
		if(dman!=null)
		{
			return dman.getObjectByName(selection, type);
		}
		return null;
	}

	@Override
	public void updateObject(Object oldObj, Object newObj, DBObjectType type) {
		if(newObj instanceof DB_Object)
		{
			if (dman != null) {
				dman.updateRecord((DB_Object)oldObj, (DB_Object)newObj, type);
			}
		}
		
	}

	@Override
	public void insertObject(Object newObj, DBObjectType type) {
		if(newObj instanceof DB_Object)
		{
			if (dman != null) {
				dman.addRecord((DB_Object) newObj, type);
			}
		}
		
	}
}
