package org.codemap.plugin.search;

import org.codemap.mapview.ICodemapPluginAction;
import org.eclipse.jface.action.Action;


public class ShowSearchAction implements ICodemapPluginAction {
	
	private Action action;


	@Override
	public void run(Action act) {
		action = act;
	}

	public boolean isChecked() {
		if (action == null) return false;
		return action.isChecked();
	}

}
