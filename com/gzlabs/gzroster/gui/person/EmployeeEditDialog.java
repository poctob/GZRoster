package com.gzlabs.gzroster.gui.person;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.xpresstek.gzrosterdata.Person;
import com.xpresstek.gzrosterdata.gui.BaseItemEditDialog;
import com.gzlabs.utils.WidgetUtilities;

import org.eclipse.swt.SWT;
import org.eclipse.wb.swt.SWTResourceManager;

public class EmployeeEditDialog extends BaseItemEditDialog {

	private Text nameText;
	private Text addressText;
	private Text homephoneText;
	private Text mobilePhoneText;
	private Text emailText;
	
	private Label lblWebName;
	private Label lblName;
	private Label lblAddress;
	private Label lblMobilePhone;
	private Label lblHomePhone;
	
	private Button activeCheck;
	
	public EmployeeEditDialog(Shell parentShell) {
		super(parentShell);
		setShellStyle(SWT.APPLICATION_MODAL);
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
		parent.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_BACKGROUND_GRADIENT));
		Composite container = (Composite) super.createDialogArea(parent);
		container.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_BACKGROUND_GRADIENT));
		GridLayout gridLayout = (GridLayout) container.getLayout();
		gridLayout.verticalSpacing = 5;
		gridLayout.horizontalSpacing = 5;
		gridLayout.marginBottom = 5;
		gridLayout.marginTop = 5;
		gridLayout.marginRight = 5;
		gridLayout.marginLeft = 5;
		gridLayout.numColumns = 2;
		
		lblName = new Label(container, SWT.NONE);
		lblName.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_BACKGROUND_GRADIENT));
		lblName.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false,
				false, 1, 1));
		lblName.setText("Name");
		
		nameText = new Text(container, SWT.BORDER);
		nameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
				false, 1, 1));
		
		lblAddress = new Label(container, SWT.NONE);
		lblAddress.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_BACKGROUND_GRADIENT));
		lblAddress.setText("Address");
		lblAddress.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false,
				false, 1, 1));
		
		addressText = new Text(container, SWT.BORDER | SWT.MULTI);
		GridData gd_addressText = new GridData(SWT.FILL, SWT.TOP, false,
				false, 1, 1);
		gd_addressText.widthHint = 302;
		gd_addressText.heightHint = 100;
		addressText.setLayoutData(gd_addressText);
				
		lblHomePhone = new Label(container, SWT.NONE);
		lblHomePhone.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_BACKGROUND_GRADIENT));
		lblHomePhone.setText("Home Phone");
		lblHomePhone.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false,
				false, 1, 1));
			
		homephoneText = new Text(container, SWT.BORDER);
		homephoneText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
				false, 1, 1));
		
		lblMobilePhone = new Label(container, SWT.NONE);
		lblMobilePhone.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_BACKGROUND_GRADIENT));
		lblMobilePhone.setText("Mobile Phone");
		lblMobilePhone.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false,
				false, 1, 1));
			
		mobilePhoneText = new Text(container, SWT.BORDER);
		mobilePhoneText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
				false, 1, 1));		

		activeCheck = new Button(container, SWT.CHECK);
		activeCheck.setSelection(true);
		activeCheck.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_BACKGROUND_GRADIENT));
		activeCheck.setText("Active?");
		activeCheck.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 2, 1));	

		
		lblWebName = new Label(container, SWT.NONE);
		lblWebName.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_BACKGROUND_GRADIENT));
		lblWebName.setText("Email");
		lblWebName.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false,
				false, 1, 1));
		
		emailText = new Text(container, SWT.BORDER);
		GridData gd_emailText = new GridData(SWT.FILL, SWT.CENTER, false,
				false, 1, 1);
		gd_emailText.heightHint = 20;
		emailText.setLayoutData(gd_emailText);		
		populateControls();
		
		return container;
	}
	@Override
	protected void populateControls() {		
		Person person=(Person) getOldObject();
		if(person!=null)
		{
			WidgetUtilities.safeTextSet(nameText, person.getName());
			WidgetUtilities.safeTextSet(addressText, person.getM_address());
			WidgetUtilities.safeTextSet(homephoneText, person.getM_home_phone());
			WidgetUtilities.safeTextSet(mobilePhoneText, person.getM_mobile_phone());
			activeCheck.setSelection(person.isActive());
			WidgetUtilities.safeTextSet(emailText, person.getM_email());
		}
	}
	
	@Override
	protected void populateNewObject() {	
		current_object=new Person(nameText.getText(),
				addressText.getText(),
				homephoneText.getText(),
				mobilePhoneText.getText(),
				activeCheck.getSelection(),
				emailText.getText());
		if(getOldObject()!=null)
		{
			((Person)current_object).setM_positions(((Person) getOldObject()).getM_positions());
			((Person)current_object).setM_privileges(((Person) getOldObject()).getM_privileges());
		}
	}

	@Override
	protected void clear() {
		nameText.setText("");
		addressText.setText("");
		homephoneText.setText("");
		mobilePhoneText.setText("");
		emailText.setText("");
		
	}
	
	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(446, 350);
	}

	@Override
	protected Color getBackgroundColor() {		
		return SWTResourceManager.getColor(SWT.COLOR_TITLE_BACKGROUND_GRADIENT);
	}

}
