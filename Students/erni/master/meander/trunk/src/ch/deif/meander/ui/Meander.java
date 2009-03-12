package ch.deif.meander.ui;

import java.awt.Frame;

import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

import processing.core.PApplet;
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

public class Meander {

    public static void main(String... args) {
//         new GlitchSticksWindow();
        new MeanderWindow();
    }
    
    private static abstract class AppletWindow extends ApplicationWindow {
        
        public AppletWindow() {
            super(null);
            // Don't return from open() until window closes
            setBlockOnOpen(true);
            // Open the main window
            open();
            // Dispose the display
            Display.getCurrent().dispose();            
        }
        
        protected Control createContents(Composite parent) {
            Composite mapComposite = new Composite(parent.getShell(), SWT.EMBEDDED);
            Frame mapFrame = SWT_AWT.new_Frame(mapComposite);
            PApplet pa = createApplet();
            mapFrame.add(pa);
            mapComposite.setSize(pa.getWidth(), pa.getHeight());
           
            return parent;
          }
        
        protected abstract PApplet createApplet();
        
    }
    
    private static class MeanderWindow extends AppletWindow {
        
        protected PApplet createApplet() {
            MapVisualization viz = createVizualization();
            PApplet pa = new Applet.MapViz(viz);
            pa.init();
            int width = viz.map.getParameters().width;
            int height = viz.map.getParameters().height;
            // frame.setSize(width, height);
            pa.setSize(width, height);
            return pa;
        }

        protected MapVisualization createVizualization() {

            int nth = 10;
            Serializer ser = new Serializer();
            ser.model().importMSEFile("mse/junit_meander.mse");
            MSEProject proj = ser.model().all(MSEProject.class).iterator()
                    .next();
            MSERelease rel = Get.element(nth, proj.releases);
            MapBuilder builder = Map.builder();
            for (MSELocation each : rel.locations) {
                builder.location(each.x, each.y, each.height);
            }
            Map map = builder.build();

            new NormalizeLocationsAlgorithm(map).run();
            new DEMAlgorithm(map).run();
            new NormalizeElevationAlgorithm(map).run();
            new HillshadeAlgorithm(map).run();
            new ContourLineAlgorithm(map).run();
            // MapVisualization viz = new SketchVisualization(map);
            MapVisualization viz = new HillshadeVisualization(map);
            return viz;
        }        
      }    

    private static class GlitchSticksWindow extends AppletWindow {

        @Override
        protected PApplet createApplet() {
            PApplet pa = new Applet.GlitchSticks();
            pa.setSize(512, 512);
            pa.init();
            return pa;
        }

    }
}
