package com.gzlabs.gzroster.gui;

import java.util.ArrayList;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import com.gzlabs.gzroster.sql.DBObjectType;
import com.gzlabs.utils.WidgetUtilities;

/**
 * Provides a checkbox collection with positions that an employee is allowed to
 * hold.
 * 
 * @author apavlune
 * 
 */
public abstract class CheckBoxComposite extends BaseElementMenu {
	
	/**
	 * Location of the first checkbox.
	 */
	private static final int Y_POS_FIRST = 10;
	
	/**
	 * Spacing between objects
	 */
	private static final int Y_SPACER = 28;
	
	// Checkbox collection
	private ArrayList<Button> pos_boxes;
	
	/**
	 * Composite for checkboxes
	 */
	private ScrolledComposite scrolledComposite;
	
	/**
	 * Currently selected item.
	 */
	private String current_selection;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public CheckBoxComposite(Composite parent, int style, IItemsManager man) {
		super(parent, style, man);

		scrolledComposite = new ScrolledComposite(this, SWT.BORDER
				| SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setBounds(10, 10, 260, 246);
		scrolledComposite.setExpandHorizontal(true);		
		pos_boxes = new ArrayList<Button>();
		if(man!=null)
		{
			updateItems();
		}	
		setUpContextMenu();
		

		final Action add = new Action("New") {
			public void run() {
				addItem();
			}
		};
		
		final MenuManager mgr_new = new MenuManager();
		mgr_new.setRemoveAllWhenShown(true);

		mgr_new.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {

				mgr_new.add(add);
			}
		});
		
		scrolledComposite.setMenu(mgr_new.createContextMenu(scrolledComposite));
		
	}

	/**
	 * Deletes all positions from the list
	 */
	public void removeAll() {
		for (int i = 0; i < pos_boxes.size(); i++) {
			pos_boxes.get(i).dispose();
			pos_boxes.remove(i);			
		}
	}

	/**
	 * Adds new button to the list
	 * 
	 * @param label
	 *            Button label
	 */
	public void addButton(String label) {
		if (buttonExist(label)) {
			return;
		}
		Button button = new Button(scrolledComposite, SWT.CHECK);

		int y_pos = Y_POS_FIRST;
		if (pos_boxes.size() > 0) {
			Button btn = pos_boxes.get(pos_boxes.size() - 1);
			y_pos = btn.getBounds().y + Y_SPACER;
		}

		button.setBounds(10, y_pos, 250, 22);
		WidgetUtilities.safeButtonSet(button, label);
		pos_boxes.add(button);
	}

	/**
	 * Checks if button is already in the list
	 * 
	 * @param label
	 *            Name to search for
	 * @return True is it exists
	 */
	private boolean buttonExist(String label) {
		if (label != null && pos_boxes != null) {
			for (Button b : pos_boxes) {
				if (b != null && b.getText().equals(label)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Checks specified boxes
	 * 
	 * @param ids
	 *            List of boxes to check
	 */
	public void checkBoxes(ArrayList<String> ids) {
		unCheckAll();

		if (ids != null && pos_boxes != null) {
			for (String s : ids) {
				for (Button b : pos_boxes) {
					if (b != null && b.getText().equals(s))
						b.setSelection(true);
				}

			}
		}
	}

	/**
	 * Fetches box labels
	 * 
	 * @return String list of box labels
	 */
	public ArrayList<String> getBoxes() {
		ArrayList<String> retval = new ArrayList<String>();
		if (pos_boxes != null) {
			for (Button b : pos_boxes) {
				if (b != null && b.getSelection()) {
					retval.add(b.getText());
				}
			}
		}
		return retval;
	}

	/**
	 * Checks all boxes
	 */
	public void checkAll() {
		if (pos_boxes != null) {
			for (Button b : pos_boxes) {
				if (b != null)
					b.setSelection(true);
			}
		}
	}

	/**
	 * Unchecks all boxes
	 */
	public void unCheckAll() {
		if (pos_boxes != null) {
			for (Button b : pos_boxes) {
				if (b != null)
					b.setSelection(false);
			}
		}
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	/**
	 * @see BaseElementMenu#getSelection()
	 */
	@Override
	public String getSelection() {		
		return current_selection;
	}

	@Override
	abstract protected DBObjectType getObjectType();

	/**
	 * @see BaseElementMenu#updateItems()
	 */
	@Override
	protected void updateItems() {
		removeAll();
		pos_boxes.clear();
		ArrayList<String> elements=getManager().getData(getObjectType());
		if(elements!=null)
		{
			for(String s:elements)
			{
				addButton(s);
			}
		}
		
	}

	/**
	 * @see BaseElementMenu#getExtraActions()
	 */
	@Override
	protected ArrayList<Action> getExtraActions() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Adds menu to the check box elements.
	 * @see BaseElementMenu#setMenu(MenuManager)
	 */
	@Override
	protected void setMenu(MenuManager mgr) {
		if(pos_boxes != null)
		{
			for(final Button b:pos_boxes)
			{
				b.setMenu(mgr.createContextMenu(b));
				
				/**
				 * This focus listener captures 
				 * currently selected item.
				 */
				b.addFocusListener(new FocusListener(){

					@Override
					public void focusGained(FocusEvent e) {
						current_selection=b.getText();						
					}

					@Override
					public void focusLost(FocusEvent e) {
						current_selection="";
						
					}
					
				});			
			}
		}		
		
	}
}
