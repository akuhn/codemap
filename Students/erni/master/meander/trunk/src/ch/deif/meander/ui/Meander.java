package ch.deif.meander.ui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.util.TreeMap;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;

import processing.core.PApplet;
import ch.akuhn.hapax.corpus.Document;
import ch.akuhn.hapax.corpus.Terms;
import ch.akuhn.util.Bag;
import ch.akuhn.util.Get;
import ch.akuhn.util.Separator;
import ch.deif.meander.Location;
import ch.deif.meander.Map;
import ch.deif.meander.MapBuilder;
import ch.deif.meander.MapVisualization;
import ch.deif.meander.Serializer;
import ch.deif.meander.Serializer.MSEDocument;
import ch.deif.meander.Serializer.MSEProject;
import ch.deif.meander.Serializer.MSERelease;
import ch.deif.meander.ui.Applet.MapViz;

public class Meander {

    public static void main(String... args) {
        // new GlitchSticksWindow();
        new MeanderWindow();
    }

    public static class EventHandler {

        private MeanderWindow window;
        private MapViz applet;

        public EventHandler(MeanderWindow m, Applet.MapViz a) {
            window = m;
            applet = a;
            a.registerHandler(this);
            m.registerHandler(this);
        }

        public void onAppletSelectionCleared() {
            // System.out.println("clear selection");
            assert window.display() != null;
            window.display().syncExec(new Runnable() {
                public void run() {
                    window.files().deselectAll();
                    assert window.cloud() != null;
                    window.cloud().clear();
                }
            });
        }

        public void onAppletSelection(Location location) {
            final Document document = location.document;
            // System.out.println("selecting: " + document.name());
            window.display().syncExec(new Runnable() {
                public void run() {
                    int index = window.files().indexOf(document.name());
                    window.files().select(index);
                    window.cloud().append(document.terms());
                }
            });
        }

        public void onMeanderSelection(int[] indices) {
            applet.indicesSelected(indices);
        }

    }

    public static abstract class AppletWindow extends ApplicationWindow {

        protected Composite mapComposite;
        protected PApplet applet;
        protected Frame mapFrame;

        public AppletWindow() {
            super(null);
            // Don't return from open() until window closes
            setBlockOnOpen(true);
            // Open the main window
            open();
            // Dispose the display
            applet.destroy();
            display().dispose();
        }

        public Display display() {
            return getShell().getDisplay();
        }

        protected Control createContents(Composite parent) {
            Shell shell = parent.getShell();
            mapComposite = new Composite(shell, SWT.EMBEDDED);

            mapFrame = SWT_AWT.new_Frame(mapComposite);
            mapFrame.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));

            applet = createApplet();
            mapFrame.add(applet);

