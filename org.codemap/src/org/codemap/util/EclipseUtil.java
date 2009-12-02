package org.codemap.util;

import org.eclipse.swt.widgets.Display;

public class EclipseUtil {

	/** Displays save dialog and asks user for filename.
	 * 
	 */
	public static String filenameFromUser(String fname, final String suffix) {
		IFileNameCallback callback = new IFileNameCallback() {
			@Override
			public String checkFileName(String path) {
				if (!path.endsWith(suffix)) path += suffix;
				return path;
			}
		};
        SafeSaveDialog dialog = new SafeSaveDialog(Display.getDefault().getActiveShell(), callback);
        dialog.setFileName(callback.checkFileName(fname));
        String[] filterExt = { "*" + suffix };
        dialog.setFilterExtensions(filterExt);
        String userHome = System.getProperty("user.home");
        if (userHome != null) dialog.setFilterPath(userHome);
        return dialog.open();
	}
	
	
}
