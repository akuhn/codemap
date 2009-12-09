package org.codemap.mapview.action;

import org.codemap.MapPerProject;
import org.codemap.mapview.MapController;


public abstract class MenuAction extends CodemapAction {

    public MenuAction(String text, int style, MapController theController) {
		super(text, style, theController);
		MapPerProject activeMap = theController.getActiveMap();
		if (activeMap == null) return;
        configureAction(activeMap);
	}
    	
}
