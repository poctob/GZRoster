package com.gzlabs.gzroster.gui;

import java.util.Properties;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.SWT;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Button;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class ConfigurationDialog extends Dialog {

	protected Object result;
	protected Shell shlConfigurationDialog;
	private Properties prop;
	private IDetailsManager idm;
	
	private final FormToolkit m_toolkit = new FormToolkit(Display.getDefault());
	private ScrolledForm m_form;
	private Composite m_composite;
	private Label lblDbDriver;
	private Label lblDbUrl;
	private Label lblNewLabel;
	private Label lblPassword;
	private Text dbDriverText;
	private Text dbURLText;
	private Text dbUserNameText;
	private Text dbPasswordText;
	private Section server_section;
	private Label lblNewLabel_1;
	private Label lblNewLabel_2;
	private Label lblNewLabel_3;
	private Label lblNewLabel_4;
	private Label lblNewLabel_5;
	private Text serverHostText;
	private Text serverPortServer;
	private Text serverUserNameText;
	private Text serverPasswordText;
	private Text serverFilePathText;
	private Section ical_section;
	private Composite ical_composite;
	private Label lblFilePath;
	private Text icsPathText;
	private Button iscPathButton;
	private Section sctnAutoPurge;
	private Composite composite;
	private Button autoPurgeCheck;
	private Label lblNewLabel_6;
	private Text autoPurgeDaysText;
	private Label lblDays;
	private Section sctnDisplay;
	private Composite composite_1;
	private Label lblDayStart;
	private Label lblDayEnd;
	private Combo displayDayStartCombo;
	private Combo displayDayEndCombo;
	private Label lblInterval;
	private Combo displayIntervalCombo;
	private Section db_section;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public ConfigurationDialog(Shell parent, int style, Properties prop, IDetailsManager idm) {
		super(parent, style);
		setText("Configuration");
		this.prop=prop;
		this.idm=idm;
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		createContents();
		shlConfigurationDialog.open();
		shlConfigurationDialog.layout();
		Display display = getParent().getDisplay();
		while (!shlConfigurationDialog.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}
	
	private void clearData()
	{
		dbDriverText.setText("");
		dbURLText.setText("");
		dbUserNameText.setText("");
		dbPasswordText.setText("");
		
		serverHostText.setText("");
		serverPortServer.setText("");
		serverUserNameText.setText("");
		serverPasswordText.setText("");
		serverFilePathText.setText("");
		
		icsPathText.setText("");
		
		autoPurgeCheck.setSelection(false);
		
		autoPurgeDaysText.setText("");
		
		displayDayStartCombo.clearSelection();
		displayDayEndCombo.clearSelection();
		displayIntervalCombo.clearSelection();
	}
	
	private void expandAll()
	{
		server_section.setExpanded(true);
		ical_section.setExpanded(true);
		sctnAutoPurge.setExpanded(true);
		sctnDisplay.setExpanded(true);
		db_section.setExpanded(true);
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shlConfigurationDialog = new Shell(getParent(), SWT.SHELL_TRIM | SWT.BORDER);
		shlConfigurationDialog.setSize(861, 585);
		shlConfigurationDialog.setText("Configuration Dialog");		
		shlConfigurationDialog.setLayout(new FormLayout());
		
		m_composite = 	new Composite(shlConfigurationDialog, SWT.NONE);
		m_composite.setLayout(new FormLayout());
		FormData fd_m_composite = new FormData();
		fd_m_composite.top = new FormAttachment(0);
		fd_m_composite.left = new FormAttachment(0);
		fd_m_composite.bottom = new FormAttachment(0, 545);
		fd_m_composite.right = new FormAttachment(0, 849);
		m_composite.setLayoutData(fd_m_composite);
		m_composite.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_BACKGROUND_GRADIENT));
		m_toolkit.adapt(m_composite);
		m_toolkit.paintBordersFor(m_composite);
		
		m_form=m_toolkit.createScrolledForm(m_composite);
		FormData fd_m_form = new FormData();
		fd_m_form.right = new FormAttachment(0, 839);
		fd_m_form.bottom = new FormAttachment(0, 499);
		fd_m_form.top = new FormAttachment(0);
		fd_m_form.left = new FormAttachment(0);
		m_form.setLayoutData(fd_m_form);
		m_form.setText("Configuration Data");
		m_form.getBody().setLayout(new GridLayout(2, false));
		db_section=m_toolkit.createSection(m_form.getBody(),Section.DESCRIPTION | Section.TWISTIE | Section.TITLE_BAR);
		GridData gd_db_section = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
		gd_db_section.widthHint = 394;
		db_section.setLayoutData(gd_db_section);
		
		db_section.setText("Database");
		db_section.setExpanded(true);
		db_section.setDescription("Database configuration data.");
		Composite db_composite=m_toolkit.createComposite(db_section);
		db_composite.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_BACKGROUND_GRADIENT));
		GridLayout gl_db_composite = new GridLayout();
		gl_db_composite.numColumns = 2;
		db_composite.setLayout(gl_db_composite);
		
		db_section.setClient(db_composite);
		
		lblDbDriver = new Label(db_composite, SWT.NONE);
		lblDbDriver.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		m_toolkit.adapt(lblDbDriver, true, true);
		lblDbDriver.setText("Driver");
		
		dbDriverText = new Text(db_composite, SWT.BORDER);
		dbDriverText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		m_toolkit.adapt(dbDriverText, true, true);
		
		lblDbUrl = new Label(db_composite, SWT.NONE);
		lblDbUrl.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		m_toolkit.adapt(lblDbUrl, true, true);
		lblDbUrl.setText("URL");
		
		dbURLText = new Text(db_composite, SWT.BORDER | SWT.H_SCROLL | SWT.CANCEL);
		dbURLText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		m_toolkit.adapt(dbURLText, true, true);
		
		lblNewLabel = new Label(db_composite, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		m_toolkit.adapt(lblNewLabel, true, true);
		lblNewLabel.setText("User Name");
		
		dbUserNameText = new Text(db_composite, SWT.BORDER);
		dbUserNameText.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, true, false, 1, 1));
		m_toolkit.adapt(dbUserNameText, true, true);
		
		lblPassword = new Label(db_composite, SWT.NONE);
		lblPassword.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		m_toolkit.adapt(lblPassword, true, true);
		lblPassword.setText("Password");
		
		dbPasswordText = new Text(db_composite, SWT.BORDER | SWT.PASSWORD);
		dbPasswordText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		m_toolkit.adapt(dbPasswordText, true, true);
		
		server_section = m_toolkit.createSection(m_form.getBody(),Section.DESCRIPTION | Section.TWISTIE | Section.TITLE_BAR);
		GridData gd_server_section = new GridData(SWT.RIGHT, SWT.TOP, false, false, 1, 1);
		gd_server_section.widthHint = 435;
		server_section.setLayoutData(gd_server_section);
		
		server_section.setText("Remote Server");
		server_section.setExpanded(true);
		server_section.setDescription("Remote server configuration data.");
		Composite server_composite=m_toolkit.createComposite(server_section);
		server_composite.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_BACKGROUND_GRADIENT));
		GridLayout gl_server_composite = new GridLayout();
		gl_server_composite.numColumns = 2;
		server_composite.setLayout(gl_server_composite);
		
		server_section.setClient(server_composite);
		
		lblNewLabel_1 = m_toolkit.createLabel(server_composite, "Host", SWT.NONE);
		lblNewLabel_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		
		serverHostText = new Text(server_composite, SWT.BORDER);
		serverHostText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		m_toolkit.adapt(serverHostText, true, true);
		
		lblNewLabel_2 = m_toolkit.createLabel(server_composite, "Port", SWT.NONE);
		lblNewLabel_2.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		
		serverPortServer = new Text(server_composite, SWT.BORDER);
		serverPortServer.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		m_toolkit.adapt(serverPortServer, true, true);
		
		lblNewLabel_3 = m_toolkit.createLabel(server_composite, "User Name", SWT.NONE);
		lblNewLabel_3.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		
		serverUserNameText = new Text(server_composite, SWT.BORDER);
		serverUserNameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		m_toolkit.adapt(serverUserNameText, true, true);
		
		lblNewLabel_4 = m_toolkit.createLabel(server_composite, "Password", SWT.NONE);
		lblNewLabel_4.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		
		serverPasswordText = new Text(server_composite, SWT.BORDER | SWT.PASSWORD);
		serverPasswordText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		m_toolkit.adapt(serverPasswordText, true, true);
		
		lblNewLabel_5 = m_toolkit.createLabel(server_composite, "File Path", SWT.NONE);
		lblNewLabel_5.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		
		serverFilePathText = new Text(server_composite, SWT.BORDER);
		serverFilePathText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		m_toolkit.adapt(serverFilePathText, true, true);
		
		ical_section = m_toolkit.createSection(m_form.getBody(), Section.DESCRIPTION | Section.TWISTIE | Section.TITLE_BAR);
		GridData gd_ical_section = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
		gd_ical_section.widthHint = 394;
		ical_section.setLayoutData(gd_ical_section);
		ical_section.setDescription("iCal calendar file data.");
		m_toolkit.paintBordersFor(ical_section);
		ical_section.setText("Calendar File");
		ical_section.setExpanded(true);
		
		ical_composite = m_toolkit.createComposite(ical_section);
		ical_composite.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_BACKGROUND_GRADIENT));
		ical_composite.setLayout(new GridLayout(3, false));
		
		ical_section.setClient(ical_composite);
		
		lblFilePath = m_toolkit.createLabel(ical_composite, "File Path", SWT.NONE);
		lblFilePath.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		
		icsPathText = new Text(ical_composite, SWT.BORDER);
		icsPathText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		m_toolkit.adapt(icsPathText, true, true);
		
		iscPathButton = m_toolkit.createButton(ical_composite, "...", SWT.NONE);
		iscPathButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				icsFileSelect(getParent());
			}
		});
		
		sctnAutoPurge = m_toolkit.createSection(m_form.getBody(), Section.DESCRIPTION | Section.TWISTIE | Section.TITLE_BAR);
		sctnAutoPurge.setDescription("Auto purge configuration");
		GridData gd_sctnAutoPurge = new GridData(SWT.RIGHT, SWT.TOP, false, false, 1, 1);
		gd_sctnAutoPurge.widthHint = 435;
		sctnAutoPurge.setLayoutData(gd_sctnAutoPurge);
		m_toolkit.paintBordersFor(sctnAutoPurge);
		sctnAutoPurge.setText("Auto Purge");
		
		composite = m_toolkit.createComposite(sctnAutoPurge, SWT.NONE);
		composite.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_BACKGROUND_GRADIENT));
		m_toolkit.paintBordersFor(composite);
		sctnAutoPurge.setClient(composite);
		composite.setLayout(new GridLayout(3, false));
		
		autoPurgeCheck = m_toolkit.createButton(composite, "Auto Purge", SWT.CHECK);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		
		lblNewLabel_6 = m_toolkit.createLabel(composite, "Auto Purge Records Older Than", SWT.NONE);
		lblNewLabel_6.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		
		autoPurgeDaysText = new Text(composite, SWT.BORDER);
		autoPurgeDaysText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		m_toolkit.adapt(autoPurgeDaysText, true, true);
		
		lblDays = new Label(composite, SWT.NONE);
		m_toolkit.adapt(lblDays, true, true);
		lblDays.setText("days");
		
		sctnDisplay = m_toolkit.createSection(m_form.getBody(), Section.DESCRIPTION | Section.TWISTIE | Section.TITLE_BAR);
		GridData gd_sctnDisplay = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
		gd_sctnDisplay.widthHint = 396;
		sctnDisplay.setLayoutData(gd_sctnDisplay);
		m_toolkit.paintBordersFor(sctnDisplay);
		sctnDisplay.setText("Display");
		
		composite_1 = m_toolkit.createComposite(sctnDisplay, SWT.NONE);
		composite_1.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_BACKGROUND_GRADIENT));
		m_toolkit.paintBordersFor(composite_1);
		sctnDisplay.setClient(composite_1);
		composite_1.setLayout(new GridLayout(4, false));
		
		lblDayStart = m_toolkit.createLabel(composite_1, "Day Start", SWT.NONE);
		lblDayStart.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		
		displayDayStartCombo = new Combo(composite_1, SWT.NONE);
		GridData gd_displayDayStartCombo = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_displayDayStartCombo.widthHint = 76;
		displayDayStartCombo.setLayoutData(gd_displayDayStartCombo);
		m_toolkit.adapt(displayDayStartCombo);
		m_toolkit.paintBordersFor(displayDayStartCombo);
		
		lblDayEnd = new Label(composite_1, SWT.NONE);
		lblDayEnd.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		m_toolkit.adapt(lblDayEnd, true, true);
		lblDayEnd.setText("Day End");
		
		displayDayEndCombo = new Combo(composite_1, SWT.NONE);
		GridData gd_displayDayEndCombo = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_displayDayEndCombo.widthHint = 68;
		displayDayEndCombo.setLayoutData(gd_displayDayEndCombo);
		m_toolkit.adapt(displayDayEndCombo);
		m_toolkit.paintBordersFor(displayDayEndCombo);
		
		lblInterval = new Label(composite_1, SWT.NONE);
		lblInterval.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		m_toolkit.adapt(lblInterval, true, true);
		lblInterval.setText("Interval");
		
		displayIntervalCombo = new Combo(composite_1, SWT.NONE);
		GridData gd_displayIntervalCombo = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_displayIntervalCombo.widthHint = 88;
		displayIntervalCombo.setLayoutData(gd_displayIntervalCombo);
		m_toolkit.adapt(displayIntervalCombo);
		m_toolkit.paintBordersFor(displayIntervalCombo);
		new Label(composite_1, SWT.NONE);
		new Label(composite_1, SWT.NONE);
		new Label(m_form.getBody(), SWT.NONE);
		
		Button btnCancel = m_toolkit.createButton(m_composite, "Cancel", SWT.NONE);
		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shlConfigurationDialog.close();
			}
		});
		FormData fd_btnCancel = new FormData();
		fd_btnCancel.right = new FormAttachment(0, 98);
		fd_btnCancel.top = new FormAttachment(0, 505);
		fd_btnCancel.left = new FormAttachment(0, 10);
		btnCancel.setLayoutData(fd_btnCancel);
		
		Button btnClear = m_toolkit.createButton(m_composite, "Clear", SWT.NONE);
		btnClear.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				expandAll();
				clearData();
			}
		});
		FormData fd_btnClear = new FormData();
		fd_btnClear.right = new FormAttachment(0, 213);
		fd_btnClear.top = new FormAttachment(0, 505);
		fd_btnClear.left = new FormAttachment(0, 125);
		btnClear.setLayoutData(fd_btnClear);
		
		Button btnSave = m_toolkit.createButton(m_composite, "Save", SWT.NONE);
		btnSave.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				saveConfig();
				shlConfigurationDialog.close();
			}
		});
		FormData fd_btnSave = new FormData();
		fd_btnSave.right = new FormAttachment(0, 328);
		fd_btnSave.top = new FormAttachment(0, 505);
		fd_btnSave.left = new FormAttachment(0, 240);
		btnSave.setLayoutData(fd_btnSave);

		populateCombos();
		loadConf();
		
	}
	
	public void setFocus()
	{
		m_form.setFocus();
	}
	
	public void dispose()
	{
		m_toolkit.dispose();
	}
	/**
	 * Presents file dialog to select ics file path
	 * @param parent
	 */
	private void icsFileSelect(Shell parent) {
		FileDialog dialog = new FileDialog(parent);
		String result = dialog.open();
		if(result!=null)
		{
			icsPathText.setText(result);
		}
	}
	
	/**
	 * Saves configuration into a file.
	 */
	private void saveConfig()
	{		
		prop.setProperty("db_driver",dbDriverText.getText());
		prop.setProperty("db_url",dbURLText.getText());
		prop.setProperty("db_username",dbUserNameText.getText());
		prop.setProperty("db_password",dbPasswordText.getText());
		prop.setProperty("ssh_host",serverHostText.getText());
		prop.setProperty("ssh_username",serverUserNameText.getText());
		prop.setProperty("ssh_password",serverPasswordText.getText());
		prop.setProperty("ssh_port",serverPortServer.getText());
		prop.setProperty("ssh_destination",serverFilePathText.getText());
		prop.setProperty("ical_file_path",icsPathText.getText());
		prop.setProperty("auto_purge_interval",autoPurgeDaysText.getText());

		prop.setProperty("auto_purge",autoPurgeCheck.getSelection()?"1":"0");	
		
		prop.setProperty("day_start",displayDayStartCombo.getText());
		prop.setProperty("day_end",displayDayEndCombo.getText());
		prop.setProperty("interval_minutes",displayIntervalCombo.getText());
		
		if(idm!=null)
		{
			idm.updateProperties(prop);
		}
		
	}
	
	/**
	 * Loads configuration from a file.
	 */
	private void loadConf() {
		if (prop.size()>0) {
			dbDriverText.setText(prop.getProperty("db_driver"));
			dbURLText.setText(prop.getProperty("db_url"));
			dbUserNameText.setText(prop.getProperty("db_username"));
			dbPasswordText.setText(prop.getProperty("db_password"));
			serverHostText.setText(prop.getProperty("ssh_host"));
			serverUserNameText.setText(prop.getProperty("ssh_username"));
			serverPasswordText.setText(prop.getProperty("ssh_password"));
			serverPortServer.setText(prop.getProperty("ssh_port"));
			serverFilePathText.setText(prop.getProperty("ssh_destination"));
			icsPathText.setText(prop.getProperty("ical_file_path"));
			autoPurgeDaysText.setText(prop.getProperty("auto_purge_interval"));
			
			if(prop.getProperty("auto_purge").equals("1"))
			{
				autoPurgeCheck.setSelection(true);
			}
			else
				autoPurgeCheck.setSelection(false);

			displayDayStartCombo.select(displayDayStartCombo.indexOf(prop.getProperty("day_start")));
			displayDayEndCombo.select(displayDayEndCombo.indexOf(prop.getProperty("day_end")));
			displayIntervalCombo.select(displayIntervalCombo.indexOf(prop.getProperty("interval_minutes")));
		}
	}
	
	private void populateCombos()
	{
		displayDayStartCombo.removeAll();
		displayDayEndCombo.removeAll();
		displayIntervalCombo.removeAll();
		for(int i=0; i<24; i++)
		{
			displayDayStartCombo.add(Integer.toString(i));
			displayDayEndCombo.add(Integer.toString(i));			
		}
		
		displayIntervalCombo.add("5");
		displayIntervalCombo.add("10");
		displayIntervalCombo.add("15");
		displayIntervalCombo.add("30");
		displayIntervalCombo.add("60");
	}
	
}
