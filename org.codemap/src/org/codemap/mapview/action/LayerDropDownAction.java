package org.codemap.mapview.action;

import static org.codemap.util.CodemapIcons.LAYERS;

import org.codemap.callhierarchy.CallHierarchyTracker;
import org.codemap.callhierarchy.LinkWithCallHierarchyAction;
import org.codemap.util.CodemapIcons;
import org.codemap.util.ExtensionPoints;
import org.eclipse.swt.widgets.Menu;

public class LayerDropDownAction extends ExtensionPointDropDownAction {
    
	private CallHierarchyTracker callHierarchyTracker;

    public LayerDropDownAction(CallHierarchyTracker callHierarchyTracker) {
	    this.callHierarchyTracker = callHierarchyTracker;
    }

    @Override
    protected void createMenu(Menu menu) {
        super.createMenu(menu);
        addActionToMenu(menu, new LinkWithCallHierarchyAction(callHierarchyTracker));
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
