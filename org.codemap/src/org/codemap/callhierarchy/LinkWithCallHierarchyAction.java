package org.codemap.callhierarchy;

import static org.codemap.util.CodemapIcons.CALL_HIERARCHY;

import org.codemap.mapview.action.MenuAction;
import org.codemap.util.CodemapIcons;
import org.eclipse.jface.action.IAction;

public class LinkWithCallHierarchyAction extends MenuAction {


    private CallHierarchyTracker callHierarchyTracker;

    public LinkWithCallHierarchyAction(CallHierarchyTracker tracker) {
	    super("Link with Call Hierarchy", IAction.AS_CHECK_BOX);
	    setImageDescriptor(CodemapIcons.descriptor(CALL_HIERARCHY));
	    callHierarchyTracker = tracker;
	}

    @Override
	public void run() {
        super.run();
		if (isChecked()) showFLow();
		else hideFlow();
	}
	
	private void showFLow() {
	    callHierarchyTracker.enable();
	}

	private void hideFlow() {
	    callHierarchyTracker.disable();
	}

    @Override
    protected String getKey() {
        return "show_flow";
    }

    @Override
    protected boolean isDefaultChecked() {
        return true;
    }

}
