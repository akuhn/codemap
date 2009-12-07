package org.codemap.mapview.action;

import static org.codemap.util.CodemapIcons.PACKAGES;

import org.codemap.MapPerProject;
import org.codemap.commands.ColoringCommand;
import org.codemap.commands.ColoringCommand.Coloring;
import org.codemap.mapview.MapController;
import org.codemap.util.CodemapIcons;


public class ShowPackageColorsAction extends MenuAction {

    private ColoringCommand coloringCommand;

    public ShowPackageColorsAction(int style, MapController theController) {
        super("Color by Package", style, theController);
        setImageDescriptor(CodemapIcons.descriptor(PACKAGES));
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
        return ColoringCommand.Coloring.BY_PACKAGE;
    }

}
