package ch.deif.aNewMeander;

import ch.akuhn.hapax.corpus.Document;

public class Location extends Point {

	protected int documentSize;
	protected Document document;

	public Location(Point p, Document document, int documentSize) {
		super(p);
		this.documentSize = documentSize;
		this.document = document;
	}
	
	public Location(Location location) {
		this(location, location.document, location.documentSize);
	}

	public int getDocumentSize() {
		return documentSize;
	}

	public Document getDocument() {
		return document;
	}

	public Location normalize(double minX, double minY, double width, double height) {
		return (Location) this.withXY((x - minX) / width, (y - minY) / height);
	}

}
