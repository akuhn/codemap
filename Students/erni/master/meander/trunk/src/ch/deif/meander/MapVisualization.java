package ch.deif.meander;

import java.io.File;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PGraphicsJava2D;
import processing.pdf.PGraphicsPDF;

public abstract class MapVisualization {
    
    protected final Map map;

    public MapVisualization(Map map) {
        this.map = map;
    }
    
    public abstract void draw(PGraphics pg);
    
    public void openApplet() {
        new PViewer(this);
    }
    
    public void drawToPDF(String name) {
        PGraphicsPDF pg;
        PApplet pa;
        pg = new PGraphicsPDF();
        pa = new PApplet();
        pa.init();
        pg.setParent(pa);
        pg.setPath(qname(name, "pdf"));
        setupAndDraw(pg);
        pg.dispose();
        pg = null;
    }

    public static String qname(String name, String ext) {
        return System.getProperty("user.dir")+File.pathSeparator+name+"."+ext;
    }

    private void setupAndDraw(PGraphics pg) {
        pg.hint(PConstants.ENABLE_NATIVE_FONTS);
        pg.setSize(200, 200);
        pg.beginDraw();
        if (pg instanceof PGraphicsPDF) pg.textMode(PConstants.SHAPE);
        draw(pg);
        pg.endDraw();
    }

    public void drawToPNG(String name) {
        PGraphics pg = new PGraphicsJava2D();
        PApplet pa = new PApplet();
        pa.init();
        pg.setParent(pa);
        setupAndDraw(pg);
        pg.save(qname(name,"png"));
    }

    
}
