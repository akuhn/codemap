package ch.deif.meander.visual;

import java.applet.Applet;
import java.io.File;

import javax.swing.JFrame;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.pdf.PGraphicsPDF;
import ch.deif.meander.MapInstance;
import ch.deif.meander.ui.MeanderApplet;

public class MapVisualization {

	private MapInstance map;
	private Layer visual;
	private boolean redraw = true;

	public MapVisualization(MapInstance map, Layer visual) {
		assert map != null;
		assert visual != null;
		this.map = map;
		this.visual = visual;
	}
	
	public void redraw() {
		redraw = true;
	}

	public void openApplet() {
		MeanderApplet applet = new MeanderApplet();
		applet.init();
		applet.setVisualization(this);
		JFrame win = new JFrame("Map Viewer");
		win.setResizable(false);
		win.getContentPane().add(applet);
		win.pack();
		win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		win.setVisible(true);
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
		pg.setPath(prependCurrentDirectory(name, "pdf"));
		draw(pg, null);
		pg.dispose();
		pg = null;
	}

	public void draw(PGraphics pg, MeanderApplet pa) {
		assert map.width == pg.width : "Width mismatch (map vs P5): " + map.width + " vs " + pg.width;
		assert map.height == pg.height;
		if (!redraw && pa != null && pa.mouseEvent == null) return;
		redraw  = false; 
		visual.draw(map, pg, pa);
		pa.mouseEvent = null; // Don't remove this line, we depend in this!
	}

	public final void drawToPGraphics(PGraphics pg) {
		pg.hint(PConstants.ENABLE_NATIVE_FONTS);
		pg.smooth();
		pg.setSize(map.width, map.height);
		pg.beginDraw();
		if (pg instanceof PGraphicsPDF)
			pg.textMode(PConstants.SHAPE);
		this.draw(pg, null);
		pg.endDraw();
	}

	public final void drawToPNG(String name) {
		// TODO find solution for viz with fonts, they seem to need an enclosing
		// applet!
		PApplet pa = new PApplet();
		pa.init();
		PGraphics pg = pa.createGraphics(map.width, map.height,
				PConstants.JAVA2D);
		pg.beginDraw();
		draw(pg, null);
		pg.endDraw();
		pg.save(prependCurrentDirectory(name, "png"));
		pa.destroy();
	}

	private String prependCurrentDirectory(String name, String ext) {
		// TODO don't prepend current directory for absolute names.
		return System.getProperty("user.dir") + File.separator + name + "."
				+ ext;
	}

	public final PGraphics drawImage() {
		PGraphics img = new PGraphics();
		img.loadPixels();
		img.updatePixels();
		this.draw(img, null);
		img.updatePixels();
		return img;
	}

	public static Applet makeApplet(MapInstance map2, MapVisualization visual) {
		// TODO Auto-generated method stub
		return null;
	}

	public MapInstance getMap() {
		return map;
	}

	public int getWidth() {
		return map.width;	
	}
	
	public int getHeight() {
		return map.height;
	}
	
}