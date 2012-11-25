package org.codemap.mapview.action;

import static org.codemap.util.CodemapIcons.TRACE;
import static org.codemap.util.CodemapIcons.descriptor;

import org.eclipse.jface.resource.ImageDescriptor;

public class ShowHeatMapColorsAction extends RadioButtonAction {
    
    public ShowHeatMapColorsAction(ActionStore actionStore) {
        super("Show Heatmap Colors", actionStore);
    }
    
    @Override
    protected ImageDescriptor getImage() {
        return descriptor(TRACE);
    }    
}
