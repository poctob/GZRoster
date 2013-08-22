package com.gzlabs.gzroster.gui.time_off;

import java.util.ArrayList;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.widgets.Composite;

import com.gzlabs.gzroster.data.TimeOff;
import com.gzlabs.gzroster.gui.GZTableViewer;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Cursor;

/**
 * Widget displaying details table
 * 
 * @author apavlune
 * 
 */
public class TimeOffWidget extends Composite {

	private ITimeOffManager tom;
	/************************************************************/
	// Details Tab
	private GZTableViewer tv;

	//Currently Selected element
	private Object current_selection;
	/************************************************************/
	private Button btnDelete;
	private Button btnUpdate;
	private TimeOffModelProvider modelProvider;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public TimeOffWidget(Composite parent, int style, ITimeOffManager p_tom) {
		super(parent, style);
		if (p_tom == null) {
			return;
		}
		tom = p_tom;
		tv = new TimeOffTableViewer(this);
		
		Group group = new Group(this, SWT.NONE);
		group.setBounds(0, 10, 891, 68);
		
		btnDelete = new Button(group, SWT.NONE);
		btnDelete.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(MessageDialog.openConfirm(getShell(), "Confirm Delete", "Are you sure?"))
				{
					IStructuredSelection selection = (IStructuredSelection)tv.getSelection();
				    Object element = selection.getFirstElement();
					if(modelProvider.getTimeOff().remove(element))
					{
						updateData();
						tv.refresh();
					}
				}
			}
		});
		btnDelete.setBounds(699, 28, 88, 30);
		btnDelete.setText("Delete");
		
		btnUpdate = new Button(group, SWT.NONE);
		btnUpdate.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				updateData();
			}
		});
		btnUpdate.setBounds(793, 28, 88, 30);
		btnUpdate.setText("Update");
		
		Button btnNew = new Button(group, SWT.NONE);
		btnNew.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TimeOffDialog to_dialog=new TimeOffDialog(getShell(),
						tom.getNameOptions(),
						tom.getTimeOffStatusOptions(),
						tom.getTimeSpan());
				to_dialog.open();
				if(to_dialog.getTimeOff()!=null)
				{
					modelProvider.getTimeOff().add(to_dialog.getTimeOff());
					updateData();
					tv.refresh();
				}
			}
		});
		btnNew.setBounds(10, 28, 88, 30);
		btnNew.setText("New");
		tv.createTableViewerColumn("Name", 150, 0, new ColumnLabelProvider()
		{
			 @Override
		      public String getText(Object element) {
		        TimeOff p = (TimeOff) element;
		        return p.getName();
			 }
		},
		new PersonEditingSupport(tv,tom.getNameOptions())
		);
		
		tv.createTableViewerColumn("Start", 200, 1, new ColumnLabelProvider()
		{
			 @Override
		      public String getText(Object element) {
				TimeOff p = (TimeOff) element;
		        return p.getStartStr();
			 }
		},
		new StartDateEditingSupport(tv));
		
		tv.createTableViewerColumn("End", 200, 2, new ColumnLabelProvider()
		{
			 @Override
		      public String getText(Object element) {
				TimeOff p = (TimeOff) element;
		        return p.getEndStr();
			 }
		},
	    new EndDateEditingSupport(tv));
		
		tv.createTableViewerColumn("Status", 150, 3, new ColumnLabelProvider()
		{
			 @Override
		      public String getText(Object element) {
				TimeOff p = (TimeOff) element;
		        return p.getStatus();
			 }
		},
		new StatusEditingSupport(tv,tom.getTimeOffStatusOptions())
		);
		
		tv.addSelectionChangedListener(new ISelectionChangedListener()
		{
			@Override
			  public void selectionChanged(SelectionChangedEvent event) {
				timeOffSelectionChanged(tv.getSelection());
			  }
		}
		);
		
		btnDelete.setVisible(false);
		btnUpdate.setVisible(false);

	}
	
	
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	/**
	 * Public wrapper for data initialization.
	 * 
	 * @param data
	 *            Data to pass to the table viewer
	 */
	public void initiateData(ArrayList<Object> data) {
		if (tv != null) {
			modelProvider=new TimeOffModelProvider(data);
			tv.initiateData(modelProvider.getTimeOff());
		}
	}
	
	private void timeOffSelectionChanged(ISelection iSelection)
	{
		 IStructuredSelection selection = (IStructuredSelection)iSelection;
		 current_selection = selection.getFirstElement();
		 toggleButtonVisibility(current_selection!=null);
	//	 updateData();
	}
	
	private void toggleButtonVisibility(boolean visible)
	{
		btnDelete.setVisible(visible);
		btnUpdate.setVisible(visible);
	}
	
	private void updateData()
	{
			getShell().setCursor(new Cursor(getDisplay(), SWT.CURSOR_WAIT));
			tom.updateTimesOff(modelProvider.getTimeOff());
			getShell().setCursor(new Cursor(getDisplay(), SWT.CURSOR_ARROW));
	}
}
