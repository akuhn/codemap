package org.codemap.mapview.action;

import org.codemap.CodemapCore;
import org.eclipse.jface.action.Action;

public class ReloadMapAction extends Action {
    
    public ReloadMapAction() {
        super();
        setText("Generate new Codemap");
    }
    
    @Override
    public void run() {
        CodemapCore core = CodemapCore.getPlugin();
        core.getActiveMap().reloadFromScratch();
    }

}
