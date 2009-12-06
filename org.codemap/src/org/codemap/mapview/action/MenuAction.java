package org.codemap.mapview.action;

import org.codemap.CodemapCore;
import org.codemap.MapPerProject;
import org.codemap.mapview.MapController;


public abstract class MenuAction extends CodemapAction {

    public MenuAction(String text, int style, MapController theController) {
		super(text, style, theController);
		MapPerProject activeMap = theController.getActiveMap();
		if (activeMap == null) return;
		setChecked(activeMap.getPropertyOrDefault(getKey(), isDefaultChecked()));
	}
    
	@Override
	public void configureAction(MapPerProject map) {
		setChecked(map.getPropertyOrDefault(getKey(), isDefaultChecked()));
//		TODO: find a way to re-run the action-enabling on startup
//		do *not* use run() as it causes the program to loop infinitely
//		run();
	}
	
	@Override
	public void run() {
		super.run();
		CodemapCore.getPlugin().getActiveMap().setProperty(getKey(), isChecked());		
	}
	
	protected abstract String getKey();

	protected abstract boolean isDefaultChecked();

	
}
