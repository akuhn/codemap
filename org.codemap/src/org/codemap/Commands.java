package org.codemap;

public class Commands {
    
    private LabelingCommand labelingCommand;
    
    public static String makeCommandId(String command) {
        return CodemapCore.PLUGIN_ID + "." + command;
    }

    public Commands(MapPerProject mapPerProject) {
        labelingCommand = new LabelingCommand(mapPerProject);
    }

    public LabelingCommand getLabelingCommand() {
        return labelingCommand;
    }

}
