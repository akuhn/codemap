package org.codemap.commands;

import static org.codemap.commands.Commands.makeCommandId;

import org.codemap.CodemapCore;
import org.codemap.MapPerProject;
import org.codemap.commands.Commands.Command;
import org.codemap.marker.MarkerController;
import org.codemap.marker.ShowMarkersAction;

public class MarkerCommand extends Command {

    private static final String MARKER_KEY = makeCommandId("marker");
    private boolean showMarkers;

    public MarkerCommand(MapPerProject mapPerProject) {
        super(mapPerProject);
        showMarkers = getMyMap().getPropertyOrDefault(MARKER_KEY, false);
    }

    public void setShowMarkers(boolean checked) {
        showMarkers = checked;
        applyState();
    }

    public void apply(ShowMarkersAction action) {
        action.setChecked(showMarkers);
        applyState();
    }

    private void applyState() {
        if (showMarkers) showMarkers();
        else hideMarkers();
        getMyMap().setProperty(MARKER_KEY, showMarkers);
    }

    private void showMarkers() {
        getMarkerController().onLayerActivated();
    }

    private void hideMarkers() {
        getMarkerController().onLayerDeactivated();
    }    
    
    protected MarkerController getMarkerController() {
        return CodemapCore.getPlugin().getController().getMarkerController();
    }    
    
    

}
