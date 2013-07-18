package com.gzlabs.gzroster.gui;

import java.util.ArrayList;
import java.util.Collections;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.wb.swt.SWTResourceManager;

import com.gzlabs.utils.WidgetUtilities;

/**
 * Handles employee manipulations
 * 
 * @author apavlune
 * 
 */
public class EmployeesWidget extends Composite {

	private final FormToolkit formToolkit = new FormToolkit(
			Display.getDefault());

	private IEmployeeManager iem;

	/************************************************************/
	// Employee Tab
	private Text nameText;
	private Text addressText;
	private Text homephoneText;
	private Text mobilePhoneText;
	private Text emailText;

	private List employeesList;
	private Button employeesDeleteButton;
	private Button employeesEditButton;
	private Button btnClear;
	private Button btnEmployeeCancel;

	private Label lblWebName;
	private Label lblName;
	private Label lblAddress;
	private Label lblMobilePhone;
	private Label lblHomePhone;

	private Button activeCheck;
	private Button pinButton;
	/************************************************************/

	private String old_name;
	private String old_address;
	private String old_hphone;
	private String old_mphone;
	private String old_active;
	private String old_email;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public EmployeesWidget(Composite parent, int style, IEmployeeManager em) {
		super(parent, style);
		setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_BACKGROUND_GRADIENT));
		if (em == null) {
			return;
		}

		iem = em;

		ListViewer employeesListViewer = new ListViewer(this, SWT.BORDER
				| SWT.V_SCROLL);
		employeesList = employeesListViewer.getList();
		employeesList.setBackground(SWTResourceManager.getColor(255, 255, 255));
		employeesList.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (employeesList.getSelectionIndex() == -1) {
					employeesDeleteButton.setEnabled(false);
				} else {

					if (iem != null) {
						employeesDeleteButton.setEnabled(true);
						clearEmployeeData();
						iem.setEmployeeDetails(employeesList.getSelection()[0]);
						toggleEmployeeEdit(true);
						setOld_name(nameText.getText());
						setOld_address(addressText.getText());
						setOld_hphone(homephoneText.getText());
						setOld_mphone(mobilePhoneText.getText());
						setOld_active(activeCheck.getSelection() ? "1" : "0");
						setOld_email(emailText.getText());
					}
				}
			}

		});
		employeesList.setBounds(10, 46, 143, 248);

		employeesEditButton = formToolkit.createButton(this, "Save", SWT.NONE);
		employeesEditButton.setEnabled(false);
		employeesEditButton.setImage(SWTResourceManager.getImage(
				MainWindow.class,
				"/javax/swing/plaf/metal/icons/ocean/floppy.gif"));
		employeesEditButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setCursor(new Cursor(getDisplay(), SWT.CURSOR_WAIT));
				if(checkNameValidity())
				{
					iem.processEmployeeData();
				}
				else
				{
					MessageDialog.openError(new Shell(), "Invalid Name", "Invalid or Duplicate Name detected");
				}
				setCursor(new Cursor(getDisplay(), SWT.CURSOR_ARROW));
			}
		});
		employeesEditButton.setBounds(471, 256, 88, 30);

		employeesDeleteButton = formToolkit.createButton(this, "Delete",
				SWT.NONE);
		employeesDeleteButton.setEnabled(false);
		employeesDeleteButton.setImage(SWTResourceManager.getImage(
				MainWindow.class,
				"/org/eclipse/jface/dialogs/images/message_error.gif"));
		
		
		employeesDeleteButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (MessageDialog.openConfirm(null, "Confirm Delete",
						"Are you sure that you want to delete this employee?")) {
					if(iem !=null && employeesList!=null)
					{
						setCursor(new Cursor(getDisplay(), SWT.CURSOR_WAIT));
						iem.deleteEmployee(employeesList.getSelection());
						iem.populateData();
						setCursor(new Cursor(getDisplay(), SWT.CURSOR_ARROW));
					}
				}
			}
		});
		employeesDeleteButton.setBounds(10, 300, 88, 30);

		Button btnNew = new Button(this, SWT.NONE);
		btnNew.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				toggleEmployeeEdit(true);
				clearEmployeeData();
				
				if(employeesList!=null)
					employeesList.deselectAll();
			}
		});
		btnNew.setImage(SWTResourceManager.getImage(MainWindow.class,
				"/javax/swing/plaf/metal/icons/ocean/file.gif"));
		btnNew.setBounds(10, 10, 88, 30);
		formToolkit.adapt(btnNew, true, true);
		btnNew.setText("New");

		nameText = new Text(this, SWT.BORDER);
		nameText.setEnabled(false);
		nameText.setBounds(168, 46, 130, 28);
		formToolkit.adapt(nameText, true, true);

		lblName = new Label(this, SWT.NONE);
		lblName.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_BACKGROUND_GRADIENT));
		lblName.setEnabled(false);
		lblName.setBounds(168, 22, 69, 18);
		formToolkit.adapt(lblName, true, true);
		lblName.setText("Name");

		addressText = new Text(this, SWT.BORDER | SWT.MULTI);
		addressText.setEnabled(false);
		addressText.setBounds(326, 46, 233, 86);
		formToolkit.adapt(addressText, true, true);

		lblAddress = new Label(this, SWT.NONE);
		lblAddress.setEnabled(false);
		lblAddress.setText("Address");
		lblAddress.setBounds(326, 22, 69, 18);
		formToolkit.adapt(lblAddress, true, true);

		homephoneText = new Text(this, SWT.BORDER);
		homephoneText.setEnabled(false);
		homephoneText.setBounds(168, 104, 130, 28);
		formToolkit.adapt(homephoneText, true, true);

		lblHomePhone = new Label(this, SWT.NONE);
		lblHomePhone.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_BACKGROUND_GRADIENT));
		lblHomePhone.setEnabled(false);
		lblHomePhone.setText("Home Phone");
		lblHomePhone.setBounds(168, 80, 130, 18);
		formToolkit.adapt(lblHomePhone, true, true);

		lblMobilePhone = new Label(this, SWT.NONE);
		lblMobilePhone.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_BACKGROUND_GRADIENT));
		lblMobilePhone.setEnabled(false);
		lblMobilePhone.setText("Mobile Phone");
		lblMobilePhone.setBounds(168, 138, 130, 18);
		formToolkit.adapt(lblMobilePhone, true, true);

		mobilePhoneText = new Text(this, SWT.BORDER);
		mobilePhoneText.setEnabled(false);
		mobilePhoneText.setBounds(168, 162, 130, 28);
		formToolkit.adapt(mobilePhoneText, true, true);

		activeCheck = new Button(this, SWT.CHECK);
		activeCheck.setEnabled(false);
		activeCheck.setSelection(true);
		activeCheck.setBounds(326, 168, 111, 22);
		formToolkit.adapt(activeCheck, true, true);
		activeCheck.setText("Active?");

		emailText = new Text(this, SWT.BORDER);
		emailText.setEnabled(false);
		emailText.setBounds(168, 222, 130, 28);
		formToolkit.adapt(emailText, true, true);

		lblWebName = new Label(this, SWT.NONE);
		lblWebName.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_BACKGROUND_GRADIENT));
		lblWebName.setEnabled(false);
		lblWebName.setText("Email");
		lblWebName.setBounds(168, 198, 130, 18);
		formToolkit.adapt(lblWebName, true, true);

		btnClear = new Button(this, SWT.NONE);
		btnClear.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				clearEmployeeData();
			}
		});
		btnClear.setEnabled(false);
		btnClear.setImage(SWTResourceManager.getImage(MainWindow.class,
				"/com/sun/java/swing/plaf/motif/icons/Error.gif"));
		btnClear.setBounds(166, 256, 88, 30);
		formToolkit.adapt(btnClear, true, true);
		btnClear.setText("Clear");

		btnEmployeeCancel = new Button(this, SWT.NONE);
		btnEmployeeCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				clearEmployeeData();
				toggleEmployeeEdit(false);
			}
		});
		btnEmployeeCancel.setEnabled(false);
		btnEmployeeCancel.setImage(SWTResourceManager.getImage(
				MainWindow.class,
				"/javax/swing/plaf/metal/icons/ocean/close.gif"));
		btnEmployeeCancel.setBounds(313, 256, 88, 30);
		formToolkit.adapt(btnEmployeeCancel, true, true);
		btnEmployeeCancel.setText("Cancel");
		
		pinButton = new Button(this, SWT.NONE);

		pinButton.setEnabled(false);
		pinButton.setBounds(471, 162, 88, 30);
		formToolkit.adapt(pinButton, true, true);
		pinButton.setText("Reset Pin");

	}

	/**
	 * Checks for the name not to be empty or duplicate.
	 * @return
	 */
	protected boolean checkNameValidity() {
		int selection_idx=employeesList.getSelectionIndex();
		String newname=nameText.getText();

		if(newname.length()>0)
		{
			if(employeesList.indexOf(newname)==-1)
			{
				return true;
			}
			else if(selection_idx>=0 && newname.equals(employeesList.getItem(selection_idx)))
			{
				return true;
			}
		}
		
		return false;
	}

	/**
	 * Clears employee data controls
	 */
	private void clearEmployeeData() {
		nameText.setText("");
		addressText.setText("");
		homephoneText.setText("");
		mobilePhoneText.setText("");
		emailText.setText("");
	}

	/**
	 * Removes all entries from the employees list
	 */
	public void clearEmployeesList() {
		employeesList.removeAll();
	}

	/**
	 * Adds new entry into the employees list
	 * @param data
	 */
	public void addEmployee(ArrayList<String> data) {
		
		if(data!=null)
		{
			Collections.sort(data);
			for (String s : data) {				
				WidgetUtilities.safeListAdd(employeesList, s);				
			}
		}
	}

	/**
	 * Resets all controls
	 */
	public void resetControls() {
		clearEmployeesList();
		toggleEmployeeEdit(false);
	}

	/**
	 * Adds employee to a list
	 * @param s
	 */
	public void addEmployee(String s) {
		WidgetUtilities.safeListAdd(employeesList, s);	
	}

	/**
	 * Enables and disables employee edit controls.
	 * 
	 * @param enable
	 *            Specify to enable or disable
	 */
	private void toggleEmployeeEdit(boolean enable) {
		employeesEditButton.setEnabled(enable);
		btnClear.setEnabled(enable);
		btnEmployeeCancel.setEnabled(enable);

		nameText.setEnabled(enable);
		addressText.setEnabled(enable);
		homephoneText.setEnabled(enable);
		mobilePhoneText.setEnabled(enable);
		emailText.setEnabled(enable);

		lblWebName.setEnabled(enable);
		lblName.setEnabled(enable);
		lblAddress.setEnabled(enable);
		lblMobilePhone.setEnabled(enable);
		lblHomePhone.setEnabled(enable);
		activeCheck.setEnabled(enable);
		pinButton.setEnabled(enable);
		
		if(enable)
		{
			pinButton.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					PinDialog pd=new PinDialog(new Shell(), iem, nameText.getText());
					pd.open();
				}
			});
		}
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	/****************************************************************************/
	/**Accessors*/
	/****************************************************************************/
	public void setNameText(String value) {
		WidgetUtilities.safeTextSet(nameText, value);

	}

	public void setAddressText(String value) {
		WidgetUtilities.safeTextSet(addressText, value);
	}

	public void setHomephoneText(String value) {
		WidgetUtilities.safeTextSet(homephoneText, value);

	}

	public void setMobilePhoneText(String value) {
		WidgetUtilities.safeTextSet(mobilePhoneText, value);
	}

	public void setEmailText(String value) {
		WidgetUtilities.safeTextSet(emailText, value);

	}

	public void setActiveCheck(boolean b) {
		activeCheck.setSelection(b);

	}

	public String getNameText() {
		return nameText.getText();
	}

	public String getAddressText() {
		return addressText.getText();
	}

	public String getHomephoneText() {
		return homephoneText.getText();
	}

	public String getMobilePhoneText() {
		return mobilePhoneText.getText();
	}

	public String getEmailText() {
		return emailText.getText();
	}

	public boolean getActiveCheck() {
		return activeCheck.getSelection();
	}

	public int getSelectionIndex() {
		return employeesList.getSelectionIndex();
	}

	/**
	 * @return the old_name
	 */
	public String getOld_name() {
		return old_name;
	}

	/**
	 * @param old_name
	 *            the old_name to set
	 */
	public void setOld_name(String old_name) {
		this.old_name = old_name;
	}

	/**
	 * @return the old_address
	 */
	public String getOld_address() {
		return old_address;
	}

	/**
	 * @param old_address
	 *            the old_address to set
	 */
	public void setOld_address(String old_address) {
		this.old_address = old_address;
	}

	/**
	 * @return the old_hphone
	 */
	public String getOld_hphone() {
		return old_hphone;
	}

	/**
	 * @param old_hphone
	 *            the old_hphone to set
	 */
	public void setOld_hphone(String old_hphone) {
		this.old_hphone = old_hphone;
	}

	/**
	 * @return the old_mphone
	 */
	public String getOld_mphone() {
		return old_mphone;
	}

	/**
	 * @param old_mphone
	 *            the old_mphone to set
	 */
	public void setOld_mphone(String old_mphone) {
		this.old_mphone = old_mphone;
	}

	/**
	 * @return the old_active
	 */
	public String getOld_active() {
		return old_active;
	}

	/**
	 * @param old_active
	 *            the old_active to set
	 */
	public void setOld_active(String old_active) {
		this.old_active = old_active;
	}

	/**
	 * @return the old_email
	 */
	public String getOld_email() {
		return old_email;
	}

	/**
	 * @param old_email
	 *            the old_email to set
	 */
	public void setOld_email(String old_email) {
		this.old_email = old_email;
	}
}
