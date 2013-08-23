package com.gzlabs.gzroster.gui.time_off;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;

import com.xpresstek.gzrosterdata.TimeOff;
import com.gzlabs.utils.DateUtils;
import com.gzlabs.utils.WidgetUtilities;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class TimeOffDialog extends Dialog {

	private Combo toTimeCombo;
	private Combo fromTimeCombo;
	private Button fromAllDay;
	private Button toAllDay;
	private DateTime fromDateTime;
	private DateTime toDateTime;
	private Label lblPerson;
	private Combo personCombo;
	private Label lblStatus;
	private Combo statusCombo;
	private ArrayList<String> mNames;
	private ArrayList<String> mStatus;
	private ArrayList<Object> mTimeSpans;
	private TimeOff timeOff;

	/**
	 * Create the dialog.
	 * 
	 * @param parentShell
	 */
	public TimeOffDialog(Shell parentShell, ArrayList<String> names,
			ArrayList<String> status, ArrayList<Object> timespans) {
		super(parentShell);
		mNames = names;
		mStatus = status;
		mTimeSpans = timespans;
	}

	/**
	 * Create contents of the dialog.
	 * 
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		GridLayout gridLayout = (GridLayout) container.getLayout();
		gridLayout.numColumns = 4;

		lblPerson = new Label(container, SWT.NONE);
		lblPerson.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		lblPerson.setText("Person:");

		personCombo = new Combo(container, SWT.NONE);
		personCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 2, 1));
		new Label(container, SWT.NONE);

		Label lblFrom = new Label(container, SWT.NONE);
		lblFrom.setText("From:");

		fromDateTime = new DateTime(container, SWT.BORDER | SWT.DROP_DOWN);

		GridData gd_fromDateTime = new GridData(SWT.LEFT, SWT.CENTER, false,
				false, 1, 1);
		gd_fromDateTime.widthHint = 113;
		fromDateTime.setLayoutData(gd_fromDateTime);

		fromTimeCombo = new Combo(container, SWT.NONE);
		GridData gd_fromTimeCombo = new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1);
		gd_fromTimeCombo.widthHint = 89;
		fromTimeCombo.setLayoutData(gd_fromTimeCombo);

		fromAllDay = new Button(container, SWT.CHECK);
		fromAllDay.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				fromTimeCombo.setEnabled(!fromAllDay.getSelection());
			}
		});
		fromAllDay.setText("All Day");

		Label lblTo = new Label(container, SWT.NONE);
		lblTo.setText("To:");

		toDateTime = new DateTime(container, SWT.BORDER | SWT.DROP_DOWN);
		GridData gd_toDateTime = new GridData(SWT.LEFT, SWT.CENTER, false,
				false, 1, 1);
		gd_toDateTime.widthHint = 116;
		toDateTime.setLayoutData(gd_toDateTime);

		toTimeCombo = new Combo(container, SWT.NONE);
		toTimeCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));

		toAllDay = new Button(container, SWT.CHECK);
		toAllDay.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				toTimeCombo.setEnabled(!toAllDay.getSelection());
			}
		});
		toAllDay.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1,
				1));
		toAllDay.setText("All Day");

		lblStatus = new Label(container, SWT.NONE);
		lblStatus.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		lblStatus.setText("Status:");

		statusCombo = new Combo(container, SWT.NONE);
		statusCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 2, 1));
		new Label(container, SWT.NONE);

		populateCombos();
		return container;
	}

	/**
	 * Create contents of the button bar.
	 * 
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		Button button = createButton(parent, IDialogConstants.OK_ID,
				IDialogConstants.OK_LABEL, true);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				processTimeOff();
				close();
			}
		});
		button.setText("Submit");
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(366, 267);
	}

	private void populateCombos() {
		if (mTimeSpans != null) {
			fromTimeCombo.removeAll();
			toTimeCombo.removeAll();
			for (Object value : mTimeSpans) {

				WidgetUtilities.safeComboAdd(fromTimeCombo, (String) value);
				WidgetUtilities.safeComboAdd(toTimeCombo, (String) value);
			}
		}

		if(mNames!=null)
		{
			personCombo.removeAll();
			for (String value : mNames) {

				WidgetUtilities.safeComboAdd(personCombo, value);
			}
		}
		
		if(mStatus!=null)
		{
			statusCombo.removeAll();
			for (String value : mStatus) {

				WidgetUtilities.safeComboAdd(statusCombo, value);
			}
		}
	

}

	private void processTimeOff() {
		String start_time = fromAllDay.getSelection() ? "00:00:00.0"
				: fromTimeCombo.getText() + ":00.0";
		String end_time = toAllDay.getSelection() ? "23:59:59.0" : toTimeCombo
				.getText() + ":00.0";
		
		if(start_time.length()==9)
		{
			start_time="0"+start_time;
		}
		if(end_time.length()==9)
		{
			end_time="0"+end_time;
		}


		String start_date = DateUtils.dateStringFromWidget(fromDateTime, null);
		String end_date = DateUtils.dateStringFromWidget(toDateTime, null);
		Calendar startDate=DateUtils.calendarFromString(start_date+" "+start_time);
		Calendar endDate=DateUtils.calendarFromString(end_date+" "+end_time);
		
		timeOff=new TimeOff(startDate, endDate, statusCombo.getText(),personCombo.getText());
	}
	
	@Override
	protected void okPressed(){}
	
	public TimeOff getTimeOff()
	{
		return timeOff;
	}
}
