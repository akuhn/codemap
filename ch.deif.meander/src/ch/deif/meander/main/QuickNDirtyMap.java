package ch.deif.meander.main;

import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import ch.deif.meander.Configuration;
import ch.deif.meander.Labeling;
import ch.deif.meander.Location;
import ch.deif.meander.MapInstance;
import ch.deif.meander.Point;
import ch.deif.meander.internal.DEMAlgorithm;
import ch.deif.meander.internal.ShadeAlgorithm;
import ch.deif.meander.map.ComputeBackgroundTask.FastBackgroundRenderer;
import ch.deif.meander.swt.Label;
import ch.deif.meander.swt.LabelOverlay;
import ch.deif.meander.util.CodemapColors;
import ch.deif.meander.util.KdTree;
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
        
        // needed for debugging
        shell.addMouseMoveListener(new MouseMoveListener() {
            @Override
            public void mouseMove(MouseEvent e) {
//                System.out.println(e.x + " " + e.y);
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
//        addLedPoints(points);
//        addEdgePoints(points);
        addDebugPoints(points);
        
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
        
        String makeRed = "MapPerProject";
        
        CodemapColors colors = new CodemapColors();
        colors.setColor(makeRed, new MColor(255, 0, 0));
        Iterable<Location> locations = mapInstance.locations();
        ColorBrewer colorBrewer = new ColorBrewer();
        for (Location location : locations) {
            MColor color = colorBrewer.forPackage(location.getDocument());
            colors.setColor(location.getDocument(), color);
        }
        
        Color black = new Color(gc.getDevice(), 0, 0, 0);
        gc.setBackground(black);
        gc.fillRectangle(gc.getClipping());
        black.dispose();        
        Image image = new FastBackgroundRenderer(DEM, shading, mapInstance, colors, device).render();        
        gc.drawImage(image, 0, 0);
        
//        gc.setForeground(new Color(gc.getDevice(), 123, 0, 0));
//        ImageData id = image.getImageData();
//        for(int x = 0; x < id.width; x++) {
//            for (int y = 0; y < id.height; y++) {
//                Location nn = mapInstance.nearestNeighbor(x, y);
//                if (! nn.getDocument().equals(makeRed)) continue;
//                gc.drawPoint(x, y);
//            }
//        }
        gc.dispose();
        
        Location mpp = getLocation(mapInstance, "MapPerProject");
        Location dda = getLocation(mapInstance, "ColorDropDownAction");        
        System.out.println("Distance to MapPerProject: " +  Math.sqrt(Math.pow(575 - mpp.px, 2) + Math.pow(500 - mpp.py, 2)));
        System.out.println("Distance to ColorDropDownAction: " + Math.sqrt(Math.pow(575 - dda.px, 2) + Math.pow(500 - dda.py, 2)));
        
        KdTree kdTree = mapInstance.getKdTree();
        kdTree.setLog(new DebugLog());
        System.out.println(mapInstance.nearestNeighbor(575, 500).getDocument());
    }
    
    private static Location getLocation(MapInstance mapInstance, String makeRed) {
        Iterable<Location> locations = mapInstance.locations();
        for (Location location : locations) {
            if (location.getDocument().equals(makeRed)) return location;
        }
        return null;
    }

    public static void addDebugPoints(HashMap<Point, Integer> points) {
        points.put(new Point(0.1952614392558253, 0.7973625155288466, "ICodemapPluginAction"), 11);
        points.put(new Point(0.2997628719618165, 0.8839712425057394, "SelectionTracker"), 57);
        points.put(new Point(0.3153878368460545, 0.0806776829023445, "DropDownAction"), 34);
        points.put(new Point(0.3828895156652152, 0.9109339389285401, "Icons"), 36);
        points.put(new Point(0.3968125656075399, 0.4362473833425696, "CodemapLabels"), 29);
        points.put(new Point(0.4950288573576329, 0.6365531563547007, "ColorDropDownAction"), 27);
        points.put(new Point(0.0500000000000000, 0.5247563995404056, "EclipseTaskFactory"), 42);
        points.put(new Point(0.5774641824500757, 0.0500000000000000, "ExtensionPoints"), 13);
        points.put(new Point(0.6489117292694555, 0.5396422786708468, "MapPerProject"), 71);
        points.put(new Point(0.6960067092724127, 0.1555076942233186, "CodemapCore"), 53);
        points.put(new Point(0.7268853978776577, 0.3475992284079742, "MapSelectionProvider"), 63);
        points.put(new Point(0.7600622955440098, 0.8797211618406255, "SelectionView"), 55);
        points.put(new Point(0.8105299716734261, 0.8072889681888357, "NewCodemapCreationWizard"), 46);
        points.put(new Point(0.8192338076414015, 0.6928376033888902, "LabelAction"), 37);
        points.put(new Point(0.8938838884307299, 0.6124397498165601, "LabelDrowDownAction"), 21);
        points.put(new Point(0.8107878256537154, 0.4685307490261092, "ForceSelectionAction"), 24);
        points.put(new Point(0.6556864041712878, 0.6831087428983751, "LayerDropDownAction"), 21);
        points.put(new Point(0.45923918701873373, 0.9500000000000001, "Resources"), 40);
        points.put(new Point(0.34896659980846995, 0.5833954562953987, "LinkWithSelectionAction"), 26);
        points.put(new Point(0.5191126103494813, 0.7843102749657223, "LazyPluginAction"), 40);
        points.put(new Point(0.14828704059937745, 0.16333248586351976, "OpenFileIconsLayer"), 44);
        points.put(new Point(0.8554598699279349, 0.23813880969111528, "CodemapEditor"), 33);
        points.put(new Point(0.4605707957609675, 0.1372402079862871, "ColorBrewer"), 32);
        points.put(new Point(0.18042734489778411, 0.6422998959265609, "EclipseMapValues"), 19);
        points.put(new Point(0.6155635440238818, 0.8804054961044556, "ResizeListener"), 36);
        points.put(new Point(0.8315198055143184, 0.7788090954597433, "SaveAsPNGAction"), 15);
        points.put(new Point(0.567574873156203, 0.1990501419859792, "Log"), 50);
        points.put(new Point(0.8004721068096122, 0.20324985924961286, "MapController"), 35);
        points.put(new Point(0.9500000000000001, 0.4936556272010293, "EditorPartListener"), 50);
        points.put(new Point(0.5649162427388706, 0.3982205157252395, "ComputeElementsTask"), 35);
        points.put(new Point(0.6386389084123331, 0.07742589785663238, "NewCodemapCreationWizardPage"), 100);
        points.put(new Point(0.06509840553447456, 0.5795058435351216, "ShowDefaultColorsAction"), 27);
        points.put(new Point(0.1863723751984574, 0.4391463171858582, "MapView"), 96);
        points.put(new Point(0.14183064977585613, 0.2660280354486197, "ComputeEclipseIndexTask"), 28);
        points.put(new Point(0.3293501975286699, 0.2872755859696598, "ShowPackageColorsAction"), 48);
        points.put(new Point(0.11418906590372001, 0.7398334200203752, "CodemapAction"), 16);
        points.put(new Point(0.3695694181939686, 0.7445567652679885, "ExtensionPointDropDownAction"), 35);
        points.put(new Point(0.09610385856107856, 0.3956128608530838, "ID"), 16);
        points.put(new Point(0.39700882874501414, 0.056832811084625935, "MenuAction"), 23);
        points.put(new Point(0.9371398584844339, 0.35247595768423856, "EclipseMapValuesBuilder"), 42);
        points.put(new Point(0.27661348425113796, 0.19757922574828785, "Adaptables"), 15);
    }

    private static void addEdgePoints(HashMap<Point, Integer> points) {
        // debugging off-by-one shading error
        points.put(new Point(.95, .5, ""), 200);
        points.put(new Point(.05, .5, ""), 200);
        points.put(new Point(.5, .05, ""), 200);
        points.put(new Point(.5, .95, ""), 200);
        
    }

    private static void addLedPoints(HashMap<Point, Integer> points) {
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
    }

    private static int toMapCoords(double x) {
        return (int)(mapSize * x);
    }
}
