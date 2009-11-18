package ch.deif.meander.main;

import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import ch.deif.meander.Configuration;
import ch.deif.meander.Labeling;
import ch.deif.meander.MapInstance;
import ch.deif.meander.Point;
import ch.deif.meander.internal.DEMAlgorithm;
import ch.deif.meander.internal.ShadeAlgorithm;
import ch.deif.meander.map.ComputeBackgroundTask.FastBackgroundRenderer;
import ch.deif.meander.swt.Label;
import ch.deif.meander.swt.LabelOverlay;
import ch.deif.meander.util.CodemapColors;
import ch.deif.meander.util.MColor;
import ch.deif.meander.util.MapScheme;

public class QuickNDirtyMap {
    
    static final int mapSize = 1000;
    private static Image background;
    private static Labeling labeling;

    public static void main(String[] args) {
        final Display display = new Display();
        final Shell shell = new Shell(display);
        
        shell.addPaintListener(new PaintListener() {

            public void paintControl(PaintEvent event) {
                event.gc.drawImage(background, 0, 0);
                new LabelOverlay().paintLabels(labeling, event.gc);
            }
        });
        
        prepare();
        
        shell.setBounds(10, 10, mapSize, mapSize);
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch())
                display.sleep();
        }
        display.dispose();
        System.exit(1);
    }

    private static void prepare() {
        
        final HashMap<Point, Integer> points = new HashMap<Point, Integer>();
        double f = 0.8;
        points.put(new Point(.1, .1*f, "LCD"), 128);
        points.put(new Point(.1, .15*f, ""), 30);        
        points.put(new Point(.15, .15*f, ""), 75);
        points.put(new Point(.13, .2*f, ""), 25);        
        points.put(new Point(.5, .15*f, ""), 40);
        points.put(new Point(.25, .2*f, ""), 60);
        points.put(new Point(.20, .18*f, ""), 70);
        
        points.put(new Point(.8, .2*f, "OEL"), 100);
        points.put(new Point(.7, .28*f, "OLED"), 160);
        points.put(new Point(.6, .3*f, ""), 100);
        points.put(new Point(.55, .28*f, ""), 80);
        points.put(new Point(.5, .25*f, ""), 50);
        points.put(new Point(.5, .5*f, "LED"), 120);
        points.put(new Point(.54, .55*f, ""), 40);
        points.put(new Point(.48, .53*f, ""), 33);
        
        points.put(new Point(.6, .4*f, ""), 50);
        points.put(new Point(.65, .35*f, ""), 60);
        
        // debugging off by one shading error
        points.put(new Point(.95, .7*f, ""), 200);
        points.put(new Point(.05, .7*f, ""), 200);
        points.put(new Point(.5, .05, ""), 200);
        points.put(new Point(.5, .95, ""), 200);        
        
        Configuration configuration = new Configuration(points.keySet());
        
        ArrayList<Label> labels = new ArrayList<Label>();
        for(Point each: points.keySet()) {
            int x = toMapCoords(each.x);
            int y = toMapCoords(each.y);
            org.eclipse.swt.graphics.Point extent = new org.eclipse.swt.graphics.Point(x, y);
            int fontsize = (points.get(each))/3;
            labels.add(new Label(x-20, y-60, extent, fontsize, each.getDocument()));
        }
        labeling = new Labeling(labels);
        
        MapInstance mapInstance = configuration.withSize(mapSize, new MapScheme<Double>() {
            @Override
            public Double forLocation(Point location) {
                // TODO fill in size
                return (double) points.get(location);

            }
        });  
        
        DEMAlgorithm algorithm = new DEMAlgorithm();
        algorithm.setMap(mapInstance);
        float[][] DEM = algorithm.call();
        
        ShadeAlgorithm hsa = new ShadeAlgorithm();
        hsa.setMap(mapInstance);
        double[][] shading = hsa.call();
        
        int mapSize = mapInstance.getWidth();
        Device device = Display.getCurrent();
        background = new Image(device, mapSize, mapSize);
        GC gc = new GC(background);
        
        CodemapColors colors = new CodemapColors();
        colors.setColor("OLED", new MColor(255, 0, 0));
        
        Color black = new Color(gc.getDevice(), 0, 0, 0);
        gc.setBackground(black);
        gc.fillRectangle(gc.getClipping());
        black.dispose();        
        Image image = new FastBackgroundRenderer(DEM, shading, mapInstance, colors, device).render();        
        gc.drawImage(image, 0, 0);
        gc.dispose();
    }
    
    private static int toMapCoords(double x) {
        return (int)(mapSize * x);
    }
}
