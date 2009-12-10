package org.codemap.mapview;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.codemap.MapSelection;
import org.codemap.util.Resources;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IPathEditorInput;
import org.eclipse.ui.IWorkbenchPart;


public class EditorPartListener implements IPartListener {

	private MapSelection editorSelection;
	private MapSelection youAreHereSelection;
    private OpenFilesTrace trace;

	public EditorPartListener(MapSelection editorSelection, MapSelection youAreHereSelection, MapController theController) {
		this.editorSelection = editorSelection;
		this.youAreHereSelection = youAreHereSelection;
		trace = new OpenFilesTrace(theController);
	}

	@Override
	public void partActivated(IWorkbenchPart part) {
		// not related to our interests
	}

	@Override
	public void partBroughtToTop(IWorkbenchPart part) {
		updateEditorSelection(part);
		updateYouAreHereSelection(part);
	}

	@Override
	public void partClosed(IWorkbenchPart part) {
		updateEditorSelection(part);
	}

	@Override
	public void partDeactivated(IWorkbenchPart part) {
		// not related to our interests
	}

	@Override
	public void partOpened(IWorkbenchPart part) {
		updateEditorSelection(part);
	}

	private void updateEditorSelection(IWorkbenchPart part) {
		if (!(part instanceof IEditorPart)) return;
		Collection<String> selection = new ArrayList<String>();
		for (IEditorReference each: part.getSite().getPage().getEditorReferences()) {
		    //restore the editor parts to be able to access the content.
            for (IFile file: getFiles(each.getPart(true))) {
                selection.add(Resources.asPath(file));
            }
		}
		editorSelection.replaceAll(selection);
		if (selection.isEmpty()) youAreHereSelection.clear();
	}
	
	@SuppressWarnings("deprecation")
	private IFile[] getFiles(IWorkbenchPart part) {
		if (!(part instanceof IEditorPart)) return new IFile[] {};
		IEditorInput input = ((IEditorPart) part).getEditorInput();
		if (!(input instanceof IPathEditorInput)) return new IFile[] {};
		IPathEditorInput pathInput = (IPathEditorInput) input;
//		System.out.println(pathInput.getPath());
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		return workspace.getRoot().findFilesForLocation(pathInput.getPath());
	}

	private void updateYouAreHereSelection(IWorkbenchPart part) {
		List<String> selection = new ArrayList<String>();
		for (IFile file: getFiles(part)) {
			selection.add(Resources.asPath(file));
		}
		youAreHereSelection.replaceAll(selection);
		trace.append(selection);
	}

    public OpenFilesTrace getTrace() {
        return trace;
    }

}
