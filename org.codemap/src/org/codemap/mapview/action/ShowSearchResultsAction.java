package org.codemap.mapview.action;

import static org.codemap.util.CodemapIcons.SEARCH;
import static org.codemap.util.CodemapIcons.descriptor;

import org.eclipse.jface.resource.ImageDescriptor;

public class ShowSearchResultsAction extends CheckBoxAction {

    public ShowSearchResultsAction(ActionStore actionStore) {
        super("Show Search Results", actionStore);
    }

    @Override
    protected ImageDescriptor getImage() {
        return descriptor(SEARCH);
    }
}
