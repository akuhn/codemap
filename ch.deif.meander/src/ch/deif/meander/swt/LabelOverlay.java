package ch.deif.meander.swt;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;

import ch.akuhn.util.Get;
import ch.deif.meander.Labeling;
import ch.deif.meander.Location;
import ch.deif.meander.MapInstance;
import ch.deif.meander.map.MapValues;

public class LabelOverlay extends SWTLayer {

    // TODO cache the labels

    private static final String ARIAL_NARROW = "Arial Narrow";

    @Override
    public void paintMap(MapValues map, GC gc) {
        Labeling labeling = map.labeling.value();
        if (labeling == null) return;
        paintLabels(labeling, gc);
    }
    
    private void paintLabels(Labeling labeling, GC gc) {
        Device device = gc.getDevice();
        String fname = ARIAL_NARROW;
        Font basefont = new Font(device, fname, 12, SWT.NORMAL);
        Color white = new Color(device, 255, 255, 255);
        Color black = new Color(device, 0, 0, 0);
        gc.setFont(basefont); // required by labelsFor
        for (Label each: labeling.labels()) {
            FontData[] fontData = basefont.getFontData();
            //			int height = (int) (Math.sqrt(each.getElevation()) * 2);
            for (FontData fd: fontData) fd.setHeight(each.fontHeight);
            Font font = new Font(gc.getDevice(), fontData);
            gc.setFont(font);
            gc.setAlpha(128);
            gc.setForeground(black);
            gc.drawText(each.text, each.bounds.x + 1, each.bounds.y + 1, SWT.DRAW_TRANSPARENT);
            gc.setAlpha(255);
            gc.setForeground(white);
            gc.drawText(each.text, each.bounds.x, each.bounds.y, SWT.DRAW_TRANSPARENT);
            font.dispose();
        }
        basefont.dispose();
        black.dispose();
        white.dispose();	
    }

    private Iterable<Label> labelsFor(MapInstance map, GC gc) {
        Iterable<Label> labels = makeLabels(map, gc);
        return new LayoutAlgorithm().layout(labels);
    }

    private Iterable<Label> makeLabels(MapInstance map, GC gc) {
        Collection<Label> labels = new ArrayList<Label>();
        Font basefont = new Font(gc.getDevice(), ARIAL_NARROW, 12, SWT.NORMAL);
        gc.setFont(basefont); 
        for (Location each: map.locations()) {
            FontData[] fontData = basefont.getFontData();
            int height = (int) (Math.sqrt(each.getElevation()) * 2);
            for (FontData fd: fontData) fd.setHeight(height);
            Font font = new Font(gc.getDevice(), fontData);
            gc.setFont(font);
            String text = labelScheme.forLocation(each.getPoint());
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
