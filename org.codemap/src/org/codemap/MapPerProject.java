package org.codemap;

import static org.codemap.util.Icons.FILE;

import java.util.HashMap;
import java.util.Map;

import org.codemap.mapview.ProviderDrivenImageOverlay;
import org.codemap.resources.NewMapResource;
import org.codemap.util.CodemapColors;
import org.codemap.util.CodemapLabels;
import org.codemap.util.Icons;
import org.codemap.util.JobMonitor;
import org.codemap.util.JobValue;
import org.codemap.util.Log;
import org.codemap.util.Resources;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.osgi.service.prefs.BackingStoreException;

import ch.akuhn.util.Arrays;
import ch.akuhn.util.Pair;
import ch.akuhn.values.ActionValue;
import ch.akuhn.values.Arguments;
import ch.akuhn.values.Value;
import ch.deif.meander.Configuration;
import ch.deif.meander.MapInstance;
import ch.deif.meander.Point;
import ch.deif.meander.Configuration.Builder;
import ch.deif.meander.builder.Meander;
import ch.deif.meander.swt.Background;
import ch.deif.meander.swt.CodemapVisualization;
import ch.deif.meander.swt.CompositeLayer;
import ch.deif.meander.swt.CurrSelectionOverlay;
import ch.deif.meander.swt.SWTLayer;
import ch.deif.meander.swt.YouAreHereOverlay;

/**
 * Holds corpus, map and visualization of a project. Use this class to store project specific information.
 * 
 */
public class MapPerProject {

    private Map<String,String> properties = new HashMap<String,String>();
    private CodemapColors colorScheme = new CodemapColors();
    private CodemapLabels labelScheme = new CodemapLabels();
    private CompositeLayer sharedLayer = new CompositeLayer();

    private static Map<IJavaProject,MapPerProject> mapPerProjectCache;

    private static final String POINT_NODE_ID = CodemapCore.PLUGIN_ID + ".points"; 
    private static final int MINIMAL_SIZE = 256;

    private final IJavaProject project;
    private NewMapResource mapResource;
    private Value<CodemapVisualization> visual;

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
        mapResource = new NewMapResource("default.map", 
                Arrays.asList(Resources.asPath(project)),
                Arrays.asList("*.java"),
                readPreviousMapState());
        visual = new JobValue<CodemapVisualization>("Codemap visualization", mapResource.mapInstance) {
            @Override
            protected CodemapVisualization computeValue(JobMonitor monitor) {
                return computeCodemapVisualization(monitor);
            }
        };
        new ActionValue<Void>(visual) {
            @Override
            protected Void performAction(Arguments args) {
                CodemapCore.getPlugin().getMapView().newProjectSelected();
                return null;
            }
        };
    }

    public CodemapVisualization getVisualizationOrNull() {
        return visual.getValue();		
    }

    public IProject getProject() {
        return getJavaProject().getProject();
    }

    public Configuration getConfiguration() {
        return mapResource.configuration.awaitValue();
    }

    private IJavaProject getJavaProject() {
        return project;
    }

    private CodemapVisualization computeCodemapVisualization(JobMonitor monitor) {
        MapInstance map = monitor.nextArgument();

        Background background = Meander.background()
        .withColors(colorScheme)
        .makeBackground();

        SWTLayer layer = Meander.layers()
        .withLabels(labelScheme)
        .withSelection(new CurrSelectionOverlay(), CodemapCore.getPlugin().getCurrentSelection())
        .withSelection(new ProviderDrivenImageOverlay(Icons.getImage(FILE), new WorkbenchLabelProvider()), CodemapCore.getPlugin().getOpenFilesSelection())
        .withSelection(new YouAreHereOverlay(), CodemapCore.getPlugin().getYouAreHereSelection())				   
        .withLayer(sharedLayer)
        .makeLayer();

        return new CodemapVisualization(map)
        .add(layer)
        .addBackground(background);

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
        mapResource.mapSize.setValue(size0);
        visual.getValue();
        return this;
    }
    
    public static void saveMapState() {
        for (MapPerProject each: mapPerProjectCache.values()) each.writePointPreferences();
    }


    private void writePointPreferences() {
        IEclipsePreferences node = readPointPreferences();
        for(Point each: getConfiguration().points()) {
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

    public CodemapColors getColorScheme() {
        return colorScheme;
    }

    public CodemapLabels getLabelScheme() {
        return labelScheme;
    }

    public boolean getPropertyOrDefault(String key, boolean defaultValue) {
        String value = properties.get(key);
        return value == null ? defaultValue : Boolean.parseBoolean(value);
    }

    public void setProperty(String key, boolean checked) {
        properties.put(key, Boolean.toString(checked));
    }

    public void addLayer(SWTLayer layer) {
        sharedLayer.add(layer);
    }

    public void removeLayer(SWTLayer layer) {
        sharedLayer.remove(layer);
    }	

}
