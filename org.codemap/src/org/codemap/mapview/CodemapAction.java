package org.codemap.mapview;

import org.codemap.MapPerProject;
import org.eclipse.jface.action.Action;

public abstract class CodemapAction extends Action {
	
	public CodemapAction(String text, int style) {
		super(text, style);
	}

	public abstract void configureAction(MapPerProject map);

}
