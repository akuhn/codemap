package org.codemap.callhierarchy;

import static org.codemap.util.CodemapIcons.FLOW;

import org.codemap.CodemapCore;
import org.codemap.MapPerProject;
import org.codemap.mapview.MenuAction;
import org.codemap.util.CodemapIcons;
import org.eclipse.jface.action.IAction;

public class LinkWithCallHierarchyAction extends MenuAction {


    public LinkWithCallHierarchyAction() {
	    super("Show Flow", IAction.AS_CHECK_BOX);
	    setImageDescriptor(new CodemapIcons().descriptor(FLOW));
	}

    @Override
	public void run() {
        super.run();
		if (isChecked()) showFLow();
		else hideFlow();
	}
	
	private void showFLow() {
        MapPerProject activeMap = CodemapCore.getPlugin().getActiveMap();
        if (activeMap.containsLayer(CallOverlay.class)) return;
        
//        activeMap.addLayer(new FLowOverlay());
	}

	private void hideFlow() {
	    MapPerProject activeMap = CodemapCore.getPlugin().getActiveMap();
	    activeMap.remove(CallOverlay.class);
	}

    @Override
    protected String getKey() {
        return "show_flow";
    }

    @Override
    protected boolean isDefaultChecked() {
        return false;
    }

}
