package org.codemap.commands;

import org.codemap.CodemapCore;
import org.codemap.MapPerProject;

public class Commands {
    
    private LabelingCommand labelingCommand;
    private ColoringCommand coloringCommand;
    private CallHierarchyCommand callHierachyCommand;
    private SearchResultCommand searchResultCommand;
    private MarkerCommand markerCommand;
    
    public static String makeCommandId(String command) {
        return CodemapCore.PLUGIN_ID + "." + command;
    }

    public Commands(MapPerProject mapPerProject) {
        labelingCommand = new LabelingCommand(mapPerProject);
        coloringCommand = new ColoringCommand(mapPerProject);
        callHierachyCommand = new CallHierarchyCommand(mapPerProject);
        searchResultCommand = new SearchResultCommand(mapPerProject);
        markerCommand = new MarkerCommand(mapPerProject);
    }

    public LabelingCommand getLabelingCommand() {
        return labelingCommand;
    }

    public ColoringCommand getColoringCommand() {
        return coloringCommand;
    }

    public CallHierarchyCommand getCallHierarchyCommand() {
        return callHierachyCommand;
    }

    public SearchResultCommand getSearchResultsCommand() {
        return searchResultCommand;
    }
    
    public MarkerCommand getMarkerCommand() {
        return markerCommand;
    }
    
    public static class Command {
        
        private MapPerProject myMap;

        public Command(MapPerProject mapPerProject) {
            myMap = mapPerProject;
        }

        protected MapPerProject getMyMap() {
            return myMap;
        }        
    }
}