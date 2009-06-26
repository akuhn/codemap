package ch.unibe.softwaremap.mapview;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import ch.unibe.softwaremap.util.Log;

public class LazyPluginAction extends Action {
	
	private static final String ATT_TEXT = "text";
	private static final String ATT_ICON = "icon";
	private static final String ATT_CLASS = "class";		

	private IConfigurationElement configElement;
	private ICodemapPluginAction pluginAction;

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

	@Override
	public void run() {
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
			pluginAction = (ICodemapPluginAction) configElement.createExecutableExtension(ATT_CLASS);
		} catch (Exception e) {
			Log.instantiatePluginError(e, configElement, ATT_CLASS);
		}
	}
}
