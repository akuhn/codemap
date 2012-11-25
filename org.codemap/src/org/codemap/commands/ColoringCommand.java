package org.codemap.commands;

import static org.codemap.commands.Command.makeCommandId;

import org.codemap.ByPackageColorScheme;
import org.codemap.CodemapCore;
import org.codemap.MapPerProject;
import org.codemap.mapview.action.CommandAction;
import org.codemap.mapview.action.ShowCoverageAction;
import org.codemap.mapview.action.ShowDefaultColorsAction;
import org.codemap.mapview.action.ShowHeatMapColorsAction;
import org.codemap.mapview.action.ShowPackageColorsAction;
import org.codemap.resources.MapValues;

public class ColoringCommand extends DropDownCommand<AbstractColoringCommand> implements IConfigureMapValues {
    
    private static final String COLORING_KEY = makeCommandId("coloring");

    public ColoringCommand(MapPerProject mapPerProject) {
        super(mapPerProject);
        add(new DefaultColoring(this));
        add(new ByPackageColoring(this, getMyMap()));
        add(new HeatMapColoring(this));
        add(new CoverageColoring(this));
    }

    @Override
    protected String getKey() {
        return COLORING_KEY;
    }

    @Override
    protected Class<?> getDefaultCommandClass() {
        return DefaultColoring.class;
    }
}

abstract class AbstractColoringCommand extends Command {

    private ColoringCommand coloring;

    public AbstractColoringCommand(ColoringCommand coloringCommand) {
        coloring = coloringCommand;
        enabled = getColoring().getEnabled(this);
    }

    protected ColoringCommand getColoring() {
        return coloring;
    }

    @Override
    protected void applyState() {
        if (isEnabled()) {
            getColoring().setEnabled(this);
        }
        getColoring().applyState();
    }

    @Override
    protected boolean initEnabled() {
        return false;
    }
}

class DefaultColoring extends AbstractColoringCommand {

    public DefaultColoring(ColoringCommand coloringCommand) {
        super(coloringCommand);
    }

    @Override
    public void configure(MapValues mapValues) {
        if (!isEnabled()) return;
        mapValues.colorScheme.setValue(CodemapCore.getPlugin().getDefaultColorScheme());        
    }

    @Override
    protected Class<? extends CommandAction> getActionID() {
        return ShowDefaultColorsAction.class;
    }
}

class ByPackageColoring extends AbstractColoringCommand {

    private MapPerProject map;

    public ByPackageColoring(ColoringCommand coloringCommand, MapPerProject mapPerProject) {
        super(coloringCommand);
        map = mapPerProject;
    }

    @Override
    public void configure(MapValues mapValues) {
        if (!isEnabled()) return;
        mapValues.colorScheme.setValue(new ByPackageColorScheme(map));
        
    }

    @Override
    protected Class<? extends CommandAction> getActionID() {
        return ShowPackageColorsAction.class;
    }
}

class HeatMapColoring extends AbstractColoringCommand {

    public HeatMapColoring(ColoringCommand coloringCommand) {
        super(coloringCommand);
    }

    @Override
    public void configure(MapValues mapValues) {
        CodemapCore.getPlugin().getController().utils().setHeatmapEnabled(isEnabled());
    }

    @Override
    protected Class<? extends CommandAction> getActionID() {
        return ShowHeatMapColorsAction.class;
    }
}

class CoverageColoring extends AbstractColoringCommand {

    public CoverageColoring(ColoringCommand coloringCommand) {
        super(coloringCommand);
    }

    @Override
    public void configure(MapValues mapValues) {
        CodemapCore.getPlugin().getController().utils().setCoverageEnabled(isEnabled());
    }

    @Override
    protected Class<? extends CommandAction> getActionID() {
        return ShowCoverageAction.class;
    }
}

