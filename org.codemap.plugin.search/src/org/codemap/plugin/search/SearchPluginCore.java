package org.codemap.plugin.search;

import java.util.HashMap;
import java.util.Map;

import org.codemap.CodemapCore;
import org.codemap.MapPerProject;
import org.eclipse.search.ui.NewSearchUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import ch.deif.meander.MapSelection;

/**
 * The activator class controls the plug-in life cycle
 */
public class SearchPluginCore extends AbstractUIPlugin {

    public static final String PLUGIN_ID = "org.codemap.plugin.search";
    private static SearchPluginCore plugin;

    private QueryListener theQueryListener;
    private SearchResultController theController;
    
	private Map<MapPerProject, MapSelection> selectionCache = new HashMap<MapPerProject, MapSelection>();

    @Override
    public void start(BundleContext context) throws Exception {
        super.start(context);
        plugin = this;

        init();
        registerQueryListener();
        handleOldQueries();
    }

    private void handleOldQueries() {
        theController.onLayerActivated();
    }

    public SearchResultController getController() {
        return theController;
    }

    private void init() {
        theController = new SearchResultController();
        theQueryListener = new QueryListener(theController);
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        unregisterQueryListener();
        destroy();
        super.stop(context);
    }

    private void destroy() {
        plugin = null;
        theController = null;
    }

    public MapSelection getSearchSelection() {
		MapPerProject activeMap = CodemapCore.getPlugin().getActiveMap();
		MapSelection mapSelection = selectionCache.get(activeMap);
		if (mapSelection == null) {
			mapSelection = new MapSelection();
			SearchResultsOverlay searchOverlay = new SearchResultsOverlay();
			activeMap.addLayer(searchOverlay);
			
			selectionCache.put(activeMap, mapSelection);
		}
		return mapSelection;    	
    	
    	
    }

    public static SearchPluginCore getPlugin() {
        return plugin;
    }

    private void registerQueryListener() {
        NewSearchUI.addQueryListener(theQueryListener);
    }

    private void unregisterQueryListener() {
        NewSearchUI.removeQueryListener(theQueryListener);
    }	
}
