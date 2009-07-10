package org.codemap.mapview;

import org.codemap.util.Icons;
import org.eclipse.jface.action.Action;

public class ForceSelectionAction extends Action {
	
	public static final boolean DEFAULT_VALUE = true;
	private MapSelectionProvider selectionProvider;

	public ForceSelectionAction(MapSelectionProvider provider) {
		super("Force Package Explorer Selection", AS_CHECK_BOX);
		selectionProvider = provider;
		setChecked(DEFAULT_VALUE);
		setImageDescriptor(Icons.getImageDescriptor(Icons.PACKAGE));
	}

	@Override
	public void setChecked(boolean checked) {
		super.setChecked(checked);
		selectionProvider.setForceEnabled(checked);
	}

	@Override
	public void run() {
	    selectionProvider.setForceEnabled(isChecked());
	}	

}
