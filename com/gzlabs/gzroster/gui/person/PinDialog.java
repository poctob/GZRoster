package com.gzlabs.gzroster.gui.person;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;


public class PinDialog extends Dialog {
	private Text pin;
	private Text confirm;
	private IEmployeeManager iem;
	private static final int PIN_MIN_LENGTH=4;
	private String m_name;

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public PinDialog(Shell parentShell, IEmployeeManager p_iem, String name) {
		super(parentShell);
		setShellStyle(SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		this.iem=p_iem;
		this.m_name=name;
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		GridLayout gridLayout = (GridLayout) container.getLayout();
		gridLayout.numColumns = 2;
		
		Label lblNewPin = new Label(container, SWT.NONE);
		lblNewPin.setText("New Pin:");
		
		pin = new Text(container, SWT.BORDER | SWT.PASSWORD);
		pin.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblConfirm = new Label(container, SWT.NONE);
		lblConfirm.setText("Confirm:");
		
		confirm = new Text(container, SWT.BORDER | SWT.PASSWORD);
		confirm.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Button btnCancel = new Button(container, SWT.NONE);
		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				close();
			}
		});
		GridData gd_btnCancel = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnCancel.widthHint = 93;
		btnCancel.setLayoutData(gd_btnCancel);
		btnCancel.setText("Cancel");
		
		Button btnSubmit = new Button(container, SWT.NONE);
		btnSubmit.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				submitData();
			}
		});
		GridData gd_btnSubmit = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnSubmit.widthHint = 109;
		btnSubmit.setLayoutData(gd_btnSubmit);
		btnSubmit.setText("Submit");

		return container;
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		parent.setVisible(false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(227, 181);
	}
	
	private void submitData()
	{
		if(!pin.getText().equals(confirm.getText()))
		{
			MessageDialog.openError(new Shell(), "Error", "Pins Don't Match!");
			return;
		}
		
		if(pin.getText().length()<PIN_MIN_LENGTH)
		{
			MessageDialog.openError(new Shell(), "Error", "Pin is too short!");
			return;
		}
		if(iem!=null)
		{
			iem.setPin(pin.getText(), m_name);
		}
		close();
	}


}
