package org.codemap.commands;

import org.codemap.MapPerProject;
import org.codemap.resources.MapValues;

public class MapCommands extends CompositeCommand<IConfigureMapValues> {

    public MapCommands(MapPerProject mapPerProject) {
        super(mapPerProject);
        add(new LabelingCommand(mapPerProject));
        add(new ColoringCommand(mapPerProject));
        add(new CallHierarchyCommand(mapPerProject));
        add(new SearchResultCommand(mapPerProject));
        add(new MarkerCommand(mapPerProject));        
    }

    public void applyOn(MapValues mapValues) {
        for(IConfigureMapValues each: getCommands()) {
            each.configure(mapValues);
        }
    }

}
