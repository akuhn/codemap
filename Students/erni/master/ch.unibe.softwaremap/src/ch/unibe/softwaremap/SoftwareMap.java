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
public class SoftwareMap extends AbstractUIPlugin {

	public static final String PLUGIN_ID = SoftwareMap.class.getPackage().getName();
	private static SoftwareMap plugin;
	
	private Map<IProject,ProjectMap> hashmap;
	// TODO is there a better way to manage the single MapView instance?
	private MapView mapView;
	private int currentMapDimension;
	private IProject currentProject;
	
	private List<IMeanderPlugin> plugins;
	private MeanderQueryListener queryListener;

	public SoftwareMap() {
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

	public static SoftwareMap core() {
		return plugin;
	}

	public final static String makeID(Class<?> javaClass) {
		return PLUGIN_ID + "." + javaClass.getSimpleName();
	}
	
	public ProjectMap mapForProject(IProject project) {
		if (hashmap == null) {
			// FIXME find out why we can't call that from start()
			core().registerQueryListener();
			
			hashmap = new HashMap<IProject,ProjectMap>();
		}
		ProjectMap map = hashmap.get(project);
		if (map == null) {
			hashmap.put(project, map = new ProjectMap(project));
		}
		return map.updateSize(currentMapDimension);
	}
	
	public ProjectMap mapForChangedProject(IProject project) {
		currentProject = project;
		return mapForProject(project);
	}
	
	public IProject currentProject() {
		return currentProject;
	}

	public void setMapView(MapView view) {
		mapView = view;
	}

	public MapView getMapView() {
		return mapView;
	}

	public void updateMapdimension(int newDimension) {
		currentMapDimension = newDimension;
		MapVisualization viz = mapForChangedProject(currentProject()).updateSize(currentMapDimension).getVisualization();
		if (viz != null) {
			mapView.updateMapVisualization(viz);
		}
	}
	
	public void updateMap() {
		mapForProject(currentProject()).updateMap();
	}

	public void register(IMeanderPlugin plugin) {
		plugins.add(plugin);
	}
}
