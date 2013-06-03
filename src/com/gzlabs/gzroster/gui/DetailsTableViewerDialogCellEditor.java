package com.gzlabs.gzroster.gui;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.wb.swt.SWTResourceManager;

/**
 * Provides editing capabilities for the Details table.
 * @author apavlune
 *
 */
public class DetailsTableViewerDialogCellEditor extends DialogCellEditor{

	
	final private String BUTTON_IMAGE_PATH="/org/eclipse/jface/dialogs/images/message_error.gif";
	final private String DIALOG_TITLE= "Confirm Delete";
	final private String DIALOG_MESSAGE="Are you sure that you want to delete this item?";
	
	/**
	 * Default constructor, inherited from the parent.
	 * @param parent Composite that this control belongs to.
	 */
	public DetailsTableViewerDialogCellEditor(Composite parent)
	{
		super(parent);
	}
	
	/**
	 * Creates button for this control
	 */
    @Override
    protected Button createButton(Composite parent) {
           Button button = super.createButton(parent);
           button.setText("");
           button.setImage(SWTResourceManager.getImage(DetailsTableViewerDialogCellEditor.class, BUTTON_IMAGE_PATH));
           return button;
    }
    
    /**
     * When the button is clicked, this dialog box is presented.
     */
	@Override
	protected Object openDialogBox(Control cellEditorWindow) {	
		if(MessageDialog.openConfirm(cellEditorWindow.getShell(), DIALOG_TITLE, DIALOG_MESSAGE))
		{
			fireApplyEditorValue();
		}
		else
		{
			fireCancelEditor();
		}
		return null;
	}



}
