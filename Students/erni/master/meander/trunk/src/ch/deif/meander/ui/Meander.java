package ch.deif.meander.ui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.util.Collection;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;

import processing.core.PApplet;
import ch.akuhn.hapax.corpus.Corpus;
import ch.akuhn.hapax.corpus.Document;
import ch.akuhn.hapax.corpus.Terms;
import ch.akuhn.util.Get;
import ch.deif.meander.Map;
import ch.deif.meander.MapBuilder;
import ch.deif.meander.MapVisualization;
import ch.deif.meander.Serializer;
import ch.deif.meander.Serializer.MSEDocument;
import ch.deif.meander.Serializer.MSEProject;
import ch.deif.meander.Serializer.MSERelease;

public class Meander {

    public static void main(String... args) {
        // new GlitchSticksWindow();
        new MeanderWindow();
    }
    
    private static class Doc extends Document {
        
        private Terms terms;
        
        public Doc(String name, String version) {
            super(name, version);
            terms = new Terms();
        }

        @Override
        public Document addTerms(Terms terms) {
            terms.addAll(terms);
            return this;
        }
        
        public Document addTerms(Collection<String> terms) {
            if (terms != null) {
                terms.addAll(terms);                
            }
            return this;
        }        

        @Override
        public Corpus owner() {
            return null;
        }

        @Override
        public Terms terms() {
            return terms;
        }
        
    }

    private static abstract class AppletWindow extends ApplicationWindow {

        protected Composite map;
        protected PApplet applet;
        protected Frame mapFrame;

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
            Shell shell = parent.getShell();
            map = new Composite(shell, SWT.EMBEDDED);

            mapFrame = SWT_AWT.new_Frame(map);
            mapFrame.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));

            applet = createApplet();
            mapFrame.add(applet);

            return parent;
        }

        protected abstract PApplet createApplet();

    }

    private static class MeanderWindow extends AppletWindow {

        private static int MAP_DIM = 700;
        private MSEProject project;
        private MSERelease release;

        protected PApplet createApplet() {
            MapVisualization viz = createVizualization();
            PApplet pa = new Applet.MapViz(viz);
            pa.init();
            int width = viz.map.getParameters().width;
            int height = viz.map.getParameters().height;
            pa.setSize(width, height);
            return pa;
        }

        protected Control createContents(Composite parent) {
            Control control = super.createContents(parent);
            Shell shell = control.getShell();

            int width = applet.getWidth();
            int height = applet.getHeight();
            System.out.println(width + " - " + height);

            mapFrame.setMaximumSize(new Dimension(width, height));
            mapFrame.setLocation(0, 0);
            mapFrame.setSize(width, height);
            // System.out.println(mapFrame);

            map.setSize(width, height);
            map.setLayout(new FillLayout());
            // "debug" to see what components are where ...
            // map.setBackground(new Color(Display.getCurrent(), 0, 255, 0));

            Composite rightPane = new Composite(shell, SWT.NONE);
            List files = new List(rightPane, SWT.MULTI | SWT.V_SCROLL);
            for (MSEDocument each : release.documents) {
                files.add(each.name);
            }

            Canvas tagCloud = new Canvas(rightPane, SWT.NONE);

            GridDataFactory.swtDefaults().hint(MAP_DIM / 2, height / 2)
                    .applyTo(files);
            GridDataFactory.swtDefaults().hint(MAP_DIM / 2, height / 2)
                    .applyTo(tagCloud);
            GridLayoutFactory.fillDefaults().spacing(5, 5).generateLayout(
                    rightPane);

            GridDataFactory.swtDefaults().hint(width, height).applyTo(map);
            GridDataFactory.swtDefaults().hint(MAP_DIM / 2, height).applyTo(
                    rightPane);
            GridLayoutFactory.swtDefaults().numColumns(2).generateLayout(shell);

            // get rid of magically appearing label
            // FIXME find out where this label comes from
            Control[] children = shell.getChildren();
            if (children.length > 1) {
                Control unwanted = children[0];
                if (unwanted instanceof Label)
                    unwanted.dispose();
            }
            shell.pack();
            return control;
        }

        protected MapVisualization createVizualization() {

            int nth = 10;
            Serializer ser = new Serializer();
            ser.model().importMSEFile("mse/junit_meander.mse");
            project = ser.model().all(MSEProject.class).iterator().next();
            release = Get.element(nth, project.releases);
            MapBuilder builder = Map.builder().size(MAP_DIM, MAP_DIM);
            
//            for (Pair<MSELocation, MSEDocument> each : zip(release.locations,
//                    release.documents)) {
//                Doc d = new Doc(each.snd.name, release.name);
//                d.addTerms(each.snd.terms);
//                System.out.println(each.snd.terms);
//                builder.location(each.fst.x, each.fst.y, each.fst.height, d);
//            }

//            System.out.println(release.locations.size());
            System.out.println(release.documents.size());

//            for (MSELocation each : release.locations) {
//                builder.location(each.x, each.y, each.height);
//            }

            Map map = builder.build();
            return map.getDefauVisualization();
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
