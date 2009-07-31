package org.codemap.wizards;

import org.eclipse.jdt.internal.ui.wizards.dialogfields.DialogField;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.IDialogFieldListener;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.IListAdapter;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.LayoutUtil;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.ListDialogField;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.Separator;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.StringDialogField;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

public class NNewCodemapCreationWizardPage extends WizardPage {

	public class FileExtensionsLabelProvider implements ILabelProvider {

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
		public void addListener(ILabelProviderListener listener) {
			// TODO Auto-generated method stub

		}

		@Override
		public void dispose() {
			// TODO Auto-generated method stub

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
		public void addListener(ILabelProviderListener listener) {
			// TODO Auto-generated method stub

		}

		@Override
		public void dispose() {
			// TODO Auto-generated method stub

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

	public class FieldAdapter implements IDialogFieldListener, IListAdapter {

		@Override
		public void dialogFieldChanged(DialogField field) {
			// TODO Auto-generated method stub

		}

		@Override
		public void customButtonPressed(ListDialogField field, int index) {
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

	private StringDialogField fMapNameDialogField;
	private ListDialogField fProjectListDialogField;
	private ListDialogField fFileExtensionsDialogField;


	protected NNewCodemapCreationWizardPage(ISelection selection) {
		super("wizardPage");
		this.setTitle("Codemap");
		this.setDescription("Create a new codemap.");

		FieldAdapter adapter = new FieldAdapter();
		
		fMapNameDialogField = new StringDialogField();
		fMapNameDialogField.setDialogFieldListener(adapter);
		fMapNameDialogField.setLabelText("Map name:");
		
		fProjectListDialogField= new ListDialogField(adapter, new String[] { "Add...", 	null, "Remove" }, new ProjectListLabelProvider());
		fProjectListDialogField.setDialogFieldListener(adapter);
		//fProjectListDialogField.setTableColumns(new ListDialogField.ColumnsDescription(1, false));
		fProjectListDialogField.setLabelText("Projects:");
		fProjectListDialogField.setRemoveButtonIndex(2);

		fFileExtensionsDialogField= new ListDialogField(adapter, new String[] { "Add...", 	null, "Remove" }, new FileExtensionsLabelProvider());
		fFileExtensionsDialogField.setDialogFieldListener(adapter);
		//fFileExtensionsDialogField.setTableColumns(new ListDialogField.ColumnsDescription(1, false));
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
		createSeparator(composite, nColumns);
		createProjectListControls(composite, nColumns);
		createProjectListControls(composite, nColumns);
		createFileExtensionsControls(composite, nColumns);

		setControl(composite);
		Dialog.applyDialogFont(composite);
		// PlatformUI.getWorkbench().getHelpSystem().setHelp(composite, IJavaHelpContextIds.NEW_CLASS_WIZARD_PAGE);
	}

	protected void createSeparator(Composite composite, int nColumns) {
		(new Separator(SWT.SEPARATOR | SWT.HORIZONTAL)).doFillIntoGrid(composite, nColumns, convertHeightInCharsToPixels(1));
	}
	
	private void createFileExtensionsControls(Composite composite, int nColumns) {
		fFileExtensionsDialogField.doFillIntoGrid(composite, nColumns);
		final TableViewer tableViewer= fFileExtensionsDialogField.getTableViewer();
		GridData gd= (GridData) fFileExtensionsDialogField.getListControl(null).getLayoutData();
		gd.heightHint= convertHeightInCharsToPixels(4);
		gd.grabExcessVerticalSpace= false;
//		gd.widthHint= convertWidthInCharsToPixels(40);
	}

	private void createProjectListControls(Composite composite, int nColumns) {
		fProjectListDialogField.doFillIntoGrid(composite, nColumns);
		final TableViewer tableViewer= fProjectListDialogField.getTableViewer();
		GridData gd= (GridData) fProjectListDialogField.getListControl(null).getLayoutData();
		gd.heightHint= convertHeightInCharsToPixels(8);
		gd.grabExcessVerticalSpace= false;
//		gd.widthHint= convertWidthInCharsToPixels(40);
	}

	private void createMapNameControls(Composite composite, int nColumns) {
		fMapNameDialogField.doFillIntoGrid(composite, nColumns);
		Text text= fMapNameDialogField.getTextControl(null);
//		LayoutUtil.setWidthHint(text, convertWidthInCharsToPixels(40));
	}

}
