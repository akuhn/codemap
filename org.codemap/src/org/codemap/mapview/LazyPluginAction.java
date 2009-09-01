package org.codemap.mapview;

import org.codemap.util.Log;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;


public class LazyPluginAction extends MenuAction {
	
	private static final String ATTR_TEXT = "text";
	private static final String ATTR_ICON = "icon";
	private static final String ATTR_CLASS = "class";		

	private IConfigurationElement configElement;
	private ICodemapPluginAction pluginAction;
	private String key;

	public LazyPluginAction(IConfigurationElement elem, int style) {
		super("", style); // lol, we can't set the style value some other way ...
		configElement = elem;
		initFromConfigElement();
	}

	private void initFromConfigElement() {
		setText(textFromConfig());
		setImageDescriptor(iconFromConfig());
	}

	private ImageDescriptor iconFromConfig() {
		String iconPath = configElement.getAttribute(ATTR_ICON);
		IExtension extension = configElement.getDeclaringExtension();
		key = extension.getNamespaceIdentifier();
		return AbstractUIPlugin.imageDescriptorFromPlugin(key, iconPath);
	}

	private String textFromConfig() {
		return configElement.getAttribute(ATTR_TEXT);
	}

	@Override
	public void run() {
		super.run();		
		ICodemapPluginAction action = getAction();
		if (action == null) return;
		
		action.run(this);
	}

	private ICodemapPluginAction getAction() {
		if (pluginAction == null){
			createPluginAction();
		}
		return pluginAction;
	}

	private void createPluginAction() {
		try {
			pluginAction = (ICodemapPluginAction) configElement.createExecutableExtension(ATTR_CLASS);
		} catch (Exception e) {
			Log.instantiatePluginError(e, configElement, ATTR_CLASS);
		}
	}

	@Override
	protected String getKey() {
		return key;
	}

	@Override
	protected boolean isDefaultChecked() {
		return false;
	}
}
