package org.codemap.mapview.action;

import static org.codemap.util.CodemapIcons.YOU_ARE_HERE;
import static org.codemap.util.CodemapIcons.descriptor;

import org.eclipse.jface.resource.ImageDescriptor;

public class ShowYouAreHereAction extends CheckBoxAction {

    public ShowYouAreHereAction(ActionStore actionStore) {
        super("Show active File", actionStore);
    }

    @Override
    protected ImageDescriptor getImage() {
        return descriptor(YOU_ARE_HERE);
    }
}
