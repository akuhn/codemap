package ch.unibe.softwaremap.ui;

import org.eclipse.jface.action.Action;

import ch.unibe.softwaremap.Icon;

public class LinkWithSelectionAction extends Action {

	private static final boolean DEFAULT = true;
	private SelectionTracker selectionTracker;

	public LinkWithSelectionAction(SelectionTracker tracker) {
		super("Link with selection", AS_CHECK_BOX);
		selectionTracker = tracker;
		
		setChecked(DEFAULT);
		setImageDescriptor(Icon.getImageDescriptor(Icon.LINKED));
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
