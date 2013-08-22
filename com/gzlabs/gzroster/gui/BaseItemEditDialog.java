package com.gzlabs.gzroster.gui;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;

/**
 * A base for the edit dialog for data objects.
 * @author apavlune
 *
 */
public abstract class BaseItemEditDialog extends Dialog {

	/**
	 * Current object.
	 */
	protected Object current_object;
	
	/**
	 * Old object
	 */
	private Object old_object;
	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public BaseItemEditDialog(Shell parentShell) {
		super(parentShell);
	}

	/**
	 * Opens a dialog with object's properties.
	 * @param obj Object to used for data population.
	 * @return Dialog open code.
	 */
	public int open(Object obj)
	{
		old_object=obj;
		return super.open();
	}
	/**
	 * Create contents of the button bar.
	 * Creates clear, submit and cancel buttons.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		Button button = createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				populateNewObject();
				close();
			}
		});
		button.setText("Submit");
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
		Button button_1 = createButton(parent, IDialogConstants.CLIENT_ID+1,
				"Clear", false);
		button_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				clear();
			}
		});
		
		parent.setBackground(getBackgroundColor());
	}
	
	/**
	 * Derived classes implement this method to clear controls.
	 */
	protected abstract void clear();
	
	/**
	 * Checks if this is new object creation.
	 * @return True if no previous object was specified.
	 */
	protected boolean isNew()
	{
		return old_object==null;
	}
	
	/**
	 * Accessor for the new object.
	 * @return New Object.
	 */
	public Object getNewObject()
	{
		return current_object;
	}
	
	/**
	 * Accessor for the old object.
	 * @return Old Object.
	 */
	protected Object getOldObject()
	{
		return old_object;
	}
	
	/**
	 * Derived classes use this method to populate controls with data.
	 */
	protected abstract void populateControls();
	
	
	/**
	 * Derived classes populate new object.
	 */
	protected abstract void populateNewObject();
	
	/**
	 * Custom background color
	 * @return Background color
	 */
	protected abstract Color getBackgroundColor();
	
	/**
	 * Overriden to prevent default dispose behavior.
	 */
	@Override
	protected void okPressed(){}
	
	
}
