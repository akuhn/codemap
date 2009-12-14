package org.codemap.mapview.action;

import static org.codemap.util.CodemapIcons.SELECTION;
import static org.codemap.util.CodemapIcons.descriptor;

import org.eclipse.jface.resource.ImageDescriptor;

public class ShowSelectionAction extends CheckBoxAction {

    public ShowSelectionAction(ActionStore actionStore) {
        super("Show Selection", actionStore);
    }

    @Override
    protected ImageDescriptor getImage() {
        return descriptor(SELECTION);
    }
}
