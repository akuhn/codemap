package edu.berkeley.guir.prefuse.samples;

import java.awt.Color;
import java.awt.Paint;

import javax.swing.JFrame;

import edu.berkeley.guir.prefuse.Display;
import edu.berkeley.guir.prefuse.ItemRegistry;
import edu.berkeley.guir.prefuse.NodeItem;
import edu.berkeley.guir.prefuse.VisualItem;
import edu.berkeley.guir.prefuse.action.RepaintAction;
import edu.berkeley.guir.prefuse.action.assignment.ColorFunction;
import edu.berkeley.guir.prefuse.action.filter.GraphFilter;
import edu.berkeley.guir.prefuse.activity.ActionList;
import edu.berkeley.guir.prefuse.graph.Graph;
import edu.berkeley.guir.prefuse.graph.GraphLib;
import edu.berkeley.guir.prefuse.render.DefaultEdgeRenderer;
import edu.berkeley.guir.prefuse.render.DefaultRendererFactory;
import edu.berkeley.guir.prefuse.render.ShapeRenderer;
import edu.berkeley.guir.prefuse.render.TextItemRenderer;
import edu.berkeley.guir.prefusex.controls.DragControl;
import edu.berkeley.guir.prefusex.layout.ForceDirectedLayout;

/**
 * Visualizes an animated graph. Nodes are drawn with text labels,
 *  and a force directed layout is used to determine node positions.
 */
public class AnimateGraph extends JFrame{
	
	private ItemRegistry registry;
	private ActionList filter, forces;
	
	public AnimateGraph() {
		super("AnimateGraph");
		
		// creates a new graph
		Graph g = GraphLib.getStar(10);
		
		// create a new item registry
		//  the item registry stores all the visual
		//  representations of different graph elements
		registry = new ItemRegistry(g);
		
		// set up a renderer such that nodes show text labels
		TextItemRenderer nodeRenderer = new TextItemRenderer();
		ShapeRenderer    edgeRenderer = new DefaultEdgeRenderer();
		//nodeRenderer.setRenderType(ShapeRenderer.RENDER_TYPE_FILL);
        
		// create a new renderer factory and associate it 
		// with the item registry
		registry.setRendererFactory(new DefaultRendererFactory(
				nodeRenderer,
				edgeRenderer,
				null));
		
		// create a new display component to show the data
		Display display = new Display(registry);
		display.setSize(400,400);
        
		// let users drag nodes around on screen
        //  disable repainting by the DragControl because the force
        //  simulator will repaint for us.
        boolean repaint = false;
		display.addControlListener(new DragControl(repaint));
		
		// create a new action list that
		// (a) filters visual representations from the original graph
		filter = new ActionList(registry);
		filter.add(new GraphFilter());
        filter.add(new MyColorFunction());
		
		// create a new action list that 
		// (a) uses a force model to continually layout nodes
		// (b) sets the node colors
		// (c) repaints the display
		
		// run this action list for an infinite duration (duration value
		// equals -1), re-executing the action list every 15 milliseconds
		forces = new ActionList(registry,-1,15);
		forces.add(new ForceDirectedLayout(true));
		forces.add(new RepaintAction());
		
		// set up this JFrame
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		getContentPane().add(display);
		pack();
		setVisible(true);
		
		// now execute the action lists to visualize the graph
        filter.runNow(); // filter runs once
        forces.runNow(); // forces re-runs every 15ms, indefinitely
	}
	
	/**
	 * Runs this application
	 */
	public static void main(String[] args) {
		new AnimateGraph();
	}
	
	/**
	 * A custom color function that fills nodes with a
	 * light blue color, and returns black for all other cases.
	 */
	public class MyColorFunction extends ColorFunction {
		private Color nodeColor = new Color(200,200,255);
		public Paint getColor(VisualItem item) {
			return Color.BLACK;
		}
		public Paint getFillColor(VisualItem item) {
			if ( item instanceof NodeItem ) {
				return nodeColor;
			} else {
				return Color.BLACK;
			}
		}
	}
	
}
