package org.codemap.mapview.action;

import org.codemap.MapPerProject;
import org.codemap.commands.ColoringCommand;
import org.codemap.commands.ColoringCommand.Coloring;
import org.codemap.mapview.MapController;

public abstract class ShowColorsAction extends MenuAction {

    private ColoringCommand coloringCommand;

    public ShowColorsAction(String text, int style, MapController theController) {
        super(text, style, theController);
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
    
    protected ColoringCommand getColoringCommand() {
        return coloringCommand;
    }
    
    protected boolean isMyColoring(Coloring currentColoring) {
        return currentColoring.equals(getMyColoring());
     }    
    
    protected abstract Coloring getMyColoring();
}
