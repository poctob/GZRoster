package com.gzlabs.gzroster.gui;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.wb.swt.SWTResourceManager;

public class DetailsTableViewerDialogCellEditor extends DialogCellEditor{

	private boolean isConfirmed;
	
	public DetailsTableViewerDialogCellEditor(Composite parent)
	{
		super(parent);
		setConfirmed(false);
	}
	
    @Override
    protected Button createButton(Composite parent) {
           Button button = super.createButton(parent);
           button.setText("");
           button.setImage(SWTResourceManager.getImage(DetailsTableViewerDialogCellEditor.class, "/org/eclipse/jface/dialogs/images/message_error.gif"));
           return button;
    }
    
	@Override
	protected Object openDialogBox(Control cellEditorWindow) {	
		setConfirmed(MessageDialog.openConfirm(cellEditorWindow.getShell(), "Confirm Delete", "Are you sure that you want to delete this this?"));
		return null;
	}

	public boolean isConfirmed() {
		return isConfirmed;
	}

	public void setConfirmed(boolean isConfirmed) {
		this.isConfirmed = isConfirmed;
	}

}
