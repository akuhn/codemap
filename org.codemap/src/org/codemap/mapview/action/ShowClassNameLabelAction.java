package org.codemap.mapview.action;

import org.eclipse.jface.resource.ImageDescriptor;

public class ShowClassNameLabelAction extends RadioButtonAction {

    public ShowClassNameLabelAction(ActionStore actionStore) {
        super("Class Name", actionStore);
    }

    @Override
    protected ImageDescriptor getImage() {
        return null;
    }
}
