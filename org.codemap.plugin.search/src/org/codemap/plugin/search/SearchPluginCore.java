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

	// The plug-in ID
	public static final String PLUGIN_ID = "org.codemap.plugin.search";

	// The shared instance
	private static SearchPluginCore plugin;

	private MeanderQueryListener queryListener;

	private SearchResultsOverlay searchResultsOverlay;

	private MapSelection searchSelection;
	
	/**
	 * The constructor
	 */
	public SearchPluginCore() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		
		registerQueryListener();
		searchSelection = new MapSelection();
		searchResultsOverlay = new SearchResultsOverlay();
		searchResultsOverlay.setSelection(searchSelection);
		CodemapCore.getPlugin().addLayer(searchResultsOverlay);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		CodemapCore.getPlugin().removeLayer(searchResultsOverlay);
		searchResultsOverlay = null;
		searchSelection = null;
		unregisterQueryListener();
		super.stop(context);
	}
	
	public MapSelection getSearchSelection() {
		return searchSelection;
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static SearchPluginCore getPlugin() {
		return plugin;
	}
	

	private void registerQueryListener() {
		queryListener = new MeanderQueryListener();
		NewSearchUI.addQueryListener(queryListener);
	}

	private void unregisterQueryListener() {
		NewSearchUI.removeQueryListener(queryListener);
	}	

}
