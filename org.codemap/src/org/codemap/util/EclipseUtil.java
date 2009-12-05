package org.codemap.util;

import org.eclipse.core.resources.IFile;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

public class EclipseUtil {

	/** 
	 * Displays save dialog and asks user for filename.
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
	
	/**
	 * Returns the active IWorkbenchPage 
	 */
	public static IWorkbenchPage getActivePage() {
        return PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();	    
	}
	
	/**
	 * Opens the given file in an editor
	 */
    public static void openInEditor(final IFile file) {
        Display.getDefault().asyncExec(new Runnable() {
            @Override
            public void run() {
                try {
                    IDE.openEditor(getActivePage(), file, true);
                } catch (PartInitException e) {
                    Log.error(e);
                }
            }
        });
    }	
}
