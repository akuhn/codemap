package org.codemap;

import static org.codemap.Commands.makeCommandId;

import org.codemap.util.MapScheme;

import ch.akuhn.values.Value;

public class LabelingCommand {

    private static final String LABELING_KEY = makeCommandId("labeling");
    private Labeling labeling = Labeling.DEFAULT;
    private MapPerProject myMap;
    
    public static enum Labeling {
        DEFAULT, 
        NONE;
    }

    public LabelingCommand(MapPerProject mapPerProject) {
        myMap = mapPerProject;
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
        myMap.setProperty(LABELING_KEY, labeling.toString());
        apply(myMap.getValues().labelScheme);
    }
}
