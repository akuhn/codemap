package org.codemap.mapview;

import java.util.Arrays;
import java.util.List;

import org.codemap.CodemapCore;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.widgets.Menu;


public abstract class ExtensionPointDropDownAction extends DropDownAction {
	
	@Override
	protected void createMenu(Menu menu) {
		createDefaultMenu(menu);
		createExtensionMenu(menu);
	}
	
	protected void createDefaultMenu(Menu menu) {
		// add stuff if needed
	};
	
	protected void createExtensionMenu(Menu menu) {
		IExtensionPoint extensionPoint = Platform.getExtensionRegistry().getExtensionPoint(CodemapCore.PLUGIN_ID, getExtensionPointName());
		IExtension[] extensions_arr = extensionPoint.getExtensions();
		List<IExtension> extensions = Arrays.asList(extensions_arr);
		for (IExtension extension: extensions) {
			parseConfig(extension.getConfigurationElements(), menu);
		}
	}

	protected abstract String getExtensionPointName();
	
	private void parseConfig(IConfigurationElement[] configurationElements, Menu menu) {
		List<IConfigurationElement> configelems = Arrays.asList(configurationElements);
		for (IConfigurationElement each: configelems) {
			addActionToMenu(menu, new LazyPluginAction(each, getActionStyle()));
		}
	}

	protected abstract int getActionStyle();
}
