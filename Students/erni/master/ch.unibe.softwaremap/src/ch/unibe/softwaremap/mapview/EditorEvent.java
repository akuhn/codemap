package ch.unibe.softwaremap.mapview;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.PartInitException;

public class EditorEvent {

	private IJavaElement input;

	public EditorEvent(Object editorReference) {
		if (!(editorReference instanceof IEditorReference)) return;
		
		try {
			IEditorInput input = ((IEditorReference)editorReference).getEditorInput();
			editorActivated(input);			
		} catch (PartInitException e) {
			// ignore
		}
	}

	public void editorActivated(IEditorInput editorInput) {
		if (editorInput == null) return;
		getInputFromEditor(editorInput);
	}

	private void getInputFromEditor(IEditorInput editorInput) {
		IJavaElement javaElement = JavaUI.getEditorInputJavaElement(editorInput);
		if (javaElement == null) return;
		if (!(javaElement instanceof ICompilationUnit)) return;

		input = javaElement;
	}

	public boolean hasInput() {
		return input != null;
	}

	public IJavaElement getInput() {
		return input;
	}

}
