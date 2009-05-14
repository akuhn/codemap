package ch.deif.meander;

import ch.akuhn.hapax.corpus.Document;


public class MapBuilder {

	private Parameters params;
	private LocationList locations;
	private Location recentLocation;
	private boolean useNormalization;

	public MapBuilder() {
		params = new Parameters();
		locations = new LocationList();
	}

	public MapBuilder pixelSize(int pixelScale) {
		params.width = pixelScale;
		return this;
	}

	public MapBuilder location(double x, double y, double elevation) {
		recentLocation = locations.makeLocation(x, y, elevation);
		return this;
	}

	public Map done() {
		locations.normalizePixelXY(params.width);
		Map map = new Map(params, locations);
		return map;
	}

	public void normalize() {
		locations.normalizeLocations();
	}

	public MapBuilder color(Colors red) {
		recentLocation.setColor(red);		
		return this;
	}

	public MapBuilder document(Document document) {
		recentLocation.setDocument(document);		
		return this;
	}

	public MapBuilder name(String string) {
		recentLocation.setName(string);		
		return this;
	}

	public MapBuilder normalizeElevation() {
		locations.normalizeElevation();
		return this;
	}

}
