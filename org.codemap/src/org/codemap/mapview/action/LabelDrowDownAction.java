package org.codemap.mapview.action;

import static org.codemap.util.CodemapIcons.LABELS;

import org.codemap.mapview.MapController;
import org.codemap.mapview.action.LabelAction.IdentifierLabelAction;
import org.codemap.mapview.action.LabelAction.NoLabelAction;
import org.codemap.util.CodemapIcons;
import org.eclipse.swt.widgets.Menu;

public class LabelDrowDownAction extends DropDownAction {
	
    private IdentifierLabelAction identifierLabelAction;
    private NoLabelAction noLabelAction;

    public LabelDrowDownAction(MapController theController) {
        super(theController);
        registerAction(identifierLabelAction = new LabelAction.IdentifierLabelAction(getController()));
        registerAction(noLabelAction = new LabelAction.NoLabelAction(getController()));
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
