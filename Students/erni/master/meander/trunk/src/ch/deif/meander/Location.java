package ch.deif.meander;

import java.awt.Point;

import ch.akuhn.hapax.corpus.Document;

/**
 * Elevation on the map, (scalable value object);
 * 
 * @author Adrian Kuhn
 */
public interface Location {

	public double elevation();

	public double x();

	public double y();

	public void setDocument(Document document);

	public Document getDocument();

	public double normElevation();

	public int py();

	public int px();

	public void setName(String string);

	public String getName();

	public Point getPointOn(Map map);

}
