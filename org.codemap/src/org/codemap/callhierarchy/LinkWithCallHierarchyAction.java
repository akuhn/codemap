package org.codemap.callhierarchy;

import static org.codemap.util.CodemapIcons.CALL_HIERARCHY;

import org.codemap.mapview.MapController;
import org.codemap.mapview.action.MenuAction;
import org.codemap.util.CodemapIcons;
import org.eclipse.jface.action.IAction;

public class LinkWithCallHierarchyAction extends MenuAction {

    public LinkWithCallHierarchyAction(MapController theController) {
	    super("Link with Call Hierarchy", IAction.AS_CHECK_BOX, theController);
	    setImageDescriptor(CodemapIcons.descriptor(CALL_HIERARCHY));
	}

    @Override
	public void run() {
        super.run();
		if (isChecked()) showFLow();
		else hideFlow();
	}
	
	private void showFLow() {
	    getCallHierarchyTracker().enable();
	}

	private CallHierarchyTracker getCallHierarchyTracker() {
	    return getController().getCallHierarchyTracker();
    }

    private void hideFlow() {
	    getCallHierarchyTracker().disable();
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
