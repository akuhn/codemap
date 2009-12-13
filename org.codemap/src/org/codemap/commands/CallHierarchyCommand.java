package org.codemap.commands;

import static org.codemap.commands.CompositeCommand.makeCommandId;

import org.codemap.CodemapCore;
import org.codemap.MapPerProject;
import org.codemap.callhierarchy.CallHierarchyTracker;
import org.codemap.mapview.action.CommandAction;
import org.codemap.mapview.action.LinkWithCallHierarchyAction;
import org.codemap.resources.MapValues;

public class CallHierarchyCommand extends CheckedCommand {

    private static final String CALL_HIERARCHY_KEY = makeCommandId("call_hierarchy");

    public CallHierarchyCommand(MapPerProject mapPerProject) {
        super(mapPerProject);
    }

    @Override
    protected void applyState() {
        if (isEnabled()) showFLow();
        else hideFlow();
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

    @Override
    protected String getKey() {
        return CALL_HIERARCHY_KEY;
    }

    @Override
    protected Class<? extends CommandAction> getActionID() {
        return LinkWithCallHierarchyAction.class;
    }

    @Override
    public void configure(MapValues mapValues) {
        applyState();
    }
}
