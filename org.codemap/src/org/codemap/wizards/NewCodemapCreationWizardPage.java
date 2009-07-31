package org.codemap.wizards;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.internal.ui.filters.ClosedProjectFilter;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.DialogField;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.IDialogFieldListener;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.IListAdapter;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.ListDialogField;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.Separator;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.StringDialogField;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ListSelectionDialog;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.eclipse.ui.model.WorkbenchViewerComparator;

import ch.akuhn.foreach.Each;
import ch.akuhn.foreach.For;

@SuppressWarnings("restriction") // TODO
public class NewCodemapCreationWizardPage extends WizardPage implements IDialogFieldListener, IListAdapter {

	private ListDialogField fFileExtensionsDialogField;

	private StringDialogField fMapNameDialogField;

	private ListDialogField fProjectListDialogField;

	protected NewCodemapCreationWizardPage(ISelection selection) {
		super("wizardPage");
		this.setTitle("Codemap");
		this.setDescription("Create a new codemap.");

		fMapNameDialogField = new StringDialogField();
		fMapNameDialogField.setDialogFieldListener(this);
		fMapNameDialogField.setLabelText("Map name:");
		fMapNameDialogField.setText("default.map");
		
		fProjectListDialogField= new ListDialogField(this, new String[] { "Add...", 	null, "Remove" },
				new WorkbenchLabelProvider());
		fProjectListDialogField.setDialogFieldListener(this);
		fProjectListDialogField.setLabelText("Projects:");
		fProjectListDialogField.setRemoveButtonIndex(2);

		fFileExtensionsDialogField= new ListDialogField(this, new String[] { "Add...", 	null, "Remove" },
				new LabelProvider());
		fFileExtensionsDialogField.setDialogFieldListener(this);
		fFileExtensionsDialogField.setLabelText("File extensions:");
		fFileExtensionsDialogField.setRemoveButtonIndex(2);
		initialize(selection);
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
		createSeparator(composite, nColumns);
		createProjectListControls(composite, nColumns);
		createProjectListControls(composite, nColumns);
		createFileExtensionsControls(composite, nColumns);

		setControl(composite);
		Dialog.applyDialogFont(composite);
		// PlatformUI.getWorkbench().getHelpSystem().setHelp(composite, IJavaHelpContextIds.NEW_CLASS_WIZARD_PAGE);
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
			if (resource != null) projects.add(resource.getProject());
		}
		String fileExtension = "*.txt";
		for (Object each: projects) {
			if (JavaCore.create((IProject) each) != null) fileExtension = "*.java";
		}
		fFileExtensionsDialogField.addElement(fileExtension);
		for (Object each: projects) fProjectListDialogField.addElement(each);
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
		// TODO Auto-generated method stub

	}

	@Override
	public void doubleClicked(ListDialogField field) {
		// TODO Auto-generated method stub

	}

	@Override
	public void selectionChanged(ListDialogField field) {
		// TODO Auto-generated method stub

	}

}
