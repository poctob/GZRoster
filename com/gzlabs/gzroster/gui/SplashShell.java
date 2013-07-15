package com.gzlabs.gzroster.gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class SplashShell extends Shell {

	/**
	 * Launch the application.
	 * 
	 * @param args
	 */

	private Display display;
	private Button btnClose;
	private Label lblConnectingPleaseWait;

	/**
	 * Create the shell.
	 * 
	 * @param display
	 * @param stat
	 *            Connection status interface
	 */
	public SplashShell(Display display) {
		super(display, SWT.NO_TRIM | SWT.CLOSE | SWT.APPLICATION_MODAL);
		this.display = display;

		setBackground(SWTResourceManager.getColor(SWT.COLOR_DARK_CYAN));

		lblConnectingPleaseWait = new Label(this, SWT.NONE);
		lblConnectingPleaseWait.setAlignment(SWT.CENTER);
		lblConnectingPleaseWait.setForeground(SWTResourceManager
				.getColor(SWT.COLOR_LIST_SELECTION_TEXT));
		lblConnectingPleaseWait.setBackground(SWTResourceManager
				.getColor(SWT.COLOR_DARK_CYAN));
		lblConnectingPleaseWait.setBounds(10, 93, 300, 18);
		lblConnectingPleaseWait.setText("Connecting, Please wait...");
		createContents();
		setCursor(new Cursor(display, SWT.CURSOR_WAIT));
		
		btnClose = new Button(this, SWT.BORDER);
		btnClose.setImage(SWTResourceManager.getImage(SplashShell.class, "/org/eclipse/jface/dialogs/images/message_error.gif"));
		btnClose.setFont(SWTResourceManager.getFont("Dingbats", 11, SWT.NORMAL));
		btnClose.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				System.exit(-1);
			}
		});
		btnClose.setBounds(117, 136, 81, 30);
		btnClose.setText("Close");
		btnClose.setVisible(false);
	}

	/**
	 * Create contents of the shell.
	 */
	protected void createContents() {
		setText("GZRoster Connection");
		setSize(320, 207);
		Rectangle splashRect = getBounds();
		Rectangle displayRect = display.getPrimaryMonitor().getBounds();
		int x = (displayRect.width - splashRect.width) / 2;
		int y = (displayRect.height - splashRect.height) / 2;
		setLocation(x, y);

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
	
	public void activateError()
	{
		setCursor(new Cursor(display, SWT.CURSOR_ARROW));
		btnClose.setVisible(true);
		lblConnectingPleaseWait.setText("Initialization Error. Unable to Continue.");
	}
}
