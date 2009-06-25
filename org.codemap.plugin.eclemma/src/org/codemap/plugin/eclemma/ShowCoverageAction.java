package org.codemap.plugin.eclemma;

import org.eclipse.jface.action.Action;

import ch.unibe.softwaremap.SoftwareMap;
import ch.unibe.softwaremap.ui.ICodeMapPluginAction;

public class ShowCoverageAction implements ICodeMapPluginAction {
	
	private Action action;

	@Override
	public void run(Action act) {
		action = act;
		SoftwareMap.core().updateMap();
	}

	public boolean isChecked() {
		if (action == null) return false;
		return action.isChecked();
	}

}
