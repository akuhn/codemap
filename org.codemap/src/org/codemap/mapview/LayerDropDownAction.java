package org.codemap.mapview;

import static org.codemap.util.Icons.LAYERS;

import org.codemap.util.Icons;

public class LayerDropDownAction extends ExtensionPointDropDownAction {
	
	public LayerDropDownAction() {
		super();
	}
	
	@Override
	protected void setup() {
		setImageDescriptor(Icons.getImageDescriptor(LAYERS));
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
