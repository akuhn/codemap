package org.codemap.commands;

import org.codemap.CodemapCore;
import org.codemap.MapPerProject;
import org.codemap.mapview.action.CommandAction;
import org.codemap.mapview.action.ShowYouAreHereAction;
import org.codemap.resources.MapValues;

public class YouAreHereCommand extends CheckedCommand {
    
    private static final String YOUR_ARE_HERE_KEY = makeCommandId("you_are_here");

    public YouAreHereCommand(MapPerProject mapPerProject) {
        super(mapPerProject);
    }
    
    @Override
    protected void applyState() {
        super.applyState();
        if (isEnabled()) showYouAreHere();
        else hideYouAreHere();
    }

    private void hideYouAreHere() {
        getMyMap().getYouAreHereOverlay().setEnabled(false);
        triggerChange();
    }

    private void showYouAreHere() {
        getMyMap().getYouAreHereOverlay().setEnabled(true);
        triggerChange();        
    }
    
    private void triggerChange() {
        // FIXME, nononononoonono!
        CodemapCore.getPlugin().getOpenFilesSelection().triggerChange();
    }

    @Override
    protected String getKey() {
        return YOUR_ARE_HERE_KEY;
    }

    @Override
    protected Class<? extends CommandAction> getActionID() {
        return ShowYouAreHereAction.class;
    }

    @Override
    public void configure(MapValues mapValues) {
        applyState();
    }
}
