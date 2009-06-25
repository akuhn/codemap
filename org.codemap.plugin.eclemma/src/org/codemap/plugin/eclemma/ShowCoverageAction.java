package org.codemap.plugin.eclemma;

import org.eclipse.jface.action.Action;
import ch.unibe.softwaremap.Icon;
import ch.unibe.softwaremap.SoftwareMap;

public class ShowCoverageAction extends Action {
	
	public ShowCoverageAction() {
		super("Show Coverage.", AS_CHECK_BOX);
		setImageDescriptor(Icon.getImageDescriptor(Icon.CATEGORY));		
	}

	@Override
	public void run() {
		SoftwareMap.core().updateMap();
	}

	

}
