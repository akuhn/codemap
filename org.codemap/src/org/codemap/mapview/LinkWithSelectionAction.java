package org.codemap.mapview;

import org.codemap.util.Icons;

public class LinkWithSelectionAction extends MenuAction {

	private SelectionTracker selectionTracker;

	public LinkWithSelectionAction(SelectionTracker tracker) {
		super("Link with Current Selection", AS_CHECK_BOX);
		selectionTracker = tracker;
		setChecked(isDefaultChecked());
		setImageDescriptor(Icons.getImageDescriptor(Icons.LINKED));
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

	@Override
	protected String getKey() {
		// FIXME damn, we do not want them saved by mapinstance ...		
		return "link_with_selection";
	}

	@Override
	protected boolean isDefaultChecked() {
		return true;
	}
}
