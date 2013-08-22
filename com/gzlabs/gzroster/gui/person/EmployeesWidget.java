package com.gzlabs.gzroster.gui.person;

import java.util.ArrayList;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;

import com.gzlabs.gzroster.data.Person;
import com.gzlabs.gzroster.gui.BaseListViewerWidget;
import com.gzlabs.gzroster.gui.ElementsChangedEvent;
import com.gzlabs.gzroster.gui.ElementsChangedListener;
import com.gzlabs.gzroster.sql.DBObjectType;
import com.gzlabs.gzroster.gui.CheckBoxComposite;
import com.gzlabs.gzroster.gui.position.PositionsComposite;
import com.gzlabs.gzroster.gui.privilege.PrivilegeComposite;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;

/**
 * Handles employee manipulations
 * 
 * @author apavlune
 * 
 */
public class EmployeesWidget extends BaseListViewerWidget {

	private IEmployeeManager iem;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public EmployeesWidget(Composite parent, int style, IEmployeeManager em) {
		super(parent, style,em);
		List list = getViewer().getList();
		
		setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_BACKGROUND_GRADIENT));
		if (em == null) {
			return;
		}

		iem = em;

		getViewer().getList().setBackground(SWTResourceManager.getColor(255, 255, 255));
		getViewer().getList().setBounds(10, 10, 171, 372);
		setEditDialog(new EmployeeEditDialog(getShell()));
		
		final CheckBoxComposite positionsComposite = new PositionsComposite(this, SWT.NONE, getManager());
		positionsComposite.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_BACKGROUND_GRADIENT));
		positionsComposite.setBounds(187, 26, 277, 268);
		
		final CheckBoxComposite checkBoxComposite = new PrivilegeComposite(this, SWT.NONE, getManager());
		checkBoxComposite.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_BACKGROUND_GRADIENT));
		checkBoxComposite.setBounds(470, 26, 277, 268);
		
		Label lblAllowedPositions = new Label(this, SWT.NONE);
		lblAllowedPositions.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_BACKGROUND_GRADIENT));
		lblAllowedPositions.setAlignment(SWT.CENTER);
		lblAllowedPositions.setBounds(259, 10, 125, 18);
		lblAllowedPositions.setText("Allowed Positions");
		
		Label lblNewLabel = new Label(this, SWT.NONE);
		lblNewLabel.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_BACKGROUND_GRADIENT));
		lblNewLabel.setAlignment(SWT.CENTER);
		lblNewLabel.setBounds(565, 10, 69, 18);
		lblNewLabel.setText("Privileges");
		
		Button btnUpdate = new Button(this, SWT.NONE);
		btnUpdate.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Person old_person=(Person)getItem();
				if(old_person!=null)
				{
					Person new_person=new Person();
					new_person.populateProperties(old_person);

					new_person.setM_positions(positionsComposite.getBoxes());
					new_person.setM_privileges(checkBoxComposite.getBoxes());
					getManager().updateObject(old_person, new_person,getObjectType());
				}
			}
		});
		btnUpdate.setBounds(197, 300, 88, 30);
		btnUpdate.setText("Update");
			
		
		list.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				Person old_person=(Person)getItem();
				if(old_person!=null)
				{				
					positionsComposite.checkBoxes(old_person.getM_positions());
					checkBoxComposite.checkBoxes(old_person.getM_privileges());					
				}
			}
		});
		
		positionsComposite.addElementsChangedListener(new ElementsChangedListener(){

			@Override
			public void elementsChanged(ElementsChangedEvent event) {
				triggerElementsChangedListener(event.getType());
			}			
		}
		);
		
		checkBoxComposite.addElementsChangedListener(new ElementsChangedListener(){

			@Override
			public void elementsChanged(ElementsChangedEvent event) {
				triggerElementsChangedListener(event.getType());
			}			
		}
		);
	}

	@Override
	protected ArrayList<Action> getExtraActions()
	{
		ArrayList <Action> actions=new ArrayList<Action>();
		final Action changePin = new Action("Change Pin") {
			public void run()
			{
				PinDialog pd=new PinDialog(new Shell(), iem, getSelection());
				pd.open();
			}
		};
		actions.add(changePin);
		return actions;
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	@Override
	protected DBObjectType getObjectType() {
		return DBObjectType.PERSON;
	}
}
