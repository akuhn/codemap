package org.codemap.commands;

import static org.codemap.commands.ColoringCommand.Coloring.COVERAGE;
import static org.codemap.commands.Commands.makeCommandId;

import java.util.List;

import org.codemap.CodemapCore;
import org.codemap.MapPerProject;
import org.codemap.commands.Commands.Command;
import org.codemap.eclemma.CoverageListener;
import org.codemap.util.CodemapColors;
import org.codemap.util.MColor;
import org.codemap.util.MapScheme;

import ch.akuhn.util.Pair;
import ch.akuhn.values.Value;

public class ColoringCommand extends Command {
    
    private static final String COLORING_KEY = makeCommandId("coloring");

    private Coloring coloring;

    private List<Pair<String, Double>> lastCoverageInfo;
    
    public static enum Coloring {
        GREEN, 
        BY_PACKAGE,
        HEATMAP,
        COVERAGE,
    }    

    public ColoringCommand(MapPerProject mapPerProject) {
        super(mapPerProject);
        String setting = mapPerProject.getPropertyOrDefault(COLORING_KEY, Coloring.GREEN.toString());        
        coloring = Coloring.valueOf(setting);
    }

    public void apply(Value<MapScheme<MColor>> colorScheme) {
        CodemapCore.getPlugin().getController().utils().setHeatmapEnabled(coloring.equals(Coloring.HEATMAP));
        CodemapCore.getPlugin().getController().utils().setCoverageEnabled(coloring.equals(Coloring.COVERAGE));
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
