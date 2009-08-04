package org.codemap.mapview;

import org.codemap.MapPerProject;
import org.codemap.util.Icons;

public class ForceSelectionAction extends MenuAction {
	
	public static final boolean DEFAULT_VALUE = true;
	private MapSelectionProvider selectionProvider;

	public ForceSelectionAction(MapSelectionProvider provider) {
		super("Force Package Explorer Selection", AS_CHECK_BOX);
		selectionProvider = provider;
		setChecked(DEFAULT_VALUE);
		setImageDescriptor(Icons.getImageDescriptor(Icons.PACKAGE_HIERARCHY));
	}

	@Override
	public void setChecked(boolean checked) {
		super.setChecked(checked);
		selectionProvider.setForceEnabled(checked);
	}

	@Override
	public void run() {
		super.run();
	    selectionProvider.setForceEnabled(isChecked());
	}

	@Override
	public void configureAction(MapPerProject map) {
		// TODO DEIF
		
	}

	@Override
	protected String getKey() {
		// FIXME damn, we do not want them saved by mapinstance ...
		return "";
	}

	@Override
	protected boolean isDefaultChecked() {
		return true;
	}	

}
