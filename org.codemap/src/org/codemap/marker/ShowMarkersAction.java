package org.codemap.marker;

import static org.codemap.util.CodemapIcons.MARKER;

import org.codemap.mapview.MapController;
import org.codemap.mapview.action.MenuAction;
import org.codemap.util.CodemapIcons;
import org.eclipse.jface.action.IAction;

public class ShowMarkersAction extends MenuAction {

	private MarkerController controller;

	public ShowMarkersAction(MapController theController) {
	    super("Markers", IAction.AS_CHECK_BOX);
	    controller = theController.getMarkerController();
		controller.register(this);
		setImageDescriptor(CodemapIcons.descriptor(MARKER));
	}

	@Override
	public void run() {
	    super.run();
		if (isChecked()) showMarkers();
		else hideMarkers();
	}

	private void showMarkers() {
		controller.onLayerActivated();
	}

	private void hideMarkers() {
		controller.onLayerDeactivated();
	}

    @Override
    protected String getKey() {
        return "show_markers";
    }

    @Override
    protected boolean isDefaultChecked() {
        return false;
    }
}
