package org.codemap.plugin.marker;

import org.codemap.mapview.ICodemapPluginAction;
import org.eclipse.jface.action.Action;

public class ShowMarkersAction implements ICodemapPluginAction {

	private Action action;

	public ShowMarkersAction() {
		MarkerPluginCore.getPlugin().register(this);
	}

	@Override
	public void run(Action act) {
		action = act;
		if (action.isChecked()) showMarkers();
		else hideMarkers();
	}
	
	public boolean isChecked() {
		if (action == null) return false;
		return action.isChecked();
	}	

	private void showMarkers() {
		
	}

	private void hideMarkers() {
		
	}

}
