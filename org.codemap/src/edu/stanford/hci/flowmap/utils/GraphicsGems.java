package edu.stanford.hci.flowmap.utils;

import org.codemap.util.geom.Line2D;
import org.codemap.util.geom.Point2D;
import org.codemap.util.geom.Rectangle2D;


/**
 * This software is distributed under the Berkeley Software Distribution License.
 * Please see http://graphics.stanford.edu/~dphan/code/bsd.license.html
 *
 */
public class GraphicsGems {
	
	public static void checkNaN(Point2D pt) {
		assert (!Double.isNaN(pt.getX()));
		assert (!Double.isNaN(pt.getY()));
		
	}
	
	/**
	 * Copied from http://www.webreference.com/programming/java/beginning/chap5/3/3.html
	 * If the lines are parallel, the denominator in the equation for t will be zero, 
	 * something you should really check for in the code. For the moment, we will ignore 
	 * it and end up with coordinates that are Infinity if it occurs.
	 * @param line1
	 * @param line2
	 * @return
	 */
	public static Point2D intersectSegments(Line2D line1, Line2D line2) {
		//System.out.println("GraphicsGem.intersects: line1" + line1.getP1() + "," + line1.getP2() + " line2: " + line2.getP1() + "," + line2.getP2());
		if(!line1.intersectsLine(line2)){
			//System.out.println("No Intersection!1");
			return new Point2D.Double(Double.POSITIVE_INFINITY,Double.POSITIVE_INFINITY);
		}
		
		Point2D localPoint = new Point2D.Double(0, 0);
		
		double num = (line2.getP2().getY()-line2.getP1().getY())*(line2.getP1().getX()-line1.getP1().getX()) -
		(line2.getP2().getX()-line2.getP1().getX())*(line2.getP1().getY()-line1.getP1().getY());
		
		double denom = (line2.getP2().getY()-line2.getP1().getY())*(line1.getP2().getX()-line1.getP1().getX()) -
		(line2.getP2().getX()-line2.getP1().getX())*(line1.getP2().getY()-line1.getP1().getY());
		
		double t = num/denom;
		if(t < 0 || t > 1){
			//System.out.println("No Intersection!2");
			return new Point2D.Double(Double.POSITIVE_INFINITY,Double.POSITIVE_INFINITY);
		}
		
		double x = line1.getP1().getX() + (line1.getP2().getX()-line1.getP1().getX())*t;
		double y = line1.getP1().getY() + (line1.getP2().getY()-line1.getP1().getY())*t;
		localPoint.setLocation(x, y);
		
		//System.out.println("Intersection at: " + localPoint);
		return localPoint;
	}
	
	/**
	 * Copied from http://www.webreference.com/programming/java/beginning/chap5/3/3.html
	 * If the lines are parallel, the denominator in the equation for t will be zero, 
	 * something you should really check for in the code. For the moment, we will ignore 
	 * it and end up with coordinates that are Infinity if it occurs.
	 * @param line1
	 * @param line2
	 * @return
	 */
	public static Point2D intersectInfiniteLines(Line2D line1, Line2D line2) {
		Point2D localPoint = new Point2D.Double(0, 0);
		
		double num = (line2.getP2().getY()-line2.getP1().getY())*(line2.getP1().getX()-line1.getP1().getX()) -
		(line2.getP2().getX()-line2.getP1().getX())*(line2.getP1().getY()-line1.getP1().getY());
		
		double denom = (line2.getP2().getY()-line2.getP1().getY())*(line1.getP2().getX()-line1.getP1().getX()) -
		(line2.getP2().getX()-line2.getP1().getX())*(line1.getP2().getY()-line1.getP1().getY());
		
		double t = num/denom;
		double x = line1.getP1().getX() + (line1.getP2().getX()-line1.getP1().getX())*t;
		double y = line1.getP1().getY() + (line1.getP2().getY()-line1.getP1().getY())*t;
		localPoint.setLocation(x, y);
		
		//System.out.println("Intersection at: " + localPoint);
		return localPoint;
	}
	
