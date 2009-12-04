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
//        addDebugPoints(points);
        anotherDebugPointSet(points);
        
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
        
        
        gc.setForeground(new Color(gc.getDevice(), 255, 0, 0));
        for(int x = 0; x < mapInstance.width; x++) {
            for (int y = 0; y < mapInstance.height; y++) {
                Location kdTreeNearest = mapInstance.kdTreeNearest(x, y);
                Location naiveNearest = mapInstance.naiveNearest(x, y);
                if (naiveNearest.equals(kdTreeNearest)) continue;
                
                gc.drawPoint(x, y);
//                assertEquals(naiveNearest, kdTreeNearest);
            }
        }        
        
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
        
//        Location mpp = getLocation(mapInstance, "MapPerProject");
//        Location dda = getLocation(mapInstance, "ColorDropDownAction");        
//        System.out.println("Distance to MapPerProject: " +  Math.sqrt(Math.pow(575 - mpp.px, 2) + Math.pow(500 - mpp.py, 2)));
//        System.out.println("Distance to ColorDropDownAction: " + Math.sqrt(Math.pow(575 - dda.px, 2) + Math.pow(500 - dda.py, 2)));
        
//        KdTree kdTree = mapInstance.getKdTree();
//        kdTree.setLog(new DebugLog());
        System.out.println(mapInstance.nearestNeighbor(575, 500).getDocument());
    }
    
    private static Location getLocation(MapInstance mapInstance, String makeRed) {
        Iterable<Location> locations = mapInstance.locations();
        for (Location location : locations) {
            if (location.getDocument().equals(makeRed)) return location;
        }
        return null;
    }
    
    public static void anotherDebugPointSet(HashMap<Point, Integer> points) {
        points.put(new Point(0.727982013571401, 0.2015012075161291, "DigitalElevationModel"), 13);
        points.put(new Point(0.12209471257111137, 0.41856172072710734, "Label"), 25);
        points.put(new Point(0.47729332118731826, 0.36723768655363476, "DEMAlgorithmTest"), 45);
        points.put(new Point(0.17992872725322762, 0.42339774596246355, "Background"), 16);
        points.put(new Point(0.4452025593828695, 0.36965759335616194, "ComputeHillShadingTask"), 31);
        points.put(new Point(0.6991351300857797, 0.5217493337409814, "ComputeConfigurationTask"), 24);
        points.put(new Point(0.9161531420179493, 0.4318100278031642, "HillShading"), 20);
        points.put(new Point(0.8570590768479509, 0.5691210629310043, "Point"), 34);
        points.put(new Point(0.47225520382440234, 0.42309419203753873, "DEMAlgorithm"), 44);
        points.put(new Point(0.35154190197260193, 0.39734456783186906, "ContourLineAlgorithm"), 29);
        points.put(new Point(0.4513358771188642, 0.3292220956007367, "MapValueBuilder"), 44);
        points.put(new Point(0.05, 0.36352119121629467, "CodemapEventRegistry"), 22);
        points.put(new Point(0.6298226948086383, 0.43359247287423325, "Grayscales"), 21);
        points.put(new Point(0.07946042589040347, 0.41484208920724125, "SparseTrueBooleanListTest"), 26);
        points.put(new Point(0.630095046819459, 0.41061805721935074, "HausdorffDistance"), 36);
        points.put(new Point(0.6456237874721577, 0.45734409241498486, "OpenFilesOverlay"), 28);
        points.put(new Point(0.10322438277079068, 0.3269586972498873, "ComputeLabelingTask"), 49);
        points.put(new Point(0.33691101376819776, 0.38996864161127137, "SmallDocumentCorporaTest"), 17);
        points.put(new Point(0.7523161105535109, 0.3208210993734694, "DefaultLabelScheme"), 19);
        points.put(new Point(0.4944504378202183, 0.5369022177270648, "Configuration"), 46);
        points.put(new Point(0.6955092286410955, 0.5351757698625104, "BackgroundBuilder"), 14);
        points.put(new Point(0.43081699222996955, 0.19166161457893255, "CodemapListener"), 11);
        points.put(new Point(0.5172643910564242, 0.404000331843547, "CompositeLayer"), 37);
        points.put(new Point(0.3673980987395062, 0.4666504593793392, "MersenneTwister"), 100);
        points.put(new Point(0.5655037268614987, 0.28994995638832216, "Labeling"), 12);
        points.put(new Point(0.5377493645735713, 0.9500000000000001, "SWTLayer"), 34);
        points.put(new Point(0.5152555581996034, 0.3723720040806855, "MeanderError"), 11);
        points.put(new Point(0.4818486534932965, 0.33781698027823204, "MapVisualization"), 27);
        points.put(new Point(0.318016802743569, 0.28259579933293394, "MapAlgorithm"), 14);
        points.put(new Point(0.522318577424264, 0.4351405745626467, "RawElevationBackground"), 28);
        points.put(new Point(0.5192303805134787, 0.36946927600104257, "ExampleMap"), 8);
        points.put(new Point(0.8432292989990462, 0.5486997851171438, "SparseTrueBooleanList"), 22);
        points.put(new Point(0.6432905872589707, 0.15671820075525372, "CodemapColors"), 23);
        points.put(new Point(0.38937045833405015, 0.48921803384071855, "MapValues"), 39);
        points.put(new Point(0.4063108212029932, 0.4621536890132572, "CodemapEvent"), 16);
        points.put(new Point(0.46730963146613774, 0.34664316038557624, "LayersBuilder"), 18);
        points.put(new Point(0.4379483657817863, 0.32393646533261944, "ColorsTest"), 16);
        points.put(new Point(0.21470043718129933, 0.5036560990073666, "MapSelection"), 31);
        points.put(new Point(0.39765091057238006, 0.5086486435181407, "AssertionsEnabledTest"), 10);
        points.put(new Point(0.2889275726399062, 0.476057585720651, "RunLengthEncodedList"), 27);
        points.put(new Point(0.21992081869525126, 0.4074167047374584, "ComputeElevationModelTask"), 28);
        points.put(new Point(0.5173137368904185, 0.4414935957711703, "HillshadeAlgorithm"), 33);
        points.put(new Point(0.6001180458218406, 0.4418556299519342, "MapInstance"), 66);
        points.put(new Point(0.46180742085710835, 0.32384455402588624, "LabelOverlay"), 32);
        points.put(new Point(0.5137970941285689, 0.4869605955671055, "CurrSelectionOverlay"), 50);
        points.put(new Point(0.22058139659686388, 0.4122237107928034, "MapCaches"), 35);
        points.put(new Point(0.2871885059819287, 0.7869154368782301, "MeanderEventListener"), 11);
        points.put(new Point(0.7893003173396159, 0.38908510474834707, "YouAreHereOverlay"), 38);
        points.put(new Point(0.2256521543212685, 0.23175273761735904, "ComputeBackgroundTask"), 65);
        points.put(new Point(0.5635523569125178, 0.37696310190348553, "MapBuilder"), 21);
        points.put(new Point(0.5934965024086764, 0.4012583117685836, "Meander"), 34);
        points.put(new Point(0.47250659810833273, 0.250594696723062, "MapSetting"), 27);
        points.put(new Point(0.5594123440680524, 0.4449952930335879, "NearestNeighborAlgorithm"), 27);
        points.put(new Point(0.33963258764981585, 0.21330559358033246, "CodemapVisualization"), 60);
        points.put(new Point(0.7693014802918043, 0.11768958231820768, "Location"), 30);
        points.put(new Point(0.5910673951826484, 0.4757855227520342, "ComputeIndexTask"), 29);
        points.put(new Point(0.2994431028447028, 0.3244946853193488, "Delimiter"), 22);
        points.put(new Point(0.947532943597702, 0.4463810500028635, "ComputeMapInstanceTask"), 29);
        points.put(new Point(0.9500000000000001, 0.4312819509079541, "RunLengthEncodedListTest"), 43);
        points.put(new Point(0.6293353128823594, 0.24724398030391875, "SelectionOverlay"), 27);
        points.put(new Point(0.8249995758455634, 0.27964294342822366, "MapScheme"), 18);
        points.put(new Point(0.5654801804834333, 0.32597600957685224, "MColor"), 27);
        points.put(new Point(0.38383959960374464, 0.4868400574925613, "MDS"), 33);
        points.put(new Point(0.505278014077507, 0.05, "DefaultLabelScheme"), 19);        
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
