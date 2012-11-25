package org.codemap.mapview.action;

import static org.codemap.util.CodemapIcons.LABELS;

import org.codemap.mapview.MapController;
import org.codemap.util.CodemapIcons;
import org.eclipse.swt.widgets.Menu;

public class LabelDrowDownAction extends DropDownAction {

    private ShowClassNameLabelAction identifierLabelAction;
    private ShowNoLabelAction noLabelAction;

    public LabelDrowDownAction(MapController theController) {
    }

    public LabelDrowDownAction(ActionStore actionStore) {
        super();
        identifierLabelAction = new ShowClassNameLabelAction(actionStore);
        noLabelAction = new ShowNoLabelAction(actionStore);
    }

    @Override
    protected void setup() {
        setText("Labels");
        setImageDescriptor(CodemapIcons.descriptor(LABELS));
    }

    @Override
    protected void createMenu(Menu menu) {
        addActionToMenu(menu, identifierLabelAction);
        addActionToMenu(menu, noLabelAction);
    }
}
