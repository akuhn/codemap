package org.codemap;

import java.util.HashMap;
import java.util.Map;

import org.codemap.resources.EclipseMapValues;
import org.codemap.resources.EclipseMapValuesBuilder;
import org.codemap.util.CodemapLabels;
import org.codemap.util.Log;
import org.codemap.util.Resources;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.eclipse.jdt.core.IJavaProject;
import org.osgi.service.prefs.BackingStoreException;

import ch.akuhn.util.Arrays;
import ch.akuhn.values.ActionValue;
import ch.akuhn.values.Arguments;
import ch.deif.meander.Configuration;
import ch.deif.meander.Point;
import ch.deif.meander.Configuration.Builder;
import ch.deif.meander.map.MapVisualization;
import ch.deif.meander.swt.CodemapVisualization;
import ch.deif.meander.swt.CompositeLayer;
import ch.deif.meander.swt.SWTLayer;
import ch.deif.meander.util.CodemapColors;

/**
 * Holds corpus, map and visualization of a project. Use this class to store project specific information.
 * 
 */
public class MapPerProject {

    private Map<String,String> properties = new HashMap<String,String>();
//    private CodemapColors colorScheme = new CodemapColors();
//    private CodemapLabels labelScheme = new CodemapLabels();
//    private CompositeLayer sharedLayer = new CompositeLayer();

    private static Map<IJavaProject,MapPerProject> mapPerProjectCache;

    private static final String POINT_NODE_ID = CodemapCore.PLUGIN_ID + ".points"; 
    private static final int MINIMAL_SIZE = 256;

    private final IJavaProject project;
    private EclipseMapValues mapValues;
    private MapVisualization mapVisualization;

    public static MapPerProject forProject(IJavaProject project) {
        if (mapPerProjectCache == null) mapPerProjectCache = new HashMap<IJavaProject,MapPerProject>();
        MapPerProject map = mapPerProjectCache.get(project);
        if (map != null) return map;
        mapPerProjectCache.put(project, map = new MapPerProject(project));
        map.initialize();
        return map;
    }

    private MapPerProject(IJavaProject project) {
        this.project = project;
        // don't initialize;
    }

    private void initialize() {
        EclipseMapValuesBuilder b = new EclipseMapValuesBuilder();
        b.setName("default.map");
        b.setProjects(Arrays.asList(Resources.asPath(project)));
        b.setExtensions(Arrays.asList("*.java"));
        b.setInitialConfiguration(readPreviousMapState());
        mapValues = new EclipseMapValues(b);
        mapVisualization = new MapVisualization(mapValues);
        mapVisualization.getSharedLayer().add(makeOpenFilesLayer());
        new ActionValue<Void>(
                mapValues.mapInstance, 
                mapValues.background,
                mapValues.labeling) {
            @Override
            protected Void performAction(Arguments args) {
                CodemapCore.getPlugin().getMapView().newProjectSelected();
                return null;
            }
        };
    }

    private SWTLayer makeOpenFilesLayer() {
        return new OpenFileIconsLayer();
    }

    public CodemapVisualization getVisualization() {
        return mapVisualization.getVisualization();		
    }

    public IProject getProject() {
        return getJavaProject().getProject();
    }

    private IJavaProject getJavaProject() {
        return project;
    }

    private Configuration readPreviousMapState() {
        IEclipsePreferences node = readPointPreferences();
        Builder builder = Configuration.builder();
        try {
            for (String key: node.keys()) {
                String pointString = node.get(key, null);
                String[] split = pointString.split("@");
                // cover legacy format
                if (split.length != 2 ) split = pointString.split("#");
                if (split.length != 2 ) {
                    Log.error(new RuntimeException("Invalid format of point storage for " + getProject().getName() + ": " + pointString));
                    continue;
                }
                double x = Double.parseDouble(split[0]);
                double y = Double.parseDouble(split[1]);
                builder.add(key, x, y);
            }
            return builder.build();
        } catch (BackingStoreException e) {
            Log.error(e);
        }
        return null;
    }

    public MapPerProject updateSize(int size) {
        int size0 = Math.max(size, MINIMAL_SIZE);
        mapValues.mapSize.setValue(size0);
        mapValues.mapInstance.getValue(); // trigger computation
        return this;
    }
    
    public static void saveMapState() {
        for (MapPerProject each: mapPerProjectCache.values()) each.writePointPreferences();
    }


    private void writePointPreferences() {
        if (mapValues.configuration.isError()) return;
        Configuration config = mapValues.configuration.getValueOrFail();
        IEclipsePreferences node = readPointPreferences();
        for(Point each: config.points()) {
            node.put(each.getDocument(), each.x + "@" + each.y);
        }
        try {
            node.flush();
        } catch (BackingStoreException e) {
            Log.error(e);
        }		
    }

    private IEclipsePreferences readPointPreferences() {
        IScopeContext context = new ProjectScope(getProject());
        IEclipsePreferences node = context.getNode(POINT_NODE_ID);
        return node;
    }

    public String getPropertyOrDefault(String key, String defaultValue) {
        String value = properties.get(key);
        return value == null ? defaultValue : value;
    }

    public void setProperty(String key, String value) {
        properties.put(key, value);
    }

    public boolean getPropertyOrDefault(String key, boolean defaultValue) {
        String value = properties.get(key);
        return value == null ? defaultValue : Boolean.parseBoolean(value);
    }

    public void setProperty(String key, boolean checked) {
        properties.put(key, Boolean.toString(checked));
    }

    public void addLayer(SWTLayer layer) {
        mapVisualization.getSharedLayer().add(layer);
    }

    public void removeLayer(SWTLayer layer) {
        mapVisualization.getSharedLayer().remove(layer);
    }

    public Configuration getConfiguration() {
        return mapValues.configuration.getValue(); // what if it's null?
    }

    public EclipseMapValues getValues() {
        return mapValues;
    }	

}
