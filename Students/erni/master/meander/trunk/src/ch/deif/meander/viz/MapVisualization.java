package ch.deif.meander.viz;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PGraphicsJava2D;
import processing.pdf.PGraphicsPDF;
import ch.deif.meander.Map;
import ch.deif.meander.ui.PViewer;


public abstract class MapVisualization implements Drawable {

    public final Map map;
    protected final int pixelScale;
    private Collection<Drawable> children;

    public void addChild(Drawable child) {
        if (children == null) children = new ArrayList<Drawable>();
        children.add(child);
    }
    
    public MapVisualization(Map map) {
        this.map = map;
        this.pixelScale = map.getParameters().width;
    }

    public abstract void draw(PGraphics pg);

    public void openApplet() {
        new PViewer(this);
    }

    public void drawToPDF(String name, Object... args) {
        this.drawToPDF(String.format(name, args));
    }

    public void drawToPNG(String name, Object... args) {
        this.drawToPNG(String.format(name, args));
    }

    public void drawToPDF(String name) {
        PGraphicsPDF pg;
        PApplet pa;
        pg = new PGraphicsPDF();
        pa = new PApplet();
        pa.init();
        pg.setParent(pa);
        pg.setPath(qname(name, "pdf"));
        performDraw(pg);
        pg.dispose();
        pg = null;
    }

    private String qname(String name, String ext) {
        // TODO don't prepend current directory for absolute names.
        return System.getProperty("user.dir") + File.separator + name + "."
                + ext;
    }

    public void performDraw(PGraphics pg) {
        pg.hint(PConstants.ENABLE_NATIVE_FONTS);
        pg.smooth();
        pg.setSize(pixelScale, pixelScale);
        pg.beginDraw();
        if (pg instanceof PGraphicsPDF) pg.textMode(PConstants.SHAPE);
        this.draw(pg);
        if (children != null)
            for (Drawable each: children) {
                each.draw(pg);
            }
        pg.endDraw();
    }

    public void drawToPNG(String name) {
        PGraphics pg = new PGraphicsJava2D();
        // TODO find solution for viz with fonts, they seem to need an enclosing
        // applet!
        // PApplet pa = new PApplet();
        // pa.init();
        // pg.setParent(pa);
        performDraw(pg);
        pg.save(qname(name, "png"));
    }

    public PGraphics getImage() {
        PGraphics img = new PGraphics();
        img.loadPixels();
        img.updatePixels();
        this.performDraw(img);
        img.updatePixels();
        return img;
    }

    public int pixelScale() {
        return pixelScale;
    }

}
