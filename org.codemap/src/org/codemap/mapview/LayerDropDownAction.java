package org.codemap.mapview;

import org.codemap.util.Icons;

public class LayerDropDownAction extends ExtensionPointDropDownAction {
	
	public LayerDropDownAction() {
		super();
	}
	
	@Override
	protected void setup() {
		setImageDescriptor(Icons.getImageDescriptor(Icons.LAYERS));
		setText("Layers"); 
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
