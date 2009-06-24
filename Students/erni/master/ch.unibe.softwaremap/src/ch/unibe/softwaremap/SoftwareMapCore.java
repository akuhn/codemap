package ch.unibe.softwaremap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.search.ui.NewSearchUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import ch.deif.meander.viz.MapVisualization;
import ch.unibe.softwaremap.search.MeanderQueryListener;
import ch.unibe.softwaremap.ui.MapView;

/**
 * One singleton to rule them all.
 * 
 */
public class SoftwareMapCore extends AbstractUIPlugin {

	public static final String PLUGIN_ID = SoftwareMapCore.class.getPackage().getName();
	private static SoftwareMapCore plugin;
	private static Map<IProject,ProjectMap> hashmap;
	// TODO is there a better way to manage the single MapView instance?
	private static MapView mapView;
	private static int currentMapDimension;
	private static IProject currentProject;
	
	private static List<IMeanderPlugin> plugins;
	private MeanderQueryListener queryListener;

	public SoftwareMapCore() {
		plugins = new ArrayList<IMeanderPlugin>();
	}

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		
		// FIXME find out why this does not work that way
//		registerQueryListener();
	}

	protected void registerQueryListener() {
		queryListener = new MeanderQueryListener();
		NewSearchUI.addQueryListener(queryListener);
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		unregisterQueryListener();
		
		plugin = null;
		super.stop(context);
	}

	protected void unregisterQueryListener() {
		NewSearchUI.removeQueryListener(queryListener);
	}

	public static SoftwareMapCore getDefault() {
		return plugin;
	}

	public final static String makeID(Class<?> javaClass) {
		return PLUGIN_ID + "." + javaClass.getSimpleName();
	}

	public static ProjectMap at(IProject project) {
		currentProject = project;
		if (hashmap == null) {
			// FIXME find out why we can't call that from start()
			getDefault().registerQueryListener();
			
			hashmap = new HashMap<IProject,ProjectMap>();
		}
		ProjectMap map = hashmap.get(project);
		if (map == null) {
			hashmap.put(project, map = new ProjectMap(project));
		}
		return map.updateSize(currentMapDimension);
	}
	
	public static IProject currentProject() {
		return currentProject;
	}

	public static void setMapView(MapView mapView) {
		SoftwareMapCore.mapView = mapView;
	}

	public static MapView getMapView() {
		return mapView;
	}

	public static void updateMapdimension(int newDimension) {
		currentMapDimension = newDimension;
		MapVisualization viz = at(currentProject).updateSize(currentMapDimension).getVisualization();
		if (viz != null) {
			mapView.updateMapVisualization(viz);
		}
	}

	public static void register(IMeanderPlugin plugin) {
		plugins.add(plugin);
	}
}
