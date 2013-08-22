package com.gzlabs.gzroster.gui;

import java.util.ArrayList;
import java.util.Vector;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Composite;
import com.gzlabs.gzroster.sql.DBObjectType;

/**
 * Context menu base for data manipulation widgets
 * @author apavlune
 *
 */
public abstract class BaseElementMenu extends Composite{
	
	/**
	 * Data management interface.
	 */
	private IItemsManager manager;
	
	/**
	 * Data edit dialog.
	 */
	private BaseItemEditDialog editDialog;
	
	/**
	 * Vector for storing listeners.
	 */
	private Vector<ElementsChangedListener> elementsChangesListeners;
	
	/**
	 * Local menu manager object.
	 */
	private MenuManager mgr;

	/**
	 * Default constructor.  Initializes member variables.
	 * @param parent Parent composite element
	 * @param style SWT style for the composite
	 * @param man Management interface
	 */
	public BaseElementMenu(Composite parent, int style,IItemsManager man) {
		super(parent, style);
		manager = man;		
		elementsChangesListeners=new Vector<ElementsChangedListener>();
		
	}

	/**
	 * Adds a listener to the vector.
	 * @param listener Listener to add.
	 */
	public void addElementsChangedListener(ElementsChangedListener listener)
	{
		elementsChangesListeners.addElement(listener);
	}
	
	/**
	 * Removes a listener from the vector.
	 * @param listener Listener to remove.
	 */
	public void removeElementsChangedListener(ElementsChangedListener listener)
	{
		elementsChangesListeners.removeElement(listener);
	}
	
	/**
	 * Notifies all subscribed listeners of the event.
	 * @param type Object type that has changed.
	 */
	public void triggerElementsChangedListener(DBObjectType type)
	{
		ElementsChangedEvent event=new ElementsChangedEvent(this, type);
		int size=elementsChangesListeners.size();
		for(int i=0;i<size;i++)
		{
			ElementsChangedListener listener=elementsChangesListeners.elementAt(i);
			listener.elementsChanged(event);
		}
	}
	
	/**
	 * Initiates item delete operation.
	 */
	private void deleteItem() {
		String item=getSelection();
		if (MessageDialog.openConfirm(null, "Confirm Delete",
				"Are you sure that you want to delete " + getSelection() + "?")) {
			if (manager != null) {
				manager.deleteItem(manager.getItem(item, getObjectType()), getObjectType());
			}
			updateItems();
			triggerElementsChangedListener(getObjectType());
		}
	}

	/**
	 * Initiates add item operation.
	 */
	protected void addItem() {
		if (editDialog != null) {
			editDialog.open(null);
			if (editDialog.getNewObject() != null && manager != null) {
				manager.insertObject(editDialog.getNewObject(), getObjectType());
				updateItems();
				setMenu(mgr);
				triggerElementsChangedListener(getObjectType());
			}
		}
	}

	/**
	 * Initiates edit item operation.
	 */
	private void editItem() {
		if (editDialog != null) {
			editDialog.open(getItem());
			if (editDialog.getNewObject() != null && manager != null) {
				manager.updateObject(editDialog.getOldObject(),
						editDialog.getNewObject(), getObjectType());
				updateItems();
				setMenu(mgr);
				triggerElementsChangedListener(getObjectType());
			}

		}
	}

	/**
	 * Setter for the edit dialog.
	 * @param dialog Dialog to set.
	 */
	protected void setEditDialog(BaseItemEditDialog dialog) {
		editDialog = dialog;
	}

	/**
	 * Retrieves a database object based on the current selection.
	 * @return DB_Object corresponding to the selected item.
	 */
	public Object getItem() {
		if (manager != null) {
			return manager.getItem(getSelection(), getObjectType());
		}
		return null;
	}

	/**
	 * Sets up actual menu.
	 */
	protected void setUpContextMenu() {
		final Action delete = new Action("Delete") {
			public void run() {
				deleteItem();
			}
		};

		final Action add = new Action("New") {
			public void run() {
				addItem();
			}
		};

		final Action edit = new Action("Edit") {
			public void run() {
				editItem();
			}
		};
		mgr = new MenuManager();
		mgr.setRemoveAllWhenShown(true);

		mgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {

				mgr.add(delete);
				mgr.add(edit);
				mgr.add(add);

				ArrayList<Action> actions = getExtraActions();
				if (actions != null) {
					for (Action action : actions) {
						mgr.add(action);
					}
				}

			}
		});
		setMenu(mgr);		
	}
	
	/**
	 * Items manager accessor
	 * @return Management interface.
	 */
	protected IItemsManager getManager()
	{
		return manager;
	}

	/**
	 * Fetches currently selected item name.
	 * @return Name of the currently selected item.
	 */
	abstract public String getSelection();

	/**
	 * Allows all derived classes to specify a type of object to operate on.
	 * @return Object type
	 */
	abstract protected DBObjectType getObjectType();

	/**
	 * Updates displayed data.
	 */
	abstract protected void updateItems();

	/**
	 * Allows derived class to specify extra menu actions.
	 * @return List of extra menu actions.
	 */
	abstract protected ArrayList<Action> getExtraActions();

	/**
	 * Binds a menu to the actual control.
	 * @param mgr Menu manager to bind.
	 */
	abstract protected void setMenu(final MenuManager mgr);


}