	/**
	 * @param rect
	 * @param line
	 * @param point
	 * @return the closest point on the boundary of rect that is on line, measured from point
	 */
	public static Point2D closestIntersectBox(Rectangle2D rect, Line2D line, Point2D point) {
		//System.out.println("Looking at box: " + rect + " line from " + line.getP1() + " to " + line.getP2() + " and point " + point);
		Point2D closest, candidate;
		double closestDist, candidateDist;
		
		
		Point2D topLeft = new Point2D.Double(rect.getX(), rect.getY());
		Point2D topRight = new Point2D.Double(rect.getX()+rect.getWidth(), rect.getY());
		Point2D bottomLeft = new Point2D.Double(rect.getX(), rect.getY()+rect.getHeight());
		Point2D bottomRight = new Point2D.Double(rect.getX()+rect.getWidth(),rect.getY()+rect.getHeight() );
		
		
		// top of rectangle
		Line2D top = new Line2D.Double(topLeft, topRight);
		closest = intersectSegments(line, top);
		closestDist = point.distance(closest);
		
		// left of rectangle
		Line2D left = new Line2D.Double(topLeft, bottomLeft);
		candidate = intersectSegments(line, left);
		candidateDist = point.distance(candidate);
		if (candidateDist < closestDist) {
			closestDist = candidateDist;
			closest = candidate;
		}
		
		
		// right of rectangle
		Line2D right = new Line2D.Double(topRight, bottomRight);
		candidate = intersectSegments(line, right);
		candidateDist = point.distance(candidate);
		if (candidateDist < closestDist) {
			closestDist = candidateDist;
			closest = candidate;
		}
		
		// bottom of rectangle
		Line2D bottom = new Line2D.Double(bottomLeft, bottomRight);
		candidate = intersectSegments(line, bottom);
		candidateDist = point.distance(candidate);
		if (candidateDist < closestDist) {
			closestDist = candidateDist;
			closest = candidate;
		}

		return closest;
		
	}
	
