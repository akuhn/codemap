 /**
 * 
 */
package org.codemap.builder;

import org.codemap.util.Resources;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.runtime.CoreException;

import ch.akuhn.hapax.corpus.Terms;
import ch.akuhn.hapax.index.TermDocumentMatrix;

/**
 * Creates TDM from all ICompilationUnit resources in an IProject.
 * 
 */
class FullBuildVisitor implements IResourceVisitor {

	private TermDocumentMatrix TDM;

	public FullBuildVisitor() {
		TDM = new TermDocumentMatrix();
	}

	public boolean visit(IResource resource) throws CoreException {
		if (!(resource instanceof IFile)) return true;
		String extension = resource.getFullPath().getFileExtension();
		if (extension == null || !extension.equals("java")) return true;
		return addDocument((IFile) resource);
	}

	private boolean addDocument(IFile file) throws CoreException {
		Terms terms = new Terms(file.getContents());
		String path = Resources.asPath(file);
		TDM.putDocument(path, terms);
		return false;
	}
	
	public TermDocumentMatrix getResult() {
		return TDM;
	}

}