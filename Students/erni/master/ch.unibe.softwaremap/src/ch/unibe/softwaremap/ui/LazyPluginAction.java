package ch.unibe.softwaremap.ui;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;

public class LazyPluginAction extends Action {
	
	private static final String ATT_TEXT = "text";
	private static final String ATT_ICON = "icon";	

	private IConfigurationElement configElement;

	public LazyPluginAction(IConfigurationElement elem) {
		super("", AS_CHECK_BOX); // lol, we can't set the style value some other way ...
		configElement = elem;
		initFromConfigElement();
	}

	private void initFromConfigElement() {
		setText(textFromConfig());
		setImageDescriptor(iconFromConfig());
	}

	private ImageDescriptor iconFromConfig() {
		String iconPath = configElement.getAttribute(ATT_ICON);
		IExtension extension = configElement.getDeclaringExtension();
		String pluginId = extension.getNamespaceIdentifier();
		return AbstractUIPlugin.imageDescriptorFromPlugin(pluginId, iconPath);
	}

	private String textFromConfig() {
		return configElement.getAttribute(ATT_TEXT);
	}
	
	

}
