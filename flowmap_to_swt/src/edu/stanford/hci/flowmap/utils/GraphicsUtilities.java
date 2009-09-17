package edu.stanford.hci.flowmap.utils;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;


/**
 * This software is distributed under the Berkeley Software Distribution License.
 * Please see http://graphics.stanford.edu/~dphan/code/bsd.license.html
 *
 */
public class GraphicsUtilities {

	// directions for the icon
	public static final int DIR_UP = 0;

	public static final int DIR_DOWN = 1;

	public static final int DIR_LEFT = 2;

	public static final int DIR_RIGHT = 3;

	/**
	 * Creates the little chevron icon that is displayed... on next, previous
	 * buttons, etc...
	 * 
	 * @param type
	 * @return
	 */

	private static RenderingHints hintsGood = null;

	private static RenderingHints hintsGreat = null;

	// navigation icons
	public static final ImageIcon ICON_DOWN = createIcon(DIR_DOWN);

	public static final ImageIcon ICON_LEFT = createIcon(DIR_LEFT);

	public static final ImageIcon ICON_RIGHT = createIcon(DIR_RIGHT);

	public static final ImageIcon ICON_UP = createIcon(DIR_UP);

	private static RenderingHints hintsBasic;

	public static ImageIcon createIcon(int type) {
		int width = 14;
		int height = 14;
		BufferedImage bi = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = (Graphics2D) bi.getGraphics();
		Polygon p = new Polygon();

		// // Make the background transparent.
		g2d.setRenderingHints(GraphicsUtilities.getGoodRenderingHints());
		g2d.setColor(new Color(0, 0, 0, 0 /* alpha */));
		g2d.fillRect(0, 0, width, height);

		g2d.setColor(new Color(0f, 0f, 0f, 0.8f /* alpha */));

		switch (type)

		{
		case DIR_UP:
			p.addPoint(0, height);
			p.addPoint(width / 2, 0);
			p.addPoint(width, height);
			break;
		case DIR_DOWN:
			p.addPoint(0, 0);
			p.addPoint(width, 0);
			p.addPoint(width / 2, height);
			break;
		case DIR_LEFT:
			p.addPoint(width, 0);
			p.addPoint(width, height);
			p.addPoint(0, height / 2);
			break;
		case DIR_RIGHT: // fall through to default
		default:
			p.addPoint(0, 0);
			p.addPoint(width, height / 2);
			p.addPoint(0, height);
			break;
		}
		g2d.fillPolygon(p);
		return new ImageIcon(bi);
	}

	/**
	 * The distance between two points.
	 * 
	 * @param p1
	 * @param p2
	 * @return
	 */
	public static double getDistance(Point2D p1, Point2D p2) {
		double p1x = p1.getX();
		double p1y = p1.getY();
		double p2x = p2.getX();
		double p2y = p2.getY();
		double diffX = p1x - p2x;
		double diffY = p1y - p2y;
		return Math.sqrt((diffX * diffX) + (diffY * diffY));
	}

	public static RenderingHints getBasicRenderingHints() {
		if (hintsBasic == null) {
			hintsBasic = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_OFF);
			hintsBasic.add(new RenderingHints(RenderingHints.KEY_RENDERING,
					RenderingHints.VALUE_RENDER_QUALITY));
		}
		return hintsBasic;
	}

	/**
	 * @return
	 */
	public static RenderingHints getGoodRenderingHints() {
		if (hintsGood == null) {
			hintsGood = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
			hintsGood.add(new RenderingHints(RenderingHints.KEY_RENDERING,
					RenderingHints.VALUE_RENDER_QUALITY));
		}
		return hintsGood;
	}

	/**
	 * @return
	 */
	public static RenderingHints getGreatRenderingHints() {
		if (hintsGreat == null) {
			hintsGreat = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
			hintsGreat.add(new RenderingHints(RenderingHints.KEY_RENDERING,
					RenderingHints.VALUE_RENDER_QUALITY));
			hintsGreat.add(new RenderingHints(RenderingHints.KEY_INTERPOLATION,
					RenderingHints.VALUE_INTERPOLATION_BILINEAR));

		}
		return hintsGreat;
	}

	/**
	 * @param images
	 * @param hostComponent
	 */
	public static void loadAllImages(Image[] images, Component hostComponent) {
		MediaTracker mediaTracker = new MediaTracker(hostComponent);
		for (int i = 0; i < images.length; i++) {
			mediaTracker.addImage(images[i], i);
		}

		try {
			mediaTracker.waitForAll();
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		}
	}

	/**
	 * @param image
	 * @param hostComponent
	 */
	public static void loadImage(Image image, Component hostComponent) {
		MediaTracker mediaTracker = new MediaTracker(hostComponent);
		mediaTracker.addImage(image, 0);
		try {
			mediaTracker.waitForID(0);
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		}
	}
}
