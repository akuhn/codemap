package ch.unibe.softwaremap.ui;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IMarkSelection;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.text.TextViewer;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.eclipse.ui.part.PageBook;
import org.eclipse.ui.part.ViewPart;

import ch.unibe.softwaremap.Log;
import ch.unibe.softwaremap.SoftwareMapCore;

/**
 * This view simply mirrors the current selection in the workbench window. It works for both, element and text
 * selection.
 */
public class SelectionView extends ViewPart {

	public static final String SELECTION_VIEW_ID = SoftwareMapCore.makeID(SelectionView.class);
	public static final String PACKAGE_EXPLORER_ID = "org.eclipse.jdt.ui.PackageExplorer";

	private PageBook pagebook;
	private TableViewer tableviewer;
	private TextViewer textviewer;

	// the listener we register with the selection service
	private ISelectionListener listener = new ISelectionListener() {
		public void selectionChanged(IWorkbenchPart sourcepart, ISelection selection) {
			// we ignore our own selections
			if (sourcepart != SelectionView.this) {
				showSelection(sourcepart, selection);
			}
		}
	};

	/**
	 * Shows the given selection in this view.
	 */
	public void showSelection(IWorkbenchPart sourcepart, ISelection selection) {
		setContentDescription(sourcepart.getTitle() + " (" + selection.getClass().getName() + ")");
		if (selection instanceof IStructuredSelection) {
			IStructuredSelection ss = (IStructuredSelection) selection;
			showItems(ss.toArray());
		}
		if (selection instanceof ITextSelection) {
			ITextSelection ts = (ITextSelection) selection;
			showText(ts.getText());
		}
		if (selection instanceof IMarkSelection) {
			IMarkSelection ms = (IMarkSelection) selection;
			try {
				showText(ms.getDocument().get(ms.getOffset(), ms.getLength()));
			} catch (BadLocationException e) {
				Log.error(e);
			}
		}
	}

	private void showItems(Object[] items) {
		tableviewer.setInput(items);
		pagebook.showPage(tableviewer.getControl());
	}

	private void showText(String text) {
		textviewer.setDocument(new Document(text));
		pagebook.showPage(textviewer.getControl());
	}

	@Override
	public void createPartControl(Composite parent) {
		// the PageBook allows simple switching between two viewers
		pagebook = new PageBook(parent, SWT.NONE);

		tableviewer = new TableViewer(pagebook, SWT.NONE);
		tableviewer.setLabelProvider(new WorkbenchLabelProvider());
		tableviewer.setContentProvider(new ArrayContentProvider());
		tableviewer.setSelection(null);

		// we're cooperative and also provide our selection
		// at least for the tableviewer
		getSite().setSelectionProvider(tableviewer);

		textviewer = new TextViewer(pagebook, SWT.H_SCROLL | SWT.V_SCROLL);
		textviewer.setEditable(false);

		getSite().getWorkbenchWindow().getSelectionService().addSelectionListener(PACKAGE_EXPLORER_ID, listener);
	}

	@Override
	public void setFocus() {
		pagebook.setFocus();
	}

	@Override
	public void dispose() {
		getSite().getWorkbenchWindow().getSelectionService().removeSelectionListener(PACKAGE_EXPLORER_ID, listener);
		super.dispose();
	}

}
