package ch.unibe.softwaremap;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.search.ui.NewSearchUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import ch.deif.meander.MapSelection;
import ch.unibe.softwaremap.mapview.MapView;
import ch.unibe.softwaremap.search.MeanderQueryListener;

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
	private MeanderQueryListener queryListener;
	

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
		mapPerProjectCache = new HashMap<IProject,MapPerProject>();
		youAreHereSelection = new MapSelection();
		openFilesSelection = new MapSelection();	
		currentSelection = new MapSelection();
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
		if (queryListener == null) {
			getPlugin().registerQueryListener(); // FIXME fix for start-up problem, see #start()
		}
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
	
}
