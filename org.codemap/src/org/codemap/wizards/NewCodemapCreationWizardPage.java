package org.codemap.wizards;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.internal.corext.util.Messages;
import org.eclipse.jdt.internal.ui.dialogs.StatusInfo;
import org.eclipse.jdt.internal.ui.filters.ClosedProjectFilter;
import org.eclipse.jdt.internal.ui.viewsupport.BasicElementLabels;
import org.eclipse.jdt.internal.ui.wizards.NewWizardMessages;
import org.eclipse.jdt.internal.ui.wizards.TypedElementSelectionValidator;
import org.eclipse.jdt.internal.ui.wizards.TypedViewerFilter;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.DialogField;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.IDialogFieldListener;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.IListAdapter;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.IStringButtonAdapter;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.ListDialogField;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.Separator;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.StringButtonDialogField;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.StringDialogField;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.eclipse.ui.dialogs.ISelectionStatusValidator;
import org.eclipse.ui.dialogs.ListSelectionDialog;
import org.eclipse.ui.model.WorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.eclipse.ui.model.WorkbenchViewerComparator;
import org.eclipse.ui.views.navigator.ResourceComparator;

import ch.akuhn.foreach.For;

@SuppressWarnings("restriction") // TODO
public class NewCodemapCreationWizardPage extends WizardPage implements IDialogFieldListener, IListAdapter, IStringButtonAdapter {

	private ListDialogField fFileExtensionsDialogField;
	private StringDialogField fMapNameDialogField;
	private ListDialogField fProjectListDialogField;
	private StringButtonDialogField fFolderNameDialogField;

	private IResource fCurrProject;

	protected NewCodemapCreationWizardPage(ISelection selection) {
		super("wizardPage");
		this.setTitle("Codemap");
		this.setDescription("Create a new codemap.");
		
		initializeDialogFields();
		initialize(selection);
	}

	private void initializeDialogFields() {
		fFolderNameDialogField= new StringButtonDialogField(this);
		fFolderNameDialogField.setDialogFieldListener(this);
		fFolderNameDialogField.setLabelText("Folder name:");
		fFolderNameDialogField.setButtonLabel("Browse...");
		
		fMapNameDialogField = new StringDialogField();
		fMapNameDialogField.setDialogFieldListener(this);
		fMapNameDialogField.setLabelText("Map name:");
		fMapNameDialogField.setText("default.map");
		
		fProjectListDialogField= new ListDialogField(this, 
				new String[] { "Add...", 	null, "Remove" },
				new WorkbenchLabelProvider());
		fProjectListDialogField.setDialogFieldListener(this);
		fProjectListDialogField.setLabelText("Projects:");
		fProjectListDialogField.setRemoveButtonIndex(2);

		fFileExtensionsDialogField= new ListDialogField(this, 
				new String[] { "Add...", 	null, "Remove" },
				new LabelProvider());
		fFileExtensionsDialogField.setDialogFieldListener(this);
		fFileExtensionsDialogField.setLabelText("File extensions:");
		fFileExtensionsDialogField.setRemoveButtonIndex(2);
	}
	@Override
	public void createControl(Composite parent) {
		initializeDialogUnits(parent);
		Composite composite= new Composite(parent, SWT.NONE);
		composite.setFont(parent.getFont());
		int nColumns= 3;
		composite.setLayout(new GridLayout(nColumns, false));

		// pick & choose the wanted UI components
		createMapNameControls(composite, nColumns);
		createFolderNameControls(composite, nColumns);
		createSeparator(composite, nColumns);
		createProjectListControls(composite, nColumns);
		createProjectListControls(composite, nColumns);
		createFileExtensionsControls(composite, nColumns);

		setControl(composite);
		Dialog.applyDialogFont(composite);
		// PlatformUI.getWorkbench().getHelpSystem().setHelp(composite, IJavaHelpContextIds.NEW_CLASS_WIZARD_PAGE);
	}
	
	private void createFolderNameControls(Composite composite, int nColumns) {
		fFolderNameDialogField.doFillIntoGrid(composite, nColumns);
	}
	
	@SuppressWarnings("unchecked")
	private void initialize(ISelection selection) {
		if (selection == null || selection.isEmpty()) return;
		if (!(selection instanceof StructuredSelection)) return;
		Collection projects = new HashSet();
		for (Object each: For.each(((StructuredSelection) selection).iterator())) {
			IResource resource = null;
			if (each instanceof IResource) resource = (IResource) each;
			else if (each instanceof IAdaptable) resource = (IResource) ((IAdaptable) each).getAdapter(IResource.class);
			if (resource != null) {
				projects.add(resource.getProject());
				if (fCurrProject == null) {
					fCurrProject = resource.getProject();
					if (resource.getType() == IResource.FILE) {
						fFolderNameDialogField.setText(resource.getParent().getFullPath().makeRelative().toString());
					} else {
						fFolderNameDialogField.setText(resource.getFullPath().makeRelative().toString());
					}
				}
			}
		}
		String fileExtension = "*.txt";
		for (Object each: projects) {
			if (JavaCore.create((IProject) each) != null) fileExtension = "*.java";
		}
		fFileExtensionsDialogField.addElement(fileExtension);
		for (Object each: projects) fProjectListDialogField.addElement(each);
		fCurrProject = (IProject) projects.iterator().next();
	}
	
