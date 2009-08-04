package org.codemap.plugin.marker;

import java.util.HashMap;
import java.util.Map;

import org.codemap.CodemapCore;
import org.codemap.MapPerProject;
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
	private MarkerController markerController;
	
	private Map<MapPerProject, MapSelection> selectionCache = new HashMap<MapPerProject, MapSelection>();
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		markerController = new MarkerController();
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
//		CodemapCore.getPlugin().removeLayer(markersOverlay);
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

	public MapSelection getCurrentMarkerSelection() {
		MapPerProject activeMap = CodemapCore.getPlugin().getActiveMap();
		MapSelection mapSelection = selectionCache.get(activeMap);
		if (mapSelection == null) {
			mapSelection = new MapSelection();
			MarkersOverlay markersOverlay = new MarkersOverlay();
			markersOverlay.setSelection(mapSelection);
			activeMap.addLayer(markersOverlay);
			
			selectionCache.put(activeMap, mapSelection);
		}
		return mapSelection;
	}
}
