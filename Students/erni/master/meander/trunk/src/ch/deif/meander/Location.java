package ch.deif.meander;

import ch.akuhn.hapax.corpus.Document;
import ch.akuhn.util.Throw;
import ch.deif.aNewMeander.MapColor;
import ch.deif.meander.util.MeanderError;



/**
 * Location on a map.
 * 
 * @author Adrian Kuhn
 */
public class Location {

	private double x, y;
	private double elevation;
	private int px, py;
	private String name; // TODO is this name freetext? or does codemap use it for identifiers?
	private Document document;
	private MapColor color;

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
		if (name == null) {
			if (document() == null) return "";
			return selectFileNameOnly(document.name());			
		}
		return name;
	}

	private String selectFileNameOnly(String name) {
		int lastPathSeparator = Math.max(name.lastIndexOf('\\'), name.lastIndexOf('/'));
		int lastDot = name.lastIndexOf('.');
		if (lastPathSeparator < lastDot) return name.substring(lastPathSeparator + 1, lastDot);
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

	public void setColor(MapColor color) {
		this.color = color;
	}

	public MapColor color() {
		return color == null ? MapColor.HILLGREEN : color;
	}
	
	public String toString() {
		return px + ", " + py + " elevation: " + elevation;
	}

	public void removeColor() {
		color = null;
	}

	public String getIdentifier() {
		if (document() == null) Throw.exception(new MeanderError("Document is not expected to be null."));
		return document().getIdentifier();
	}
	
}
