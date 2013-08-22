package com.gzlabs.gzroster.gui;

import java.util.ArrayList;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.List;
import org.eclipse.wb.swt.SWTResourceManager;

import com.gzlabs.gzroster.sql.DBObjectType;

/**
 * Base for the list viewer object manipulation widget.
 * 
 * @author apavlune
 * 
 */
public abstract class BaseListViewerWidget extends BaseElementMenu {
	
	/**
	 * Base item list.
	 */
	private List itemsList;
	
	/**
	 * Base item list viewer.
	 */
	private ListViewer itemsListViewer;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public BaseListViewerWidget(Composite parent, int style, IItemsManager man) {
		super(parent, style, man);
		setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_BACKGROUND_GRADIENT));
		
		itemsListViewer = new ListViewer(this, SWT.BORDER
				| SWT.V_SCROLL);
		itemsList = itemsListViewer.getList();
		itemsListViewer.setContentProvider(new ArrayContentProvider());
		if(man!=null)
		{
			itemsListViewer.setInput(man.getData(getObjectType()));		
		}
		setUpContextMenu();	
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
	
	/**
	 * @link {@link BaseElementMenu#getSelection()}
	 */
	@Override
	public String getSelection()
	{
		if(itemsList.getSelectionIndex()>=0)
		{
			return itemsList.getItem(itemsList.getSelectionIndex());
		}
		return null;
	}
	
	/**
	 *  @see BaseElementMenu#getItem()
	 */
	@Override
	public Object getItem() {

		return getManager().getItem(getSelection(), getObjectType());
	}
	
	/**
	 * Viewer accessor
	 * @return Member viewer.
	 */
	public ListViewer getViewer()
	{
		return itemsListViewer;
	}
	
	/**
	 * @see BaseElementMenu#getObjectType()
	 */
	abstract protected DBObjectType getObjectType();
	
	/**
	 * @see BaseElementMenu#getExtraActions()
	 */
	@Override
	protected ArrayList<Action> getExtraActions()
	{
		return null;
	}
	
	/**
	 * @see BaseElementMenu#updateItems()
	 */
	@Override
	 protected void updateItems()
	 {
		 itemsListViewer.setInput(getManager().getData(getObjectType()));
	 }
	 /**
	  * @see BaseElementMenu#setMenu(MenuManager)
	  */
	@Override
	 protected void setMenu(final MenuManager mgr)
	 {
		 itemsListViewer.getControl().setMenu(mgr.createContextMenu(itemsListViewer.getControl()));
	 }
	
}
