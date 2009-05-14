/**
 * 
 */
package ch.unibe.softwaremap.builder;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IBuffer;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;

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
		IJavaElement javaElement = JavaCore.create(resource);
		if (javaElement == null) return true;
		if (javaElement.getElementType() != IJavaElement.COMPILATION_UNIT) return true;
		ICompilationUnit compilationUnit = (ICompilationUnit) javaElement.getAdapter(ICompilationUnit.class);
		return visit(compilationUnit);
	}

	private boolean visit(ICompilationUnit compilationUnit) throws JavaModelException {
		IBuffer buf = compilationUnit.getBuffer();
		String contents = buf.getContents();
		String name = this.shortenCompilationUnitName(compilationUnit);
		TDM.makeDocument(name).addTerms(new Terms(contents));
		return false;
	}
	
	private String shortenCompilationUnitName(ICompilationUnit compilationUnit) {
		// shorten the fullname which looks like: =Fame/test<ch.akuhn.fame.test{ArrayFieldTest.java
		String[] split = compilationUnit.getHandleIdentifier().split("\\{");
		if (!(split.length == 2)) return compilationUnit.getHandleIdentifier();
		String shorter = split[split.length-1];
		shorter.replaceAll(".java", "");
		return shorter;
	}

	public TermDocumentMatrix getResult() {
		return TDM;
	}

}