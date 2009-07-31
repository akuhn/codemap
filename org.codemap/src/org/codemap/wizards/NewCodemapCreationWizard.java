package org.codemap.wizards;

import org.eclipse.core.internal.resources.Workspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.content.IContentType;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IFileEditorMapping;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.internal.WorkbenchPlugin;


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
		
		IContentType[] allContentTypes = Platform.getContentTypeManager().getAllContentTypes();
		IFileEditorMapping[] fileEditorMappings = WorkbenchPlugin.getDefault().getEditorRegistry().getFileEditorMappings();
		
		return true;
	}
	
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.fSelection = selection;
	}
}