package org.codemap.plugin.search;

import org.codemap.CodemapCore;
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
    private SearchResultsOverlay searchResultsOverlay;
    private MapSelection searchSelection;
    private SearchResultController theController;

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

        searchSelection = new MapSelection();
        searchResultsOverlay = new SearchResultsOverlay();
        searchResultsOverlay.setSelection(searchSelection);
        CodemapCore.getPlugin().addLayer(searchResultsOverlay);
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
        CodemapCore.getPlugin().removeLayer(searchResultsOverlay);
        searchResultsOverlay = null;
        searchSelection = null;
    }

    public MapSelection getSearchSelection() {
        return searchSelection;
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
