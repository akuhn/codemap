package ch.deif.meander;

import ch.akuhn.hapax.corpus.Corpus;
import ch.akuhn.hapax.corpus.Document;
import ch.akuhn.hapax.corpus.Terms;
import ch.akuhn.util.Bag;

public class MapBuilder {

	private static class Doc extends Document {

		private Terms terms;

		public Doc(String name, String version) {
			super(name, version);
			terms = new Terms();
		}

		@Override
		public Document addTerms(Terms terms) {
			this.terms.addAll(terms);
			return this;
		}

		@Override
		public Corpus owner() {
			return null;
		}

		@Override
		public Terms terms() {
			return this.terms;
		}

		public Document addTerms(Bag<String> terms) {
			this.terms.addAll(terms);
			return this;
		}

	}

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

	public MapBuilder location(double xNormed, double yNormed, double h) {
		locations.makeLocation(xNormed, yNormed, h);
		return this;
	}

	public MapBuilder location(double xNormed, double yNormed, double h, Document document) {
		locations.makeLocation(xNormed, yNormed, h).setDocument(document);
		return this;
	}

	public Map done() {
		Map map = new Map(params, locations);
		map.name = name;
		return map;
	}

	public MapBuilder name(String versionName) {
		name = versionName;
		return this;
	}

	public MapBuilder location(double x, double y, int z, String string) {
		locations.makeLocation(x, y, z).setName(string);
		return this;
	}

	public void normalizeXY() {
		locations.normalizeXY();
	}

}
