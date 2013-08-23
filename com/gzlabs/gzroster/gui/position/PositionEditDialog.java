package com.gzlabs.gzroster.gui.position;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.xpresstek.gzrosterdata.Position;
import com.xpresstek.gzrosterdata.gui.BaseItemEditDialog;
import com.gzlabs.utils.WidgetUtilities;

import org.eclipse.swt.SWT;
import org.eclipse.wb.swt.SWTResourceManager;

public class PositionEditDialog extends BaseItemEditDialog {

	private Text nameText;
	private Text noteText;
	private Label lblName;
	private Label lblNote;
	
	public PositionEditDialog(Shell parentShell) {
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
		nameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
				false, 1, 1));
		
		lblNote = new Label(container, SWT.NONE);
		lblNote.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_BACKGROUND_GRADIENT));
		lblNote.setText("Note");
		lblNote.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false,
				false, 1, 1));
		
		noteText = new Text(container, SWT.BORDER | SWT.MULTI);
		GridData gd_addressText = new GridData(SWT.FILL, SWT.TOP, false,
				false, 1, 1);
		gd_addressText.widthHint = 302;
		gd_addressText.heightHint = 100;
		noteText.setLayoutData(gd_addressText);
		populateControls();
		
		return container;
	}
	@Override
	protected void populateControls() {		
		Position position=(Position) getOldObject();
		if(position!=null)
		{
			WidgetUtilities.safeTextSet(nameText, position.getName());
			WidgetUtilities.safeTextSet(noteText, position.getNote());

		}
	}
	
	@Override
	protected void populateNewObject() {	
		current_object=new Position(nameText.getText(),
				noteText.getText());
	}

	@Override
	protected void clear() {
		nameText.setText("");
		noteText.setText("");		
	}
	
	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(446, 233);
	}

	@Override
	protected Color getBackgroundColor() {		
		return SWTResourceManager.getColor(SWT.COLOR_TITLE_BACKGROUND_GRADIENT);
	}

}
