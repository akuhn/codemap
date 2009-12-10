package org.codemap.mapview.action;

import static org.codemap.util.CodemapIcons.TRACE;
import static org.codemap.util.CodemapIcons.descriptor;

import org.codemap.MapPerProject;
import org.codemap.commands.ColoringCommand;
import org.codemap.commands.ColoringCommand.Coloring;
import org.codemap.mapview.MapController;

public class ShowHeatMapColorsAction extends MenuAction {

    private ColoringCommand coloringCommand;

    public ShowHeatMapColorsAction(int style, MapController theController) {
        super("Show Heatmap Colors", style, theController);
        setImageDescriptor(descriptor(TRACE));        
    }
    
    @Override
    public void run() {
        super.run();
        if (!isChecked()) return;
        coloringCommand.setCurrentColoring(getMyColoring());
    }    

    @Override
    public void configureAction(MapPerProject map) {
        coloringCommand = map.getCommands().getColoringCommand();
        setChecked(isMyColoring(coloringCommand.getCurrentColoring()));
    }
    
    private boolean isMyColoring(Coloring currentColoring) {
        return currentColoring.equals(getMyColoring());
    }
    
    private Coloring getMyColoring() {
        return ColoringCommand.Coloring.HEATMAP;
    }    

}
