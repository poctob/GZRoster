package com.gzlabs.gzroster.gui.position;

import org.eclipse.swt.widgets.Composite;
import com.gzlabs.gzroster.gui.CheckBoxComposite;
import com.gzlabs.gzroster.gui.IItemsManager;
import com.gzlabs.gzroster.sql.DBObjectType;

public class PositionsComposite extends CheckBoxComposite {

	
	public PositionsComposite(Composite parent, int style, IItemsManager man) {
		super(parent, style, man);
		setEditDialog(new PositionEditDialog(getShell()));
	}

	@Override
	protected DBObjectType getObjectType() {
		return DBObjectType.POSITION;
	}

}
