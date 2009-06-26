package org.codemap.plugin.eclemma;

import org.eclipse.jface.action.Action;

import ch.unibe.softwaremap.CodemapCore;
import ch.unibe.softwaremap.mapview.ICodeMapPluginAction;

public class ShowCoverageAction implements ICodeMapPluginAction {
	
	private Action action;
	
	public ShowCoverageAction() {
		EclemmaOverlay.registerCoverageAction(this);
	}

	@Override
	public void run(Action act) {
		action = act;
		CodemapCore r = CodemapCore.getPlugin();
		r.getMapView().updateMap(r);
	}

	public boolean isChecked() {
		if (action == null) return false;
		return action.isChecked();
	}

}
