package org.codemap.callhierarchy;

import static org.codemap.util.CodemapIcons.CALL_HIERARCHY;

import org.codemap.CodemapCore;
import org.codemap.mapview.MenuAction;
import org.codemap.util.CodemapIcons;
import org.eclipse.jface.action.IAction;

public class LinkWithCallHierarchyAction extends MenuAction {


    private CallHierarchyTracker callHierarchyTracker;

    public LinkWithCallHierarchyAction() {
	    super("Link with Call Hierarchy", IAction.AS_CHECK_BOX);
	    setImageDescriptor(new CodemapIcons().descriptor(CALL_HIERARCHY));
	    callHierarchyTracker = CodemapCore.getPlugin().callHierarchyTracker;
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
        return false;
    }

}
