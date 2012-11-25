package org.codemap.wizards;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;


public class NewCodemapCreationWizard extends Wizard implements INewWizard {

	private NewCodemapCreationWizardPage page;
	private IStructuredSelection fSelection;


	@Override
	public void addPages() {
		page = new NewCodemapCreationWizardPage(fSelection);
		addPage(page);
	}

	@Override
	public boolean performFinish() {
		String containerName = page.getContainerPath();
		String fileName = page.getFilename();
		
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IResource resource = root.findMember(new Path(containerName));
		IContainer container = (IContainer) resource;
		final IFile file = container.getFile(new Path(fileName));
		try {
			InputStream stream = openContentStream();
			if (file.exists()) {
				file.setContents(stream, true, true, null);
			} else {
				file.create(stream, true, null);
			}
			stream.close();
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
		getShell().getDisplay().asyncExec(new Runnable() {
			public void run() {
				try {
					IDE.openEditor(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage(), file, true);
				} catch (PartInitException ex) {
					throw new RuntimeException(ex);
				}
			}
		});
		
//		IContentType[] allContentTypes = Platform.getContentTypeManager().getAllContentTypes();
//		IFileEditorMapping[] fileEditorMappings = WorkbenchPlugin.getDefault().getEditorRegistry().getFileEditorMappings();
		
		return true;
	}

	private InputStream openContentStream() {
		String contents =
			"All your bytes are belong to us.";
		return new ByteArrayInputStream(contents.getBytes());
	}
	
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.fSelection = selection;
	}
}