	private void createFileExtensionsControls(Composite composite, int nColumns) {
		fFileExtensionsDialogField.doFillIntoGrid(composite, nColumns);
		GridData gd= (GridData) fFileExtensionsDialogField.getListControl(null).getLayoutData();
		gd.heightHint= convertHeightInCharsToPixels(4);
		gd.grabExcessVerticalSpace= false;
	}


	private void createMapNameControls(Composite composite, int nColumns) {
		fMapNameDialogField.doFillIntoGrid(composite, nColumns);
	}

	private void createProjectListControls(Composite composite, int nColumns) {
		fProjectListDialogField.doFillIntoGrid(composite, nColumns);
		GridData gd= (GridData) fProjectListDialogField.getListControl(null).getLayoutData();
		gd.heightHint= convertHeightInCharsToPixels(8);
		gd.grabExcessVerticalSpace= false;
	}

	protected void createSeparator(Composite composite, int nColumns) {
		(new Separator(SWT.SEPARATOR | SWT.HORIZONTAL)).doFillIntoGrid(composite, nColumns, convertHeightInCharsToPixels(1));
	}
	
	@Override
	public void customButtonPressed(ListDialogField field, int index) {
		if (field == fProjectListDialogField && index == 0) {
			chooseProjects();
		}
	}

	private void chooseProjects() {
		ListSelectionDialog dialog= new ListSelectionDialog(
				getShell(), 
				getNotYetRequiredProjects(),
				ArrayContentProvider.getInstance(),
				new WorkbenchLabelProvider(),
				"message");
		dialog.setTitle("title");
		dialog.setHelpAvailable(false);
		if (dialog.open() != Window.OK) return;
		for (Object each: dialog.getResult()) fProjectListDialogField.addElement(each);
	}

	@SuppressWarnings("unchecked")
	private Collection getNotYetRequiredProjects() {
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		Object[] projects = root.getProjects();
		new WorkbenchViewerComparator().sort(null, projects);
		projects = new ClosedProjectFilter().filter(null, (Object) null, projects);
		Collection result = new ArrayList(Arrays.asList(projects));
		result.removeAll(fProjectListDialogField.getElements());
		return result;
	}

	@Override
	public void dialogFieldChanged(DialogField field) {
		dialogChanged();
	}

	@Override
	public void doubleClicked(ListDialogField field) {
		// TODO Auto-generated method stub
	}

	@Override
	public void selectionChanged(ListDialogField field) {
		dialogChanged();
	}

	@Override
	public void changeControlPressed(DialogField field) {
		if (field == fFolderNameDialogField) {
			IPath initialPath= new Path(fFolderNameDialogField.getText());
			String title= "title";
			String message= "message";
			IFolder folder= chooseFolder(title, message, initialPath);
			if (folder != null) {
				IPath path= folder.getFullPath().makeRelative();
				fFolderNameDialogField.setText(path.toString());
			}
		}
		dialogChanged();
	}
	
	
	private boolean dialogChanged() {
		String folderName= getContainerPath();
		String filename= getFilename();

		if (folderName.isEmpty()) return updateStatus("Folder must be specified");
		
		IResource container = ResourcesPlugin.getWorkspace().getRoot().findMember(folderName);

		if (container == null) return updateStatus("Folder container must exist");
		if (container.getType() == IResource.ROOT) return updateStatus("Folder must not be the root");
		if (container.getType() == IResource.FILE) return updateStatus("Folder must not be a file");
		if (!container.isAccessible()) return updateStatus("Folder must be writable");

		if (filename.isEmpty()) return updateStatus("File must be specified");
		if (filename.indexOf('/') >= 0 || filename.indexOf('\\') >= 0) return updateStatus("File name must be valid");
		if (filename.indexOf('.') >= -1 && !filename.endsWith(".map")) return updateStatus("File extension must be \"map\"");

		return updateStatus(null);
	}

	@SuppressWarnings("unchecked")
	private IFolder chooseFolder(String title, String message, IPath initialPath) {
		Class[] acceptedClasses= new Class[] { IContainer.class };
		ViewerFilter filter= new TypedViewerFilter(acceptedClasses, null);

		ILabelProvider lp= new WorkbenchLabelProvider();
		ITreeContentProvider cp= new WorkbenchContentProvider();

		ElementTreeSelectionDialog dialog= new ElementTreeSelectionDialog(getShell(), lp, cp);
		dialog.setTitle(title);
		dialog.setMessage(message);
		dialog.addFilter(filter);
		dialog.setInput(ResourcesPlugin.getWorkspace().getRoot());
		dialog.setComparator(new ResourceComparator(ResourceComparator.NAME));
//		IResource res= currProject.findMember(initialPath);
//		if (res != null) {
//			dialog.setInitialSelection(res);
//		}

		if (dialog.open() == Window.OK) {
			return (IFolder) dialog.getFirstResult();
		}
		return null;
	}	
	
	private boolean updateStatus(String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
		return message == null; 
	}
	
	public String getFilename() {
		return fMapNameDialogField.getText();
	}
	
	public String getContainerPath() {
		return fFolderNameDialogField.getText();
	}
	
}
