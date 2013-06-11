package com.gzlabs.gzroster.gui;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.wb.swt.SWTResourceManager;


public class PositionsWidget extends Composite {

	
	private final FormToolkit formToolkit = new FormToolkit(
			Display.getDefault());
	
	private IPositionsManager ipm;
	
	/************************************************************/
	// Positions Widget components
	private List positionsList;

	private Button positionsDeleteButton;
	private Button positionsEditButton;
	private Button positionsClearButton;
	private Button positionsCancelButton;

	private Text positionNameText;
	private Text positionNoteText;

	private Label positionLblName;
	private Label positionLblNote;		
	/************************************************************/
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public PositionsWidget(Composite parent, int style, IPositionsManager pm) {
		super(parent, style);
		ipm=pm;
		
		ListViewer positionsListViewer = new ListViewer(this,
				SWT.BORDER | SWT.V_SCROLL);
		positionsList = positionsListViewer.getList();
		positionsList.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (positionsList.getSelectionIndex() == -1) {
					positionsDeleteButton.setEnabled(false);
				} else {
					positionsDeleteButton.setEnabled(true);
					clearPositionData();
					if(ipm != null)
					{
						ipm.setPositionDetails(positionsList.getSelection()[0]);
					}				
					togglePositionEdit(true);
				}
			}

		});
		positionsList.setBounds(10, 46, 143, 248);

		positionsEditButton = formToolkit.createButton(this,
				"Save", SWT.NONE);
		positionsEditButton.setEnabled(false);
		positionsEditButton.setImage(SWTResourceManager.getImage(
				MainWindow.class,
				"/javax/swing/plaf/metal/icons/ocean/floppy.gif"));
		positionsEditButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (ipm!=null)
				{
					ipm.processPositionData();
					clearPositionData();
					ipm.populateData();
				}
			}
		});
		positionsEditButton.setBounds(334, 223, 77, 30);

		positionsDeleteButton = formToolkit.createButton(this,
				"Delete", SWT.NONE);
		positionsDeleteButton.setEnabled(false);
		positionsDeleteButton.setImage(SWTResourceManager.getImage(
				MainWindow.class,
				"/org/eclipse/jface/dialogs/images/message_error.gif"));
		positionsDeleteButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (MessageDialog.openConfirm(null, "Confirm Delete",
						"Are you sure that you want to delete this position?")) {
					if(ipm !=null)
					{
						ipm.deletePosition(positionsList.getSelection());
						clearPositionData();
						ipm.populateData();
					}
				}
			}
		});
		positionsDeleteButton.setBounds(10, 300, 88, 30);

		Button postionNew = new Button(this, SWT.NONE);
		postionNew.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				togglePositionEdit(true);
				clearPositionData();
				positionsList.deselectAll();
			}
		});
		postionNew.setImage(SWTResourceManager.getImage(MainWindow.class,
				"/javax/swing/plaf/metal/icons/ocean/file.gif"));
		postionNew.setBounds(10, 10, 88, 30);
		formToolkit.adapt(postionNew, true, true);
		postionNew.setText("New");

		positionNameText = new Text(this, SWT.BORDER);
		positionNameText.setEnabled(false);
		positionNameText.setBounds(168, 46, 130, 28);
		formToolkit.adapt(positionNameText, true, true);

		positionLblName = new Label(this, SWT.NONE);
		positionLblName.setEnabled(false);
		positionLblName.setBounds(168, 22, 69, 18);
		formToolkit.adapt(positionLblName, true, true);
		positionLblName.setText("Name");

		positionNoteText = new Text(this, SWT.BORDER | SWT.MULTI);
		positionNoteText.setEnabled(false);
		positionNoteText.setBounds(168, 112, 243, 86);
		formToolkit.adapt(positionNoteText, true, true);

		positionLblNote = new Label(this, SWT.NONE);
		positionLblNote.setEnabled(false);
		positionLblNote.setText("Note");
		positionLblNote.setBounds(168, 88, 69, 18);
		formToolkit.adapt(positionLblNote, true, true);

		positionsClearButton = new Button(this, SWT.NONE);
		positionsClearButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				clearPositionData();
			}
		});
		positionsClearButton.setEnabled(false);
		positionsClearButton.setImage(SWTResourceManager.getImage(
				MainWindow.class,
				"/com/sun/java/swing/plaf/motif/icons/Error.gif"));
		positionsClearButton.setBounds(168, 223, 77, 30);
		formToolkit.adapt(positionsClearButton, true, true);
		positionsClearButton.setText("Clear");

		positionsCancelButton = new Button(this, SWT.NONE);
		positionsCancelButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				clearPositionData();
				togglePositionEdit(false);
			}
		});
		positionsCancelButton.setEnabled(false);
		positionsCancelButton.setImage(SWTResourceManager.getImage(
				MainWindow.class,
				"/javax/swing/plaf/metal/icons/ocean/close.gif"));
		positionsCancelButton.setBounds(251, 223, 77, 30);
		formToolkit.adapt(positionsCancelButton, true, true);
		positionsCancelButton.setText("Cancel");

	}

	/**
	 * Clears position data controls
	 */
	private void clearPositionData() {
		positionNameText.setText("");
		positionNoteText.setText("");
	}
	
	public void clearPositionsList()
	{
		positionsList.removeAll();
	}
	
	public void addPosition(String s)
	{
		positionsList.add(s);
	}
	
	public void setPositionNameText(String s)
	{
		positionNameText.setText(s);
	}
	
	public void setPositionNoteText(String s)
	{
		positionNoteText.setText(s);
	}
	
	public String getPositionNameText()
	{
		return positionNameText.getText();
	}
	
	public String getPositionNoteText()
	{
		return positionNoteText.getText();
	}
	
	public int getSelectionIndex()
	{
		return positionsList.getSelectionIndex();
	}
	/**
	 * Enables and disables position edit controls.
	 * 
	 * @param enable
	 *            Specify to enable or disable
	 */
	private void togglePositionEdit(boolean enable) {
		positionsEditButton.setEnabled(enable);
		positionsClearButton.setEnabled(enable);
		positionsCancelButton.setEnabled(enable);

		positionNameText.setEnabled(enable);
		positionNoteText.setEnabled(enable);

		positionLblName.setEnabled(enable);
		positionLblNote.setEnabled(enable);
	}
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

}
