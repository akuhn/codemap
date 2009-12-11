package org.codemap.mapview.action;

import static org.codemap.util.CodemapIcons.PACKAGES;
import static org.codemap.util.CodemapIcons.descriptor;

import org.codemap.commands.ColoringCommand;
import org.codemap.commands.ColoringCommand.Coloring;
import org.codemap.mapview.MapController;


public class ShowPackageColorsAction extends ShowColorsAction {

    public ShowPackageColorsAction(int style, MapController theController) {
        super("Color by Package", style, theController);
        setImageDescriptor(descriptor(PACKAGES));
    }
   
    protected Coloring getMyColoring() {
        return ColoringCommand.Coloring.BY_PACKAGE;
    }

}
