package edu.stanford.hci.flowmap.utils;

import java.awt.BasicStroke;


/**
 * This software is distributed under the Berkeley Software Distribution License.
 * Please see http://graphics.stanford.edu/~dphan/code/bsd.license.html
 *
 */
public class StrokeUtilities {

	private static final HashMapFloat strokes = new HashMapFloat();
	
	private static final HashMapFloat strokesButt = new HashMapFloat();
	
	/**
	 * @param strokeWidth
	 * @return
	 */
	public static BasicStroke retrieveStroke(float strokeWidth) {
		BasicStroke bs = (BasicStroke)strokes.get(strokeWidth);
		if (bs == null) {
			bs = new BasicStroke(strokeWidth, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_ROUND);
			strokes.put(strokeWidth, bs);
		}
		return bs;
	}
	

	public static BasicStroke retrieveStrokeButt(float strokeWidth) {
		BasicStroke bs = (BasicStroke)strokesButt.get(strokeWidth);
		if (bs == null) {
			bs = new BasicStroke(strokeWidth, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND);
			strokesButt.put(strokeWidth, bs);
		}
		return bs;
	}

	
	/**
	 * @param args
	 */
	public static void main(String[] args) {

	}

}
