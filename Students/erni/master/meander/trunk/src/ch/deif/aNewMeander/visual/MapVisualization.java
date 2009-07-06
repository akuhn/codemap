package ch.deif.aNewMeander.visual;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.pdf.PGraphicsPDF;
import ch.deif.aNewMeander.MapConfigurationWithSize;

public class MapVisualization {

	private MapConfigurationWithSize map;
	private Layer visual;

	public MapVisualization(MapConfigurationWithSize map, Layer visual) {
		this.map = map;
		this.visual = visual;
	}

	public void openApplet() {
		new PViewer();
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
		draw(pg);
		pg.dispose();
		pg = null;
	}

	public void draw(PGraphics pg) {
		visual.draw(map, pg);
	}

	public final void drawToPGraphics(PGraphics pg) {
		pg.hint(PConstants.ENABLE_NATIVE_FONTS);
		pg.smooth();
		pg.setSize(map.width, map.height);
		pg.beginDraw();
		if (pg instanceof PGraphicsPDF)
			pg.textMode(PConstants.SHAPE);
		this.draw(pg);
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
		draw(pg);
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
		this.draw(img);
		img.updatePixels();
		return img;
	}

	public static Applet makeApplet(MapConfigurationWithSize map2,
			MapVisualization visual) {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("serial")
	private class PViewer extends JFrame {
		public PViewer() {
			super("Map Viewer");
			setLayout(new BorderLayout());
			PApplet pa = new PApplet() {
				@Override
				public void setup() {
					size(map.width, map.height);
					frameRate(1);
				}
				@Override
				public void draw() {
					visual.draw(map, g);
				}
			};
			getContentPane().add(pa, BorderLayout.CENTER);
			pa.init();
			pack();
			setVisible(true);
			setSize(map.width, map.height);
			setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		}
	}

}