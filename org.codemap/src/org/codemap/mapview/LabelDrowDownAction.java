package org.codemap.mapview;

import org.codemap.util.Icons;
import org.eclipse.swt.widgets.Menu;

public class LabelDrowDownAction extends DropDownAction {
	
	@Override
	protected void setup() {
		setText("Labels"); 
		setImageDescriptor(Icons.getImageDescriptor(Icons.LABELS));
	}

	@Override
	protected void createMenu(Menu menu) {
		addActionToMenu(menu, new LabelAction.IdentifierLabelAction());
		addActionToMenu(menu, new LabelAction.NoLabelAction());
	}
	
}
