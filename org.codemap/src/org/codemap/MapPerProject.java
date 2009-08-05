package org.codemap;

import static org.codemap.util.Icons.FILE;

import java.util.EventObject;
import java.util.HashMap;
import java.util.Map;

import org.codemap.mapview.ProviderDrivenImageOverlay;
import org.codemap.resources.NewMapResource;
import org.codemap.util.CodemapColors;
import org.codemap.util.CodemapLabels;
import org.codemap.util.Icons;
import org.codemap.util.Log;
import org.codemap.util.Resources;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.osgi.service.prefs.BackingStoreException;

import ch.akuhn.util.Arrays;
import ch.akuhn.util.Pair;
import ch.akuhn.values.ValueChangedListener;
import ch.deif.meander.Configuration;
import ch.deif.meander.Point;
import ch.deif.meander.builder.Meander;
import ch.deif.meander.swt.Background;
import ch.deif.meander.swt.CodemapVisualization;
import ch.deif.meander.swt.CompositeLayer;
import ch.deif.meander.swt.CurrSelectionOverlay;
import ch.deif.meander.swt.SWTLayer;
import ch.deif.meander.swt.YouAreHereOverlay;

/**
 * Holds corpus, map and visualization of a project. Use this class to store project specific information.
 * 
 */
public class MapPerProject {

	private Map<String,String> properties = new HashMap<String,String>();
	private CodemapColors colorScheme = new CodemapColors();
	private CodemapLabels labelScheme = new CodemapLabels();
	private CompositeLayer sharedLayer = new CompositeLayer();
	
	private static final String POINT_NODE_ID = CodemapCore.PLUGIN_ID + ".points"; 
	private static final int MINIMAL_SIZE = 300;

	private final IJavaProject project;
	private NewMapResource mapResource;
	private CodemapVisualization visual;

	
	public MapPerProject(IJavaProject project) {
		this.project = project;
		enableBuilder();
		
		mapResource = new NewMapResource("default.map", 
				Arrays.asList(Resources.asPath(project)),
				Arrays.asList("*.java"));
		mapResource.addMapInstanceListener(new ValueChangedListener() {
			@Override
			public void valueChanged(EventObject event) {
				makeMap();
			}
		});
		mapResource.getMapInstance(); // provoke first computation
	}

	private MapPerProject enableBuilder() {
		return this;
	}

	public CodemapVisualization getVisualization() {
		return visual;		
	}

	public IProject getProject() {
		return getJavaProject().getProject();
	}
	
	public Configuration getConfiguration() {
		return mapResource.getConfiguration();
	}

	private IJavaProject getJavaProject() {
		return project;
	}

	public void makeMap() {// TODO create only once per map/project
		Background background = Meander.background()
			.withColors(colorScheme)
			.makeBackground();

		SWTLayer layer = Meander.layers()
			.withLabels(labelScheme)
			.withSelection(new CurrSelectionOverlay(), CodemapCore.getPlugin().getCurrentSelection())
			.withSelection(new ProviderDrivenImageOverlay(Icons.getImage(FILE), new WorkbenchLabelProvider()), CodemapCore.getPlugin().getOpenFilesSelection())
			.withSelection(new YouAreHereOverlay(), CodemapCore.getPlugin().getYouAreHereSelection())				   
			.withLayer(sharedLayer)
			.makeLayer();

		visual = new CodemapVisualization(mapResource.getMapInstance())
			.add(layer).addBackground(background);

	}

	private Map<String, Pair<Double,Double>> reloadMapState() { // TODO
		IEclipsePreferences node = getPointNode();
		Map<String, Pair<Double,Double>> points = new HashMap<String,Pair<Double,Double>>();
		try {
			for(String key: node.keys()) {
				String pointString = node.get(key, null);
				String[] split = pointString.split("#");
				if (split.length != 2 ) {
					Log.error(new RuntimeException("Invalid format of point storage for " + getProject().getName() + ": " + pointString));
					continue;
				}
				double x = Double.parseDouble(split[0]);
				double y = Double.parseDouble(split[1]);
				points.put(key, new Pair<Double, Double>(x, y));
			}
			return points;
		} catch (BackingStoreException e) {
			Log.error(e);
		}
		return null;
	}

	public MapPerProject updateSize(int newMapDimension) {
		mapResource.setMapSize(Math.max(newMapDimension, MINIMAL_SIZE));
		return this;
	}

	public void saveMapState() {
		IEclipsePreferences node = getPointNode();
		for(Point each: getConfiguration().points()) {
			node.put(each.getDocument(), each.x + " # " + each.y);
		}
		try {
			node.flush();
		} catch (BackingStoreException e) {
			Log.error(e);
		}		
	}

	private IEclipsePreferences getPointNode() {
		IScopeContext context = new ProjectScope(getProject());
		IEclipsePreferences node = context.getNode(POINT_NODE_ID);
		return node;
	}
	
	public String getPropertyOrDefault(String key, String defaultValue) {
		String value = properties.get(key);
		return value == null ? defaultValue : value;
	}
	
	public void setProperty(String key, String value) {
		properties.put(key, value);
	}

	public CodemapColors getColorScheme() {
		return colorScheme;
	}

	public CodemapLabels getLabelScheme() {
		return labelScheme;
	}

	public boolean getPropertyOrDefault(String key, boolean defaultValue) {
		String value = properties.get(key);
		return value == null ? defaultValue : Boolean.parseBoolean(value);
	}

	public void setProperty(String key, boolean checked) {
		properties.put(key, Boolean.toString(checked));
	}
	
	public void addLayer(SWTLayer layer) {
		sharedLayer.add(layer);
	}

	public void removeLayer(SWTLayer layer) {
		sharedLayer.remove(layer);
	}	
	
}
