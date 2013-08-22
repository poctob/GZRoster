package com.gzlabs.gzroster.gui.privilege;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.gzlabs.gzroster.data.Privilege;
import com.gzlabs.gzroster.gui.BaseItemEditDialog;
import com.gzlabs.utils.WidgetUtilities;

import org.eclipse.swt.SWT;
import org.eclipse.wb.swt.SWTResourceManager;

public class PrivilegeEditDialog extends BaseItemEditDialog {

	private Text nameText;
	private Label lblName;
	
	public PrivilegeEditDialog(Shell parentShell) {
		super(parentShell);
		setShellStyle(SWT.APPLICATION_MODAL);
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
		parent.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_BACKGROUND_GRADIENT));
		Composite container = (Composite) super.createDialogArea(parent);
		container.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_BACKGROUND_GRADIENT));
		GridLayout gridLayout = (GridLayout) container.getLayout();
		gridLayout.verticalSpacing = 5;
		gridLayout.horizontalSpacing = 5;
		gridLayout.marginBottom = 5;
		gridLayout.marginTop = 5;
		gridLayout.marginRight = 5;
		gridLayout.marginLeft = 5;
		gridLayout.numColumns = 2;
		
		lblName = new Label(container, SWT.NONE);
		lblName.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_BACKGROUND_GRADIENT));
		lblName.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false,
				false, 1, 1));
		lblName.setText("Name");
		
		nameText = new Text(container, SWT.BORDER);
		GridData gd_nameText = new GridData(SWT.FILL, SWT.CENTER, false,
				false, 1, 1);
		gd_nameText.widthHint = 277;
		nameText.setLayoutData(gd_nameText);
		
		populateControls();
		
		return container;
	}
	@Override
	protected void populateControls() {		
		 Privilege privilege=( Privilege) getOldObject();
		if(privilege!=null)
		{
			WidgetUtilities.safeTextSet(nameText, privilege.getName());


		}
	}
	
	@Override
	protected void populateNewObject() {	
		current_object=new Privilege(nameText.getText());
	}

	@Override
	protected void clear() {
		nameText.setText("");	
	}
	
	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(360, 127);
	}

	@Override
	protected Color getBackgroundColor() {		
		return SWTResourceManager.getColor(SWT.COLOR_TITLE_BACKGROUND_GRADIENT);
	}

}
