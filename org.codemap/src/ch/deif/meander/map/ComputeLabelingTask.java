package ch.deif.meander.map;

import java.util.ArrayList;
import java.util.Collection;

import org.codemap.layers.Label;
import org.codemap.layers.LabelOverlay;
import org.codemap.util.MapScheme;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;

import ch.akuhn.util.Get;
import ch.akuhn.util.ProgressMonitor;
import ch.akuhn.values.Arguments;
import ch.akuhn.values.TaskValue;
import ch.akuhn.values.Value;
import ch.deif.meander.DefaultLabelScheme;
import ch.deif.meander.Labeling;
import ch.deif.meander.Location;
import ch.deif.meander.MapInstance;

public class ComputeLabelingTask extends TaskValue<Labeling> {

    public ComputeLabelingTask(Value<MapInstance> mapInstance, Value<MapScheme<String>> labelScheme) {
        super("Label layout", mapInstance, labelScheme);
    }

    @Override
    protected Labeling computeValue(ProgressMonitor monitor, Arguments arguments) {
        MapInstance mapInstance = arguments.nextOrFail();
        MapScheme<String> labelScheme = arguments.nextOrFail();
        if (labelScheme == null) labelScheme = new DefaultLabelScheme();
        return computeValue(monitor, mapInstance, labelScheme);
    }


    private Labeling computeValue(ProgressMonitor monitor, MapInstance mapInstance, MapScheme<String> labelScheme) {
        Device device = Display.getDefault();
        Image image = new Image(device, 8, 8); // to get a GC
        GC gc = new GC(image);
        Iterable<Label> labels = makeLabels(monitor, gc, mapInstance, labelScheme);
        labels = new LayoutAlgorithm().layout(labels);
        gc.dispose();
        image.dispose();
        return new Labeling(labels);
    }


    private Iterable<Label> makeLabels(ProgressMonitor monitor, GC gc, MapInstance map, MapScheme<String> labelScheme) {
        Collection<Label> labels = new ArrayList<Label>();
        Font basefont = new Font(gc.getDevice(), LabelOverlay.ARIAL_NARROW, 12, SWT.NORMAL);
        for (Location each: map.locations()) {
            String text = labelScheme.forLocation(each.getPoint());
            if (text == null) continue;
            FontData[] fontData = basefont.getFontData();
            int height = (int) (Math.sqrt(each.getElevation()) * 2);
            for (FontData fd: fontData) fd.setHeight(height);
            Font font = new Font(gc.getDevice(), fontData);
            gc.setFont(font);
            Point extent = gc.stringExtent(text);
            labels.add(new Label(each.px, each.py, extent, height, text));
            font.dispose();
        }
        basefont.dispose();
        return labels;
    }

    private class LayoutAlgorithm {

        Collection<Label> layout;

        public Iterable<Label> layout(Iterable<Label> labels) {
            layout = new ArrayList<Label>();
            for (Label each: Get.sorted(labels)) mayebAddToLayout(each);
            return layout;
        }

        private void mayebAddToLayout(Label label) {
            for (double[] each: ORIENTATION) {
                label.changeOrientation(each[0], each[1]);
                if (intersectsLabels(label)) continue;
                layout.add(label);
                return;
            }
        }

        private boolean intersectsLabels(Label label) {
            for (Label each: layout) if (label.intersects(each)) return true;
            return false;
        }

    }

    private static final double[][] ORIENTATION = new double[][] {
        {-.5d,-1d}, // north
        {-.5d,-.5d}, // center
        {-.5d,0d}, // south
        {-.25d,-1d}, 
        {-.25d,-.5d}, 
        {-.25d,0d}, 
        {-.75d,-1d}, 
        {-.75d,-.5d}, 
        {-.75d,0d},
        {0d,-.5d}, 
        {-1d,-.5d}};

    
    
}
