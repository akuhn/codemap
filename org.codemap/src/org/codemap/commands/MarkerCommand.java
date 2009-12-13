package org.codemap.commands;

import static org.codemap.commands.CompositeCommand.makeCommandId;

import org.codemap.CodemapCore;
import org.codemap.MapPerProject;
import org.codemap.mapview.MapView;
import org.codemap.mapview.action.CommandAction;
import org.codemap.mapview.action.ShowMarkersAction;
import org.codemap.marker.MarkerController;
import org.codemap.resources.MapValues;

public class MarkerCommand extends CheckedCommand {

    private static final String MARKER_KEY = makeCommandId("marker");

    public MarkerCommand(MapPerProject mapPerProject) {
        super(mapPerProject);
    }

    @Override
    protected void applyState() {
        if (isEnabled()) showMarkers();
        else hideMarkers();
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

    @Override
    protected String getKey() {
        return MARKER_KEY;
    }

    @Override
    protected Class<? extends CommandAction> getActionID() {
        return ShowMarkersAction.class;
    }

    @Override
    public void configure(MapValues mapValues) {
        applyState();
    }
    
    @Override
    public void configure(MapView view) {
        getMarkerController().setCurrentCommand(this);
        super.configure(view);
    }
}
