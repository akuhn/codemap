package ch.deif.meander.ui;

import java.awt.Frame;

import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import ch.akuhn.util.Get;
import ch.deif.meander.ContourLineAlgorithm;
import ch.deif.meander.DEMAlgorithm;
import ch.deif.meander.HillshadeAlgorithm;
import ch.deif.meander.HillshadeVisualization;
import ch.deif.meander.Map;
import ch.deif.meander.MapBuilder;
import ch.deif.meander.MapVisualization;
import ch.deif.meander.NormalizeElevationAlgorithm;
import ch.deif.meander.NormalizeLocationsAlgorithm;
import ch.deif.meander.Serializer;
import ch.deif.meander.Serializer.MSELocation;
import ch.deif.meander.Serializer.MSEProject;
import ch.deif.meander.Serializer.MSERelease;

import processing.core.PApplet;

public class Meander {
    
    public static void main(String ... args) {
        startSwt();
        //startPapplet();
    }
    
    private static void startPapplet() {
        new PViewer(createViz());
    }


    private static void startSwt() {
        Display display = new Display();
        Shell shell = new Shell(display);
        
        MapVisualization viz = createViz();
        // we currently need this composite, to have an embedded parent
        // object. the SWT_AWT bridge pukes if parent is not embedded
        Composite mapComposite = new Composite(shell, SWT.EMBEDDED);
        Frame mapFrame = SWT_AWT.new_Frame(mapComposite);
        PApplet pa = new InnerApplet(viz);
        mapFrame.add(pa);
        pa.init();
        int width = viz.map.getParameters().width;
        int height = viz.map.getParameters().height;
        //frame.setSize(width, height);
        mapComposite.setSize(width, height);
        
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch())
                display.sleep();
        }
        display.dispose();
    }


    private static MapVisualization createViz() {
        int nth = 10;
        Serializer ser = new Serializer();
        ser.model().importMSEFile("mse/junit_meander.mse");
        MSEProject proj = ser.model().all(MSEProject.class).iterator().next();
        MSERelease rel = Get.element(nth, proj.releases);
        MapBuilder builder = Map.builder();
        for (MSELocation each: rel.locations) {
            builder.location(each.x, each.y, each.height);
        }
        Map map = builder.build();
        
        new NormalizeLocationsAlgorithm(map).run();
        new DEMAlgorithm(map).run();
        new NormalizeElevationAlgorithm(map).run();
        new HillshadeAlgorithm(map).run();
        new ContourLineAlgorithm(map).run();
        //MapVisualization viz = new SketchVisualization(map);
        MapVisualization viz = new HillshadeVisualization(map);
        return viz;
    }
    
    
    @SuppressWarnings("serial")
    private static class InnerApplet extends PApplet {
        
        private MapVisualization viz;
        
        public InnerApplet(MapVisualization viz) {
            this.viz = viz;
        }
        
        @Override
        public void setup() {
            size(viz.map.getParameters().width, viz.map.getParameters().height);
            frameRate(1);
        }
    
        @Override
        public void draw() {
            viz.draw(g);
        }
    }

}
