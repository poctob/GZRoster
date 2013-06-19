package com.gzlabs.gzroster.gui;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.SWT;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import com.gzlabs.drosterheper.DRosterHelper;
import com.gzlabs.gzroster.data.DateUtils;

public class PurgeDataDialog extends Dialog {

	protected Object result;
	protected Shell shlPurgeData;
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private final IDetailsManager idm;
	private final DRosterHelper drh;
	
	private DateTime fromDate;
	private DateTime toDate;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public PurgeDataDialog(Shell parent, int style, DRosterHelper drh, IDetailsManager idm) {
		super(parent, SWT.DIALOG_TRIM | SWT.MIN | SWT.MAX);
		setText("SWT Dialog");
		this.idm=idm;
		this.drh=drh;
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		createContents();
		shlPurgeData.open();
		shlPurgeData.layout();
		Display display = getParent().getDisplay();
		while (!shlPurgeData.isDisposed()) {
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
		shlPurgeData = new Shell(getParent(), SWT.SHELL_TRIM | SWT.BORDER | SWT.APPLICATION_MODAL);
		shlPurgeData.setSize(249, 232);
		shlPurgeData.setText("Purge Data");
		
		Composite composite = new Composite(shlPurgeData, SWT.NONE);
		composite.setBounds(10, 10, 226, 180);
		
		Form frmNewForm = formToolkit.createForm(composite);
		frmNewForm.setBounds(0, 0, 226, 180);
		formToolkit.paintBordersFor(frmNewForm);
		frmNewForm.setText("Purge Data");
		
		Label lblFrom = formToolkit.createLabel(frmNewForm.getBody(), "From:", SWT.NONE);
		lblFrom.setBounds(10, 21, 69, 18);
		
		Label lblTo = formToolkit.createLabel(frmNewForm.getBody(), "To:", SWT.NONE);
		lblTo.setBounds(10, 60, 69, 18);
		
		fromDate = new DateTime(frmNewForm.getBody(), SWT.BORDER | SWT.DROP_DOWN);
		fromDate.setBounds(85, 10, 128, 29);
		formToolkit.adapt(fromDate);
		formToolkit.paintBordersFor(fromDate);
		
		toDate = new DateTime(frmNewForm.getBody(), SWT.BORDER | SWT.DROP_DOWN);
		toDate.setBounds(85, 49, 128, 29);
		formToolkit.adapt(toDate);
		formToolkit.paintBordersFor(toDate);
		
		Button btnCancel = formToolkit.createButton(frmNewForm.getBody(), "Cancel", SWT.NONE);
		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shlPurgeData.close();
			}
		});
		btnCancel.setBounds(10, 109, 88, 30);
		
		Button btnPurge = formToolkit.createButton(frmNewForm.getBody(), "Purge", SWT.NONE);
		btnPurge.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(idm!=null)
				{
					String start=DateUtils.dateStringFromWidget(fromDate, null);
					String end=DateUtils.dateStringFromWidget(toDate, null);
					drh.purgeData(null, start, end);
					idm.refreshData();
					shlPurgeData.close();
					
				}
			}
		});
		btnPurge.setBounds(125, 109, 88, 30);

	}
}
