package ch.deif.meander;

import ch.akuhn.hapax.corpus.Document;

public class Point {

	protected double x, y;
	protected Document document;

	public Point(double x, double y, Document document) {
		this.x = x;
		this.y = y;
		this.document = document;
	}

	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}	
	
	public Document getDocument() {
		return document;
	}

	public Point normalize(double minX, double minY, double width, double height) {
		return new Point((x - minX) / width, (y - minY) / height, document);
	}

}
