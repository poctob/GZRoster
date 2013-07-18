package com.gzlabs.gzroster.gui.time_off;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableColumn;

import com.gzlabs.gzroster.gui.GZTableViewer;

/**
 * Customized table viewer. Provides additional functionality for cell
 * manipulation.
 * 
 * @author apavlune
 * 
 */
public class TimeOffTableViewer extends GZTableViewer {
	
	/**
	 * Default constructor. Initializes member variables.
	 * 
	 * @param parent
	 *            Composite parent of this control.
	 * @param dutyupdater
	 *            Data updater interface.
	 */
	public TimeOffTableViewer(Composite parent) {
		super(parent);
		comparator=new TimeOffViewerComparator();
		this.setComparator(comparator);
	}
	
	@Override
	protected SelectionAdapter getSelectionAdapter(final TableColumn column,
		      final int index) {
		    SelectionAdapter selectionAdapter = new SelectionAdapter() {
		      @Override
		      public void widgetSelected(SelectionEvent e) {
		    	((TimeOffViewerComparator)comparator).setColumn(index);
		        int dir = ((TimeOffViewerComparator)comparator).getDirection();
		        getTable().setSortDirection(dir);
		        getTable().setSortColumn(column);
		        refresh();
		      }
		    };
		    return selectionAdapter;
		  }

}
