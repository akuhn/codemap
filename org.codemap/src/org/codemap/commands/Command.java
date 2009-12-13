package org.codemap.commands;

import org.codemap.mapview.MapView;
import org.codemap.mapview.action.CommandAction;

public abstract class Command implements IConfigureMapValues {

    protected boolean enabled;

    public Command() {
        // FIXME pass theController along the Command hierarchy
        enabled = initEnabled();        
    }

    @Override
    public void configure(MapView view) {
        CommandAction action = view.getAction(getActionID());
        if (action != null) {
            action.setCommand(this);
        }
        applyState();
    }
    
    /**
     * get the class of the ui-element we take care of
     */
    protected abstract Class<? extends CommandAction> getActionID();

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean checked) {
        enabled = checked;
        applyState();
    }
    
    protected boolean initEnabled() {
        return false;
    };    

    protected abstract void applyState();
}