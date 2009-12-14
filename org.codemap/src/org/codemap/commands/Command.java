package org.codemap.commands;

import java.util.HashSet;
import java.util.Set;

import org.codemap.CodemapCore;
import org.codemap.mapview.MapView;
import org.codemap.mapview.action.CommandAction;

public abstract class Command implements IConfigureMapValues {
    
    private static Set<String> usedIds = new HashSet<String>();
    
    public static String makeCommandId(String command) {
        String id = CodemapCore.PLUGIN_ID + "." + command;
        if (!usedIds.add(id)) {
            throw new RuntimeException("Duplicate use of Command-id: " + id);
        }
        return id;
    }    

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