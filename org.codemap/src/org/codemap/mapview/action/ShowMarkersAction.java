package org.codemap.mapview.action;

import static org.codemap.util.CodemapIcons.MARKER;
import static org.codemap.util.CodemapIcons.descriptor;

import org.eclipse.jface.resource.ImageDescriptor;

public class ShowMarkersAction extends CheckBoxAction {

    public ShowMarkersAction(ActionStore actionStore) {
        super("Show Markers", actionStore);
    }

    @Override
    protected ImageDescriptor getImage() {
        return descriptor(MARKER);
    }
}
