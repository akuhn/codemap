package org.codemap.mapview;

import static org.codemap.util.CodemapIcons.LINKED;

import org.codemap.util.CodemapIcons;
import org.eclipse.jface.action.Action;

public class LinkWithSelectionAction extends Action {

	private SelectionTracker selectionTracker;
	
	// TODO remember that value when closing the view (have a look at IMemento stuff)
	private static final boolean DEFAULT_CHECKED = true;

	public LinkWithSelectionAction(SelectionTracker tracker) {
		super("Link with Current Selection", AS_CHECK_BOX);
		selectionTracker = tracker;
		setChecked(DEFAULT_CHECKED);
		setImageDescriptor(new CodemapIcons().descriptor(LINKED));
	}

	@Override
	public void setChecked(boolean checked) {
		selectionTracker.setEnabled(checked);
		super.setChecked(checked);
	}

	@Override
	public void run() {
		super.run();		
	    selectionTracker.setEnabled(isChecked());
	}

}
