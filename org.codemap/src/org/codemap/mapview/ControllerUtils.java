package org.codemap.mapview;

import org.eclipse.swt.graphics.Image;

/**
 * Contains a collection of utility functions that come in handy like
 * abbreviations for common accessors and so on ...
 * 
 * @author deif
 */
public class ControllerUtils {
    
    private MapController theController;

    public ControllerUtils(MapController mapController) {
        theController = mapController;
    }

    /**
     * Might return null if the image could not be rendered.
     * Please make sure to dispose the image once you do not need
     * it any longer.
     * 
     * @return a new Image instance looking exactly like the image 
     *         currently displayed as `Codemap`.
     */
    public Image copyCurrentCodemapImage() {
        return theController.getView().newCodemapImage();
    }   
    
    /**
     * @return the name for the currently active project
     */
    public String activeProjectName() {
        return theController.getActiveMap().getProject().getName();
    }

    public void enableHeatMap() {
        theController.getSelectionTracker().getEditorPartListener().getTrace().enable();
        
    }

    public void disableHeatMap() {
        theController.getSelectionTracker().getEditorPartListener().getTrace().disable();
    }    

}
