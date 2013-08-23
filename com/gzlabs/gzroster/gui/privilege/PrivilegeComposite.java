package com.gzlabs.gzroster.gui.privilege;

import org.eclipse.swt.widgets.Composite;

import com.xpresstek.gzrosterdata.gui.CheckBoxComposite;
import com.xpresstek.gzrosterdata.gui.IItemsManager;
import com.xpresstek.gzrosterdata.sql.DBObjectType;

public class PrivilegeComposite extends CheckBoxComposite {

	public PrivilegeComposite(Composite parent, int style, IItemsManager man) {
		super(parent, style, man);
		setEditDialog(new PrivilegeEditDialog(getShell()));
	}

	@Override
	protected DBObjectType getObjectType() {
		return DBObjectType.PRIVILEGE;
	}

}
