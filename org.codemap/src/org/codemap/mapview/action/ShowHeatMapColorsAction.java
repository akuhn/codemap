package org.codemap.mapview.action;

import static org.codemap.util.CodemapIcons.TRACE;
import static org.codemap.util.CodemapIcons.descriptor;

import org.codemap.commands.ColoringCommand;
import org.codemap.commands.ColoringCommand.Coloring;
import org.codemap.mapview.MapController;

public class ShowHeatMapColorsAction extends ShowColorsAction {

    public ShowHeatMapColorsAction(int style, MapController theController) {
        super("Show Heatmap Colors", style, theController);
        setImageDescriptor(descriptor(TRACE));        
    }
    
    protected Coloring getMyColoring() {
        return ColoringCommand.Coloring.HEATMAP;
    }    

}
