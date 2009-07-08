package org.codemap.util;

import java.net.URL;

import org.codemap.CodemapCore;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;


public class Icon {

	public static final String LINKED = "icons/eclipse/linked.gif";
	public static final String CATEGORY = "icons/eclipse/category.gif";	

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

	protected static CodemapCore getActivator() {
		return CodemapCore.getPlugin();
	}

}
