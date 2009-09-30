package ch.deif.meander.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;

import ch.deif.meander.Labeling;
import ch.deif.meander.map.MapValues;

public class LabelOverlay extends SWTLayer {

    public static final String ARIAL_NARROW = "Arial Narrow";

    @Override
    public void paintMap(MapValues map, GC gc) {
        Labeling labeling = map.labeling.getValue();
        if (labeling == null) return;
        paintLabels(labeling, gc);
    }
    
    public void paintLabels(Labeling labeling, GC gc) {
        Device device = gc.getDevice();
        String fname = ARIAL_NARROW;
        Font basefont = new Font(device, fname, 12, SWT.NORMAL);
            Color white = device.getSystemColor(SWT.COLOR_WHITE);
            Color black = device.getSystemColor(SWT.COLOR_BLACK);
            for (Label each: labeling.labels()) {
                FontData[] fontData = basefont.getFontData();
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
    }

}
