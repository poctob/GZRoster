package com.gzlabs.gzroster.gui.privilege;

import org.eclipse.swt.widgets.Composite;

import com.gzlabs.gzroster.gui.CheckBoxComposite;
import com.gzlabs.gzroster.gui.IItemsManager;
import com.gzlabs.gzroster.sql.DBObjectType;

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
