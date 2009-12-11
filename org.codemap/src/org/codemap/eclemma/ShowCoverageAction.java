package org.codemap.eclemma;

import static org.codemap.util.CodemapIcons.COVERAGE;
import static org.codemap.util.CodemapIcons.descriptor;

import org.codemap.commands.ColoringCommand;
import org.codemap.commands.ColoringCommand.Coloring;
import org.codemap.mapview.MapController;
import org.codemap.mapview.action.ShowColorsAction;


public class ShowCoverageAction extends ShowColorsAction {

    public ShowCoverageAction(int style, MapController theController) {
        super("Color by Coverage", style, theController);
        setImageDescriptor(descriptor(COVERAGE));
        boolean enabled = theController.utils().isEclemmaPluginAvailable();
        setEnabled(enabled);
    }
   
    protected Coloring getMyColoring() {
        return ColoringCommand.Coloring.COVERAGE;
    }

}
