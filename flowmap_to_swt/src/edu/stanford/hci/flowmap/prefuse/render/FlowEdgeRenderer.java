package edu.stanford.hci.flowmap.prefuse.render;

import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.util.Iterator;
import java.util.LinkedList;

import edu.berkeley.guir.prefuse.VisualItem;
import edu.berkeley.guir.prefuse.render.DefaultEdgeRenderer;
import edu.stanford.hci.flowmap.db.QueryRecord;
import edu.stanford.hci.flowmap.main.Options;
import edu.stanford.hci.flowmap.prefuse.item.FlowEdgeItem;
import edu.stanford.hci.flowmap.prefuse.item.FlowNodeItem;

/**
 * The renderer for a FlowEdge. 
 *
 *
 * This software is distributed under the Berkeley Software Distribution License.
 * Please see http://graphics.stanford.edu/~dphan/code/bsd.license.html
 *
 */
public abstract class FlowEdgeRenderer extends DefaultEdgeRenderer {
	protected Options userOptions;
	protected QueryRecord flowRecord;
	protected FlowScale linearScale, logScale, polyScale;
	
	protected FlowNodeItem rootNodeItem;
	
	
	public FlowEdgeRenderer(Options userOptions, QueryRecord flowRecord) {
		super();
		this.userOptions = userOptions;
		this.flowRecord = flowRecord;
				
		linearScale = new FlowScale.Linear(userOptions, flowRecord);
		logScale = new FlowScale.Log(userOptions, flowRecord);
		polyScale = new FlowScale.Poly(userOptions, flowRecord);
	}
	
	public void setRootNodeItem(FlowNodeItem root) {
		this.rootNodeItem = root;
	}
	
	public FlowNodeItem getRootNodeItem() {
		return rootNodeItem;
	}
	
	public void render(Graphics2D g2d, VisualItem item) {
		// save the state of the render
		AffineTransform old = g2d.getTransform();
		Paint oldPaint = g2d.getPaint();
		Stroke stroke =  g2d.getStroke();
		
		FlowScale scale = getUserScaling();
		
		renderHelper(g2d, (FlowEdgeItem) item, scale);
		
		// restore the state of the render
		g2d.setStroke(stroke);
		g2d.setTransform(old);
		g2d.setPaint(oldPaint);
		
	}
	
	/**
	 * @return
	 */
	public FlowScale getUserScaling() {
		FlowScale scale;
		if (userOptions.getBoolean(Options.LINEAR_SCALE))
			scale = linearScale;
		else if (userOptions.getBoolean(Options.LOG_SCALE))
			scale = logScale;
		else
			scale = polyScale;
		return scale;
	}
	
	protected abstract void renderHelper(Graphics2D g, FlowEdgeItem item, FlowScale scale); 
	
	/**
	 * Computes the spline for the given edge item
	 * @param item the edge item
	 * @param scale the object that tells us how to scale the flow down
	 * @return the display width of the flow
	 */
	protected abstract double computeEdge(FlowEdgeItem item, FlowScale scale);
	
	public void initializeRenderTree(FlowNodeItem root) {
		
		FlowScale scale = getUserScaling();
		LinkedList nodeQ = new LinkedList();
		nodeQ.add(root);
		
		while(nodeQ.size() > 0) {
			FlowNodeItem nodeItem = (FlowNodeItem) nodeQ.removeFirst();
			Iterator i = nodeItem.getOutEdges().iterator();
			//System.out.println("initTree.NodeItem: " + nodeItem);
			while (i.hasNext()) {
				FlowEdgeItem edgeItem = (FlowEdgeItem) i.next();
				//System.out.println("initTree.EdgeItem: " + edgeItem);
				
				computeEdge(edgeItem, scale);
				
				nodeQ.add(edgeItem.getSecondNode());
				/*
				if(edgeItem.getSecondNode() instanceof FlowDummyNodeItem){
					System.out.println(((FlowDummyNodeItem)(edgeItem.getSecondNode())).dummyName);
				}*/
				
				
			}
		}
	}
}
