package edu.stanford.hci.flowmap.prefuse.render;

import org.codemap.flow.vizualization.Options;

import edu.stanford.hci.flowmap.structure.Graph;

/**
 * Abstract class that scales a flow from its real width to a width suitable for display.
 * 
 *
 * This software is distributed under the Berkeley Software Distribution License.
 * Please see http://graphics.stanford.edu/~dphan/code/bsd.license.html
 *
 */
public abstract class FlowScale {
	
	protected double displayMin, displayMax;
    protected Graph graph;
	
	public FlowScale(Options userOptions, Graph g) {
		graph = g;
		displayMin = userOptions.getDouble(Options.MIN_DISPLAY_WIDTH);
		displayMax = userOptions.getDouble(Options.MAX_DISPLAY_WIDTH);
	}
		
	public double getDisplayWidth(double flowCurr) {
		//System.out.println("flowcurr: " + flowCurr + " and " + columnName);
		//System.out.println("FlowScale is: dispMin:" + displayMin + " dispMax:" + displayMax + " flowMin:" + flowMin + " flowMax:" + flowMax);
		//System.out.println("Flow Min: " + flowMin + " Flow Max: " + flowMax);
		
		double width = displayMin + (f(flowCurr) * (displayMax - displayMin));
		//System.out.println("1flowCurr is " + flowCurr + " to " + width);
		
		if ((width < 0) || Double.isNaN(width) || Double.isInfinite(width))
			width = displayMin;
		//System.out.println("2flowCurr is " + flowCurr + " to " + width);
		return width;
	}
	
	protected abstract double f(double flowCurr);
	
	public static class Linear extends FlowScale {

		public Linear(Options userOptions, Graph graph) {
			super(userOptions, graph);
		}
				
		protected double f(double flowCurr) {
			return flowCurr/graph.getTotalWeightValue();
		}
	}
	
	public static class Log extends FlowScale {

		public Log(Options userOptions, Graph graph) {
			super(userOptions, graph);
		}

		protected double f(double flowCurr) {
			double flowMin, flowMax;
			flowMin = graph.getMinWeightValue();
			flowMax = graph.getTotalWeightValue();
			return Math.log(flowCurr/flowMin) / Math.log(flowMax/flowMin);
		}		
	}
	
	public static class Poly extends FlowScale {
		
		public Poly(Options userOptions, Graph graph) {
			super(userOptions, graph);
		}
		
		protected double f(double flowCurr) {
			double flowMin, flowMax;
			flowMin = graph.getMinWeightValue();
			flowMax = graph.getTotalWeightValue();
			
			double flowMagnitude = flowMax/flowMin;
			double displayMagnitude = displayMax/displayMin;
			double exponent = Math.log(displayMagnitude) / Math.log(flowMagnitude);
			if (exponent > 1)
				exponent = 1;
			return Math.pow(flowCurr/flowMax, exponent);
		}
	}
}
