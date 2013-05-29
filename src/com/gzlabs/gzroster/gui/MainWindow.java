package com.gzlabs.gzroster.gui;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.List;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.TextViewer;

import com.gzlabs.drosterheper.IDisplayStatus;
import com.gzlabs.gzroster.data.DataManager;
import com.gzlabs.gzroster.data.DateUtils;
import com.gzlabs.gzroster.data.DutiesManager;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.jface.viewers.TreeViewer;

public class MainWindow implements IDisplayStatus{

	private static final int DAYS_IN_THE_WEEK = 7;
	
	
	protected Shell shell;
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private Table table;
	private StyledText statusWindow;
	private DataManager dman;
	private Properties prop;
	
	private Combo scheduleEmployeeCombo;
	private Combo schedulePositionCombo; 
	
	private boolean widgetsready=false;
	
	
	/************************************************************/
	//Main Window Container
	private TabFolder tabFolder;
	/************************************************************/
	
	/************************************************************/
	//Schedule Tab
	private DateTime dateTime;
	private TableItem dateRow;
	private TableColumn[] weekDayColumns;
	private DateTime scheduleDate;
	private Combo startTimeCombo;
	private Combo endTimeCombo;
	/************************************************************/
	
	
	
	/************************************************************/
	//Employee Tab
	private Text nameText;
	private Text addressText;
	private Text homephoneText;
	private Text mobilePhoneText;
	private Text webNameText;
	private Text webPasswordText;	
	
	private List employeesList;
	private Button employeesDeleteButton;
	private Button employeesEditButton;
	private Button btnClear;
	private Button btnEmployeeCancel;
	
	private Label lblWebName;
	private Label lblWebPassword;
	private Label lblName;
	private Label lblAddress;
	private Label lblMobilePhone;
	private Label lblHomePhone;
	
	private Button activeCheck;
	/************************************************************/
	
	/************************************************************/
	//Positions Tab
	private List positionsList;
	
	private Button positionsDeleteButton;
	private Button positionsEditButton;
	private Button positionsClearButton;
	private Button positionsCancelButton;
	
	private Text positionNameText;
	private Text positionNoteText;
	
	private Label positionLblName;
	private Label positionLblNote;
	private TabItem tbtmDetails;
	
	/************************************************************/
	
	/************************************************************/
	//Details Tab
	private DetailsTableViewer tv;
	private DutiesManager dutiesman;
	private TabItem tbtmAddShift;
	/************************************************************/

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
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
		shell.setSize(1024, 789);
		shell.setText("SWT Application");
		shell.setLayout(null);
		
