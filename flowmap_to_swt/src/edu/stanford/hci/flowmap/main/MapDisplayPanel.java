package edu.stanford.hci.flowmap.main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import edu.berkeley.guir.prefuse.Display;
import edu.berkeley.guir.prefuse.ItemRegistry;
import edu.berkeley.guir.prefuse.action.RepaintAction;
import edu.berkeley.guir.prefuse.activity.ActionList;
import edu.berkeley.guir.prefuse.render.DefaultRendererFactory;
import edu.stanford.hci.flowmap.db.QueryRecord;
import edu.stanford.hci.flowmap.prefuse.action.FlowMapLayoutAction;
import edu.stanford.hci.flowmap.prefuse.item.FlowDummyNodeItem;
import edu.stanford.hci.flowmap.prefuse.item.FlowEdgeItem;
import edu.stanford.hci.flowmap.prefuse.item.FlowRealNodeItem;
import edu.stanford.hci.flowmap.prefuse.render.FlowClusterRenderer;
import edu.stanford.hci.flowmap.prefuse.render.FlowNodeRenderer;
import edu.stanford.hci.flowmap.prefuse.render.SimpleFlowEdgeRenderer;
import edu.stanford.hci.flowmap.prefuse.structure.FlowDummyNode;
import edu.stanford.hci.flowmap.prefuse.structure.FlowEdge;
import edu.stanford.hci.flowmap.prefuse.structure.FlowMapStructure;
import edu.stanford.hci.flowmap.prefuse.structure.FlowNode;
import edu.stanford.hci.flowmap.structure.Graph;


/**
 * This software is distributed under the Berkeley Software Distribution License.
 * Please see http://graphics.stanford.edu/~dphan/code/bsd.license.html
 *
 */
public class MapDisplayPanel extends JPanel {

	// Have a rendered image object serving as an alternate buffer
	private BufferedImage bufferImage;

	private ItemRegistry m_registry;

	/**************************************************************************
	 * Class Definition
	 ***************************************************************************/
	Display m_prefuseDisplay = null;
	
	FlowNodeRenderer m_nodeRenderer;
	SimpleFlowEdgeRenderer m_edgeRenderer;

	public MapDisplayPanel(Dimension screenSize) {
		super();
		
		
		this.setPreferredSize(screenSize);
		this.setBackground(Color.white);

		bufferImage = new BufferedImage(screenSize.width, screenSize.height,
				BufferedImage.TYPE_INT_RGB);
		
		//creating the registry
		m_registry = new ItemRegistry(new FlowMapStructure());
		m_registry.addItemClass(FlowNode.class.getName(), FlowRealNodeItem.class);
		m_registry.addItemClass(FlowDummyNode.class.getName(), FlowDummyNodeItem.class);
		m_registry.addItemClass(FlowEdge.class.getName(), FlowEdgeItem.class);		
	}

	public void updateDisplay(Graph originalGraph, Options userOptions, QueryRecord flowRecord) {
		

		m_registry.clear();
		m_nodeRenderer = new FlowNodeRenderer();
		m_edgeRenderer = new SimpleFlowEdgeRenderer(userOptions, flowRecord);
		
		/***********************************************************************
		 * Initialize the display
		 ***********************************************************************/		
		FlowClusterRenderer clusterRenderer = new FlowClusterRenderer();
		
		
		m_registry.setRendererFactory(new DefaultRendererFactory(m_nodeRenderer,
				m_edgeRenderer, clusterRenderer));
		
		// create a new display component to show the data
		m_prefuseDisplay = new Display(m_registry);
		m_prefuseDisplay.setSize(this.getSize());
		m_prefuseDisplay.setBackground(Color.WHITE);
		

		
		/***********************************************************************
		 * Create the actionlists that will realize the renderable flowmap
		 * 
		 * Following the steps done in main
		 ***********************************************************************/
		ActionList initFlowMap = new ActionList(m_registry);
		
		FlowMapLayoutAction layoutAction = new FlowMapLayoutAction(
				userOptions, originalGraph.getRootNode(), originalGraph.getAllNodes(),
				m_edgeRenderer) ;
		initFlowMap.add(layoutAction);
		
		initFlowMap.runNow();

		/***********************************************************************
		 * Initialize the rest of the controllers
		 ***********************************************************************/
		ActionList repaint = new ActionList(m_registry, -1);
		repaint.add(new RepaintAction());
		repaint.runNow();

		
		this.removeAll();
		this.add(m_prefuseDisplay);
		this.repaint();
		
		//System.out.println("DisplayPanel done");
	}

	
	public Display getPrefuseDisplay() {
		return m_prefuseDisplay;
	}
	
	public Dimension getMinimumSize() {
		return getPreferredSize();
	}

	public void updateScreenSize() {
		Dimension size = this.getSize();
		if ((size.height == 0) && (size.width == 0))
			return;
		
		//System.out.println("MapDisplayPanel: resizing to" + this.getSize());
		
		if (m_prefuseDisplay != null)
			m_prefuseDisplay.setSize(this.getSize());	
	}

}