package org.codemap.mapview;

import static org.codemap.util.Icons.LAYERS;

import org.codemap.util.ExtensionPoints;
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
		return ExtensionPoints.LAYERS;
	}

	@Override
	protected int getActionStyle() {
		return AS_CHECK_BOX;
	}

}
