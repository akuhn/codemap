package edu.stanford.hci.flowmap.main;


import java.awt.Color;
import java.awt.Dimension;

import edu.stanford.hci.flowmap.utils.GoodColorChooser;

/**
 * This software is distributed under the Berkeley Software Distribution License.
 * Please see http://graphics.stanford.edu/~dphan/code/bsd.license.html
 *
 */
public class Globals {
	
	public static enum ProgramType {INTERACTIVE, FOOTPRINT, EXPLORE};
	public static ProgramType currentType = ProgramType.INTERACTIVE;
	
	private static final Dimension footprintDimension = new Dimension(790, 396);
	//private static final Dimension interactiveDimension = new Dimension(1400, 1050);
	//private static final Dimension interactiveDimension = new Dimension(2000, 2000);
	private static final Dimension interactiveDimension = new Dimension(1024, 768);
	private static final Dimension exploreDimension = new Dimension(1280, 1024);
	
	public static boolean showDummyNodeLabels = false;
	public static boolean showDummyNodeCircles = true;
	public static boolean showDestinationLabels = true;
	
	
	public static boolean runNodeEdgeRouting = true;
	public static boolean useLayoutAdjustment = false;
	
	public static Color currentColor = GoodColorChooser.rose;
	
	public static Dimension getScreenDimension() {
		switch(Globals.currentType) {
			case FOOTPRINT:
				return footprintDimension;
			case INTERACTIVE:
				return interactiveDimension;
			case EXPLORE:
				return exploreDimension;
		}
		return interactiveDimension;
	}
}
