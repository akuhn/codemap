package ch.deif.meander.ui;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import processing.core.PApplet;
import ch.deif.meander.viz.MapVisualization;


@SuppressWarnings("serial")
public class PViewer extends JFrame {
	
	public PViewer(MapVisualization<?> viz) {
		super("Map Viewer");
		
		setLayout(new BorderLayout());
		PApplet pa = new InnerApplet(viz);
		getContentPane().add(pa, BorderLayout.CENTER);
		pa.init();
		pack();
		setVisible(true);
		setSize(viz.pixelScale(), viz.pixelScale());
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}
	
	private class InnerApplet extends PApplet {
		
		private MapVisualization<?> viz;
		
		public InnerApplet(MapVisualization<?> viz) {
			this.viz = viz;
		}
		
		@Override
		public void setup() {
			size(viz.pixelScale(), viz.pixelScale());
			frameRate(1);
		}
		
		@Override
		public void draw() {
			viz.drawToPGraphics(g);
		}
		
	}
	
}
