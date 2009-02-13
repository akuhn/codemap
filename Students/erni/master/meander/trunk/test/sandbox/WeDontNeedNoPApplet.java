package sandbox;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PFont;
import processing.core.PGraphics;
import processing.core.PGraphicsJava2D;
import processing.pdf.PGraphicsPDF;

public class WeDontNeedNoPApplet {

    private static final PFont FONT = new PFont(PFont.findFont("Andale Mono"), true, PFont.DEFAULT_CHARSET);

    
    public static void main(String[] args) throws Throwable {
        //for (Object each: PFont.list()) System.out.println(each);
        drawToPNG();
        drawToPDF();
    }

    private static void drawToPDF() throws Throwable {
        PGraphicsPDF pg;
        PApplet pa;
        pg = new PGraphicsPDF();
        pa = new PApplet();
        pa.init();
        pg.setParent(pa);
        pg.setPath(System.getProperty("user.dir")+"/example.pdf");
        setupAndDraw(pg);
        pg.dispose();
        pg = null;
    }

    private static void setupAndDraw(PGraphics pg) {
        pg.hint(PConstants.ENABLE_NATIVE_FONTS);
        pg.setSize(200, 200);
        pg.beginDraw();
        draw(pg);
        pg.endDraw();
    }

    private static void draw(PGraphics pg) {
        pg.textMode(PConstants.SHAPE); // Embed PDF fonts, prints err for Java2D :(
        pg.fill(0xFFFF0000);
        pg.rect(20, 20, 50, 70);
        pg.textFont(FONT, 30);
        pg.text("Hello Worlds!", 30, 30);
    }

    private static void drawToPNG() throws Throwable {
        PGraphics pg = new PGraphicsJava2D();
        PApplet pa = new PApplet();
        pa.init();
        pg.setParent(pa);
        setupAndDraw(pg);
        pg.save(System.getProperty("user.dir")+"/example.png");
    }
    
}
