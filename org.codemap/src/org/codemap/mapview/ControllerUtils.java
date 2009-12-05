package org.codemap.mapview;

import org.eclipse.swt.graphics.Image;

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
    
    public String activeProjectName() {
        return theController.getActiveMap().getProject().getName();
    }    

}
