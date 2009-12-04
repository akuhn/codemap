package org.codemap.mapview.action;

import static org.codemap.util.CodemapIcons.LABELS;

import org.codemap.util.CodemapIcons;
import org.eclipse.swt.widgets.Menu;

public class LabelDrowDownAction extends DropDownAction {
	
    @Override
	protected void setup() {
		setText("Labels"); 
		setImageDescriptor(CodemapIcons.descriptor(LABELS));
	}

	@Override
	protected void createMenu(Menu menu) {
		addActionToMenu(menu, new LabelAction.IdentifierLabelAction());
		addActionToMenu(menu, new LabelAction.NoLabelAction());
	}
	
}
