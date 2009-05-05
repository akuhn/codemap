package ch.deif.meander;

import ch.akuhn.hapax.corpus.Document;

public class MapBuilder {

	private Parameters params;
	private LocationList locations;
	private String name;

	public MapBuilder() {
		params = new Parameters();
		locations = new LocationList();
	}

	public MapBuilder size(int pixelScale) {
		params.width = pixelScale;
		params.height = pixelScale;
		return this;
	}

	public MapBuilder location(double x, double y, double elevation) {
		locations.makeLocation(x, y, elevation);
		return this;
	}

	public MapBuilder location(double x, double y, double elevation, Document document) {
		locations.makeLocation(x, y, elevation).setDocument(document);
		return this;
	}

	public Map done() {
		locations.normalizePixelXY(params.width);
		Map map = new Map(params, locations);
		map.name = name;
		return map;
	}

	public MapBuilder name(String versionName) {
		name = versionName;
		return this;
	}

	public MapBuilder location(double x, double y, int elevation, String string) {
		locations.makeLocation(x, y, elevation).setName(string);
		return this;
	}

	public void normalize() {
		locations.normalizeLocations();
	}

	public MapBuilder color(Colors red) {
		locations.last().setColor(red);		
		return this;
	}

}
