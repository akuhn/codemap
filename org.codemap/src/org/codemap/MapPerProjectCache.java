package org.codemap;

import java.util.HashMap;
import java.util.Map;

import org.codemap.mapview.MapController;
import org.eclipse.jdt.core.IJavaProject;

public class MapPerProjectCache {

    private Map<IJavaProject,MapPerProject> cache = new HashMap<IJavaProject,MapPerProject>();
    private MapController theController;

    public MapPerProjectCache(MapController mapController) {
        theController = mapController;
    }

    public MapPerProject forProject(IJavaProject project) {
        if (project == null) return null;
        MapPerProject map = cache.get(project);
        if (map != null) return map;
        map = new MapPerProject(project, this);
        cache.put(project, map);
        map.initialize(); // breaks circular setup dependencies
        return map;
    }

    public void saveMapState() {
        for (MapPerProject each: cache.values()) {
            each.saveState();
        }
    }

    /*default*/ void reload(MapPerProject mapPerProject) {
        IJavaProject project = mapPerProject.getJavaProject();
        cache.remove(project);
        theController.onNewProjectSelected();
    }
}
