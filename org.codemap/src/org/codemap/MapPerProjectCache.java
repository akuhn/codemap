package org.codemap;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.core.IJavaProject;

/*default*/ class MapPerProjectCache {

    private Map<IJavaProject,MapPerProject> mapPerProjectCache = new HashMap<IJavaProject,MapPerProject>();

    public MapPerProject forProject(IJavaProject project) {
        if (project == null) return null;
        MapPerProject map = mapPerProjectCache.get(project);
        if (map != null) return map;
        mapPerProjectCache.put(project, map = new MapPerProject(project));
        map.initialize();
        return map;
    }

    public void saveMapState() {
        for (MapPerProject each: mapPerProjectCache.values()) {
            each.saveState();
        }
    }
}
