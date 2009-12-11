package org.codemap.commands;

import static org.codemap.commands.ColoringCommand.Coloring.COVERAGE;
import static org.codemap.commands.ColoringCommand.Coloring.GREEN;
import static org.codemap.commands.ColoringCommand.Coloring.HEATMAP;
import static org.codemap.commands.Commands.makeCommandId;

import org.codemap.CodemapCore;
import org.codemap.MapPerProject;
import org.codemap.commands.Commands.Command;
import org.codemap.util.MColor;
import org.codemap.util.MapScheme;

import ch.akuhn.values.Value;

public class ColoringCommand extends Command {
    
    private static final String COLORING_KEY = makeCommandId("coloring");

    private Coloring coloring;

    public static enum Coloring {
        GREEN, 
        BY_PACKAGE,
        HEATMAP,
        COVERAGE,
    }    

    public ColoringCommand(MapPerProject mapPerProject) {
        super(mapPerProject);
        String setting = mapPerProject.getPropertyOrDefault(COLORING_KEY, GREEN.toString());        
        coloring = Coloring.valueOf(setting);
    }

    public void apply(Value<MapScheme<MColor>> colorScheme) {
        // TODO fixme
        CodemapCore.getPlugin().getController().utils().setHeatmapEnabled(coloring.equals(HEATMAP));
        CodemapCore.getPlugin().getController().utils().setCoverageEnabled(coloring.equals(COVERAGE));
        switch(coloring){
        case GREEN:
            colorScheme.setValue(CodemapCore.getPlugin().getDefaultColorScheme());
            break;
        case BY_PACKAGE:
            colorScheme.setValue(new ByPackageColorScheme(getMyMap()));
            break;
        }
    }    

    public Coloring getCurrentColoring() {
        return coloring;
    }

    public void setCurrentColoring(Coloring newColoring) {
        coloring = newColoring;
        getMyMap().setProperty(COLORING_KEY, coloring.toString());
        apply(getMyMap().getValues().colorScheme);
    }
}
