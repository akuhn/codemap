package org.codemap.mapview;

import org.codemap.util.Icons;
import org.eclipse.swt.widgets.Menu;

public class LayerDropDownAction extends ExtensionPointDropDownAction {
	
	private SelectionTracker selectionTracker;
	private MapSelectionProvider selectionProvider;
	
	public LayerDropDownAction(SelectionTracker tracker, MapSelectionProvider provider) {
		super();
		selectionTracker = tracker;
		selectionProvider = provider;
	}
	
	@Override
	protected void setup() {
		setImageDescriptor(Icons.getImageDescriptor(Icons.LAYERS));
		setText("Layers"); 
	}

	@Override
	protected void createDefaultMenu(Menu menu) {
		addActionToMenu(menu, new LinkWithSelectionAction(selectionTracker));
		addActionToMenu(menu, new ForceSelectionAction(selectionProvider));
	}

	@Override
	protected String getExtensionPointName() {
		// TODO: better name for this extension point ...
		return "mapview";
	}

	@Override
	protected int getActionStyle() {
		return AS_CHECK_BOX;
	}

}
