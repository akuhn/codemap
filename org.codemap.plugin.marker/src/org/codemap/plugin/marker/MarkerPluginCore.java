package org.codemap.plugin.marker;

import org.codemap.CodemapCore;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import ch.deif.meander.MapSelection;

/**
 * The activator class controls the plug-in life cycle
 */
public class MarkerPluginCore extends AbstractUIPlugin {
	
	// ----------------------------------------------------------------------
	// ----------------------------------------------------------------------
	// found out how to listen to resource changes from:
	// @see BreakpointManager.class
	// @see IMarker.class
	// @see MarkerUtilities.class
	// @see MarkerUtil.class (great naming btw.)
	// @see ExtendedMarkersView.class
	// @see MarkerSupportRegistry.class
	// ----------------------------------------------------------------------
	// ----------------------------------------------------------------------	
	
	public static final String PLUGIN_ID = "org.codemap.plugin.marker";
	private static MarkerPluginCore plugin;
	private MapSelection markerSelection;
	private MarkersOverlay markersOverlay;
	private MarkerController markerController;
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		markerSelection = new MapSelection();
		markersOverlay = new MarkersOverlay();
		markersOverlay.setSelection(markerSelection);
		markerController = new MarkerController(markerSelection);
		CodemapCore.getPlugin().addLayer(markersOverlay);	
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		if (markerController != null) {
			markerController.destroy();
			markerController = null;
		}
		CodemapCore.getPlugin().removeLayer(markersOverlay);
		markersOverlay = null;
		markerSelection = null;
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static MarkerPluginCore getPlugin() {
		return plugin;
	}
	
	public MarkerController getController() {
		return markerController;
	}
}