	/**
	 * @param rect
	 * @param line
	 * @param point
	 * @return the closest corner of rect measured from point
	 */
	public static Point2D closestCornerBox(Rectangle2D rect, Line2D line, Point2D point) {
		Point2D closest, candidate, furthest;
		double closestDist, candidateDist, furthestDist;
		Line2D closestLine, furthestLine;
		
		Point2D topLeft = new Point2D.Double(rect.getX(), rect.getY());
		Point2D topRight = new Point2D.Double(rect.getX()+rect.getWidth(), rect.getY());
		Point2D bottomLeft = new Point2D.Double(rect.getX(), rect.getY()+rect.getHeight());
		Point2D bottomRight = new Point2D.Double(rect.getX()+rect.getWidth(),rect.getY()+rect.getHeight() );
		
		//System.out.println("closestCornerBox: " + rect + " line1: " + line.getP1() + " line2: " + line.getP2()+ " point: " + point);
		// find the closest and furthest points of intersection
		
		// top of rectangle
		Line2D top = new Line2D.Double(topLeft, topRight);
		furthest = closest = GraphicsGems.intersectSegments(line, top);
		furthestDist = closestDist = point.distance(closest);
		closestLine = furthestLine = top;
		
		if (Double.isInfinite(furthestDist)) {
			furthestDist = -1;
		}
		
		//System.out.println("1Closest :" + closest + " furthest: " + furthest);
		
		// left of rectangle
		Line2D left = new Line2D.Double(topLeft, bottomLeft);
		candidate = GraphicsGems.intersectSegments(line, left);
		candidateDist = point.distance(candidate);
		if (!Double.isInfinite(candidateDist)) {
			if (candidateDist < closestDist) {
				closestDist = candidateDist;
				closest = candidate;
				closestLine = left;
			}
			if (candidateDist > furthestDist) {
				furthestDist = candidateDist;
				furthest = candidate;
				furthestLine = left;
			}
		//System.out.println("2Closest :" + closest + " furthest: " + furthest);
		}
		
		
		// right of rectangle
		Line2D right = new Line2D.Double(topRight, bottomRight);
		candidate = GraphicsGems.intersectSegments(line, right);
		candidateDist = point.distance(candidate);
		if (!Double.isInfinite(candidateDist)) {
			if (candidateDist < closestDist) {
				closestDist = candidateDist;
				closest = candidate;
				closestLine = right;
			}
			if (candidateDist > furthestDist) {
				furthestDist = candidateDist;
				furthest = candidate;
				furthestLine = right;
			}
		//System.out.println("3Closest :" + closest + " furthest: " + furthest);
		}
		
		
		// bottom of rectangle
		Line2D bottom = new Line2D.Double(bottomLeft, bottomRight);
		candidate = GraphicsGems.intersectSegments(line, bottom);
		candidateDist = point.distance(candidate);
		if (!Double.isInfinite(candidateDist)) {
			if (candidateDist < closestDist) {
				closestDist = candidateDist;
				closest = candidate;
				closestLine = bottom;
			}
			if (candidateDist > furthestDist) {
				furthestDist = candidateDist;
				furthest = candidate;
				furthestLine = bottom;
			}
		//System.out.println("4Closest :" + closest + " furthest: " + furthest);
		}
		

		// now we have the closest and furthest distances, check to see if the things that were
		// intersected are on opposite sides, or adjacent
		
		// if closest == furthest, it is the corner point
		if (closest == furthest)
			return null;
		assert(!Double.isInfinite(closestDist) && !Double.isInfinite(furthestDist));
		
		// if lines are adjacent
		Point2D adjacentPoint = areLinesAdjacent(closestLine, furthestLine);
		//System.out.println("Are lines adjacent: closestLine " + closestLine.getP1() + "," + closestLine.getP2() + " furthest: " + furthestLine.getP1() + "," + furthestLine.getP2());
		//System.out.println("adjacent? " + (adjacentPoint != null));
		// that adjacent point is the corner point!
		//System.out.println("ClosestPoint: " + closest + " point: " + point);
		if (adjacentPoint != null) {
			//System.out.println("GraphicsGems. closestCorner is a triangle.");
			
			return adjacentPoint;
		
		// we also want to return the corner point if the given point is on the rectangle itself
	    // this is a degenerate case
		} else if (closest.equals(point)){
			//System.out.println("GraphicsGems. closestCorner handling triangle degeneracy.");
			// find those adjacent lines
			boolean foundOne = false;
			Line2D candidate1 = null;
			Line2D candidate2 = null;
			if (isPointOnLine(top, point )) {
				foundOne = true;
				candidate1 = top;
			}
			
			if (isPointOnLine(bottom, point)) {
				if (foundOne) {
					candidate2 = bottom;
				}
				else {
					candidate1 = bottom;
					foundOne = true;
				}					
			}
		
			if (isPointOnLine(right, point)) {
				if (foundOne) {
					candidate2 = right;
				}
				else {
					candidate1 = right;
					foundOne = true;
				}					
			}
			
			if (isPointOnLine(left, point)) {
				if (foundOne) {
					candidate2 = left;
				}
				else {
					candidate1 = left;
					foundOne = true;
				}					
			}
			assert((candidate1 != null) && (candidate2 != null));
			
			// now find the adjacent lines
			
			Point2D onePt = areLinesAdjacent(candidate1, furthestLine);
			Point2D twoPt = areLinesAdjacent(candidate2, furthestLine);
			
			if (onePt == null) {
				assert(twoPt != null);
				return twoPt;
			} else {
				assert(onePt != null);
				return onePt;
			}			
			
			
			
		// if lines are opposite one another, find corner by doing area calculation
		} else {
			
			//System.out.println("GraphicsGems. closestCorner is a trapezoid.");
			
			// 1. compute area of rectangle
			double boxArea = rect.getWidth() * rect.getHeight();
			
			Line2D heightLine;//, otherLine;
			// 2. pick a line that is not closest or further line, call it the heightLine,
			//    (for the height of the trapezoid), call the remaining line the otherLine
			// note we know that the lines have to be opposite each other, so this make it easier
			if ((top != closestLine) && (top != furthestLine) && (bottom != closestLine) && (bottom != furthestLine)) {
				heightLine = top;
//				otherLine = bottom;				
			} else {
				heightLine = left;
//				otherLine = right;
			}
			
			// 3. get adjacent point of closestLine and heightLine, to compute b1
			Point2D b1Point = areLinesAdjacent(closestLine, heightLine);
			assert (b1Point != null);
			double b1 = b1Point.distance(closest);
			
			// 4. get adjacent point of furthestLine and heightLine, to compute b2
			Point2D b2Point = areLinesAdjacent(furthestLine, heightLine);
			assert(b2Point != null);
			double b2 = b2Point.distance(furthest);
			
			// 5. compute height by getting length of heightLine
			double h = heightLine.getP1().distance(heightLine.getP2());
			
			// 6. compute area of trapezoid
			//System.out.println("b1: " + b1 + " b2: " + b2 + " h: " + h);
			double trapezoidArea = .5*(b1+b2)*h;
			//System.out.println( "boxArea :" + boxArea + " trapezoid Area: " + trapezoidArea);
			assert(boxArea > trapezoidArea);
//			double otherArea = boxArea - trapezoidArea;
			
			// return the point on the side of trapezoidArea
			if (trapezoidArea > boxArea) {
				return b1Point;				
			// return the point on the side of boxArea (the point on closestLine that isn't b1Point
			} else {
				if (closestLine.getP1().equals(b1Point))
					return closestLine.getP1();
				else
					return closestLine.getP2();		
			}
		}
		
	}
	
	/**
	 * @param oneLine
	 * @param twoLine
	 * @return the adjacent point of oneLine and twoLine or null if no such point exists
	 */
	public static Point2D areLinesAdjacent(Line2D oneLine, Line2D twoLine) {
		Point2D oneP1 = oneLine.getP1();
		Point2D oneP2 = oneLine.getP2();
		Point2D twoP1 = twoLine.getP1();
		Point2D twoP2 = twoLine.getP2();
		if (oneP1.equals(twoP1) || oneP1.equals(twoP2))
			return oneP1;
		else if (oneP2.equals(twoP1) || oneP2.equals(twoP2)) 
			return oneP2;
		else 
			return null;
		
	}
	
	public static boolean isPointOnLine(Line2D line, Point2D point) {
		return (line.getP1().equals(point) || line.getP2().equals(point));
	}
}
