package edu.stanford.hci.flowmap.layout;

import java.awt.Dimension;
import java.awt.geom.Point2D;

/**
 * Encapsulates several different kinds of map projections
 *
 * This software is distributed under the Berkeley Software Distribution License.
 * Please see http://graphics.stanford.edu/~dphan/code/bsd.license.html
 *
 */
public class MapProjection {
	/** 
	 * http://mathworld.wolfram.com/MercatorProjection.html
	 * It's important that this returns a new Point (see NodeLayout)
	 * @param latitude
	 * @param longitude
	 * @return x,y values of this latitude and longitude
	 */
	public static Point2D mercatorProjection(double latitude, double longitude) {
		//System.out.println("lat: " + latitude + " lon:" + longitude);
		double yVal = Math.log(Math.tan(Math.PI*latitude/180) + (1/Math.cos(Math.PI*latitude/180)));
		return new Point2D.Double(longitude, yVal);
	}
	
	/**
	 * The world is -180 to 180 in x
	 * and ln(tan(84)+sec(84)) = 2.95
	 * So, this function needs to map the range
	 * (-180,180) for x and (-2.95, 2.95) for y to a different range, like 1024x768.
	 * 
	 * @param pt a pt with the results of the mercatorProjection function
	 * @param screenDim the dimensions of the display component
	 * */
	public static void oldFootprintMercatorToScreen(Point2D pt, Dimension screenDim) {
		double mercX, mercY;
		//System.out.println("mToScreen got " + pt);
		mercX = pt.getX();
		mercY = pt.getY();
		mercX += 180;
		mercX /= 360; // find out the fraction of its width
		mercX *= screenDim.width; // multiply by the screen size
		//mercX *= 1000; // multiply by the screen size
		
		
		mercY = 2.948 - mercY;
		mercY /= 5.897; // find out the fraction of its height
		mercY *= screenDim.height; // multiply by the screen size
		//mercY *= 1000; // multiply by the screen size
		
		pt.setLocation(mercX, mercY);
		//System.out.println("mToScreen produced " + pt);
	}
	
	/**
	 * Modifies pt in place to scale it to the screen size
	 * @param pt has latitude in x and longitude in y
	 * @param screenDim the dimensions of the screen
	 */
	public static void latLongToScreen(Point2D pt, Dimension screenDim) {
		double latScale = screenDim.getHeight() / 180;
		double lonScale = screenDim.getWidth() / 360;
		double lat = 180 + pt.getY();
		double lon = 90 - pt.getX();
		//System.out.println("Got pt: " + pt + " LonScale: " + lonScale + " latScale: " + latScale);
		pt.setLocation(lat*lonScale, lon*latScale);
	}
	
	/**
	 * In general, most of the US (except for Alaska) is below 60 N and above 0.
	 * South America and Australia don't extend below 60S. America also doesn't
	 * extend past 60W
	 * ln(tan(60)+sec(60)) = 1.3 or so. Right now, we are concentrating on 
	 * places in America. So, this function needs to map the range
	 * (-180,-60) for x and (0, 1.3) for y to a different range, like 1024x768.
	 * 
	 * @param pt a pt with the results of the mercatorProjection function
	 * @param screenDim the dimensions of the display component
	 * */
	public static void mercatorToScreen(Point2D pt, Dimension screenDim) {
		double mercX, mercY;
		//System.out.println("mToScreen got " + pt);
		mercX = pt.getX();
		mercY = pt.getY();
		mercX += 180;
		mercX /= 120; // find out the fraction of its width
		mercX *= screenDim.width; // multiply by the screen size
		
		mercY = 1.3 - mercY;
		mercY /= 1.3; // find out the fraction of its height
		mercY *= screenDim.height; // multiply by the screen size
		
		pt.setLocation(mercX, mercY);
		//System.out.println("mToScreen produced " + pt);
	}
}
