package org.codemap.wizards;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IAdaptable;
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
public class NNewCodemapCreationWizardPage extends WizardPage implements IDialogFieldListener, IListAdapter {

	private ListDialogField fFileExtensionsDialogField;

	private StringDialogField fMapNameDialogField;

	private ListDialogField fProjectListDialogField;

	protected NNewCodemapCreationWizardPage(ISelection selection) {
		super("wizardPage");
		this.setTitle("Codemap");
		this.setDescription("Create a new codemap.");

		fMapNameDialogField = new StringDialogField();
		fMapNameDialogField.setDialogFieldListener(this);
		fMapNameDialogField.setLabelText("Map name:");
		
		fProjectListDialogField= new ListDialogField(this, new String[] { "Add...", 	null, "Remove" },
				new WorkbenchLabelProvider());
		fProjectListDialogField.setDialogFieldListener(this);
		//fProjectListDialogField.setTableColumns(new ListDialogField.ColumnsDescription(1, false));
		fProjectListDialogField.setLabelText("Projects:");
		fProjectListDialogField.setRemoveButtonIndex(2);

		fFileExtensionsDialogField= new ListDialogField(this, new String[] { "Add...", 	null, "Remove" },
				new FileExtensionsLabelProvider());
		fFileExtensionsDialogField.setDialogFieldListener(this);
		//fFileExtensionsDialogField.setTableColumns(new ListDialogField.ColumnsDescription(1, false));
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
		for (Object each: projects) fProjectListDialogField.addElement(each);
	}
	
	private void createFileExtensionsControls(Composite composite, int nColumns) {
		fFileExtensionsDialogField.doFillIntoGrid(composite, nColumns);
		final TableViewer tableViewer= fFileExtensionsDialogField.getTableViewer();
		GridData gd= (GridData) fFileExtensionsDialogField.getListControl(null).getLayoutData();
		gd.heightHint= convertHeightInCharsToPixels(4);
		gd.grabExcessVerticalSpace= false;
//		gd.widthHint= convertWidthInCharsToPixels(40);
	}


	private void createMapNameControls(Composite composite, int nColumns) {
		fMapNameDialogField.doFillIntoGrid(composite, nColumns);
		Text text= fMapNameDialogField.getTextControl(null);
//		LayoutUtil.setWidthHint(text, convertWidthInCharsToPixels(40));
	}

	private void createProjectListControls(Composite composite, int nColumns) {
		fProjectListDialogField.doFillIntoGrid(composite, nColumns);
		final TableViewer tableViewer= fProjectListDialogField.getTableViewer();
		GridData gd= (GridData) fProjectListDialogField.getListControl(null).getLayoutData();
		gd.heightHint= convertHeightInCharsToPixels(8);
		gd.grabExcessVerticalSpace= false;
//		gd.widthHint= convertWidthInCharsToPixels(40);
	}

	protected void createSeparator(Composite composite, int nColumns) {
		(new Separator(SWT.SEPARATOR | SWT.HORIZONTAL)).doFillIntoGrid(composite, nColumns, convertHeightInCharsToPixels(1));
	}
	
	@Override
	public void customButtonPressed(ListDialogField field, int index) {
		if (field == fProjectListDialogField && index == 0) {
			chooseProjects();
//			List interfaces= fSuperInterfacesDialogField.getElements();
//			if (!interfaces.isEmpty()) {
//				Object element= interfaces.get(interfaces.size() - 1);
//				fSuperInterfacesDialogField.editElement(element);
//			}
		}
	}

	private void chooseProjects() {
		Object[] selectArr= getNotYetRequiredProjects();
		ListSelectionDialog dialog= new ListSelectionDialog(
				getShell(), 
				Arrays.asList(selectArr),
				new ArrayContentProvider(),
				new WorkbenchLabelProvider(),
				"message");
		dialog.setTitle("title");
		dialog.setHelpAvailable(false);
		if (dialog.open() == Window.OK) {
//		
//			Object[] result= dialog.getResult();
//				CPListElement[] cpElements= new CPListElement[result.length];
//				for (int i= 0; i < result.length; i++) {
//					IJavaProject curr= (IJavaProject) result[i];
//					cpElements[i]= new CPListElement(fCurrJProject, IClasspathEntry.CPE_PROJECT, curr.getPath(), curr.getResource());
//				}
//				return cpElements;
		}
	}

	private Object[] getNotYetRequiredProjects() {
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		Object[] projects = root.getProjects();
		new WorkbenchViewerComparator().sort(null, projects);
		projects = new ClosedProjectFilter().filter(null, (Object) null, projects);
		return projects;
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

	public class FileExtensionsLabelProvider implements ILabelProvider {

		@Override
		public void addListener(ILabelProviderListener listener) {
			// TODO Auto-generated method stub

		}

		@Override
		public void dispose() {
			// TODO Auto-generated method stub

		}

		@Override
		public Image getImage(Object element) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String getText(Object element) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean isLabelProperty(Object element, String property) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void removeListener(ILabelProviderListener listener) {
			// TODO Auto-generated method stub

		}

	}

	public class ProjectListLabelProvider implements ILabelProvider {

		@Override
		public void addListener(ILabelProviderListener listener) {
			// TODO Auto-generated method stub

		}

		@Override
		public void dispose() {
			// TODO Auto-generated method stub

		}

		@Override
		public Image getImage(Object element) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String getText(Object element) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean isLabelProperty(Object element, String property) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void removeListener(ILabelProviderListener listener) {
			// TODO Auto-generated method stub

		}

	}

}
