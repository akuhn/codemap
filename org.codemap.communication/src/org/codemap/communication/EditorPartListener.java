package org.codemap.communication;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import org.codemap.util.Resources;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IPathEditorInput;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.PlatformUI;

public class EditorPartListener implements IPartListener2 {

    private StringShare callback;
    private Collection<String> currentSelection;

    public EditorPartListener(StringShare callback) {
        this.callback = callback;
        this.currentSelection = Collections.emptySet();
        findInitialSelection();
    }

    private void findInitialSelection() {
    	Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				IEditorReference[] references = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getEditorReferences();
				updateEditorSelection(references);
			}
		});
    }

    @Override
    public void partActivated(IWorkbenchPartReference partRef) {
        // we don't care about this event 
    }

    @Override
    public void partBroughtToTop(IWorkbenchPartReference partRef) {
        updateEditorSelection(partRef);
    }

    @Override
    public void partClosed(IWorkbenchPartReference partRef) {
        updateEditorSelection(partRef);
    }

    @Override
    public void partDeactivated(IWorkbenchPartReference partRef) {
        updateEditorSelection(partRef);        
    }

    @Override
    public void partHidden(IWorkbenchPartReference partRef) {
        updateEditorSelection(partRef);        
    }

    @Override
    public void partInputChanged(IWorkbenchPartReference partRef) {
        updateEditorSelection(partRef);        
    }

    @Override
    public void partOpened(IWorkbenchPartReference partRef) {
        updateEditorSelection(partRef);        
    }

    @Override
    public void partVisible(IWorkbenchPartReference partRef) {
        updateEditorSelection(partRef);        
    }

    private void updateEditorSelection(IWorkbenchPartReference partRef) {
        IWorkbenchPart part = partRef.getPart(true);
        if (!(part instanceof IEditorPart)) return;
        updateEditorSelection(part.getSite().getPage().getEditorReferences());
    }

    private void updateEditorSelection(IEditorReference[] iEditorReferences) {
        Collection<String> newSelection = new HashSet<String>();
        for (IEditorReference each: iEditorReferences) {
            //restore the editor parts to be able to access the content.
            for (IFile file: getFiles(each.getPart(true))) {
                newSelection.add(Resources.asPath(file));
            }
        }
        checkUpdate(newSelection);
    }
    
    private void checkUpdate(Collection<String> newSelection) {
        if (currentSelection.size() == newSelection.size() &&
                currentSelection.containsAll(newSelection)) return;
        
        currentSelection = newSelection;
        callback.selectionChanged(currentSelection);
        System.out.println("triggering update" + newSelection);
    }

    @SuppressWarnings("deprecation")
    private IFile[] getFiles(IWorkbenchPart each) {
        if (!(each instanceof IEditorPart)) return new IFile[] {};
        IEditorInput input = ((IEditorPart) each).getEditorInput();
        if (!(input instanceof IPathEditorInput)) return new IFile[] {};
        IPathEditorInput pathInput = (IPathEditorInput) input;
//      System.out.println(pathInput.getPath());
        IWorkspace workspace = ResourcesPlugin.getWorkspace();
        return workspace.getRoot().findFilesForLocation(pathInput.getPath());
    }    
}
