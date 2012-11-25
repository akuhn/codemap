package org.codemap.layers;

import org.codemap.Labeling;
import org.codemap.resources.MapValues;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;


public class LabelOverlay extends Layer {

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
        
        for (Label each: labeling.labels()) {
            FontData[] fontData = basefont.getFontData();
            
            each.render(gc, fontData);
        }
        
        basefont.dispose();
    }


}
