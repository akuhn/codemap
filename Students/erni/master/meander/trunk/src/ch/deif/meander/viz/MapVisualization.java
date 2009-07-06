package ch.deif.meander.viz;

import java.applet.Applet;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.List;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.pdf.PGraphicsPDF;
import ch.deif.aNewMeander.MapConfigurationWithSize;
import ch.deif.meander.ui.PViewer;
import ch.deif.meander.ui.MeanderApplet.Events;

public abstract class MapVisualization implements Drawable {

	private Events events;

	public MapVisualization() {

	}

	public void openApplet() {
		new PViewer(this);
	}

	public final void drawToPDF(String name, Object... args) {
		this.drawToPDF(String.format(name, args));
	}

	public final void drawToPNG(String name, Object... args) {
		this.drawToPNG(String.format(name, args));
	}

	public final void drawToPDF(String name) {
		PGraphicsPDF pg;
		PApplet pa;
		pg = new PGraphicsPDF();
		pa = new PApplet();
		pa.init();
		pg.setParent(pa);
		pg.setPath(qname(name, "pdf"));
		draw(pg);
		pg.dispose();
		pg = null;
	}

	private String qname(String name, String ext) {
		// TODO don't prepend current directory for absolute names.
		return System.getProperty("user.dir") + File.separator + name + "." + ext;
	}

	public abstract void draw(PGraphics pg);

	public final void drawToPGraphics(PGraphics pg) {
		pg.hint(PConstants.ENABLE_NATIVE_FONTS);
		pg.smooth();
		pg.setSize(getWidth(), getWidth());
		pg.beginDraw();
		if (pg instanceof PGraphicsPDF) pg.textMode(PConstants.SHAPE);
		this.draw(pg);
		pg.endDraw();
	}

	public final void drawToPNG(String name) {
		// TODO find solution for viz with fonts, they seem to need an enclosing applet!
		PApplet pa = new PApplet();
		pa.init();
		PGraphics pg = pa.createGraphics(this.getWidth(), this.getWidth(), PConstants.JAVA2D);
		pg.beginDraw();
		draw(pg);
		pg.endDraw();
		pg.save(qname(name, "png"));
		pa.destroy();
	}

	public final PGraphics drawImage() {
		PGraphics img = new PGraphics();
		img.loadPixels();
		img.updatePixels();
		this.draw(img);
		img.updatePixels();
		return img;
	}
	
	
	public void mouseClicked(MouseEvent e) {}

	public void addSelection(List<String> handleIdentifiers) {}

	public void indicesSelected(int[] indices) {}

	public void updateSelection(List<String> handleIdentifiers) {}

	public void mouseDragStarted(Point dragStart) {}

	public void mouseDraggedTo(Point dragStop) {}

	public void mouseDragStopped() {}

	public void registerEventHandler(Events events) {
		this.events = events;
	}
	
	public Events events() {
		return events;
	}

	public static Applet makeApplet(MapConfigurationWithSize map2, MapVisualization visual) {
		// TODO Auto-generated method stub
		return null;
	}

}
