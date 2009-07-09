package org.codemap.mapview;

import org.codemap.util.Icons;
import org.eclipse.jface.action.Action;


public class LinkWithSelectionAction extends Action {

	private static final boolean DEFAULT = true;
	private SelectionTracker selectionTracker;

	public LinkWithSelectionAction(SelectionTracker tracker) {
		super("Show Selection", AS_CHECK_BOX);
		selectionTracker = tracker;
		
		setChecked(DEFAULT);
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
