package org.codemap.mapview.action;

import static org.codemap.util.CodemapIcons.FILE;
import static org.codemap.util.CodemapIcons.descriptor;

import org.eclipse.jface.resource.ImageDescriptor;

public class ShowOpenFilesAction extends CheckBoxAction {

    public ShowOpenFilesAction(ActionStore actionStore) {
        super("Show open Files", actionStore);
    }

    @Override
    protected ImageDescriptor getImage() {
        return descriptor(FILE);
    }
}
