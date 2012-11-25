package org.codemap.mapview.action;

import static org.codemap.util.CodemapIcons.CALL_HIERARCHY;
import static org.codemap.util.CodemapIcons.descriptor;

import org.eclipse.jface.resource.ImageDescriptor;

public class LinkWithCallHierarchyAction extends CheckBoxAction {

    public LinkWithCallHierarchyAction(ActionStore actionStore) {
        super("Link with Call Hierarchy", actionStore);
    }

    @Override
    protected ImageDescriptor getImage() {
        return descriptor(CALL_HIERARCHY);
    }
}
