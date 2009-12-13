package org.codemap.mapview.action;

import org.eclipse.jface.resource.ImageDescriptor;

public class ShowNoLabelAction extends RadioButtonAction {

    public ShowNoLabelAction(ActionStore actionStore) {
        super("No Labels", actionStore);
    }

    @Override
    protected ImageDescriptor getImage() {
        return null;
    }
}
