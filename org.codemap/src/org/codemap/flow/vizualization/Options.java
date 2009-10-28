package org.codemap.flow.vizualization;

import edu.stanford.hci.flowmap.utils.AttributeMap;

/**
 * Stores all the things users can set for the gui
 * 
 * This software is distributed under the Berkeley Software Distribution License.
 * Please see http://graphics.stanford.edu/~dphan/code/bsd.license.html
 *
 */
public class Options extends AttributeMap {
	
	public static final String MIN_DISPLAY_WIDTH = "Min. Display Width";
	public static final String MAX_DISPLAY_WIDTH = "Max. Display Width";
	public static final String LINEAR_SCALE = "Linear Scale";
	public static final String LOG_SCALE = "Log Scale";
	public static final String POLY_SCALE = "Polynomial Scale";
	
	public static final String CURRENT_FLOW_TYPE = "CurrentFlowType";
	
	public Options() {
		super();
	}
	
	public Options(Options copy) {
		this();
		for(String s : copy.keys()) {
			Object o = copy.get(s);
			if (o instanceof Double) {
				Double d = (Double)o;
				m_map.put(s, new Double(d.doubleValue()));
			} else if (o instanceof Boolean) {
				Boolean b = (Boolean)o;
				m_map.put(s, new Boolean(b.booleanValue()));
			} else {
				throw new RuntimeException();
			}
		}
	}

}
