package ch.deif.meander.viz;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.List;

import processing.core.PGraphics;
import ch.akuhn.util.Throw;
import ch.deif.aNewMeander.visual.WaterVisualization;
import ch.deif.meander.Map;
import ch.deif.meander.ui.MeanderApplet.Events;

public class Layers extends MapVisualization {

	private Drawable background;
	private Composite<MapVisualization> layers = Composite.newInstance();

	public Layers() {
		useSketchBackground();
	}

	public void useSketchBackground() {
		background = new SketchVisualization(getMap());
	}

	public Layers add(MapVisualization overlay) {
		layers.add(overlay);
		return this;
	}

	@Override
	public void draw(PGraphics pg) {
		assert pg != null;
		background.draw(pg);
		for (Drawable each: layers) each.draw(pg);
	}

	public Layers useHillshading() {
		background = Composite.of(
				new WaterVisualization(getMap()),
				new ShoreVizualization(getMap()),
				new HillshadeVisualization(getMap()),
				new LabelsOverlay(getMap()));
		return this;
	}

	public Layers add(Class<? extends MapVisualization> overlay) {
		try {
			add(overlay.getConstructor(Map.class).newInstance(getMap()));
			return this;
		} catch (Exception ex) {
			throw Throw.exception(ex);
		}
	}
	
	public MapVisualization get(Class<? extends MapVisualization> overlay) {
		for (MapVisualization each: layers) {
			if (overlay.isAssignableFrom(each.getClass())) {
				return each;
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public void alsoShowShoresOf(float[][] DEM) {
		if (!(background instanceof Composite)) return;
		for (Drawable each: (Composite<Drawable>) background) {
			if (each instanceof ShoreVizualization) {
				((ShoreVizualization) each).alsoShowShoresOf(DEM);
			}
		}
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		for (MapVisualization each: layers) {
			each.mouseClicked(e);
		}
	}
	
	@Override
	public void addSelection(List<String> handleIdentifiers) {
		for (MapVisualization each: layers) {
			each.addSelection(handleIdentifiers);
		}
	}
	
	@Override
	public void indicesSelected(int[] indices) {
		for (MapVisualization each: layers) {
			each.indicesSelected(indices);
		}		
	}
	
	@Override
	public void updateSelection(List<String> handleIdentifiers) {
		for (MapVisualization each: layers) {
			each.updateSelection(handleIdentifiers);
		}				
	}
	
	@Override
	public void mouseDragStarted(Point dragStart) {
		for (MapVisualization each: layers) {
			each.mouseDragStarted(dragStart);
		}			
	}
	
	@Override
	public void mouseDraggedTo(Point dragStop) {
		for (MapVisualization each: layers) {
			each.mouseDraggedTo(dragStop);
		}		
	}
	
	@Override
	public void mouseDragStopped() {
		for (MapVisualization each: layers) {
			each.mouseDragStopped();
		}			
	}
	
	@Override
	public void registerEventHandler(Events events) {
		for (MapVisualization each: layers) {
			each.registerEventHandler(events);
		}		
	}
}
