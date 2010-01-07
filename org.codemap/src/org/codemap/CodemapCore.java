package org.codemap;

import org.codemap.communication.ECFContribution;
import org.codemap.mapview.MapController;
import org.codemap.util.ColorScheme;
import org.codemap.util.EclipseTaskFactory;
import org.codemap.util.MColor;
import org.codemap.util.MapScheme;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import ch.akuhn.values.TaskValue;

/**
 * Single instance of the running Codemap plug-in.
 * 
 */
public class CodemapCore extends AbstractUIPlugin {

    public static final String PLUGIN_ID = CodemapCore.class.getPackage().getName();
    private static CodemapCore THE_PLUGIN;

    private MapController theController;

    public final MapSelection youAreHereSelection;
    public final MapSelection openFilesSelection;
    public final MapSelection currentSelection;
    private ECFContribution ecfContrib;
    private ColorScheme colorScheme;

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
        colorScheme = ColorScheme.blackWhite();
    }

    @Override
    public void start(BundleContext context) throws Exception {
        super.start(context);
        THE_PLUGIN = this;
        try {
            ecfContrib = new ECFContribution().start(context);            
        } catch (NoClassDefFoundError e) {
            // nothing
        }
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        if (ecfContrib != null) {
            ecfContrib.stop();
        }
        THE_PLUGIN = null;
        super.stop(context);
    }

    public static CodemapCore getPlugin() {
        return THE_PLUGIN;
    }

    public final static String makeID(Class<?> javaClass) {
        return PLUGIN_ID + "." + javaClass.getSimpleName();
    }
    
    public MapPerProject getActiveMap() {
        if (theController == null) return null;
        return theController.getActiveMap();
    }

    public MapScheme<MColor> getDefaultColorScheme() {
        return new MapScheme<MColor>(getColorScheme().getHillColor());
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
    
    public ColorScheme getColorScheme() {
        return colorScheme;
    }
    
    public static ColorScheme colorScheme() {
        return CodemapCore.getPlugin().getColorScheme();
    }    
    
}
