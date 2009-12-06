package org.codemap.mapview.action;

import org.codemap.MapPerProject;
import org.codemap.mapview.MapController;
import org.eclipse.jface.action.Action;

public abstract class CodemapAction extends Action {
    
    private final MapController theController;    
	
	public CodemapAction(String text, int style, MapController theController) {
		super(text, style);
		this.theController = theController;
	}
	

    public MapController getController() {
        return theController;
    }	

	public abstract void configureAction(MapPerProject map);

}
