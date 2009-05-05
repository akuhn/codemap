package ch.deif.meander;

import ch.akuhn.hapax.corpus.Document;



/**
 * Location on a map.
 * 
 * @author Adrian Kuhn
 */
public class Location {

	private double x, y;
	private double elevation;
	private int px, py;
	private String name;
	private Document document;
	private Colors color;

	public Location(double x, double y, double elevation) {
		this.x = x;
		this.y = y;
		this.elevation = elevation;
		this.px = -1;
		this.py = -1;
		this.name = null;
		this.document = null;
	}
	
	public double elevation() {
		return elevation;
	}

	public double x() {
		return x;
	}
	
	public double y() {
		return y;
	}
	
	protected void setDocument(Document document) {
		this.document = document;
	}

	public Document document() {
		return document;
	}

	public int py() {
		return py;
	}
	
	public int px() {
		return px;
	}

	protected void setName(String name) {
		this.name = name;
	}

	public String name() {
		return name;
	}

	protected void normalizePixelXY(int pixelSize) {
		this.px = (int) (x() * pixelSize);
		this.py = (int) (y() * pixelSize);
	}
	
	protected void normalizeElevation(double maxElevation) {
		this.elevation = 100 * this.elevation / maxElevation;
	}
	
	protected void normalizeXY(double left, double width, double top, double height) {
		this.x = (this.x - left) / width;
		this.y = (this.y - top) / height;
	}

	public void setColor(Colors color) {
		this.color = color;
	}

	public Colors color() {
		return color == null ? Colors.HILLGREEN : color;
	}
	
}
