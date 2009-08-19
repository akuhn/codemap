package org.codemap.plugin.marker;

import java.util.HashMap;
import java.util.Map;

import org.codemap.CodemapCore;
import org.codemap.MapPerProject;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class MarkerPlugin extends AbstractUIPlugin {
	
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
	private static MarkerPlugin plugin;
	private MarkerController markerController;
	
	private Map<MapPerProject, MarkerSelection> selectionCache = new HashMap<MapPerProject, MarkerSelection>();
	
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
	public static MarkerPlugin getPlugin() {
		return plugin;
	}
	
	public MarkerController getController() {
		return markerController;
	}

	public MarkerSelection getCurrentMarkerSelection() {
		MapPerProject activeMap = CodemapCore.getPlugin().getActiveMap();
		MarkerSelection selection = selectionCache.get(activeMap);
		if (selection == null) {
			selection = new MarkerSelection();
			MarkersOverlay markersOverlay = new MarkersOverlay();
			markersOverlay.setMarkerSelection(selection);
			activeMap.addLayer(markersOverlay);
			
			selectionCache.put(activeMap, selection);
			activeMap.redrawWhenChanges(selection.getSelection());
		}
		return selection;
	}
}
