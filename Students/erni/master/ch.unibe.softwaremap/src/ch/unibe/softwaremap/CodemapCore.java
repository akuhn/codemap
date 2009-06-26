package ch.unibe.softwaremap;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.search.ui.NewSearchUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import ch.deif.meander.viz.MapVisualization;
import ch.unibe.softwaremap.mapview.MapView;
import ch.unibe.softwaremap.search.MeanderQueryListener;

/**
 * One singleton to rule them all.
 * 
 */
public class CodemapCore extends AbstractUIPlugin {

	public static final String PLUGIN_ID = CodemapCore.class.getPackage().getName();
	private static CodemapCore THE_PLUGIN;
	
	private Map<IProject,MapPerProject> hashmap;
	private MapView theView;
	private int currentMapDimension;
	private IProject currentProject;
	private MeanderQueryListener queryListener;

	
	public CodemapCore() {
	}

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		THE_PLUGIN = this;
		// FIXME find out why this does not work at start-up!
		// registerQueryListener();
	}

	private void registerQueryListener() {
		queryListener = new MeanderQueryListener();
		NewSearchUI.addQueryListener(queryListener);
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		unregisterQueryListener();
		THE_PLUGIN = null;
		super.stop(context);
	}

	private void unregisterQueryListener() {
		NewSearchUI.removeQueryListener(queryListener);
	}

	public static CodemapCore getPlugin() {
		return THE_PLUGIN;
	}

	public final static String makeID(Class<?> javaClass) {
		return PLUGIN_ID + "." + javaClass.getSimpleName();
	}
	
	public MapPerProject mapForProject(IProject project) {
		if (hashmap == null) {
			hashmap = new HashMap<IProject,MapPerProject>();
			getPlugin().registerQueryListener(); // FIXME fix for start-up problem, see #start()
		}
		MapPerProject map = hashmap.get(project);
		if (map == null) {
			hashmap.put(project, map = new MapPerProject(project));
		}
		return map.updateSize(currentMapDimension);
	}
	
	public MapPerProject mapForChangedProject(IProject project) {
		currentProject = project;
		return mapForProject(project);
	}
	
	public IProject currentProject() {
		return currentProject;
	}

	public void setMapView(MapView view) {
		theView = view;
	}

	public MapView getMapView() {
		return theView;
	}

	public void updateMapdimension(int newDimension) {
		currentMapDimension = newDimension;
		MapVisualization viz = mapForChangedProject(currentProject()).updateSize(currentMapDimension).getVisualization();
		if (viz != null) {
			theView.updateMapVisualization(viz);
		}
		getMapView().redrawContainer();
	}
	
	public void updateMap() {
		mapForProject(currentProject()).updateMap();
	}
}
