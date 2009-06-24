package ch.unibe.softwaremap;

import java.net.URL;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;

public class Icon {

	public static final String LINKED = "icons/eclipse/linked.gif";

	public static ImageDescriptor getImageDescriptor(String key) {
		return loadImage(key).getDescriptor(key);
	}

	private static ImageRegistry loadImage(String path) {
		ImageRegistry reg = SoftwareMap.core().getImageRegistry();
		if (reg.getDescriptor(path) == null) {
			URL url = SoftwareMap.core().getBundle().getEntry(path);
			reg.put(path, ImageDescriptor.createFromURL(url));
		}
		return reg;
	}

}
