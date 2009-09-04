package org.codemap.mapview;

import static org.codemap.util.CodemapIcons.FORCE_SELECTION;

import org.codemap.util.CodemapIcons;
import org.eclipse.jface.action.Action;

public class ForceSelectionAction extends Action {
	
	public static final boolean DEFAULT_CHECKED = true;
	private MapSelectionProvider selectionProvider;

	public ForceSelectionAction(MapSelectionProvider provider) {
		super("Force Package Explorer Selection", AS_CHECK_BOX);
		selectionProvider = provider;
		setChecked(DEFAULT_CHECKED);
		setImageDescriptor(new CodemapIcons().descriptor(FORCE_SELECTION));
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

}
