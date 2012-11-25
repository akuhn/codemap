package org.codemap;

import org.codemap.layers.Label;

/**
 * Location of a document on a map instance. Coordinates are in pixels. When
 * normalized, elevation is between 0 and 100.
 * 
 */
public class Location implements Comparable<Location> {

    public final int px, py;
    private double elevation;
    private Point point;
    private Label label;

    private Location(Point point, double elevation, int px, int py, Label label) {
        if (Double.isNaN(elevation))
            throw new AssertionError();
        this.point = point;
        this.elevation = elevation;
        this.px = px;
        this.py = py;
        this.label = label;
    }

    public Location(Point point, double elevation, int px, int py) {
        this(point, elevation, px, py, null);
    }    

    public String getDocument() {
        return point.getDocument();
    }

    /**
     * Returns short name of location, assuming that its full name is a
     * filename.
     * 
     */
    public String getName() {
        String path = getDocument();
        int begin = path.lastIndexOf('/');
        int end = path.indexOf('.', begin);
        return path.substring(begin + 1, end < 0 ? path.length() : end);
    }

    public Location withElevation(double newElevation) {
        if (Double.isNaN(newElevation))
            throw new AssertionError();
//        Location clone = new Location(this);
        this.elevation = newElevation;
        return this;
    }

    public double getElevation() {
        return elevation;
    }

    public Point getPoint() {
        return point;
    }

    @Override
    public String toString() {
        return "Location (" + px + "@" + py + ") " + getDocument();
    }

    @Override
    public int compareTo(Location o) {
        return getDocument().compareTo(o.getDocument());
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof Location) && equalsLocation((Location) o);
    }

    protected boolean equalsLocation(Location o) {
        return (o != null) && o.getDocument().equals(this.getDocument());
    }

    public void setLabel(Label label) {
        this.label = label;
    }
    
    public Label getLabel() {
        return label;
    }
}
