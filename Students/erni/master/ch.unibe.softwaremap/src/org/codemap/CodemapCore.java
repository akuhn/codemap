
package org.codemap;

import java.util.HashMap;
import java.util.Map;

import org.codemap.mapview.MapView;
import org.codemap.util.CodemapColors;
import org.codemap.util.CodemapLabels;
import org.codemap.util.DynamicCodemapLayer;
import org.eclipse.core.resources.IProject;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import ch.deif.meander.MapSelection;
import ch.deif.meander.util.MapScheme;
import ch.deif.meander.visual.Layer;

/**
 * Single instance of the running Codemap plug-in.
 *<P>
 * Keeps a cache of MapPerProject instances, a pointer to the open MapView (if
 * any) and listens to Eclipse search queries. Do not use this class to store
 * any other information. Either use the view (for UI related state) or the map
 * instances (for project specific state).
 *   
 */
public class CodemapCore extends AbstractUIPlugin {

	public static final String PLUGIN_ID = CodemapCore.class.getPackage().getName();
	private static CodemapCore THE_PLUGIN;
	
	private Map<IProject,MapPerProject> mapPerProjectCache;
	private MapView theView;
	

	private final MapSelection youAreHereSelection;
	private final MapSelection openFilesSelection;
	private final MapSelection currentSelection;
	private final MapScheme<String> labelScheme;
	private final CodemapColors colorScheme;
	private DynamicCodemapLayer dynamicMapLayer;
	

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
		mapPerProjectCache = new HashMap<IProject,MapPerProject>();
		youAreHereSelection = new MapSelection();
		openFilesSelection = new MapSelection();	
		currentSelection = new MapSelection();
		labelScheme = new CodemapLabels();	
		colorScheme = new CodemapColors();
		dynamicMapLayer = new DynamicCodemapLayer();
	}

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		THE_PLUGIN = this;
		// FIXME find out why this does not work at start-up!
		// registerQueryListener();
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		THE_PLUGIN = null;
		super.stop(context);
	}

	public static CodemapCore getPlugin() {
		return THE_PLUGIN;
	}

	public final static String makeID(Class<?> javaClass) {
		return PLUGIN_ID + "." + javaClass.getSimpleName();
	}
	
	public MapPerProject mapForProject(IProject project) {
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
	
	public MapScheme<String> getLabelScheme() {
		return labelScheme;
	}

	public CodemapColors getColorScheme() {
		return colorScheme;
	}

	public void redrawCodemap() {
		getMapView().redraw();
	}
	
	public void redrawCodemapBackground() {
		getMapView().redrawCodemapBackground();
		getMapView().redraw();
	}

	public void addLayer(Layer layer) {
		dynamicMapLayer.append(layer);
	}

	public void removeLayer(Layer layer) {
		dynamicMapLayer.remove(layer);
	}

	protected Layer getDynamicLayer() {
		return dynamicMapLayer;
	}
	
}
