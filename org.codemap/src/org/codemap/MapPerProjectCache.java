package org.codemap;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.core.IJavaProject;

public class MapPerProjectCache {

    private Map<IJavaProject,MapPerProject> cache = new HashMap<IJavaProject,MapPerProject>();

    public MapPerProject forProject(IJavaProject project) {
        if (project == null) return null;
        MapPerProject map = cache.get(project);
        if (map != null) return map;
        cache.put(project, map = new MapPerProject(project, this));
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
        this.forProject(project);
    }
}
