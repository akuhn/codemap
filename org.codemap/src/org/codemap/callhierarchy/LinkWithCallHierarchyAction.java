package org.codemap.callhierarchy;

import static org.codemap.util.CodemapIcons.CALL_HIERARCHY;

import org.codemap.MapPerProject;
import org.codemap.commands.CallHierarchyCommand;
import org.codemap.mapview.MapController;
import org.codemap.mapview.action.MenuAction;
import org.codemap.util.CodemapIcons;
import org.eclipse.jface.action.IAction;

public class LinkWithCallHierarchyAction extends MenuAction {

    private CallHierarchyCommand callHierarchyCommand;

    public LinkWithCallHierarchyAction(MapController theController) {
	    super("Link with Call Hierarchy", IAction.AS_CHECK_BOX, theController);
	    setImageDescriptor(CodemapIcons.descriptor(CALL_HIERARCHY));
	}

    @Override
	public void run() {
        callHierarchyCommand.setIsLinkingEnabled(isChecked());
	}

    @Override
    public void configureAction(MapPerProject map) {
        callHierarchyCommand = map.getCommands().getCallHierarchyCommand();
        callHierarchyCommand.apply(this);
    }
}
