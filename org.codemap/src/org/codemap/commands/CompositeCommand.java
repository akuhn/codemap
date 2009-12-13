package org.codemap.commands;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.codemap.CodemapCore;
import org.codemap.MapPerProject;
import org.codemap.mapview.MapView;

public class CompositeCommand<E extends IConfigureMapView> implements IConfigureMapView {

    private Set<E> commands = new HashSet<E>();
    private MapPerProject map;
    
    public static String makeCommandId(String command) {
        return CodemapCore.PLUGIN_ID + "." + command;
    }

    public CompositeCommand(MapPerProject mapPerProject) {
        map = mapPerProject;
    }

    protected MapPerProject getMyMap() {
        return map;
    }

    protected void add(E c) {
        commands.add(c);
    }
    
    protected Collection<E> getCommands() {
        return commands;
    }
    
    @Override
    public void configure(MapView view) {
        for(E each: commands) {
            each.configure(view);
        }
    }
}