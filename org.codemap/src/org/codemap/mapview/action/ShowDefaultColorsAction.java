package org.codemap.mapview.action;

import static org.codemap.util.CodemapIcons.GREEN_CIRCLE;

import org.codemap.commands.ColoringCommand;
import org.codemap.commands.ColoringCommand.Coloring;
import org.codemap.mapview.MapController;
import org.codemap.util.CodemapIcons;

public class ShowDefaultColorsAction extends ShowColorsAction {
	
	public ShowDefaultColorsAction(int style, MapController theController) {
		super("Show default colors", style, theController);
		setImageDescriptor(CodemapIcons.descriptor(GREEN_CIRCLE));
	}

    protected Coloring getMyColoring() {
        return ColoringCommand.Coloring.GREEN;
    }

}
