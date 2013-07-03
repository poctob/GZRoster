package com.gzlabs.gzroster.gui;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Rectangle;

public class SSHPasswordDialog extends Dialog {

	protected Object result;
	protected Shell shlSshPasswordDialog;
	private Text textPassword;	

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public SSHPasswordDialog(Shell parent, int style) {
		super(parent, style);
		setText("SWT Dialog");
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		createContents();
		Display display = getParent().getDisplay();
	    Rectangle bounds = getParent().getBounds();
	    Rectangle rect = 
	    	    shlSshPasswordDialog.getBounds();
	    
	    int x = bounds.x + (bounds.width - rect.width) / 2;
	    int y = bounds.y + (bounds.height - rect.height) / 2;
	    
	    shlSshPasswordDialog.setLocation(x, y);
		shlSshPasswordDialog.open();
		shlSshPasswordDialog.layout();
		
		while (!shlSshPasswordDialog.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shlSshPasswordDialog = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL | SWT.ON_TOP);
		shlSshPasswordDialog.setSize(263, 136);
		shlSshPasswordDialog.setText("SSH Password Dialog");
		
		Label lblEnterRemoteServer = new Label(shlSshPasswordDialog, SWT.CENTER);
		lblEnterRemoteServer.setBounds(10, 10, 241, 18);
		lblEnterRemoteServer.setText("Enter Remote Server Pasword");
		
		textPassword = new Text(shlSshPasswordDialog, SWT.BORDER | SWT.PASSWORD);
		textPassword.setBounds(10, 34, 241, 28);
		
		Button btnOk = new Button(shlSshPasswordDialog, SWT.NONE);
		btnOk.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				result=textPassword.getText();
				shlSshPasswordDialog.close();
			}
		});
		btnOk.setBounds(163, 68, 88, 30);
		btnOk.setText("OK");
		
		Button btnCancel = new Button(shlSshPasswordDialog, SWT.NONE);
		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				result=null;
				shlSshPasswordDialog.close();
			}
		});
		btnCancel.setBounds(10, 68, 88, 30);
		btnCancel.setText("Cancel");

	}
}
