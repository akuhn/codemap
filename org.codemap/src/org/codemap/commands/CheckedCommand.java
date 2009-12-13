package org.codemap.commands;

import org.codemap.MapPerProject;

public abstract class CheckedCommand extends Command {

    private MapPerProject map;

    public CheckedCommand(MapPerProject mapPerProject) {
        map = mapPerProject;
        enabled = getMyMap().getPropertyOrDefault(getKey(), getDefaultChecked());
    }
    
    protected MapPerProject getMyMap() {
        return map;
    }

    protected boolean getDefaultChecked() {
        return true;
    }

    protected void applyState() {
        getMyMap().setProperty(getKey(), isEnabled());        
    }    
    
    protected abstract String getKey();        
}
