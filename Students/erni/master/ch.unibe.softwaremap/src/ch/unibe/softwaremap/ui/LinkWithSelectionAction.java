package ch.unibe.softwaremap.ui;

import org.eclipse.jface.action.Action;

import ch.unibe.softwaremap.Icon;

public class LinkWithSelectionAction extends Action {

	private SelectionTracker selectionTracker;

	public LinkWithSelectionAction(SelectionTracker tracker) {
		super("Link with selection.", AS_CHECK_BOX);
		
		selectionTracker = tracker;
		setImageDescriptor(Icon.getImageDescriptor(Icon.LINKED));
		setChecked(false);
	}

	@Override
	public void run() {
		System.out.println(isChecked());
	    selectionTracker.setEnabled(isChecked());
	}
	
	

}
