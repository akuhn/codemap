package ch.deif.meander.viz;

import java.io.File;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.pdf.PGraphicsPDF;
import ch.deif.meander.Map;
import ch.deif.meander.ui.PViewer;

public abstract class MapVisualization implements Drawable {

	public final Map map;

	public MapVisualization(Map map) {
		this.map = map;
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

	public int getWidth() {
		return map.getWidth();
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

}
