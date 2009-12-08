package org.codemap.util;

import org.eclipse.core.resources.IFile;
import org.eclipse.jdt.ui.IContextMenuConstants;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.navigator.ICommonMenuConstants;

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
    
    /**
     * Creates the Java plug-in's standard groups for view context menus.
     *
     * @param menu the menu manager to be populated
     */
    public static void createStandardGroups(IMenuManager menu) {
        if (!menu.isEmpty())
            return;

        menu.add(new Separator(IContextMenuConstants.GROUP_NEW));
        menu.add(new GroupMarker(IContextMenuConstants.GROUP_GOTO));
        menu.add(new Separator(IContextMenuConstants.GROUP_OPEN));
        menu.add(new GroupMarker(IContextMenuConstants.GROUP_SHOW));
        menu.add(new Separator(ICommonMenuConstants.GROUP_EDIT));
        menu.add(new Separator(IContextMenuConstants.GROUP_REORGANIZE));
        menu.add(new Separator(IContextMenuConstants.GROUP_GENERATE));
        menu.add(new Separator(IContextMenuConstants.GROUP_SEARCH));
        menu.add(new Separator(IContextMenuConstants.GROUP_BUILD));
        menu.add(new Separator(IContextMenuConstants.GROUP_ADDITIONS));
        menu.add(new Separator(IContextMenuConstants.GROUP_VIEWER_SETUP));
        menu.add(new Separator(IContextMenuConstants.GROUP_PROPERTIES));
    }
}
