package org.codemap.plugin.communication.util;

import java.net.URL;

import org.codemap.plugin.communication.ECFPlugin;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.ui.plugin.AbstractUIPlugin;


public class Icons {

	private static final String DIR_PREFIX = "icons/";

	public static final String MEEPLE = DIR_PREFIX + "meeple.gif";

	public static ImageDescriptor getImageDescriptor(String key) {
		return loadImage(key).getDescriptor(key);
	}

	private static ImageRegistry loadImage(String path) {
		ImageRegistry reg = getActivator().getImageRegistry();
		if (reg.getDescriptor(path) == null) {
			URL url = getActivator().getBundle().getEntry(path);
			reg.put(path, ImageDescriptor.createFromURL(url));
		}
		return reg;
	}

	protected static AbstractUIPlugin getActivator() {
	    return ECFPlugin.getDefault();
	}

}
