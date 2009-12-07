package org.codemap.commands;

import static org.codemap.commands.Commands.makeCommandId;

import org.codemap.DefaultLabelScheme;
import org.codemap.MapPerProject;
import org.codemap.commands.Commands.Command;
import org.codemap.util.MapScheme;

import ch.akuhn.values.Value;

public class LabelingCommand extends Command {

    private static final String LABELING_KEY = makeCommandId("labeling");
    private Labeling labeling = Labeling.DEFAULT;
    
    public static enum Labeling {
        DEFAULT, 
        NONE;
    }

    public LabelingCommand(MapPerProject mapPerProject) {
        super(mapPerProject);
        String setting = mapPerProject.getPropertyOrDefault(LABELING_KEY, Labeling.DEFAULT.toString());
        labeling = Labeling.valueOf(setting);
    }

    public void apply(Value<MapScheme<String>> labelScheme) {
        switch (labeling) {
        case NONE:
            labelScheme.setValue(new MapScheme<String>(null)); break;
        case DEFAULT:
            labelScheme.setValue(new DefaultLabelScheme()); break;
        }
    }

    public Labeling getCurrentLabeling() {
        return labeling;
    }

    public void setCurrentLabeling(Labeling newLabeling) {
        labeling = newLabeling;
        getMyMap().setProperty(LABELING_KEY, labeling.toString());
        apply(getMyMap().getValues().labelScheme);
    }

}
