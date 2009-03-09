package ch.deif.meander.ui;

import java.awt.Frame;

import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
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

    public static void main(String... args) {
//        new MeanderWindow().run();
        new GlitchSticksWindow().run();
    }

    private static void startSwt() {
        Display display = new Display();
        Shell shell = new Shell(display);

        // create menu
        Menu menuBar = new Menu(shell, SWT.BAR);
        shell.setMenuBar(menuBar);

        MenuItem file = new MenuItem(menuBar, SWT.CASCADE);
        file.setText("File");
        Menu filemenu = new Menu(shell, SWT.DROP_DOWN);
        file.setMenu(filemenu);
        MenuItem actionItem = new MenuItem(filemenu, SWT.PUSH);
        actionItem.setText("Import");

        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch())
                display.sleep();
        }
        display.dispose();
    }
    
    private abstract static class AppletWindow {

        protected Shell shell;
        protected Display display;
        protected Composite mapComposite;
        protected Frame mapFrame;
        
        public AppletWindow() {
            display = new Display();
            shell = new Shell(display);

            mapComposite = new Composite(shell, SWT.EMBEDDED);
            mapFrame = SWT_AWT.new_Frame(mapComposite);
            PApplet pa = createApplet();
            mapFrame.add(pa);
            mapComposite.setSize(pa.getWidth(), pa.getHeight());                   
        }
        
        protected void run() {
            shell.open();
            while (!shell.isDisposed()) {
                if (!display.readAndDispatch())
                    display.sleep();
            }
            display.dispose();            
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
