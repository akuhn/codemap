package org.codemap.mapview;

import static java.lang.Math.min;

import java.util.ArrayList;
import java.util.List;

import org.codemap.util.CodemapColors;
import org.codemap.util.MColor;
import org.codemap.util.MapScheme;

import ch.akuhn.values.Value;

public class OpenFilesTrace {
    
    private List<String> trace;
    private MapController theController;
    private boolean enabled;

    public OpenFilesTrace(MapController controller) {
        theController = controller;
        trace = new ArrayList<String>();
    }

    public void append(List<String> selection) {
        for(String each: selection) {
            trace.remove(each);
        }
        trace.addAll(0, selection);
        // we might add the empty selection
        if (trace.isEmpty()) return;
        trace = trace.subList(0, min(trace.size(), 5));
        updateColors();
    }

    private void updateColors() {
        if (! enabled) return;
        Value<MapScheme<MColor>> colorValue = theController.getActiveMap().getValues().colorScheme;
        
        CodemapColors colorScheme = new CodemapColors(MColor.HILLGREEN);
        double red_modifier = 0.2;
        for(String each: trace) {            
            int greenVal = (int) (MColor.HILLGREEN.getGreen() * red_modifier);
            red_modifier += 0.15;
            MColor col = new MColor(MColor.HILLGREEN.getRed(), greenVal, MColor.HILLGREEN.getBlue());
            colorScheme.setColor(each, col);        
        }
        colorValue.setValue(colorScheme);
    }

    public void enable() {
        enabled = true;
        updateColors();
    }

    public void disable() {
        enabled = false;
    }
}
