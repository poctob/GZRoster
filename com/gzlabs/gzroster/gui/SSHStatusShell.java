package com.gzlabs.gzroster.gui;

import java.util.Properties;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Button;

import com.gzlabs.gzroster.data.UploadManager;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

/**
 * Performs calendar upload to the server and displays upload status
 * @author apavlune
 *
 */
public class SSHStatusShell extends Shell implements IDisplayStatus{

	//Status window
	private StyledText styledText;
	
	//Close button
	private Button btnClose;
	
	/**
	 * Launch the application.
	 * @param args
	 */
	public static void launch(Properties prop) {
		try {
			Display display = Display.getDefault();
			SSHStatusShell shell = new SSHStatusShell(display);
			shell.open();
			shell.layout();
			shell.startUpload(prop);
			while (!shell.isDisposed()) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the shell.
	 * @param display
	 */
	public SSHStatusShell(Display display) {
		super(display, SWT.SHELL_TRIM);
		
		styledText = new StyledText(this, SWT.BORDER | SWT.V_SCROLL);
		styledText.setBounds(10, 10, 428, 231);
		
		btnClose = new Button(this, SWT.NONE);
		btnClose.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				close();
			}
		});
		btnClose.setBounds(183, 247, 88, 30);
		btnClose.setText("Close");
		btnClose.setEnabled(false);
		createContents();	
	}

	/**
	 * Create contents of the shell.
	 */
	protected void createContents() {
		setText("Calendar Upload Status");
		setSize(450, 314);

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	@Override
	public void DisplayStatus(String status) {
		if(styledText!=null)
		{
			styledText.append(status+"\n");
		}
		
	}

	@Override
	public void ShowErrorBox(String error) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * Initiates upload process
	 * @param prop Properties to use for the upload.
	 */
	private void startUpload(Properties prop)
	{
		if(prop!=null)
		{
			UploadManager drh=new UploadManager(prop, this);
			drh.processData();
		}
		else
		{
			DisplayStatus("Invalid configuration!");
		}
		btnClose.setEnabled(true);
	}
}
