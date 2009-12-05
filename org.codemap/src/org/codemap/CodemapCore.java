package org.codemap;

import org.codemap.mapview.MapController;
import org.codemap.util.EclipseTaskFactory;
import org.codemap.util.MColor;
import org.codemap.util.MapScheme;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import ch.akuhn.values.TaskValue;

/**
 * Single instance of the running Codemap plug-in.
 * <p>
 * Keeps a cache of MapPerProject instances, a pointer to the open MapView (if
 * any) and listens to Eclipse search queries. Do not use this class to store
 * any other information. Either use the view (for UI related state) or the map
 * instances (for project specific state).
 * </p>
 *   
 */
public class CodemapCore extends AbstractUIPlugin {

    public static final String PLUGIN_ID = CodemapCore.class.getPackage().getName();
    private static CodemapCore THE_PLUGIN;

    private MapController theController;

    public final MapSelection youAreHereSelection;
    public final MapSelection openFilesSelection;
    public final MapSelection currentSelection;

    public MapSelection getYouAreHereSelection() {
        return youAreHereSelection;
    }

    public MapSelection getOpenFilesSelection() {
        return openFilesSelection;
    }

    public MapSelection getCurrentSelection() {
        return currentSelection;
    }

    public CodemapCore() {
        TaskValue.setTaskFactory(new EclipseTaskFactory());
        youAreHereSelection = new MapSelection();
        openFilesSelection = new MapSelection();
        currentSelection = new MapSelection();
    }

    @Override
    public void start(BundleContext context) throws Exception {
        super.start(context);
        THE_PLUGIN = this;
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        theController.onStop();
        THE_PLUGIN = null;
        super.stop(context);
    }

    public static CodemapCore getPlugin() {
        return THE_PLUGIN;
    }

    public final static String makeID(Class<?> javaClass) {
        return PLUGIN_ID + "." + javaClass.getSimpleName();
    }
    
    // TODO: might be removed
    public MapPerProject getActiveMap() {
        if (theController == null) return null;
        return theController.getActiveMap();
    }

    public MapScheme<MColor> getDefaultColorScheme() {
        return new MapScheme<MColor>(MColor.HILLGREEN);
    }
    
    public static String activeProjectName() {
    	return getPlugin().getActiveMap().getProject().getName();
    }

    public void register(MapController mapController) {
        setMapController(mapController);
    }

    public void unregister(MapController mapController) {
        setMapController(null);
    }

    private void setMapController(MapController mapController) {
        theController = mapController;
    }

    public MapController getController() {
        return theController;
    }    
    
}
