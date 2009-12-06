package org.codemap.marker;

import static org.codemap.util.CodemapIcons.MARKER;

import org.codemap.mapview.MapController;
import org.codemap.mapview.action.MenuAction;
import org.codemap.util.CodemapIcons;
import org.eclipse.jface.action.IAction;

public class ShowMarkersAction extends MenuAction {

	public ShowMarkersAction(MapController theController) {
	    super("Markers", IAction.AS_CHECK_BOX, theController);
	    getMarkerController().register(this);
		setImageDescriptor(CodemapIcons.descriptor(MARKER));
	}
	
	protected MarkerController getMarkerController() {
	    return getController().getMarkerController();
	}

	@Override
	public void run() {
	    super.run();
		if (isChecked()) showMarkers();
		else hideMarkers();
	}

	private void showMarkers() {
		getMarkerController().onLayerActivated();
	}

	private void hideMarkers() {
		getMarkerController().onLayerDeactivated();
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
