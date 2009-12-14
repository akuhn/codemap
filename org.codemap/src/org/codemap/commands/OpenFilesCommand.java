package org.codemap.commands;

import org.codemap.CodemapCore;
import org.codemap.MapPerProject;
import org.codemap.layers.OpenFilesOverlay;
import org.codemap.mapview.action.CommandAction;
import org.codemap.mapview.action.ShowOpenFilesAction;
import org.codemap.resources.MapValues;

public class OpenFilesCommand extends CheckedCommand {

    private static final String OPEN_FILES_KEY = makeCommandId("open_files");

    public OpenFilesCommand(MapPerProject mapPerProject) {
        super(mapPerProject);
    }
    
    @Override
    protected void applyState() {
        super.applyState();
        if (isEnabled()) showOpenFiles();
        else hideOpenFiles();
    }

    private void hideOpenFiles() {
        getMyMap().getOpenFilesOverlay().setEnabled(false);
        triggerChange();
    }

    private void showOpenFiles() {
        getMyMap().getOpenFilesOverlay().setEnabled(true);
        triggerChange();        
    }
    
    private void triggerChange() {
        // FIXME, nononononoonono!
        // (and even worse, this is a copy-paste from YouAreHereCommand)
        CodemapCore.getPlugin().getOpenFilesSelection().triggerChange();
    }

    @Override
    protected String getKey() {
        return OPEN_FILES_KEY;
    }

    @Override
    protected Class<? extends CommandAction> getActionID() {
        return ShowOpenFilesAction.class;
    }

    @Override
    public void configure(MapValues mapValues) {
        applyState();
    }

}