		Menu menu = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menu);
		
		tabFolder = new TabFolder(shell, SWT.NONE);
		tabFolder.setBounds(10, 10, 1000, 557);			
		
		createScheduleTab();
		createEmployeesTab();
		createPositionsTab();
		
		TextViewer textViewer = new TextViewer(shell, SWT.BORDER);
		statusWindow = textViewer.getTextWidget();
		statusWindow.setBounds(10, 573, 778, 160);
		formToolkit.paintBordersFor(statusWindow);
		getProp();
		dman = new DataManager(prop, this); 
		dutiesman=new DutiesManager(prop);
		createDetailsTab();
		populateData();
		widgetsready=true;
	}

	/*************************************************************************/
	/**
	 * Separated for readability.
	 */
	protected void createScheduleTab()
	{
		TabItem tbtmSchedule = new TabItem(tabFolder, SWT.NONE);
		tbtmSchedule.setText("Schedule");
		
		Composite composite_2 = new Composite(tabFolder, SWT.NONE);
		tbtmSchedule.setControl(composite_2);
		formToolkit.paintBordersFor(composite_2);
		composite_2.setLayout(null);
		
		scheduleEmployeeCombo = new Combo(composite_2, SWT.NONE);
		scheduleEmployeeCombo.setBounds(347, 44, 150, 30);
		formToolkit.adapt(scheduleEmployeeCombo);
		formToolkit.paintBordersFor(scheduleEmployeeCombo);
		
		schedulePositionCombo = new Combo(composite_2, SWT.NONE);
		schedulePositionCombo.setBounds(347, 101, 150, 30);
		formToolkit.adapt(schedulePositionCombo);
		formToolkit.paintBordersFor(schedulePositionCombo);
		
		Button scheduleAddButton = new Button(composite_2, SWT.NONE);
		scheduleAddButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				processDutyData();
			}
		});
		scheduleAddButton.setBounds(609, 101, 88, 30);
		formToolkit.adapt(scheduleAddButton, true, true);
		scheduleAddButton.setText("Add");
		
		scheduleDate = new DateTime(composite_2, SWT.BORDER | SWT.CALENDAR | SWT.SHORT);
		scheduleDate.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				updateDetailsData(dutiesman.getTimeSpan());
			}
		});
		scheduleDate.setBounds(78, 0, 250, 196);
		formToolkit.adapt(scheduleDate);
		formToolkit.paintBordersFor(scheduleDate);
		
		Label lblEmployee = new Label(composite_2, SWT.NONE);
		lblEmployee.setBounds(347, 20, 69, 18);
		formToolkit.adapt(lblEmployee, true, true);
		lblEmployee.setText("Employee");
		
		Label lblPosition = new Label(composite_2, SWT.NONE);
		lblPosition.setBounds(347, 77, 69, 18);
		lblPosition.setText("Position");
		formToolkit.adapt(lblPosition, true, true);
		
		Label lblStartTime = new Label(composite_2, SWT.NONE);
		lblStartTime.setBounds(503, 20, 69, 18);
		formToolkit.adapt(lblStartTime, true, true);
		lblStartTime.setText("Start Time");
		
		Label lblEndTime = new Label(composite_2, SWT.NONE);
		lblEndTime.setBounds(503, 76, 69, 18);
		formToolkit.adapt(lblEndTime, true, true);
		lblEndTime.setText("End Time");
		
		table = new Table(composite_2, SWT.BORDER | SWT.FULL_SELECTION);
		table.setBounds(10, 255, 752, 169);
		formToolkit.adapt(table);
		formToolkit.paintBordersFor(table);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
				
		weekDayColumns=new TableColumn[DAYS_IN_THE_WEEK];
		for(int i=0;i<weekDayColumns.length; i++)
		{
			weekDayColumns[i] = new TableColumn(table, SWT.CENTER);
		//	weekDayColumns[i].setResizable(false);
			weekDayColumns[i].setWidth(100);
		}
		weekDayColumns[0].setText("Sunday");
		weekDayColumns[1].setText("Monday");
		weekDayColumns[2].setText("Tuesday");
		weekDayColumns[3].setText("Wednesday");
		weekDayColumns[4].setText("Thursday");
		weekDayColumns[5].setText("Friday");
		weekDayColumns[6].setText("Saturday");
		
		
		DateUtils dateutils=new DateUtils();
		dateRow = new TableItem(table, SWT.NONE);
		String [] days=new String[DAYS_IN_THE_WEEK];
		for(int j=0; j<days.length;j++)
		{
			days[j]=dateutils.getWeekDayDate(j+1);
		}
		
		dateRow.setText(days);
		dateRow.setFont(SWTResourceManager.getFont("Arial", 8, SWT.NORMAL));
		
		Button btnNext = new Button(composite_2, SWT.NONE);
		btnNext.setBounds(580, 219, 88, 30);
		formToolkit.adapt(btnNext, true, true);
		btnNext.setText("Next >");
		
		Button btnNewButton = new Button(composite_2, SWT.NONE);
		btnNewButton.setBounds(674, 219, 88, 30);
		formToolkit.adapt(btnNewButton, true, true);
		btnNewButton.setText("Last >>");
		
		Button button = new Button(composite_2, SWT.NONE);
		button.setBounds(10, 219, 88, 30);
		formToolkit.adapt(button, true, true);
		button.setText("<<First");
		
		Button button_1 = new Button(composite_2, SWT.NONE);
		button_1.setBounds(104, 219, 88, 30);
		formToolkit.adapt(button_1, true, true);
		button_1.setText("<Previous");
		
		Label lblWeekOf = new Label(composite_2, SWT.NONE);
		lblWeekOf.setBounds(330, 196, 88, 18);
		formToolkit.adapt(lblWeekOf, true, true);
		lblWeekOf.setText("Week of");
		
		dateTime = new DateTime(composite_2, SWT.BORDER);
		dateTime.setBounds(314, 220, 128, 29);
		dateTime.setFont(SWTResourceManager.getFont("Arial", 10, SWT.NORMAL));
		formToolkit.adapt(dateTime);
		formToolkit.paintBordersFor(dateTime);
		
		startTimeCombo = new Combo(composite_2, SWT.NONE);
		startTimeCombo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				if(endTimeCombo!=null && dutiesman!=null)
				{
					endTimeCombo.removeAll();
					endTimeCombo.setEnabled(true);
					ArrayList <String> timespans=dutiesman.getTimeSpan();							
					int index=startTimeCombo.getSelectionIndex();
					
					for(int i=index+1; i<timespans.size();i++)
					{
						endTimeCombo.add(timespans.get(i));
					}
				}
			}
		});
		startTimeCombo.setBounds(503, 44, 103, 30);
		formToolkit.adapt(startTimeCombo);
		formToolkit.paintBordersFor(startTimeCombo);
		
		endTimeCombo = new Combo(composite_2, SWT.NONE);
		endTimeCombo.setEnabled(false);
		endTimeCombo.setBounds(503, 101, 103, 30);
		formToolkit.adapt(endTimeCombo);
		formToolkit.paintBordersFor(endTimeCombo);
	}
	private void createDetailsTab()
	{
		tbtmDetails = new TabItem(tabFolder, SWT.NONE);
		tbtmDetails.setText("Details");
		
		Composite detailsComposite = new Composite(tabFolder, SWT.NONE);
		detailsComposite.setLayout(new GridLayout(1, false));
		
		tbtmDetails.setControl(detailsComposite);
		
		tv = new DetailsTableViewer(detailsComposite);
		final ArrayList <String> cols=dman.getPositions();
		cols.add(0, "Time");
		tv.initiateColumns(cols);
		tv.setLabelProvider(0, new ColumnLabelProvider(){
			public String getText(Object element)
			{
				return (String)element;
			}
		});		
		
		for(int i=1; i<cols.size(); i++)
		{
			HashMap<String, String> posdetails=dman.getPositionDetails(cols.get(i));
			final String col_id=posdetails.get("PLACE_ID");
			
			tv.setLabelProvider(i, new ColumnLabelProvider(){
				public String getText(Object element)
				{
					String time=(String)element;							
					String date=dateStringFromWidget(scheduleDate, null)+" "+time+":00.0";
					
 					ArrayList<String> persons_on_duty=null;
					if(dman!=null)
					{
						persons_on_duty=dman.isDutyOn(date, col_id);
					}
					
					if(persons_on_duty!=null && persons_on_duty.size()>0)
					{
						return persons_on_duty.toString();
					}												
					return "X";
				}
			}
			);
		}		
	}
	
	protected void updateDetailsData(ArrayList<String> data)
	{
		if(tv!=null)
		{
			tv.initiateData(data);
		}
	}
	
	
	protected void createEmployeesTab()
	{
		
		tbtmAddShift = new TabItem(tabFolder, SWT.NONE);
		tbtmAddShift.setText("Add Shift");
		
		
		TabItem tbtmEmployees = new TabItem(tabFolder, SWT.NONE);
		tbtmEmployees.setText("Employees");
		
		Composite composite = new Composite(tabFolder, SWT.NONE);
		tbtmEmployees.setControl(composite);
		formToolkit.paintBordersFor(composite);
		
		ListViewer employeesListViewer = new ListViewer(composite, SWT.BORDER | SWT.V_SCROLL);
		employeesList = employeesListViewer.getList();
		employeesList.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(!widgetsready)
					return;
				if(employeesList.getSelectionIndex()==-1)
				{
					employeesDeleteButton.setEnabled(false);
				}
				else
				{
					employeesDeleteButton.setEnabled(true);
					clearEmployeeData();
					setEmployeeDetails(dman.getEmployeeDetails
							(employeesList.getSelection()[0]));					
					toggleEmployeeEdit(true);
				}
			}

		});
		employeesList.setBounds(10, 46, 143, 248);
		
		employeesEditButton = formToolkit.createButton(composite, "Save", SWT.NONE);
		employeesEditButton.setEnabled(false);
		employeesEditButton.setImage(SWTResourceManager.getImage(MainWindow.class, "/javax/swing/plaf/metal/icons/ocean/floppy.gif"));
		employeesEditButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				processEmployeeData();
			}
		});
		employeesEditButton.setBounds(471, 256, 88, 30);
		
		employeesDeleteButton = formToolkit.createButton(composite, "Delete", SWT.NONE);
		employeesDeleteButton.setEnabled(false);
		employeesDeleteButton.setImage(SWTResourceManager.getImage(MainWindow.class, "/org/eclipse/jface/dialogs/images/message_error.gif"));
		employeesDeleteButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(!widgetsready)
					return;
				
				if(MessageDialog.openConfirm(shell, "Confirm Delete", "Are you sure that you want to delete this employee?"))
				{
						dman.deleteEmployee(employeesList.getSelection());
						populateData();
				}
			}
		});
		employeesDeleteButton.setBounds(10, 300, 88, 30);
		
		Button btnNew = new Button(composite, SWT.NONE);
		btnNew.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				toggleEmployeeEdit(true);
				clearEmployeeData();
				employeesList.deselectAll();
			}
		});
		btnNew.setImage(SWTResourceManager.getImage(MainWindow.class, "/javax/swing/plaf/metal/icons/ocean/file.gif"));
		btnNew.setBounds(10, 10, 88, 30);
		formToolkit.adapt(btnNew, true, true);
		btnNew.setText("New");
		
		nameText = new Text(composite, SWT.BORDER);
		nameText.setEnabled(false);
		nameText.setBounds(168, 46, 130, 28);
		formToolkit.adapt(nameText, true, true);
		
		lblName = new Label(composite, SWT.NONE);
		lblName.setEnabled(false);
		lblName.setBounds(168, 22, 69, 18);
		formToolkit.adapt(lblName, true, true);
		lblName.setText("Name");
		
		addressText = new Text(composite, SWT.BORDER | SWT.MULTI);
		addressText.setEnabled(false);
		addressText.setBounds(326, 46, 233, 86);
		formToolkit.adapt(addressText, true, true);
		
		lblAddress = new Label(composite, SWT.NONE);
		lblAddress.setEnabled(false);
		lblAddress.setText("Address");
		lblAddress.setBounds(326, 22, 69, 18);
		formToolkit.adapt(lblAddress, true, true);
		
		homephoneText = new Text(composite, SWT.BORDER);
		homephoneText.setEnabled(false);
		homephoneText.setBounds(168, 104, 130, 28);
		formToolkit.adapt(homephoneText, true, true);
		
		lblHomePhone = new Label(composite, SWT.NONE);
		lblHomePhone.setEnabled(false);
		lblHomePhone.setText("Home Phone");
		lblHomePhone.setBounds(168, 80, 130, 18);
		formToolkit.adapt(lblHomePhone, true, true);
		
		lblMobilePhone = new Label(composite, SWT.NONE);
		lblMobilePhone.setEnabled(false);
		lblMobilePhone.setText("Mobile Phone");
		lblMobilePhone.setBounds(168, 138, 130, 18);
		formToolkit.adapt(lblMobilePhone, true, true);
		
		mobilePhoneText = new Text(composite, SWT.BORDER);
		mobilePhoneText.setEnabled(false);
		mobilePhoneText.setBounds(168, 162, 130, 28);
		formToolkit.adapt(mobilePhoneText, true, true);
		
		activeCheck = new Button(composite, SWT.CHECK);
		activeCheck.setEnabled(false);
		activeCheck.setSelection(true);
		activeCheck.setBounds(326, 168, 111, 22);
		formToolkit.adapt(activeCheck, true, true);
		activeCheck.setText("Active?");
		
		webNameText = new Text(composite, SWT.BORDER);
		webNameText.setEnabled(false);
		webNameText.setBounds(168, 222, 130, 28);
		formToolkit.adapt(webNameText, true, true);
		
		lblWebName = new Label(composite, SWT.NONE);
		lblWebName.setEnabled(false);
		lblWebName.setText("Web Name");
		lblWebName.setBounds(168, 198, 130, 18);
		formToolkit.adapt(lblWebName, true, true);
		
		lblWebPassword = new Label(composite, SWT.NONE);
		lblWebPassword.setEnabled(false);
		lblWebPassword.setText("Web Password");
		lblWebPassword.setBounds(326, 198, 130, 18);
		formToolkit.adapt(lblWebPassword, true, true);
		
		webPasswordText = new Text(composite, SWT.BORDER | SWT.PASSWORD);
		webPasswordText.setEnabled(false);
		webPasswordText.setBounds(326, 222, 130, 28);
		formToolkit.adapt(webPasswordText, true, true);
		
		btnClear = new Button(composite, SWT.NONE);
		btnClear.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				clearEmployeeData();
			}
		});
		btnClear.setEnabled(false);
		btnClear.setImage(SWTResourceManager.getImage(MainWindow.class, "/com/sun/java/swing/plaf/motif/icons/Error.gif"));
		btnClear.setBounds(166, 256, 88, 30);
		formToolkit.adapt(btnClear, true, true);
		btnClear.setText("Clear");
		
		btnEmployeeCancel = new Button(composite, SWT.NONE);
		btnEmployeeCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				clearEmployeeData();
				toggleEmployeeEdit(false);
			}
		});
		btnEmployeeCancel.setEnabled(false);
		btnEmployeeCancel.setImage(SWTResourceManager.getImage(MainWindow.class, "/javax/swing/plaf/metal/icons/ocean/close.gif"));
		btnEmployeeCancel.setBounds(313, 256, 88, 30);
		formToolkit.adapt(btnEmployeeCancel, true, true);
		btnEmployeeCancel.setText("Cancel");	
	}
	
	protected void createPositionsTab()
	{
		TabItem tbtmPositions = new TabItem(tabFolder, SWT.NONE);
		tbtmPositions.setText("Positions");
		
		Composite positionsComposite = new Composite(tabFolder, SWT.NONE);
		tbtmPositions.setControl(positionsComposite);
		formToolkit.paintBordersFor(positionsComposite);
		
		ListViewer positionsListViewer = new ListViewer(positionsComposite, SWT.BORDER | SWT.V_SCROLL);
		positionsList = positionsListViewer.getList();
		positionsList.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(!widgetsready)
					return;
				if(positionsList.getSelectionIndex()==-1)
				{
					positionsDeleteButton.setEnabled(false);
				}
				else
				{
					positionsDeleteButton.setEnabled(true);
					clearPositionData();
					setPositionDetails(dman.getPositionDetails
							(positionsList.getSelection()[0]));					
					togglePositionEdit(true);
				}
			}

		});
		positionsList.setBounds(10, 46, 143, 248);
		
		positionsEditButton = formToolkit.createButton(positionsComposite, "Save", SWT.NONE);
		positionsEditButton.setEnabled(false);
		positionsEditButton.setImage(SWTResourceManager.getImage(MainWindow.class, "/javax/swing/plaf/metal/icons/ocean/floppy.gif"));
		positionsEditButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				processPositionData();
				clearPositionData();
				populateData();
			}
		});
		positionsEditButton.setBounds(471, 256, 88, 30);
		
		positionsDeleteButton = formToolkit.createButton(positionsComposite, "Delete", SWT.NONE);
		positionsDeleteButton.setEnabled(false);
		positionsDeleteButton.setImage(SWTResourceManager.getImage(MainWindow.class, "/org/eclipse/jface/dialogs/images/message_error.gif"));
		positionsDeleteButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(!widgetsready)
					return;
				
				if(MessageDialog.openConfirm(shell, "Confirm Delete", "Are you sure that you want to delete this position?"))
				{
						dman.deletePosition(positionsList.getSelection());
						clearPositionData();
						populateData();
				}
			}
		});
		positionsDeleteButton.setBounds(10, 300, 88, 30);
		
		Button postionNew = new Button(positionsComposite, SWT.NONE);
		postionNew.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				togglePositionEdit(true);
				clearPositionData();
				positionsList.deselectAll();
			}
		});
		postionNew.setImage(SWTResourceManager.getImage(MainWindow.class, "/javax/swing/plaf/metal/icons/ocean/file.gif"));
		postionNew.setBounds(10, 10, 88, 30);
		formToolkit.adapt(postionNew, true, true);
		postionNew.setText("New");
		
		positionNameText = new Text(positionsComposite, SWT.BORDER);
		positionNameText.setEnabled(false);
		positionNameText.setBounds(168, 46, 130, 28);
		formToolkit.adapt(positionNameText, true, true);
		
		positionLblName = new Label(positionsComposite, SWT.NONE);
		positionLblName.setEnabled(false);
		positionLblName.setBounds(168, 22, 69, 18);
		formToolkit.adapt(positionLblName, true, true);
		positionLblName.setText("Name");

		positionNoteText = new Text(positionsComposite, SWT.BORDER | SWT.MULTI);
		positionNoteText.setEnabled(false);
		positionNoteText.setBounds(326, 46, 233, 86);
		formToolkit.adapt(positionNoteText, true, true);
		
		positionLblNote = new Label(positionsComposite, SWT.NONE);
		positionLblNote.setEnabled(false);
		positionLblNote.setText("Note");
		positionLblNote.setBounds(326, 22, 69, 18);
		formToolkit.adapt(positionLblNote, true, true);

		positionsClearButton = new Button(positionsComposite, SWT.NONE);
		positionsClearButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				clearPositionData();
			}
		});
		positionsClearButton.setEnabled(false);
		positionsClearButton.setImage(SWTResourceManager.getImage(MainWindow.class, "/com/sun/java/swing/plaf/motif/icons/Error.gif"));
		positionsClearButton.setBounds(166, 256, 88, 30);
		formToolkit.adapt(positionsClearButton, true, true);
		positionsClearButton.setText("Clear");
		
		positionsCancelButton = new Button(positionsComposite, SWT.NONE);
		positionsCancelButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				clearPositionData();
				togglePositionEdit(false);
			}
		});
		positionsCancelButton.setEnabled(false);
		positionsCancelButton.setImage(SWTResourceManager.getImage(MainWindow.class, "/javax/swing/plaf/metal/icons/ocean/close.gif"));
		positionsCancelButton.setBounds(313, 256, 88, 30);
		formToolkit.adapt(positionsCancelButton, true, true);
		positionsCancelButton.setText("Cancel");
		
		
	}
	/*************************************************************************/
	
	/**
	 * Populates objects with data
	 */
	private void populateData()
	{
		ArrayList <String> employees=dman.getEmployees();
		employeesList.removeAll();
		scheduleEmployeeCombo.removeAll();
		for(String s : employees)
		{
			employeesList.add(s);
			scheduleEmployeeCombo.add(s);
		}
		
		ArrayList <String> positions=dman.getPositions();
		positionsList.removeAll();
		schedulePositionCombo.removeAll();
		for(String s : positions)
		{
			positionsList.add(s);
			schedulePositionCombo.add(s);
		}
		endTimeCombo.setEnabled(false);
		ArrayList <String> timespans=dutiesman.getTimeSpan();
		
		startTimeCombo.removeAll();
		for(String s: timespans)
		{
			startTimeCombo.add(s);
		}
		updateDetailsData(timespans);
		
	}
	
	/**
	 * Loads properties file.
	 * 
	 * @param ids
	 *            Information display interface.
	 * @return null if something went wrong. Properties loaded from file
	 *         otherwise.
	 */
	public Properties getProp() {
		DisplayStatus("Loading configuration...");
		prop = null;
		try {
			prop = new Properties();
			prop.load(new FileInputStream("GZRoster.config"));
		} catch (Exception e) {
			DisplayStatus("Unable to load configuration file!");
		}
		DisplayStatus("Done...");
		return prop;
	}

	/**
	 * Displays current status in a text area window.
	 */
	@Override
	public void DisplayStatus(String status) {
		statusWindow.append(status+"\n");		
	}
	
	/**
	 * Displays error dialog
	 */
	@Override
	public void ShowErrorBox (String error)
	{
		MessageDialog.openError(shell, "Error", error);
	}	
	
	/**
	 * Enables and disables employee edit controls.
	 * @param enable Specify to enable or disable
	 */
	private void toggleEmployeeEdit(boolean enable)	
	{
		employeesEditButton.setEnabled(enable);
		btnClear.setEnabled(enable);
		btnEmployeeCancel.setEnabled(enable);
		
		nameText.setEnabled(enable);
		addressText.setEnabled(enable);
		homephoneText.setEnabled(enable);
		mobilePhoneText.setEnabled(enable);
		webNameText.setEnabled(enable);
		webPasswordText.setEnabled(enable);
		
		lblWebName.setEnabled(enable);
		lblWebPassword.setEnabled(enable);
		lblName.setEnabled(enable);
		lblAddress.setEnabled(enable);
		lblMobilePhone.setEnabled(enable);
		lblHomePhone.setEnabled(enable);
		
		activeCheck.setEnabled(enable);
	}
	
	/**
	 * Enables and disables position edit controls.
	 * @param enable Specify to enable or disable
	 */
	private void togglePositionEdit(boolean enable)	
	{
		positionsEditButton.setEnabled(enable);
		positionsClearButton.setEnabled(enable);
		positionsCancelButton.setEnabled(enable);
		
		positionNameText.setEnabled(enable);
		positionNoteText.setEnabled(enable);
		
		positionLblName.setEnabled(enable);
		positionLblNote.setEnabled(enable);		
	}
	
	
	/**
	 * Clears employee data controls
	 */
	private void clearEmployeeData()
	{
		nameText.setText("");
		addressText.setText("");
		homephoneText.setText("");
		mobilePhoneText.setText("");
		webNameText.setText("");
		webPasswordText.setText("");
	}
	

	/**
	 * Clears position data controls
	 */
	private void clearPositionData()
	{
		positionNameText.setText("");
		positionNoteText.setText("");
	}
	
	/**
	 * Sets employee details to the specified HashMap values
	 * @param details HashMap with values.
	 */
	private void setEmployeeDetails(HashMap<String, String> details) {
		if(details!=null)
		{
			String value=(String) details.get("PERSON_NAME");
			if(value != null)
			{
				nameText.setText(value);
			}
			
			value=(String) details.get("ADDRESS");
			if(value != null)
			{
				addressText.setText(value);
			}
			
			value=(String) details.get("PHONE_HOME");
			if(value != null)
			{
				homephoneText.setText(value);
			}
			
			value=(String) details.get("PHONE_MOBILE");
			if(value != null)
			{
				mobilePhoneText.setText(value);
			}
			
			value=(String) details.get("WEB_NAME");
			if(value != null)
			{
				webNameText.setText(value);
			}
			
			value=(String) details.get("WEB_PASSWORD");
			if(value != null)
			{
				webPasswordText.setText(value);
			}	
			
			value=(String) details.get("ACTIVE_PERSON");
			if(value !=null && value.equals("1"))
			{
				activeCheck.setSelection(true);
			}
			else
			{
				activeCheck.setSelection(false);
			}
			
		}
	}
	
	/**
	 * Sets position details to the specified HashMap values
	 * @param details HashMap with values.
	 */
	private void setPositionDetails(HashMap<String, String> details) {
		if(details!=null)
		{
			String value=(String) details.get("PLACE_NAME");
			if(value != null)
			{
				positionNameText.setText(value);
			}
			
			value=(String) details.get("NOTE");
			if(value != null)
			{
				positionNoteText.setText(value);
			}							
		}
	}

	/**
	 * Updates existing employee or inserts new one.
	 */
	private void processEmployeeData()
	{			
		HashMap<String, String> details=new HashMap<String, String>();
		details.put("PERSON_NAME", nameText.getText());
		details.put("ADDRESS", addressText.getText());
		details.put("PHONE_HOME", homephoneText.getText());
		details.put("PHONE_MOBILE", mobilePhoneText.getText());
		details.put("WEB_NAME", webNameText.getText());
		details.put("WEB_PASSWORD", webPasswordText.getText());
		details.put("ACTIVE_PERSON", activeCheck.getSelection()?"1":"0");
		if(employeesList.getSelectionIndex()>=0)
		{
			dman.updateEmployee(details);
		}
		else
		{
			dman.addEmployee(details);
		}
		populateData();
	}
	
	/**
	 * Updates existing duty or inserts new one.
	 */
	private void processDutyData()
	{
		if(schedulePositionCombo.getText().length()>0 &&
				scheduleEmployeeCombo.getText().length()>0 &&
				startTimeCombo.getText().length()>0 &&
				endTimeCombo.getText().length()>0)
		{
			HashMap<String, String> details=new HashMap<String, String>();
			details.put("DUTY_START_TIME",dateStringFromWidget(scheduleDate, null)+" "+startTimeCombo.getText()+":00.0");
			details.put("DUTY_END_TIME", dateStringFromWidget(scheduleDate, null)+" "+endTimeCombo.getText()+":00.0");
			details.put("PLACE_ID", schedulePositionCombo.getText());
			details.put("PERSON_ID", scheduleEmployeeCombo.getText());
			details.put("APPROVED", "Y");
			
			dman.addDuty(details);
			populateData();
		}
		else
		{
			DisplayStatus("Both Employee and Position need to be selected!  Both Start and End times are required!");
		}
	}
	
	
	/**
	 * Updates existing position or inserts new one.
	 */
	private void processPositionData()
	{			
		HashMap<String, String> details=new HashMap<String, String>();
		details.put("PLACE_NAME", positionNameText.getText());
		details.put("NOTE", positionNoteText.getText());
		if(positionsList.getSelectionIndex()>=0)
		{
			dman.updatePosition(details);
		}
		else
		{
			dman.addPosition(details);
		}
	}
	
	private String dateStringFromWidget(DateTime date, DateTime time)
	{
		String retval="";
		if(date!=null)
		{
			String month=String.format("%02d", date.getMonth()+1);
			String day=String.format("%02d", date.getDay());
			retval=date.getYear()+"-"+month+"-"+day;
		}
		
		if(time!=null)
		{
			String hour=String.format("%02d", time.getHours());
			String minute=String.format("%02d", time.getMinutes());
			String second=String.format("%02d", time.getSeconds());
			
			retval+=" "+hour+":"+minute+":"+second+".0";
		}
		return retval;
	}
}

