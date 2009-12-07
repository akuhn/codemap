package org.codemap.marker;

import static org.codemap.util.CodemapIcons.MARKER;

import org.codemap.MapPerProject;
import org.codemap.commands.MarkerCommand;
import org.codemap.mapview.MapController;
import org.codemap.mapview.action.MenuAction;
import org.codemap.util.CodemapIcons;
import org.eclipse.jface.action.IAction;

public class ShowMarkersAction extends MenuAction {

	private MarkerCommand markerCommand;

    public ShowMarkersAction(MapController theController) {
	    super("Markers", IAction.AS_CHECK_BOX, theController);
		setImageDescriptor(CodemapIcons.descriptor(MARKER));
		// FIXME move this to the command
		theController.getMarkerController().register(this);
	}
    
	@Override
	public void run() {
        markerCommand.setShowMarkers(isChecked());
	}

    @Override
    public void configureAction(MapPerProject map) {
        markerCommand = map.getCommands().getMarkerCommand();
        markerCommand.apply(this);
    }
}
