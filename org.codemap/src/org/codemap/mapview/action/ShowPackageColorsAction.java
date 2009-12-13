package org.codemap.mapview.action;

import static org.codemap.util.CodemapIcons.PACKAGES;
import static org.codemap.util.CodemapIcons.descriptor;

import org.eclipse.jface.resource.ImageDescriptor;


public class ShowPackageColorsAction extends RadioButtonAction {
   
    public ShowPackageColorsAction(ActionStore actionStore) {
        super("Color by Package", actionStore);
    }

    @Override
    protected ImageDescriptor getImage() {
        return descriptor(PACKAGES);
    }

}
