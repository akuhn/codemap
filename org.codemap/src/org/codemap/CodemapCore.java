
package org.codemap;

import java.util.HashMap;
import java.util.Map;

import org.codemap.mapview.MapView;
import org.codemap.util.CodemapColors;
import org.codemap.util.CodemapLabels;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import ch.deif.meander.MapSelection;

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
	
	private Map<IJavaProject,MapPerProject> mapPerProjectCache;
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
		mapPerProjectCache = new HashMap<IJavaProject,MapPerProject>();
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
		saveMapState();
		THE_PLUGIN = null;
		super.stop(context);

	}

	private void saveMapState() {
		for (MapPerProject each: mapPerProjectCache.values()) {
			each.saveMapState();
		}
	}

	public static CodemapCore getPlugin() {
		return THE_PLUGIN;
	}

	public final static String makeID(Class<?> javaClass) {
		return PLUGIN_ID + "." + javaClass.getSimpleName();
	}
	
	public MapPerProject mapForProject(IJavaProject project) {
		MapPerProject map = mapPerProjectCache.get(project);
		if (map == null) {
			mapPerProjectCache.put(project, map = new MapPerProject(project));
		}
		return map;
	}
	
	public void setMapView(MapView caller) {
		theView = caller;
	}

	public MapView getMapView() {
		return theView;
	}
	
	public CodemapLabels getLabelScheme() {
		return getActiveMap().getLabelScheme();
	}

	public CodemapColors getColorScheme() {
		return getActiveMap().getColorScheme();
	}
	
	public MapPerProject getActiveMap() {
		return mapForProject(theView.getCurrentProject());
	}

	// TODO: the layers itself should issue the redraws
	public void redrawCodemap() {
		getMapView().redrawAsync();
	}
	
	public void redrawCodemapBackground() {
		getMapView().redrawCodemapBackground();
		getMapView().redrawAsync();
	}
	
}
