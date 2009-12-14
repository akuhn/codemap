package org.codemap.commands;

import org.codemap.CodemapCore;
import org.codemap.MapPerProject;
import org.codemap.mapview.action.CommandAction;
import org.codemap.mapview.action.ShowSelectionAction;
import org.codemap.resources.MapValues;

public class SelectionCommand extends CheckedCommand {
    
    private static final String SELECTION_KEY = makeCommandId("selection");
    
    public SelectionCommand(MapPerProject mapPerProject) {
        super(mapPerProject);
    }
    
    @Override
    protected void applyState() {
        if (isEnabled()) showSelection();
        else hideSelection();
        super.applyState();
    }

    private void hideSelection() {
        getMyMap().getCurrentSelectionOverlay().setEnabled(false);
        triggerChange();        
    }

    private void showSelection() {
        getMyMap().getCurrentSelectionOverlay().setEnabled(true);
        triggerChange();
    }

    private void triggerChange() {
        // FIXME ouch, and again ...
        CodemapCore.getPlugin().getCurrentSelection().triggerChange();
    }

    @Override
    protected String getKey() {
        return SELECTION_KEY;
    }

    @Override
    protected Class<? extends CommandAction> getActionID() {
        return ShowSelectionAction.class;
    }

    @Override
    public void configure(MapValues mapValues) {
        applyState();
    }

}
