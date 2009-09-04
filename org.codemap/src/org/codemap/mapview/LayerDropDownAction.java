package org.codemap.mapview;

import static org.codemap.util.CodemapIcons.LAYERS;

import org.codemap.util.CodemapIcons;
import org.codemap.util.ExtensionPoints;

public class LayerDropDownAction extends ExtensionPointDropDownAction {
	
	public LayerDropDownAction() {
		super();
	}
	
	@Override
	protected void setup() {
	    setImageDescriptor(new CodemapIcons().descriptor(LAYERS));
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
