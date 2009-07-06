package ch.deif.meander;

import java.util.ArrayList;

import ch.deif.aNewMeander.ContourLineAlgorithm;
import ch.deif.aNewMeander.DEMAlgorithm;
import ch.deif.aNewMeander.HillshadeAlgorithm;
import ch.deif.aNewMeander.visual.HillshadeVisualization;
import ch.deif.meander.internal.NearestNeighborAlgorithm;
import ch.deif.meander.internal.NormalizeElevationAlgorithm;
import ch.deif.meander.util.RunLengthEncodedList;
import ch.deif.meander.util.SparseTrueBooleanList;
import ch.deif.meander.viz.MapVisualization;
import ch.deif.meander.viz.SketchVisualization;

/**
 * Main data container of maps.
 * 
 * @author Adrian Kuhn
 * @author David Erni
 */
public class Map {

	private LocationList locations;
	private Parameters parameters;

	public Map(Parameters parameters, LocationList locations) {
		this.parameters = parameters;
		this.locations = locations;
		locations.normalizePixelXY(getWidth());
	}


	public Parameters getParameters() {
		return parameters;
	}

	public Iterable<Location> locations() {
		return locations;
	}

	public MapVisualization createVisualization() {
		return new SketchVisualization(this);
	}





	public static MapBuilder builder() {
		return new MapBuilder();
	}

	public int pixelSize() {
		return getWidth() * getWidth();
	}

	public int locationCount() {
		return locations.count();
	}

	public Object hasDEM() {
		return DEM != null;
	}

	public MapVisualization getDefauVisualization() {
		// TODO make all algorithms reentrant.
		Map map = this;
		new DEMAlgorithm(map).run();
		new NormalizeElevationAlgorithm(map).run();
		new HillshadeAlgorithm(map).run();
		new ContourLineAlgorithm(map).run();
		new NearestNeighborAlgorithm(map).run();
		// return new SketchVisualization(map);
		return new HillshadeVisualization(map);
	}

	public int getWidth() {
		return getParameters().width;
	}

	public Location randomLocation() {
		return locations.at((int) (locationCount() * Math.random()));
	}
	
	public Location locationAt(int index) {
		return locations.at(index);
	}

	public void updateDEM(float[][] DEM) {
		this.DEM = DEM;
	}

	public void setNearestNeighbors(Location[][] NN) {
		this.NN = new ArrayList<RunLengthEncodedList<Location>>(NN.length);
		for (int row = 0; row < NN.length; row++) {
			// TODO use a simple List of RLE list in worst case
			this.NN.add(new RunLengthEncodedList<Location>(NN[row]));
		}
	}

	public void needElevationModel() {
		if (DEM == null) new DEMAlgorithm(this).run();
	}

	public void needHillshading() {
		if (hillshade == null) {
			new HillshadeAlgorithm(this).run();
			new ContourLineAlgorithm(this).run();
		}
	}

	public void setContourLines(boolean[][] contours) {
		this.contours = new ArrayList<SparseTrueBooleanList>(contours.length);
		for (int row = 0; row < contours.length; row++) {
			// TODO use a simple List of RLE list in worst case
			this.contours.add(new SparseTrueBooleanList(contours[row]));
		}
	}

}
