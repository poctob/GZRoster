package com.gzlabs.gzroster.gui.time_off;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;

import com.gzlabs.gzroster.data.TimeOff;

public class TimeOffViewerComparator extends ViewerComparator {
	 private int propertyIndex;
	  private static final int DESCENDING = 1;
	  private int direction = DESCENDING;

	  public TimeOffViewerComparator() {
	    this.propertyIndex = 0;
	    direction = DESCENDING;
	  }

	  public int getDirection() {
	    return direction == 1 ? SWT.DOWN : SWT.UP;
	  }

	  public void setColumn(int column) {
	    if (column == this.propertyIndex) {
	      // Same column as last sort; toggle the direction
	      direction = 1 - direction;
	    } else {
	      // New column; do an ascending sort
	      this.propertyIndex = column;
	      direction = DESCENDING;
	    }
	  }

	  @Override
	  public int compare(Viewer viewer, Object e1, Object e2) {
	    TimeOff p1 = (TimeOff) e1;
	    TimeOff p2 = (TimeOff) e2;
	    int rc = 0;
	    switch (propertyIndex) {
	    case 0:
	      rc = p1.getName().compareTo(p2.getName());
	      break;
	    case 1:
	      rc = p1.getStart().compareTo(p2.getStart());
	      break;
	    case 2:
	      rc = p1.getEnd().compareTo(p2.getEnd());
	      break;
	    case 3:
	       rc = p1.getStatus().compareTo(p2.getStatus());
	      break;
	    default:
	      rc = 0;
	    }
	    // If descending order, flip the direction
	    if (direction == DESCENDING) {
	      rc = -rc;
	    }
	    return rc;
	  }
}