            return parent;
        }

        protected abstract PApplet createApplet();

    }

    public static class TagCloud {

        private final int MIN_COUNT = 1;
        private final int MAX_HEIGHT;
        private Terms terms;
        private StyledText text;
        private FontData fontData;
        private java.util.Map<Integer, Font> fonts;

        public TagCloud(StyledText text) {
            terms = new Terms();
            this.text = text;
            fontData = text.getFont().getFontData()[0];
            MAX_HEIGHT = fontData.getHeight() * 3;
            fonts = new TreeMap<Integer, Font>();
        }

        public void clear() {
            terms.clear();
            clearText();
        }

        public void append(Terms t) {
            clearText();
            terms.addAll(t);

            Separator separator = new Separator(" ");
            int start = 0;
            for (Bag.Count<String> each : terms.counts()) {
                if (each.count > MIN_COUNT) {
                    String tag = separator + each.element;
                    text.append(tag);

                    StyleRange style = new StyleRange();
                    style.start = start;
                    style.length = tag.length();
                    int height = fontData.getHeight() + each.count;
                    if (height > MAX_HEIGHT) {
                        height = MAX_HEIGHT;
                    }
                    Font font = fonts.get(height);
                    if (font == null) {
                        font = new Font(text.getDisplay(), fontData.getName(),
                                height, fontData.getStyle());
                        fonts.put(height, font);
                    }
                    style.font = font;
                    text.setStyleRange(style);

                    start = text.getText().length();
                }
            }
        }

        private void clearText() {
            text.setText("");
        }

    }

    public static class MeanderWindow extends AppletWindow {

        private static int MAP_DIM = 700;
        private Map map;

        private List files;
        private EventHandler event;
        private TagCloud tagCloud;

        public List files() {
            return files;
        }

        public TagCloud cloud() {
            return tagCloud;
        }

        public void registerHandler(EventHandler eventHandler) {
            this.event = eventHandler;
        }

        protected PApplet createApplet() {
            MapVisualization viz = createVizualization();
            Applet.MapViz pa = new Applet.MapViz(viz);
            pa.init();
            int width = viz.map.getParameters().width;
            int height = viz.map.getParameters().height;
            pa.setSize(width, height);

            new EventHandler(this, pa);
            return pa;
        }

        protected Control createContents(Composite parent) {
            Control control = super.createContents(parent);
            Shell shell = control.getShell();

            int width = applet.getWidth();
            int height = applet.getHeight();
            System.out.println(width + " - " + height);

            // mapFrame.setSize(width, height);
            mapFrame.setMaximumSize(new Dimension(width, height));
            mapFrame.setLocation(0, 0);
            mapFrame.setSize(width, height);
            // System.out.println(mapFrame);

            mapComposite.setSize(width, height);
            mapComposite.setLayout(new FillLayout());
            // "debug" to see what components are where ...
            // map.setBackground(new Color(Display.getCurrent(), 0, 255, 0));

            Composite rightPane = new Composite(shell, SWT.NONE);
            files = new List(rightPane, SWT.MULTI | SWT.V_SCROLL);
            files.addSelectionListener(new SelectionListener() {

                @Override
                public void widgetDefaultSelected(SelectionEvent e) {
                    onListSelected();
                }

                @Override
                public void widgetSelected(SelectionEvent e) {
                    onListSelected();
                }

                private void onListSelected() {
                    int[] indices = files.getSelectionIndices();
                    event.onMeanderSelection(indices);
                }

            });
            for (Location l : map.locations()) {
                files.add(l.document.name());
            }

            StyledText text = new StyledText(rightPane, SWT.BORDER | SWT.MULTI
                    | SWT.READ_ONLY | SWT.WRAP);
            tagCloud = new TagCloud(text);

            GridDataFactory.swtDefaults().hint(MAP_DIM / 2, height / 2)
                    .applyTo(files);
            GridDataFactory.swtDefaults().hint(MAP_DIM / 2, height / 2)
                    .applyTo(text);
            GridLayoutFactory.fillDefaults().spacing(5, 5).generateLayout(
                    rightPane);

            GridDataFactory.swtDefaults().hint(width, height).applyTo(
                    mapComposite);
            GridDataFactory.swtDefaults().hint(MAP_DIM / 2, height).applyTo(
                    rightPane);
            GridLayoutFactory.swtDefaults().numColumns(2).generateLayout(shell);

            // get rid of magically appearing label
            // FIXME find out where this label comes from
            Control[] children = shell.getChildren();
            if (children.length > 1) {
                Control unwanted = children[0];
                if (unwanted instanceof Label) unwanted.dispose();
            }
            shell.pack();
            return control;
        }

        protected MapVisualization createVizualization() {

            int nth = 1;
            Serializer ser = new Serializer();
            ser.model().importMSEFile("mse/junit_with_terms.mse");
            MSEProject project = ser.model().all(MSEProject.class).iterator()
                    .next();
            MSERelease release = Get.element(nth, project.releases);
            MapBuilder builder = Map.builder().size(MAP_DIM, MAP_DIM);
            for (MSEDocument each : release.documents) {
                builder.location(each, release.name);
            }

            map = builder.build();
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
