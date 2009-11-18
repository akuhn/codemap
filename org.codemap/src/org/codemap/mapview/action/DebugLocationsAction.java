package org.codemap.mapview.action;

import org.codemap.CodemapCore;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.action.Action;

import ch.deif.meander.Location;
import ch.deif.meander.MapInstance;
import ch.deif.meander.Point;

public class DebugLocationsAction extends Action {
    
    public DebugLocationsAction() {
        super("generate debug location code");
    }
    
    @Override
    public void run() {
        Iterable<Location> locations = CodemapCore.getPlugin().getActiveMap().getValues().mapInstance.getValue().locations();
        for (Location each : locations) {
            int elevation = (int) Math.round(each.getElevation());
            
             String name = new Path(each.getDocument()).removeFileExtension().lastSegment();
            
            //points.put(new Point(0.6200911545578028, 0.8797911758418023, ""), height);            
            System.out.println("points.put(new Point(" + each.getPoint().x + ", " + each.getPoint().y + ", \"" + name + "\"), " + elevation + ");");
        }
    }

}
