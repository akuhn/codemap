package org.codemap.mapview;

import org.codemap.util.Icons;
import org.eclipse.jface.action.Action;


public class LinkWithSelectionAction extends Action {

	public static final boolean DEFAULT_VALUE = true;
	private SelectionTracker selectionTracker;

	public LinkWithSelectionAction(SelectionTracker tracker) {
		super("Link with Current Selection", AS_CHECK_BOX);
		selectionTracker = tracker;
		setChecked(DEFAULT_VALUE);
		setImageDescriptor(Icons.getImageDescriptor(Icons.LINKED));
	}

	@Override
	public void setChecked(boolean checked) {
		super.setChecked(checked);
		selectionTracker.setEnabled(checked);
	}

	@Override
	public void run() {
	    selectionTracker.setEnabled(isChecked());
	}
}
