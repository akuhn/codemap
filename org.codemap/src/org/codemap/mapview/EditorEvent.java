package org.codemap.mapview;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.PartInitException;

public class EditorEvent {

	private IJavaElement javaElement;

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
		IJavaElement newJavaElement = JavaUI.getEditorInputJavaElement(editorInput);
		if (newJavaElement == null) return;
		if (!(newJavaElement instanceof ICompilationUnit)) return;
		javaElement = newJavaElement;
	}

	public boolean hasInput() {
		return javaElement != null;
	}

	public IJavaElement getInput() {
		return javaElement;
	}

}
