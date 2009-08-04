package org.codemap.util;

import java.net.URL;

import org.codemap.CodemapCore;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;


public class Icons {

	private static final String DIR_PREFIX = "icons/eclipse/";
	
	public static final String LINKED = DIR_PREFIX + "linked.gif";
	public static final String LAYERS = DIR_PREFIX + "layers.gif";	
	public static final String LABELS = DIR_PREFIX + "labels.gif";
	public static final String FORCE_SELECTION = DIR_PREFIX + "force_selection.gif";
	public static final String PACKAGES = DIR_PREFIX + "packages.gif";	
	public static final String JAVA_FILE = DIR_PREFIX + "java_file.gif";
	public static final String PALETTE = DIR_PREFIX + "palette.gif";		
	public static final String GREEN_CIRCLE = DIR_PREFIX + "green_circle.gif";	

	public static ImageDescriptor getImageDescriptor(String key) {
		return loadImage(key).getDescriptor(key);
	}
	
	public static Image getImage(String key) {
		return getImageDescriptor(key).createImage();
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
