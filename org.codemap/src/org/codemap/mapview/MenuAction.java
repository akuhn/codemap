package org.codemap.mapview;

import org.codemap.CodemapCore;
import org.codemap.MapPerProject;


public abstract class MenuAction extends CodemapAction {

	public MenuAction(String text, int style) {
		super(text, style);
	}

	@Override
	public void configureAction(MapPerProject map) {
		setChecked(map.getPropertyOrDefault(getKey(), isDefaultChecked()));
	}
	
	@Override
	public void run() {
		super.run();
		CodemapCore.getPlugin().getActiveMap().setProperty(getKey(), isChecked());		
	}

	protected abstract String getKey();

	protected abstract boolean isDefaultChecked();
	
}
