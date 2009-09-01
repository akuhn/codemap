package org.codemap.ecftest;

import java.util.ArrayList;
import java.util.Collection;

import org.codemap.util.Resources;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IPathEditorInput;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartReference;

public class EditorPartListener implements IPartListener2 {

    private StringShare callback;

    public EditorPartListener(StringShare callback) {
        this.callback = callback;
    }

    @Override
    public void partActivated(IWorkbenchPartReference partRef) {
        // TODO Auto-generated method stub
        
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
        Collection<String> selection = new ArrayList<String>();
        for (IEditorReference each: part.getSite().getPage().getEditorReferences()) {
            //restore the editor parts to be able to access the content.
            for (IFile file: getFiles(each.getPart(true))) {
                selection.add(Resources.asPath(file));
            }
        }
        callback.selectionChanged(selection);
        System.out.println("selection changed" + selection);
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
