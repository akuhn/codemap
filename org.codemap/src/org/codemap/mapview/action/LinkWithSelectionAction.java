package org.codemap.mapview.action;

import static org.codemap.util.CodemapIcons.LINKED;

import org.codemap.mapview.SelectionTracker;
import org.codemap.util.CodemapIcons;
import org.codemap.util.Tag;
import org.eclipse.jface.action.Action;
import org.eclipse.ui.IMemento;

public class LinkWithSelectionAction extends Action {

	private SelectionTracker selectionTracker;
	
	// TODO remember that value when closing the view (have a look at IMemento stuff)
	private static final boolean DEFAULT_CHECKED = true;

	public LinkWithSelectionAction(SelectionTracker tracker, IMemento memento) {
		super("Link with Current Selection", AS_CHECK_BOX);
		selectionTracker = tracker;
		setChecked(DEFAULT_CHECKED);
		setImageDescriptor(new CodemapIcons().descriptor(LINKED));
		init(memento);
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

	protected void init(IMemento memento) {
	    if (memento==null) return;
	    Boolean checked = memento.getBoolean(Tag.LINK_SELECTION);
	    if (checked != null) setChecked(checked);
	}
	
    public void saveState(IMemento memento) {
        memento.putBoolean(Tag.LINK_SELECTION, isChecked());
    }

}
