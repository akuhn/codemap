package org.codemap.mapview;

import java.util.Arrays;
import java.util.List;

import org.codemap.CodemapCore;
import org.codemap.util.Icons;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.widgets.Menu;

public class LayerDropDownAction extends DropDownAction {
	
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
	protected void createMenu(Menu menu) {
		addActionToMenu(menu, new LinkWithSelectionAction(selectionTracker));
		addActionToMenu(menu, new ForceSelectionAction(selectionProvider));
	    
	    IExtensionPoint extensionPoint = Platform.getExtensionRegistry().getExtensionPoint(CodemapCore.PLUGIN_ID, "mapview");
	    IExtension[] extensions_arr = extensionPoint.getExtensions();
	    List<IExtension> extensions = Arrays.asList(extensions_arr);
	    for (IExtension extension: extensions) {
	    	parseConfig(extension.getConfigurationElements(), menu);
	    }
	}
	
	private void parseConfig(IConfigurationElement[] configurationElements, Menu menu) {
		List<IConfigurationElement> configelems = Arrays.asList(configurationElements);
		for (IConfigurationElement each: configelems) {
			addActionToMenu(menu, new LazyPluginAction(each));
		}
	}	

}
