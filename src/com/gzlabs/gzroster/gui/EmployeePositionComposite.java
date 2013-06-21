package com.gzlabs.gzroster.gui;

import java.util.ArrayList;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.custom.ScrolledComposite;

/**
 * Provides a checkbox collection with positions that an employee is allowed to
 * hold.
 * 
 * @author apavlune
 * 
 */
public class EmployeePositionComposite extends Composite {
	private static final int Y_POS_FIRST = 10;
	private static final int Y_SPACER = 28;
	// Checkbox collection
	private ArrayList<Button> pos_boxes;
	private ScrolledComposite scrolledComposite;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public EmployeePositionComposite(Composite parent, int style) {
		super(parent, style);

		scrolledComposite = new ScrolledComposite(this, SWT.BORDER
				| SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setBounds(10, 10, 161, 361);
		scrolledComposite.setExpandHorizontal(true);
		// scrolledComposite.setExpandVertical(true);
		pos_boxes = new ArrayList<Button>();
	}

	/**
	 * Deletes all positions from the list
	 */
	public void removeAll() {
		for (int i = 0; i < pos_boxes.size(); i++) {
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

		button.setBounds(10, y_pos, 111, 22);
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

	@Override
	public void setEnabled(boolean enabled) {
		scrolledComposite.setEnabled(enabled);
		for (int i = 0; i < pos_boxes.size(); i++) {
			pos_boxes.get(i).setEnabled(enabled);
		}
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
	 * Fetches boxes labels
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
}
