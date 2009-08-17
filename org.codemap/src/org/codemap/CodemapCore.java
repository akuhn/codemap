
package org.codemap;

import org.codemap.mapview.MapView;
import org.codemap.util.EclipseTaskFactory;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import ch.akuhn.values.TaskValue;
import ch.deif.meander.MapSelection;
import ch.deif.meander.MapSelection;
import ch.deif.meander.util.MColor;
import ch.deif.meander.util.MapScheme;

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

    private MapView theView;

    private final MapSelection youAreHereSelection;
    private final MapSelection openFilesSelection;
    private final MapSelection currentSelection;

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
        MapPerProject.saveMapState();
        THE_PLUGIN = null;
        super.stop(context);

    }


    public static CodemapCore getPlugin() {
        return THE_PLUGIN;
    }

    public final static String makeID(Class<?> javaClass) {
        return PLUGIN_ID + "." + javaClass.getSimpleName();
    }

    public MapPerProject mapForProject(IJavaProject project) {
        return MapPerProject.forProject(project);
    }

    public void setMapView(MapView caller) {
        theView = caller;
    }

    public MapView getMapView() {
        return theView;
    }

    public MapPerProject getActiveMap() {
        return mapForProject(theView.getCurrentProject());
    }

    // redraws happen automatically
    // TODO remove calls    
    @Deprecated
    public void redrawCodemap() {
        getMapView().redrawAsync();
    }
    
    // redraws happen automatically
    // TODO remove calls
    @Deprecated
    public void redrawCodemapBackground() {
        getMapView().redrawCodemapBackground();
        getMapView().redrawAsync();
    }

    public MapScheme<MColor> getDefaultColorScheme() {
        return new MapScheme<MColor>(MColor.HILLGREEN);
    }

}
