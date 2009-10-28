package org.codemap.mapview;

import static org.codemap.util.CodemapIcons.LAYERS;

import org.codemap.flow.ShowFlowAction;
import org.codemap.util.CodemapIcons;
import org.codemap.util.ExtensionPoints;
import org.eclipse.swt.widgets.Menu;

public class LayerDropDownAction extends ExtensionPointDropDownAction {
	
    
	@Override
    protected void createMenu(Menu menu) {
        super.createMenu(menu);
        addActionToMenu(menu, new ShowFlowAction());
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
