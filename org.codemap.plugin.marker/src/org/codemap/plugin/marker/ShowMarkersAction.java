package org.codemap.plugin.marker;

import org.codemap.mapview.ICodemapPluginAction;
import org.eclipse.jface.action.Action;

public class ShowMarkersAction implements ICodemapPluginAction {

	private Action action;
	private MarkerController controller;

	public ShowMarkersAction() {
		controller = MarkerPlugin.getPlugin().getController();
		controller.register(this);
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
		controller.onLayerActivated();
	}

	private void hideMarkers() {
		controller.onLayerDeactivated();
	}

}
