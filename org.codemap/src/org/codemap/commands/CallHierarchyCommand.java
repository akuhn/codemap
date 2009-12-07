package org.codemap.commands;

import static org.codemap.commands.Commands.makeCommandId;

import org.codemap.CodemapCore;
import org.codemap.MapPerProject;
import org.codemap.callhierarchy.CallHierarchyTracker;
import org.codemap.callhierarchy.LinkWithCallHierarchyAction;
import org.codemap.commands.Commands.Command;

public class CallHierarchyCommand extends Command {

    private static final String CALL_HIERARCHY_KEY = makeCommandId("call_hierarchy");
    private boolean enabled;

    public CallHierarchyCommand(MapPerProject mapPerProject) {
        super(mapPerProject);
        enabled = getMyMap().getPropertyOrDefault(CALL_HIERARCHY_KEY, true);
    }

    public boolean isLinkingEnabled() {
        return enabled;
    }

    public void setIsLinkingEnabled(boolean enabled) {
        this.enabled = enabled;
        applyState();
    }
    
    private void applyState() {
        if (enabled) showFLow();
        else hideFlow();
        getMyMap().setProperty(CALL_HIERARCHY_KEY, enabled);
    }

    private void showFLow() {
        getCallHierarchyTracker().enable();
    }

    private CallHierarchyTracker getCallHierarchyTracker() {
        return CodemapCore.getPlugin().getController().getCallHierarchyTracker();
    }

    private void hideFlow() {
        getCallHierarchyTracker().disable();
    }

    public void apply(LinkWithCallHierarchyAction action) {
        action.setChecked(isLinkingEnabled());
        applyState();
    }    
}
