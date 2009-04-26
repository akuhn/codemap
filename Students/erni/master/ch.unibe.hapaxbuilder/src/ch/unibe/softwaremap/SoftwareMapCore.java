package ch.unibe.softwaremap;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import ch.unibe.softwaremap.ui.MapView;

/**
 * The activator class controls the plug-in life cycle
 */
public class SoftwareMapCore extends AbstractUIPlugin {

	public static final String PLUGIN_ID = SoftwareMapCore.class.getPackage().getName();
	private static SoftwareMapCore plugin;
	private static Map<IProject,ProjectMap> hashmap;
	// TODO is there a better way to manage the single MapView instance?
	private static MapView mapView;

	public SoftwareMapCore() {
	}

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	public static SoftwareMapCore getDefault() {
		return plugin;
	}

	public final static String makeID(Class<?> javaClass) {
		return PLUGIN_ID + "." + javaClass.getSimpleName();
	}

	public static ProjectMap at(IProject project) {
		if (hashmap == null) {
			hashmap = new HashMap<IProject,ProjectMap>();
		}
		ProjectMap map = hashmap.get(project);
		if (map == null) {
			hashmap.put(project, map = new ProjectMap(project));
		}
		return map;

	}

	public static void setMapView(MapView mapView) {
		SoftwareMapCore.mapView = mapView;
	}

	public static MapView getMapView() {
		return mapView;
	}
}
