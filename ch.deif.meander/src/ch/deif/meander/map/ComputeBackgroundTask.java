package ch.deif.meander.map;

import java.util.List;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;

import ch.akuhn.util.ProgressMonitor;
import ch.akuhn.values.Arguments;
import ch.akuhn.values.TaskValue;
import ch.akuhn.values.Value;
import ch.deif.meander.DigitalElevationModel;
import ch.deif.meander.HillShading;
import ch.deif.meander.Location;
import ch.deif.meander.MapInstance;
import ch.deif.meander.internal.DEMAlgorithm;
import ch.deif.meander.util.MColor;
import ch.deif.meander.util.MapScheme;
import ch.deif.meander.util.SparseTrueBooleanList;

public class ComputeBackgroundTask extends TaskValue<Image> {

    public ComputeBackgroundTask(
            Value<MapInstance> mapInstance, 
            Value<DigitalElevationModel>  elevationModel, 
            Value<HillShading> shading, 
            Value<MapScheme<MColor>> colors) {
        super("Caching background image", mapInstance, elevationModel, shading, colors);
        this.doesNotRequireAllArguments(); 
    }

    @Override
    protected Image computeValue(ProgressMonitor monitor, Arguments arguments) {
        MapInstance mapInstance = arguments.nextOrNull();
        DigitalElevationModel elevationModel = arguments.nextOrNull();
        HillShading hillShading = arguments.nextOrNull();
        MapScheme<MColor> colors = arguments.nextOrNull();
        if (colors == null) colors = MapScheme.with(MColor.HILLGREEN);
        if (mapInstance == null) return null;
        
        System.out.println("---------------");
        System.out.println(Thread.currentThread().getName());
        System.out.println(mapInstance);
        System.out.println(elevationModel);
        System.out.println(hillShading);
        System.out.println("---------------");
        
        return computeValue(monitor, mapInstance, elevationModel, hillShading, colors);
    }

    private Image computeValue(ProgressMonitor monitor, MapInstance mapInstance, DigitalElevationModel elevationModel, HillShading hillShading, MapScheme<MColor> colors) {
        int mapSize = mapInstance.getWidth();
        Device device = Display.getCurrent();
        Image image = new Image(device, mapSize, mapSize);
        GC gc = new GC(image);
        this.paintBackground(monitor, gc, mapInstance, elevationModel, hillShading, colors);
        gc.dispose();
        return image;
    }

    private void paintBackground(ProgressMonitor monitor, GC gc, MapInstance mapInstance, DigitalElevationModel elevationModel, HillShading hillShading, MapScheme<MColor> colors) {
        paintWater(monitor, gc);
        if (elevationModel == null) {
            paintDraft(monitor, gc, mapInstance);
        }
        else {
            paintShores(monitor, gc, mapInstance, elevationModel);
            paintHills(monitor, gc, mapInstance, elevationModel, hillShading, colors);
        }
    }

    private void paintDraft(ProgressMonitor monitor, GC gc, MapInstance map) {
        if (monitor.isCanceled()) return;
        if (map == null) return;
        Device device = gc.getDevice();
        Color color = new Color(device, 92, 142, 255);
        gc.setForeground(color);
        gc.setLineWidth(2);
        for (Location each: map.locations()) {
            if (monitor.isCanceled()) break;
            int r = (int) (each.getElevation() * 2 * map.getWidth() / DEMAlgorithm.MAGIC_VALUE);
            gc.drawOval(each.px - r, each.py - r, r * 2, r * 2);
        }
        color.dispose();
    }

    private void paintHills(ProgressMonitor monitor, GC gc, MapInstance mapInstance, DigitalElevationModel elevationModel, HillShading hillShading, MapScheme<MColor> colors) {
        if (monitor.isCanceled()) return;
        if (hillShading == null) return;
        int mapSize = mapInstance.getWidth();
        float[][] DEM = elevationModel.asFloatArray();
        double[][] shade = hillShading.asDoubleArray();
        List<SparseTrueBooleanList> list = hillShading.asSparseTrueBooleanList();
        Device device = gc.getDevice();
        Rectangle rect = new Rectangle(0, 0, mapSize, mapSize);
        rect.intersect(gc.getClipping());
        for (int x = rect.x; x < (rect.x + rect.width); x++) {
            if (monitor.isCanceled()) break;
            for (int y = rect.y; y < (rect.y + rect.height); y++) {
                if (DEM[x][y] > 10) {
                    double f = shade[x][y];
                    if (list.get(x).get(y)) f *= 0.5; // contour lines
                    if (f < 0.0) f = 0.0;
                    MColor mcolor = colors.forLocation(mapInstance.nearestNeighbor(x, y).getPoint());                                       
                    Color hillColor = new Color(device, (int) (mcolor.getRed() * f), (int) (mcolor.getGreen() * f), (int) (mcolor.getBlue() * f));
                    gc.setForeground(hillColor);
                    gc.drawPoint(x, y);
                    hillColor.dispose();
                }
            }
        }
    }

    private void paintShores(ProgressMonitor monitor, GC gc, MapInstance mapInstance, DigitalElevationModel elevationModel) {
        if (monitor.isCanceled()) return;
        int mapSize = mapInstance.getWidth();
        float[][] DEM = elevationModel.asFloatArray();
        Color color = new Color(gc.getDevice(), 92, 142, 255);
        gc.setForeground(color);
        Rectangle rect = new Rectangle(0, 0, mapSize, mapSize);
        rect.intersect(gc.getClipping());
        for (int x = rect.x; x < (rect.x + rect.width); x++) {
            if (monitor.isCanceled()) break;
            for (int y = rect.y; y < (rect.y + rect.height); y++) {
                if (DEM[x][y] > 2) gc.drawPoint(x, y);
            }
        }
        color.dispose();
    }
    
    private void paintWater(ProgressMonitor monitor, GC gc) {
        if (monitor.isCanceled()) return;
        Color blue = new Color(gc.getDevice(), 0, 0, 255);
        gc.setBackground(blue);
        gc.fillRectangle(gc.getClipping());
        blue.dispose();
    }

}
