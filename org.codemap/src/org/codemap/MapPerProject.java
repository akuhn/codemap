package org.codemap;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.codemap.Configuration.Builder;
import org.codemap.callhierarchy.CallOverlay;
import org.codemap.commands.MapCommands;
import org.codemap.layers.CodemapVisualization;
import org.codemap.layers.CurrentSelectionOverlay;
import org.codemap.layers.Layer;
import org.codemap.layers.OpenFileIconsLayer;
import org.codemap.layers.YouAreHereOverlay;
import org.codemap.mapview.MapController;
import org.codemap.mapview.MapView;
import org.codemap.resources.MapValueBuilder;
import org.codemap.resources.MapValues;
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
import ch.akuhn.values.Value;

/**
 * Holds corpus, map and visualization of a project. Use this class to store project specific information.
 * 
 */
public class MapPerProject {

    private Map<String,String> properties = new HashMap<String,String>();

    private static final String POINT_NODE_ID = CodemapCore.PLUGIN_ID + ".points"; 
    private static final String UI_STATE_NODE_ID = CodemapCore.PLUGIN_ID + ".ui.mapview";
    private static final int MINIMAL_SIZE = 256;

    private final IJavaProject project;
    private MapValues mapValues;
    private CodemapVisualization mapVisualization;
    private MapPerProjectCache cache;

    private ActionValue<Void> redrawAction;
    private MapCommands commands;


    /*default*/ MapPerProject(IJavaProject project, MapPerProjectCache mapPerProjectCache) {
        this.project = project;
        this.cache = mapPerProjectCache;
        readPreviousProperties();
        commands = new MapCommands(this);
    }

    public void initialize() {
        MapValueBuilder builder = new MapValueBuilder();
        builder.setName("default.map");
        builder.setProjects(Arrays.asList(Resources.asPath(project)));
        builder.setFileExtensions(Arrays.asList("*.java"));
        builder.setInitialConfiguration(readPreviousMapState());
        mapValues = new MapValues(builder);
        mapVisualization = new CodemapVisualization(mapValues);

        redrawAction = new ActionValue<Void>(
                mapValues.mapInstance, 
                mapValues.background,
                mapValues.labeling, 
                mapValues.extensions,
                mapValues.selections) {
            
            @Override
            protected Void performAction(Arguments args) {
                // well ... during shutdown everything can be null. yay.
                CodemapCore plugin = CodemapCore.getPlugin();
                if (plugin == null) return null;
                MapController controller = plugin.getController();
                if (controller == null) return null;
                controller.onRedraw();
                return null;
            }
        };
        
        commands.applyOn(getValues());
    }

    public CodemapVisualization getVisualization() {
        return mapVisualization;		
    }

    public IProject getProject() {
        return getJavaProject().getProject();
    }

    /*default*/ IJavaProject getJavaProject() {
        return project;
    }
    
    private void readPreviousProperties() {
        IEclipsePreferences node = getProjectPreferences(UI_STATE_NODE_ID);
        try {
            for(String key: node.keys()) {
                String value = node.get(key, null);
                if (value==null) continue;
                properties.put(key, value);
            }
        } catch (BackingStoreException e) {
            Log.error(e);
        }
    }      

    private Configuration readPreviousMapState() {
        IEclipsePreferences node = getProjectPreferences(POINT_NODE_ID);
        Builder builder = Configuration.builder();
        try {
            for (String key: node.keys()) {
                String pointString = node.get(key, null);
                if (pointString == null) continue;
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
        int actualsize = Math.max(size, MINIMAL_SIZE);
        mapValues.mapSize.setValue(actualsize);
        // FIXME deif set dependency in mapValueBuilder and trigger this automatically
        mapValues.mapInstance.getValue(); // trigger computation
        return this;
    }

    private void writePointPreferences() {
        if (mapValues.configuration.isError()) return;
        Configuration config = mapValues.configuration.getValueOrFail();
        IEclipsePreferences node = getProjectPreferences(POINT_NODE_ID);
        for(Point each: config.points()) {
            node.put(each.getDocument(), each.x + "@" + each.y);
        }
        try {
            node.flush();
        } catch (BackingStoreException e) {
            Log.error(e);
        }		
    }
    
    private void writeProperties() {
        IEclipsePreferences node = getProjectPreferences(UI_STATE_NODE_ID);   
        try {
            // remove any old entries
            node.clear();
            for (Entry<String, String> entry : properties.entrySet()) {
                node.put(entry.getKey(), entry.getValue());
            }
            node.flush();
        } catch (BackingStoreException e) {
            Log.error(e);
        }
    }    

    private IEclipsePreferences getProjectPreferences(String nodeId) {
        IScopeContext context = new ProjectScope(getProject());
        IEclipsePreferences node = context.getNode(nodeId);
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

    public void addLayer(Layer layer) {
        mapVisualization.getSharedLayer().add(layer);
    }
    
    public void addSelectionLayer(Layer layer, MapSelection selection) {
        addLayer(layer);
        mapValues.selections.add(selection);
    }

    public boolean containsLayer(Layer layer) {
        return mapVisualization.getSharedLayer().contains(layer);
    }
    
    public boolean containsLayer(Class<?> layerClass) {
        for (Layer each: mapVisualization.getSharedLayer()) {
            if (each.getClass().equals(layerClass)) return true;
        }
        return false;
    }

    public Configuration getConfiguration() {
        return mapValues.configuration.getValue(); // what if it's null?
    }

    public MapValues getValues() {
        return mapValues;
    }

    public void redrawWhenChanges(Value<?> selection) {
        selection.addDependent(redrawAction);
    }

    public void saveState() {
        writePointPreferences();
        writeProperties();
    }

    public void removeLayer(Class<?> layerClass) {
        for (Layer each: mapVisualization.getSharedLayer()) {
            if (each.getClass().equals(layerClass)) {
                mapVisualization.getSharedLayer().remove(each);
                return;
            }
        }
    }

    public Layer getLayer(Class<CallOverlay> layerClass) {
        for (Layer each: mapVisualization.getSharedLayer()) {
            if (each.getClass().equals(layerClass)) {
                return each;
            }
        }
        return null;
    }

    public void reloadFromScratch() {
        cache.reload(this);
    }

    public void configureOn(MapView view) {
        commands.configure(view);
        view.updateProjectName(getName());
    }

    public String getName() {
        return getProject().getName();
    }

    public YouAreHereOverlay getYouAreHereOverlay() {
        return mapVisualization.getYouAreHere();
    }
    
    public OpenFileIconsLayer getOpenFilesOverlay() {
        return mapVisualization.getOpenFiles();
    }

    public CurrentSelectionOverlay getCurrentSelectionOverlay() {
        return mapVisualization.getSelection();
    }
}
