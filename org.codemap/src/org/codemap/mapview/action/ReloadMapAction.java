package org.codemap.mapview.action;

import org.codemap.mapview.MapController;
import org.eclipse.jface.action.Action;

public class ReloadMapAction extends Action {
    
    private MapController controller;

    public ReloadMapAction(MapController theController) {
        super();
        setText("Generate new Codemap");
        controller = theController;
    }
    
    @Override
    public void run() {
        controller.getActiveMap().reloadFromScratch();
    }

}
