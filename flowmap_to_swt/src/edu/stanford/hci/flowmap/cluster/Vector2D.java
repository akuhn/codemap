package edu.stanford.hci.flowmap.cluster;

import java.awt.geom.Point2D;


/**
 * Utility class for working with 2D Vectors
 * @author dphan
 * 
 * This software is distributed under the Berkeley Software Distribution License.
 * Please see http://graphics.stanford.edu/~dphan/code/bsd.license.html
 *
 */
public class Vector2D {
	private Point2D from, to, normalized;
	/**
	 * Constructs a Vector2D from two Point2D objects
	 * @param from 
	 * @param to
	 */
	public Vector2D(Point2D from, Point2D to) {
		this.from = from;
		this.to = to;
		double x, y, dist;
		
		normalized = new Point2D.Double(0,0);
		x = to.getX() - from.getX();
		y = to.getY() - from.getY();
		
		dist = Math.sqrt(x*x+y*y);
		x /= dist;
		y /= dist;
		
		normalized.setLocation(x,y);		
	}
	
	public Point2D getFrom() {
		return from;
	}
	
	public Point2D getTo() {
		return to;
	}
	
	public String toString() {
		return from + " --> " + to; 
	}
	
	public Point2D getNormalized() {
		return normalized;
	}
	
	public double dotProduct(Vector2D other) {
		Point2D otherNorm = other.getNormalized();
		return normalized.getX()*otherNorm.getX() + normalized.getY()*otherNorm.getY();
	}
	
	public double angleBetween(Vector2D other) {
		double dotValue = dotProduct(other);
		if (dotValue >= 1)
			dotValue = 1;
		else if (dotValue <= -1)
			dotValue = -1;
		return Math.acos(dotValue);
		
	}
	
	public double absAngleBetween(Vector2D other) {
		double angleBetw = angleBetween(other);
		
		// remember that for a vector (a,b)
		// and that the perpendicular vector is (-b,a)
		// now just dot perpendicular vector with the other vector, 
		// if it is positive, do nothing, 
		// if it is negative, add Math.PI
		Point2D otherNorm = other.getNormalized();
		double dot = -normalized.getY()*otherNorm.getX() + normalized.getX()*otherNorm.getY();
		
		if (dot >= 0)
			return angleBetw;
		else
			return 2*Math.PI - angleBetw;
	}
}
